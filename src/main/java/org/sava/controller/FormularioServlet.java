package org.sava.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.sava.dao.*;
import org.sava.model.*;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@WebServlet("/formularios")
public class FormularioServlet extends HttpServlet {

    private FormularioDAO formularioDAO;
    private ProcessoAvaliativoDAO processoDAO;
    private PerfilDAO perfilDAO;
    private QuestaoDAO questaoDAO;
    private AlternativaDAO alternativaDAO;


    @Override
    public void init() {
        formularioDAO = new FormularioDAO();
        processoDAO = new ProcessoAvaliativoDAO();
        perfilDAO = new PerfilDAO();
        questaoDAO = new QuestaoDAO();
        alternativaDAO = new AlternativaDAO();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String action = req.getParameter("action");
        if (action == null) action = "listar";

        switch (action) {
            case "novo" -> mostrarFormularioNovo(req, resp);
            case "editar" -> mostrarFormularioEditar(req, resp);
            case "excluir" -> excluirFormulario(req, resp);
            case "gerenciarQuestoes" -> mostrarGerenciadorQuestoes(req, resp);
            case "excluirQuestao" -> excluirQuestao(req, resp);
            case "excluirAlternativa" -> excluirAlternativa(req, resp);
            default -> listarFormularios(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String action = req.getParameter("action");
        if (action == null) {
             salvarFormulario(req, resp);
             return;
        }

        switch (action) {
            case "adicionarQuestao" -> adicionarQuestao(req, resp);
            case "adicionarAlternativa" -> adicionarAlternativa(req, resp);
            default -> salvarFormulario(req, resp);
        }
    }

    // --- Métodos de CRUD de Formulário ---

    private void listarFormularios(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        List<Formulario> lista = formularioDAO.listar();
        req.setAttribute("formularios", lista);
        
        RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/views/formulario/lista.jsp");
        dispatcher.forward(req, resp);
    }

    private void carregarDependencias(HttpServletRequest req) {
        List<ProcessoAvaliativo> processos = processoDAO.listar();
        List<Perfil> perfis = perfilDAO.listar();
        req.setAttribute("processos", processos);
        req.setAttribute("perfis", perfis);
    }

    private void mostrarFormularioNovo(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        carregarDependencias(req);
        req.setAttribute("formulario", new Formulario());
        
        RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/views/formulario/form.jsp");
        dispatcher.forward(req, resp);
    }

    private void mostrarFormularioEditar(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        Formulario formulario = formularioDAO.buscarPorId(id);
        
        carregarDependencias(req);
        req.setAttribute("formulario", formulario);
        
        RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/views/formulario/form.jsp");
        dispatcher.forward(req, resp);
    }

    private void salvarFormulario(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        
        String idParam = req.getParameter("id");
        String titulo = req.getParameter("titulo");
        boolean anonimo = "on".equals(req.getParameter("anonimo"));
        int processoId = Integer.parseInt(req.getParameter("processoId"));
        String[] perfisIds = req.getParameterValues("perfisDestinados");

        ProcessoAvaliativo processo = processoDAO.buscarPorId(processoId);
        Set<Perfil> perfisDestinados = new HashSet<>();
        if (perfisIds != null) {
            for (String perfilId : perfisIds) {
                perfisDestinados.add(perfilDAO.buscarPorId(Integer.parseInt(perfilId)));
            }
        }

        Formulario formulario = new Formulario(titulo, anonimo, processo);
        formulario.setPerfisDestinados(perfisDestinados);

        if (idParam != null && !idParam.isEmpty()) {
            formulario.setId(Integer.parseInt(idParam));
        }

        formularioDAO.salvarOuAtualizar(formulario);
        resp.sendRedirect("formularios?action=listar");
    }

    private void excluirFormulario(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        formularioDAO.excluir(id);
        resp.sendRedirect("formularios?action=listar");
    }

    // --- NOVOS MÉTODOS PARA GERENCIAR QUESTÕES ---

    private void mostrarGerenciadorQuestoes(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        int formularioId = Integer.parseInt(req.getParameter("id"));
        
        // CORRIGIDO: Usa o novo método do DAO para evitar Lazy Load e carregar tudo
        Formulario formulario = formularioDAO.buscarPorIdComQuestoes(formularioId); 
        
        // As Questões e Alternativas são carregadas pelo método acima (FETCH JOIN)
        req.setAttribute("formulario", formulario);
        req.setAttribute("questoes", formulario.getQuestoes()); 
        
        RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/views/formulario/gerenciarQuestoes.jsp");
        dispatcher.forward(req, resp);
    }

    private void adicionarQuestao(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        int formularioId = Integer.parseInt(req.getParameter("formularioId"));
        String enunciado = req.getParameter("enunciado");
        String tipoStr = req.getParameter("tipo");
        boolean obrigatoria = "on".equals(req.getParameter("obrigatoria"));

        Formulario formulario = formularioDAO.buscarPorId(formularioId);
        Questao.TipoQuestao tipo = Questao.TipoQuestao.valueOf(tipoStr);

        Questao questao = new Questao(enunciado, obrigatoria, tipo, formulario);
        questaoDAO.salvar(questao);

        resp.sendRedirect("formularios?action=gerenciarQuestoes&id=" + formularioId);
    }

    private void excluirQuestao(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        int formularioId = Integer.parseInt(req.getParameter("formularioId"));
        int questaoId = Integer.parseInt(req.getParameter("questaoId"));
        
        questaoDAO.excluir(questaoId);
        resp.sendRedirect("formularios?action=gerenciarQuestoes&id=" + formularioId);
    }

    private void adicionarAlternativa(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        int formularioId = Integer.parseInt(req.getParameter("formularioId"));
        int questaoId = Integer.parseInt(req.getParameter("questaoId"));
        String texto = req.getParameter("textoAlternativa");

        Questao questao = questaoDAO.buscarPorId(questaoId);
        Alternativa alternativa = new Alternativa(texto, questao);
        alternativaDAO.salvar(alternativa);

        resp.sendRedirect("formularios?action=gerenciarQuestoes&id=" + formularioId);
    }

    private void excluirAlternativa(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        int formularioId = Integer.parseInt(req.getParameter("formularioId"));
        int alternativaId = Integer.parseInt(req.getParameter("alternativaId"));
        
        alternativaDAO.excluir(alternativaId);
        resp.sendRedirect("formularios?action=gerenciarQuestoes&id=" + formularioId);
    }
}