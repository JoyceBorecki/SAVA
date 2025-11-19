package org.sava.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.sava.dao.AvaliacaoDAO;
import org.sava.dao.FormularioDAO;
import org.sava.dao.TurmaDAO;
import org.sava.model.Avaliacao;
import org.sava.model.Formulario;
import org.sava.model.Turma;

import java.io.IOException;
import java.util.List;

@WebServlet("/avaliacoes")
public class AvaliacaoServlet extends HttpServlet {

    private AvaliacaoDAO avaliacaoDAO;
    private FormularioDAO formularioDAO;
    private TurmaDAO turmaDAO;

    @Override
    public void init() {
        avaliacaoDAO = new AvaliacaoDAO();
        formularioDAO = new FormularioDAO();
        turmaDAO = new TurmaDAO();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String action = req.getParameter("action");
        if (action == null) action = "listar";

        switch (action) {
            case "novo" -> mostrarFormularioNovo(req, resp);
            case "excluir" -> excluirAvaliacao(req, resp);
            default -> listarAvaliacoes(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        salvarAvaliacao(req, resp);
    }

    private void carregarDependencias(HttpServletRequest req) {
        List<Formulario> formularios = formularioDAO.listar();
        List<Turma> turmas = turmaDAO.listar();
        req.setAttribute("formularios", formularios);
        req.setAttribute("turmas", turmas);
    }

    private void listarAvaliacoes(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        List<Avaliacao> lista = avaliacaoDAO.listar();
        req.setAttribute("avaliacoes", lista);
        RequestDispatcher dispatcher = req.getRequestDispatcher("/avaliacao/lista.jsp");
        dispatcher.forward(req, resp);
    }

    private void mostrarFormularioNovo(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        carregarDependencias(req);
        req.setAttribute("avaliacao", new Avaliacao());
        RequestDispatcher dispatcher = req.getRequestDispatcher("/avaliacao/form.jsp");
        dispatcher.forward(req, resp);
    }

    private void salvarAvaliacao(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        
        int formularioId = Integer.parseInt(req.getParameter("formularioId"));
        int turmaId = Integer.parseInt(req.getParameter("turmaId"));

        Formulario formulario = formularioDAO.buscarPorId(formularioId);
        Turma turma = turmaDAO.buscarPorId(turmaId);

        if (formulario != null && turma != null) {
            Avaliacao avaliacao = new Avaliacao(formulario, turma);
            avaliacaoDAO.salvar(avaliacao);
        }
        
        resp.sendRedirect("avaliacoes?action=listar");
    }

    private void excluirAvaliacao(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        avaliacaoDAO.excluir(id);
        resp.sendRedirect("avaliacoes?action=listar");
    }
}