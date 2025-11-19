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
import org.sava.dao.UsuarioDAO;
import org.sava.model.Disciplina;
import org.sava.model.Perfil;
import org.sava.model.Turma;
import org.sava.model.Usuario;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet("/turmas")
public class TurmaServlet extends HttpServlet {

    private TurmaDAO turmaDAO;
    private DisciplinaDAO disciplinaDAO;
    private UsuarioDAO usuarioDAO;
    private PerfilDAO perfilDAO;

    @Override
    public void init() {
        turmaDAO = new TurmaDAO();
        disciplinaDAO = new DisciplinaDAO();
        usuarioDAO = new UsuarioDAO();
        perfilDAO = new PerfilDAO();
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
            case "adicionarParticipante" -> adicionarParticipante(req, resp);
            default -> salvarTurma(req, resp);
        }
    }

    private void listarTurmas(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        List<Turma> lista = turmaDAO.listar();
        req.setAttribute("turmas", lista);

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
        carregarProfessoresEAlunos(req);

        req.setAttribute("turma", new Turma());
        req.getRequestDispatcher("/WEB-INF/views/turma/form.jsp").forward(req, resp);
    }

    private void mostrarFormularioEditar(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        Turma turma = turmaDAO.buscarPorId(id);

        carregarDependencias(req);
        carregarProfessoresEAlunos(req);

        req.setAttribute("turma", turma);
        req.getRequestDispatcher("/WEB-INF/views/turma/form.jsp").forward(req, resp);
    }

    private void salvarTurma(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        String idParam = req.getParameter("id");
        String semestre = req.getParameter("semestre");
        int disciplinaId = Integer.parseInt(req.getParameter("disciplinaId"));

        Disciplina disciplina = disciplinaDAO.buscarPorId(disciplinaId);

        Turma turma;

        if (idParam == null || idParam.isBlank()) {
            turma = new Turma();
        } else {
            turma = turmaDAO.buscarPorId(Integer.parseInt(idParam));
        }

        turma.setSemestre(semestre);
        turma.setDisciplina(disciplina);

        turma.getProfessores().clear();
        String[] profIds = req.getParameterValues("professores");
        if (profIds != null) {
            for (String pid : profIds) {
                Usuario u = usuarioDAO.buscarPorId(Integer.parseInt(pid));
                if (u != null)
                    turma.getProfessores().add(u);
            }
        }

        turma.getAlunos().clear();
        String[] alunosIds = req.getParameterValues("alunos");
        if (alunosIds != null) {
            for (String aid : alunosIds) {
                Usuario u = usuarioDAO.buscarPorId(Integer.parseInt(aid));
                if (u != null)
                    turma.getAlunos().add(u);
            }
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

    private void mostrarGerenciadorTurma(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        int turmaId = Integer.parseInt(req.getParameter("id"));
        Turma turma = turmaDAO.buscarPorId(turmaId);

        List<Usuario> todosUsuarios = usuarioDAO.listar();

        List<Usuario> todosAlunos = todosUsuarios.stream()
            .filter(u -> u.getPerfil().getNome().equalsIgnoreCase("Aluno"))
            .collect(Collectors.toList());
            
        List<Usuario> todosProfessores = todosUsuarios.stream()
            .filter(u -> u.getPerfil().getNome().equalsIgnoreCase("Professor"))
            .collect(Collectors.toList());

        req.setAttribute("turma", turma);
        req.setAttribute("todosAlunos", todosAlunos);
        req.setAttribute("todosProfessores", todosProfessores);

        RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/views/turma/gerenciar.jsp");
        dispatcher.forward(req, resp);
    }

    private void adicionarParticipante(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        int turmaId = Integer.parseInt(req.getParameter("turmaId"));
        int usuarioId = Integer.parseInt(req.getParameter("usuarioId"));
        String tipo = req.getParameter("tipo");

        Turma turma = turmaDAO.buscarPorId(turmaId);
        Usuario usuario = usuarioDAO.buscarPorId(usuarioId);

        if (turma != null && usuario != null) {
            if ("aluno".equals(tipo)) {
                turma.getAlunos().add(usuario);
            } else if ("professor".equals(tipo)) {
                turma.getProfessores().add(usuario);
            }
            turmaDAO.atualizar(turma);
        }

        resp.sendRedirect("turmas?action=gerenciar&id=" + turmaId);
    }

    private void removerParticipante(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        int turmaId = Integer.parseInt(req.getParameter("turmaId"));
        int usuarioId = Integer.parseInt(req.getParameter("usuarioId"));
        String tipo = req.getParameter("tipo");

        Turma turma = turmaDAO.buscarPorId(turmaId);
        Usuario usuario = usuarioDAO.buscarPorId(usuarioId);

        if (turma != null && usuario != null) {
            if ("aluno".equals(tipo)) {
                turma.getAlunos().remove(usuario);
            } else if ("professor".equals(tipo)) {
                turma.getProfessores().remove(usuario);
            }
            turmaDAO.atualizar(turma);
        }
        
        resp.sendRedirect("turmas?action=gerenciar&id=" + turmaId);
    }

    private void carregarProfessoresEAlunos(HttpServletRequest req) {
        List<Usuario> professores = usuarioDAO.buscarPorPerfil("Professor");
        List<Usuario> alunos = usuarioDAO.buscarPorPerfil("Aluno");
        req.setAttribute("professores", professores);
        req.setAttribute("alunos", alunos);
    }
}