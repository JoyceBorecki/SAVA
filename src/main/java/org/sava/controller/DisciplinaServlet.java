package org.sava.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.sava.dao.CursoDAO;
import org.sava.dao.DisciplinaDAO;
import org.sava.model.Curso;
import org.sava.model.Disciplina;

import java.io.IOException;
import java.util.List;

@WebServlet("/disciplinas")
public class DisciplinaServlet extends HttpServlet {

    private DisciplinaDAO disciplinaDAO;
    private CursoDAO cursoDAO; 

    @Override
    public void init() {
        disciplinaDAO = new DisciplinaDAO();
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
            case "excluir" -> excluirDisciplina(req, resp);
            default -> listarDisciplinas(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // O POST sempre salva (novo ou atualização)
        salvarDisciplina(req, resp);
    }

    private void listarDisciplinas(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        List<Disciplina> lista = disciplinaDAO.listar();
        req.setAttribute("disciplinas", lista);
        
        // --- CAMINHO CORRIGIDO ---
        RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/views/disciplina/lista.jsp");
        dispatcher.forward(req, resp);
    }

    private void mostrarFormularioNovo(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        List<Curso> cursos = cursoDAO.listar();
        req.setAttribute("disciplina", new Disciplina()); 
        req.setAttribute("cursos", cursos); 
        
        // --- CAMINHO CORRIGIDO ---
        RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/views/disciplina/form.jsp");
        dispatcher.forward(req, resp);
    }

    private void mostrarFormularioEditar(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        Disciplina disciplina = disciplinaDAO.buscarPorId(id);
        List<Curso> cursos = cursoDAO.listar();

        req.setAttribute("disciplina", disciplina); 
        req.setAttribute("cursos", cursos); 
        
        // --- CAMINHO CORRIGIDO ---
        RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/views/disciplina/form.jsp");
        dispatcher.forward(req, resp);
    }

    private void salvarDisciplina(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        // Coleta dados do formulário
        String idParam = req.getParameter("id");
        String nome = req.getParameter("nome");
        String semestre = req.getParameter("semestre");
        int cursoId = Integer.parseInt(req.getParameter("cursoId"));

        // Busca o objeto Curso completo
        Curso curso = cursoDAO.buscarPorId(cursoId);
        
        // Cria ou atualiza o objeto Disciplina
        Disciplina disciplina = new Disciplina(nome, semestre, curso);
        if (idParam != null && !idParam.isEmpty()) {
            disciplina.setId(Integer.parseInt(idParam));
        }

        disciplinaDAO.salvarOuAtualizar(disciplina);
        resp.sendRedirect("disciplinas?action=listar");
    }

    private void excluirDisciplina(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        disciplinaDAO.excluir(id);
        resp.sendRedirect("disciplinas?action=listar");
    }
}