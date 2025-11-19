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

        AvaliacaoRespondida jaRespondeu = respondidaDAO.buscarPorAlunoEAvaliacao(usuarioLogado.getId(), avaliacaoId);
        if (jaRespondeu != null) {
            resp.sendRedirect("dashboard?erro=jaRespondeu");
            return;
        }

        Avaliacao avaliacao = avaliacaoDAO.buscarPorId(avaliacaoId);
        Formulario formulario = avaliacao.getFormulario();
        List<Questao> questoes = questaoDAO.listarPorFormularioId(formulario.getId());

        for (Questao q : questoes) {
            q.setAlternativas(new HashSet<>(alternativaDAO.listarPorQuestaoId(q.getId())));
        }

        req.setAttribute("avaliacao", avaliacao);
        req.setAttribute("formulario", formulario);
        req.setAttribute("questoes", questoes);

        RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/views/responder.jsp");
        dispatcher.forward(req, resp);
    }

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

        AvaliacaoRespondida recibo = new AvaliacaoRespondida(avaliacao, usuarioLogado);
        respondidaDAO.salvar(recibo);

        String[] idsDasQuestoes = req.getParameterValues("questaoId");

        if (idsDasQuestoes != null) {
            for (String questaoIdStr : idsDasQuestoes) {
                int questaoId = Integer.parseInt(questaoIdStr);
                Questao questao = questaoDAO.buscarPorId(questaoId);

                Resposta resposta = new Resposta(recibo, questao);

                switch (questao.getTipo()) {
                    case ABERTA:
                        String texto = req.getParameter("resposta_q_" + questaoId);
                        resposta.setTextoResposta(texto);
                        break;
                    
                    case UNICA:
                        String alternativaIdUnica = req.getParameter("resposta_q_" + questaoId);
                        if (alternativaIdUnica != null) {
                            Alternativa alt = alternativaDAO.buscarPorId(Integer.parseInt(alternativaIdUnica));
                            resposta.getAlternativasMarcadas().add(alt);
                        }
                        break;

                    case MULTIPLA:
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

                respostaDAO.salvar(resposta);
            }
        }

        resp.sendRedirect("dashboard?sucesso=true");
    }
}