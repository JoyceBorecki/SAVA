package org.sava.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.sava.dao.ProcessoAvaliativoDAO;
import org.sava.model.ProcessoAvaliativo;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@WebServlet("/processos")
public class ProcessoAvaliativoServlet extends HttpServlet {

    private ProcessoAvaliativoDAO processoDAO;

    @Override
    public void init() {
        processoDAO = new ProcessoAvaliativoDAO();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String action = req.getParameter("action");
        if (action == null) action = "listar";

        switch (action) {
            case "novo" -> mostrarFormularioNovo(req, resp);
            case "editar" -> mostrarFormularioEditar(req, resp);
            case "excluir" -> excluirProcesso(req, resp);
            default -> listarProcessos(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        salvarProcesso(req, resp);
    }

    private void listarProcessos(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        List<ProcessoAvaliativo> lista = processoDAO.listar();
        req.setAttribute("processos", lista);
        
        // --- CAMINHO CORRIGIDO ---
        RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/views/processo/lista.jsp");
        dispatcher.forward(req, resp);
    }

    private void mostrarFormularioNovo(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setAttribute("processo", new ProcessoAvaliativo());
        
        // --- CAMINHO CORRIGIDO ---
        RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/views/processo/form.jsp");
        dispatcher.forward(req, resp);
    }

    private void mostrarFormularioEditar(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        ProcessoAvaliativo processo = processoDAO.buscarPorId(id);
        req.setAttribute("processo", processo);
        
        // --- CAMINHO CORRIGIDO ---
        RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/views/processo/form.jsp");
        dispatcher.forward(req, resp);
    }

    private void salvarProcesso(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        
        String idParam = req.getParameter("id");
        String nome = req.getParameter("nome");
        
        // Tratamento para campos de data que podem vir vazios
        LocalDate dataInicio = null;
        if (req.getParameter("dataInicio") != null && !req.getParameter("dataInicio").isEmpty()) {
            dataInicio = LocalDate.parse(req.getParameter("dataInicio"));
        }
        
        LocalDate dataFim = null;
        if (req.getParameter("dataFim") != null && !req.getParameter("dataFim").isEmpty()) {
            dataFim = LocalDate.parse(req.getParameter("dataFim"));
        }

        ProcessoAvaliativo processo = new ProcessoAvaliativo(nome, dataInicio, dataFim);
        if (idParam != null && !idParam.isEmpty()) {
            processo.setId(Integer.parseInt(idParam));
        }

        processoDAO.salvarOuAtualizar(processo);
        resp.sendRedirect("processos?action=listar");
    }

    private void excluirProcesso(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        processoDAO.excluir(id);
        resp.sendRedirect("processos?action=listar");
    }
}