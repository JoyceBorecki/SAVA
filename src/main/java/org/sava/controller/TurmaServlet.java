package org.sava.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.sava.dao.DisciplinaDAO;
import org.sava.dao.PerfilDAO;
import org.sava.dao.TurmaDAO;
import org.sava.dao.UsuarioDAO; // Importar
import org.sava.model.Disciplina;
import org.sava.model.Perfil;
import org.sava.model.Turma;
import org.sava.model.Usuario; // Importar

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet("/turmas")
public class TurmaServlet extends HttpServlet {

    private TurmaDAO turmaDAO;
    private DisciplinaDAO disciplinaDAO;
    private UsuarioDAO usuarioDAO; // Necessário para listar alunos/professores
    private PerfilDAO perfilDAO;   // Necessário para buscar perfis

    @Override
    public void init() {
        turmaDAO = new TurmaDAO();
        disciplinaDAO = new DisciplinaDAO();
        usuarioDAO = new UsuarioDAO(); // Instanciar
        perfilDAO = new PerfilDAO();   // Instanciar
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String action = req.getParameter("action");
        if (action == null) action = "listar";

        switch (action) {
            case "novo" -> mostrarFormularioNovo(req, resp);
            case "editar" -> mostrarFormularioEditar(req, resp);
            case "excluir" -> excluirTurma(req, resp);
            // Novas ações GET
            case "gerenciar" -> mostrarGerenciadorTurma(req, resp);
            case "removerParticipante" -> removerParticipante(req, resp);
            default -> listarTurmas(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String action = req.getParameter("action");
        if (action == null) {
            salvarTurma(req, resp);
            return;
        }

        switch (action) {
            // Novas ações POST
            case "adicionarParticipante" -> adicionarParticipante(req, resp);
            default -> salvarTurma(req, resp);
        }
    }

    // --- Métodos de CRUD de Turma (existentes) ---

    private void listarTurmas(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        List<Turma> lista = turmaDAO.listar();
        req.setAttribute("turmas", lista);
        
        // --- CAMINHO CORRIGIDO ---
        RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/views/turma/lista.jsp");
        dispatcher.forward(req, resp);
    }

    private void carregarDependencias(HttpServletRequest req) {
        List<Disciplina> disciplinas = disciplinaDAO.listar();
        req.setAttribute("disciplinas", disciplinas);
    }

    private void mostrarFormularioNovo(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        carregarDependencias(req);
        req.setAttribute("turma", new Turma());
        
        // --- CAMINHO CORRIGIDO ---
        RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/views/turma/form.jsp");
        dispatcher.forward(req, resp);
    }

    private void mostrarFormularioEditar(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        Turma turma = turmaDAO.buscarPorId(id);
        
        carregarDependencias(req);
        req.setAttribute("turma", turma);
        
        // --- CAMINHO CORRIGIDO ---
        RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/views/turma/form.jsp");
        dispatcher.forward(req, resp);
    }

    private void salvarTurma(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        
        String idParam = req.getParameter("id");
        String semestre = req.getParameter("semestre");
        int disciplinaId = Integer.parseInt(req.getParameter("disciplinaId"));

        Disciplina disciplina = disciplinaDAO.buscarPorId(disciplinaId);
        
        Turma turma = new Turma(semestre, disciplina);
        if (idParam != null && !idParam.isEmpty()) {
            turma.setId(Integer.parseInt(idParam));
        }

        turmaDAO.salvarOuAtualizar(turma);
        resp.sendRedirect("turmas?action=listar");
    }

    private void excluirTurma(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        turmaDAO.excluir(id);
        resp.sendRedirect("turmas?action=listar");
    }

    // --- NOVOS MÉTODOS PARA GERENCIAR TURMA (Alunos/Professores) ---

    private void mostrarGerenciadorTurma(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        int turmaId = Integer.parseInt(req.getParameter("id"));
        Turma turma = turmaDAO.buscarPorId(turmaId); // DAO já busca alunos e professores

        // Carrega todos os usuários para os dropdowns
        List<Usuario> todosUsuarios = usuarioDAO.listar();
        
        // Separa por perfil (para facilitar nos dropdowns)
        List<Usuario> todosAlunos = todosUsuarios.stream()
            .filter(u -> u.getPerfil().getNome().equalsIgnoreCase("Aluno"))
            .collect(Collectors.toList());
            
        List<Usuario> todosProfessores = todosUsuarios.stream()
            .filter(u -> u.getPerfil().getNome().equalsIgnoreCase("Professor"))
            .collect(Collectors.toList());

        req.setAttribute("turma", turma);
        req.setAttribute("todosAlunos", todosAlunos);
        req.setAttribute("todosProfessores", todosProfessores);
        
        // --- CAMINHO CORRIGIDO ---
        RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/views/turma/gerenciar.jsp");
        dispatcher.forward(req, resp);
    }

    private void adicionarParticipante(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        int turmaId = Integer.parseInt(req.getParameter("turmaId"));
        int usuarioId = Integer.parseInt(req.getParameter("usuarioId"));
        String tipo = req.getParameter("tipo"); // "aluno" ou "professor"

        Turma turma = turmaDAO.buscarPorId(turmaId);
        Usuario usuario = usuarioDAO.buscarPorId(usuarioId);

        if (turma != null && usuario != null) {
            if ("aluno".equals(tipo)) {
                turma.getAlunos().add(usuario);
            } else if ("professor".equals(tipo)) {
                turma.getProfessores().add(usuario);
            }
            turmaDAO.atualizar(turma); // Salva a relação ManyToMany
        }

        resp.sendRedirect("turmas?action=gerenciar&id=" + turmaId);
    }

    private void removerParticipante(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        int turmaId = Integer.parseInt(req.getParameter("turmaId"));
        int usuarioId = Integer.parseInt(req.getParameter("usuarioId"));
        String tipo = req.getParameter("tipo"); // "aluno" ou "professor"

        Turma turma = turmaDAO.buscarPorId(turmaId);
        Usuario usuario = usuarioDAO.buscarPorId(usuarioId);

        if (turma != null && usuario != null) {
            if ("aluno".equals(tipo)) {
                turma.getAlunos().remove(usuario);
            } else if ("professor".equals(tipo)) {
                turma.getProfessores().remove(usuario);
            }
            turmaDAO.atualizar(turma); // Salva a relação ManyToMany
        }
        
        resp.sendRedirect("turmas?action=gerenciar&id=" + turmaId);
    }
}