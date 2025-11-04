package org.sava.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.sava.dao.CursoDAO;
import org.sava.model.Curso;

import java.io.IOException;
import java.util.List;

@WebServlet("/cursos")
public class CursoServlet extends HttpServlet {

    private CursoDAO cursoDAO;

    @Override
    public void init() {
        cursoDAO = new CursoDAO();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String action = req.getParameter("action");
        if (action == null) action = "listar";

        switch (action) {
            case "novo" -> mostrarFormularioNovo(req, resp);
            case "editar" -> mostrarFormularioEditar(req, resp);
            case "excluir" -> excluirCurso(req, resp);
            default -> listarCursos(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        salvarCurso(req, resp);
    }

    private void listarCursos(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        List<Curso> lista = cursoDAO.listar();
        req.setAttribute("cursos", lista);
        
        // --- CAMINHO CORRIGIDO ---
        RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/views/curso/lista.jsp");
        dispatcher.forward(req, resp);
    }

    private void mostrarFormularioNovo(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setAttribute("curso", new Curso());
        
        // --- CAMINHO CORRIGIDO ---
        RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/views/curso/form.jsp");
        dispatcher.forward(req, resp);
    }

    private void mostrarFormularioEditar(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        Curso curso = cursoDAO.buscarPorId(id);
        req.setAttribute("curso", curso);
        
        // --- CAMINHO CORRIGIDO ---
        RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/views/curso/form.jsp");
        dispatcher.forward(req, resp);
    }

    private void salvarCurso(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        
        String idParam = req.getParameter("id");
        String nome = req.getParameter("nome");
        
        Curso curso = new Curso(nome);
        if (idParam != null && !idParam.isEmpty()) {
            curso.setId(Integer.parseInt(idParam));
        }

        // Se você não tiver um salvarOuAtualizar() no DAO, esta lógica funciona.
        if (curso.getId() > 0) {
            cursoDAO.atualizar(curso);
        } else {
            cursoDAO.salvar(curso);
        }
        
        resp.sendRedirect("cursos?action=listar");
    }

    private void excluirCurso(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        cursoDAO.excluir(id);
        resp.sendRedirect("cursos?action=listar");
    }
}