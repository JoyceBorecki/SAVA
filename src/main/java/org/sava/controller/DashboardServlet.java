package org.sava.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.sava.dao.AvaliacaoDAO;
import org.sava.dao.AvaliacaoRespondidaDAO;
import org.sava.model.Avaliacao;
import org.sava.model.AvaliacaoRespondida;
import org.sava.model.Usuario;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {

    private AvaliacaoDAO avaliacaoDAO;
    private AvaliacaoRespondidaDAO respondidaDAO;

    @Override
    public void init() {
        avaliacaoDAO = new AvaliacaoDAO();
        respondidaDAO = new AvaliacaoRespondidaDAO();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        
        HttpSession session = req.getSession(false);

        // 1. Segurança Básica
        if (session == null || session.getAttribute("usuarioLogado") == null) {
            resp.sendRedirect("login");
            return;
        }

        Usuario usuarioLogado = (Usuario) session.getAttribute("usuarioLogado");

        // Se NÃO for Aluno, redireciona direto para a tela de Relatórios/Resultados.
        // Assim, o Admin nunca vê a tela vazia.
        if (!usuarioLogado.getPerfil().getNome().equalsIgnoreCase("Aluno")) {
            resp.sendRedirect("relatorios");
            return; // Encerra a execução aqui
        }
        // ---------------------------

        // 2. Lógica Exclusiva de ALUNO (Carrega os cards de resposta)
        List<Avaliacao> todasAvaliacoes = avaliacaoDAO.listarPorUsuario(usuarioLogado.getId());
        List<AvaliacaoRespondida> jaRespondidas = respondidaDAO.listarPorAlunoId(usuarioLogado.getId());
        
        Set<Integer> idsRespondidos = jaRespondidas.stream()
                                    .map(ar -> ar.getAvaliacao().getId())
                                    .collect(Collectors.toSet());

        List<Avaliacao> pendentes = new ArrayList<>();
        List<Avaliacao> respondidas = new ArrayList<>();

        for (Avaliacao av : todasAvaliacoes) {
            if (idsRespondidos.contains(av.getId())) {
                respondidas.add(av);
            } else {
                pendentes.add(av);
            }
        }

        req.setAttribute("usuarioLogado", usuarioLogado);
        req.setAttribute("pendentes", pendentes);
        req.setAttribute("respondidas", respondidas);
        
        RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/views/dashboard.jsp");
        dispatcher.forward(req, resp);
    }
}