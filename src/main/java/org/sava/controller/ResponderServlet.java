package org.sava.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.sava.dao.*;
import org.sava.model.*;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@WebServlet("/responder")
public class ResponderServlet extends HttpServlet {

    private AvaliacaoDAO avaliacaoDAO;
    private QuestaoDAO questaoDAO;
    private AlternativaDAO alternativaDAO;
    private AvaliacaoRespondidaDAO respondidaDAO;
    private RespostaDAO respostaDAO;


    @Override
    public void init() {
        avaliacaoDAO = new AvaliacaoDAO();
        questaoDAO = new QuestaoDAO();
        alternativaDAO = new AlternativaDAO();
        respondidaDAO = new AvaliacaoRespondidaDAO();
        respostaDAO = new RespostaDAO();
    }

    /**
     * doGet: Mostra a página do formulário para responder (RF12)
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("usuarioLogado") == null) {
            resp.sendRedirect("login");
            return;
        }

        Usuario usuarioLogado = (Usuario) session.getAttribute("usuarioLogado");
        int avaliacaoId = Integer.parseInt(req.getParameter("id"));

        // RF13: Verifica se o aluno já respondeu esta avaliação
        AvaliacaoRespondida jaRespondeu = respondidaDAO.buscarPorAlunoEAvaliacao(usuarioLogado.getId(), avaliacaoId);
        if (jaRespondeu != null) {
            // Se já respondeu, não pode responder de novo
            resp.sendRedirect("dashboard?erro=jaRespondeu");
            return;
        }

        // Carrega a avaliação, formulário, turma e questões
        Avaliacao avaliacao = avaliacaoDAO.buscarPorId(avaliacaoId);
        Formulario formulario = avaliacao.getFormulario();
        // Carrega as questões e suas alternativas
        List<Questao> questoes = questaoDAO.listarPorFormularioId(formulario.getId());
        for (Questao q : questoes) {
            // Força o carregamento das alternativas
            q.setAlternativas(alternativaDAO.listarPorQuestaoId(q.getId()));
        }

        req.setAttribute("avaliacao", avaliacao);
        req.setAttribute("formulario", formulario);
        req.setAttribute("questoes", questoes);

        RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/views/responder.jsp");
        dispatcher.forward(req, resp);
    }

    /**
     * doPost: Salva as respostas do aluno (RF08, RF09)
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("usuarioLogado") == null) {
            resp.sendRedirect("login");
            return;
        }

        Usuario usuarioLogado = (Usuario) session.getAttribute("usuarioLogado");
        int avaliacaoId = Integer.parseInt(req.getParameter("avaliacaoId"));
        
        Avaliacao avaliacao = avaliacaoDAO.buscarPorId(avaliacaoId);

        // 1. Cria o "recibo" da avaliação (RF03, RF11, RF14)
        AvaliacaoRespondida recibo = new AvaliacaoRespondida(avaliacao, usuarioLogado);
        respondidaDAO.salvar(recibo); // Salva primeiro para obter o ID

        // 2. Loop sobre todas as questões do formulário para salvar cada resposta
        // (Assume que o JSP envia os IDs de todas as questões)
        String[] idsDasQuestoes = req.getParameterValues("questaoId");

        if (idsDasQuestoes != null) {
            for (String questaoIdStr : idsDasQuestoes) {
                int questaoId = Integer.parseInt(questaoIdStr);
                Questao questao = questaoDAO.buscarPorId(questaoId);

                // Cria o objeto Resposta
                Resposta resposta = new Resposta(recibo, questao);

                // Verifica o tipo da questão para saber o que salvar
                switch (questao.getTipo()) {
                    case ABERTA:
                        // Pega a resposta textual [cite: 98-99]
                        String texto = req.getParameter("resposta_q_" + questaoId);
                        resposta.setTextoResposta(texto);
                        break;
                    
                    case UNICA:
                        // Pega a alternativa única selecionada [cite: 89-93]
                        String alternativaIdUnica = req.getParameter("resposta_q_" + questaoId);
                        if (alternativaIdUnica != null) {
                            Alternativa alt = alternativaDAO.buscarPorId(Integer.parseInt(alternativaIdUnica));
                            resposta.getAlternativasMarcadas().add(alt);
                        }
                        break;

                    case MULTIPLA:
                        // Pega todas as alternativas marcadas [cite: 94-97]
                        String[] alternativasIdsMulti = req.getParameterValues("resposta_q_" + questaoId);
                        if (alternativasIdsMulti != null) {
                            Set<Alternativa> marcadas = new HashSet<>();
                            for (String altId : alternativasIdsMulti) {
                                marcadas.add(alternativaDAO.buscarPorId(Integer.parseInt(altId)));
                            }
                            resposta.setAlternativasMarcadas(marcadas);
                        }
                        break;
                }
                
                // 3. Salva a resposta individual
                respostaDAO.salvar(resposta);
            }
        }

        // 4. Redireciona para o dashboard
        resp.sendRedirect("dashboard?sucesso=true");
    }
}