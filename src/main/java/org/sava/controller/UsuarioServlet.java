package org.sava.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.sava.dao.PerfilDAO; // Importar
import org.sava.dao.UsuarioDAO;
import org.sava.model.Perfil;
import org.sava.model.Usuario;

import java.io.IOException;
import java.util.List;

@WebServlet("/usuarios")
public class UsuarioServlet extends HttpServlet {

    private UsuarioDAO usuarioDAO;
    private PerfilDAO perfilDAO; // Precisamos do DAO de Perfil

    @Override
    public void init() {
        usuarioDAO = new UsuarioDAO();
        perfilDAO = new PerfilDAO(); // Instanciar
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String action = req.getParameter("action");
        if (action == null) action = "listar";

        switch (action) {
            case "novo" -> mostrarFormularioNovo(req, resp);
            case "editar" -> mostrarFormularioEditar(req, resp);
            case "excluir" -> excluirUsuario(req, resp);
            default -> listarUsuarios(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        salvarUsuario(req, resp);
    }

    private void listarUsuarios(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        List<Usuario> lista = usuarioDAO.listar();
        req.setAttribute("usuarios", lista);
        
        // --- CAMINHO CORRIGIDO ---
        RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/views/usuario/lista.jsp");
        dispatcher.forward(req, resp);
    }

    private void carregarPerfis(HttpServletRequest req) {
        // Método helper para carregar a lista de perfis para o dropdown
        List<Perfil> perfis = perfilDAO.listar();
        req.setAttribute("perfis", perfis);
    }

    private void mostrarFormularioNovo(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        carregarPerfis(req); // Carrega a lista de perfis
        req.setAttribute("usuario", new Usuario()); // Envia um usuário vazio
        
        // --- CAMINHO CORRIGIDO ---
        RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/views/usuario/form.jsp");
        dispatcher.forward(req, resp);
    }

    private void mostrarFormularioEditar(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        Usuario usuario = usuarioDAO.buscarPorId(id);
        
        carregarPerfis(req); // Carrega a lista de perfis
        req.setAttribute("usuario", usuario); // Envia o usuário existente
        
        // --- CAMINHO CORRIGIDO ---
        RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/views/usuario/form.jsp");
        dispatcher.forward(req, resp);
    }

    private void salvarUsuario(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        String idParam = req.getParameter("id");
        String nome = req.getParameter("nome");
        String email = req.getParameter("email");
        String senha = req.getParameter("senha");
        int perfilId = Integer.parseInt(req.getParameter("perfilId"));

        // Busca o Perfil completo
        Perfil perfil = perfilDAO.buscarPorId(perfilId);
        
        Usuario usuario;
        
        if (idParam != null && !idParam.isEmpty()) {
            // Se tem ID, estamos editando
            int id = Integer.parseInt(idParam);
            usuario = usuarioDAO.buscarPorId(id); // Busca o usuário existente
            usuario.setNome(nome);
            usuario.setEmail(email);
            usuario.setPerfil(perfil);
            // Só atualiza a senha se ela foi digitada
            if (senha != null && !senha.isEmpty()) {
                usuario.setSenha(senha); 
            }
        } else {
            // Se não tem ID, é novo
            usuario = new Usuario(nome, email, senha, perfil);
        }

        usuarioDAO.salvarOuAtualizar(usuario);
        resp.sendRedirect("usuarios?action=listar");
    }

    private void excluirUsuario(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        usuarioDAO.excluir(id);
        resp.sendRedirect("usuarios?action=listar");
    }

    
}