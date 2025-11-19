package org.sava.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.sava.dao.FormularioDAO;
import org.sava.dao.RelatorioDAO;
import org.sava.dto.EstatisticaQuestao;
import org.sava.model.Formulario;
import org.sava.model.Usuario;

import java.io.IOException;
import java.util.List;

@WebServlet("/relatorios")
public class RelatorioServlet extends HttpServlet {

    private RelatorioDAO relatorioDAO;
    private FormularioDAO formularioDAO;

    @Override
    public void init() {
        relatorioDAO = new RelatorioDAO();
        formularioDAO = new FormularioDAO();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        
        HttpSession session = req.getSession(false);
        Usuario usuario = (session != null) ? (Usuario) session.getAttribute("usuarioLogado") : null;

        if (usuario == null) {
            resp.sendRedirect("login");
            return;
        }

        String action = req.getParameter("action");
        
        if ("ver".equals(action)) {
            mostrarRelatorioDetalhado(req, resp, usuario);
        } else {
            listarRelatoriosDisponiveis(req, resp, usuario);
        }
    }

    private void listarRelatoriosDisponiveis(HttpServletRequest req, HttpServletResponse resp, Usuario usuario) 
            throws ServletException, IOException {
        // Simplesmente lista todos os formulários para o Admin/Coord escolher qual resultado ver
        // (Numa versão futura, filtrar apenas formulários das turmas do Professor)
        List<Formulario> formularios = formularioDAO.listar();
        req.setAttribute("formularios", formularios);
        
        RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/views/relatorio/lista.jsp");
        dispatcher.forward(req, resp);
    }

    private void mostrarRelatorioDetalhado(HttpServletRequest req, HttpServletResponse resp, Usuario usuario) 
            throws ServletException, IOException {
        
        int formularioId = Integer.parseInt(req.getParameter("id"));
        Formulario formulario = formularioDAO.buscarPorId(formularioId);

        // GERA AS ESTATÍSTICAS (RF17)
        List<EstatisticaQuestao> estatisticas = relatorioDAO.gerarEstatisticas(formularioId);

        // REGRA DE NEGÓCIO DE VISIBILIDADE (RF19 vs RF20)
        boolean isAdmin = usuario.getPerfil().getNome().equals("Administrador");
        boolean mostrarNomes = isAdmin || !formulario.isAnonimo();

        // Se for anônimo e não for admin, limpamos os nomes das respostas abertas na memória antes de enviar para a JSP
        if (!mostrarNomes) {
            for (EstatisticaQuestao est : estatisticas) {
                if (!est.getRespostasAbertas().isEmpty()) {
                    // Substitui nomes reais por "Aluno (Anônimo)"
                    var mapAnonimo = new java.util.HashMap<String, String>();
                    for (String resposta : est.getRespostasAbertas().values()) {
                        mapAnonimo.put("Anônimo", resposta);
                    }
                    // Truque sujo para limpar o mapa original, na prática criaríamos um novo DTO
                    est.getRespostasAbertas().clear();
                    est.getRespostasAbertas().putAll(mapAnonimo);
                }
            }
        }

        req.setAttribute("formulario", formulario);
        req.setAttribute("estatisticas", estatisticas);
        req.setAttribute("isAdmin", isAdmin);

        RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/views/relatorio/ver.jsp");
        dispatcher.forward(req, resp);
    }
}