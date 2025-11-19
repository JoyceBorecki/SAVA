package org.sava.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.sava.dao.PerfilDAO;
import org.sava.dao.UsuarioDAO;
import org.sava.model.Perfil;
import org.sava.model.Usuario;

import java.io.IOException;

@WebServlet("/cadastro")
public class CadastroServlet extends HttpServlet {

    private UsuarioDAO usuarioDAO;
    private PerfilDAO perfilDAO;

    @Override
    public void init() {
        usuarioDAO = new UsuarioDAO();
        perfilDAO = new PerfilDAO();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.sendRedirect("login"); 
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String nome = req.getParameter("nome");
        String email = req.getParameter("email");
        String senha = req.getParameter("senha");
        int perfilId = Integer.parseInt(req.getParameter("perfilId"));

        if (usuarioDAO.buscarPorEmail(email) != null) {
            req.setAttribute("erro", "Erro: Este e-mail já está cadastrado no sistema.");
            req.setAttribute("perfis", perfilDAO.listar());
            req.setAttribute("mostrarCadastro", true);
            req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
            return;
        }

        Perfil perfil = perfilDAO.buscarPorId(perfilId);
        Usuario novoUsuario = new Usuario(nome, email, senha, perfil);
        usuarioDAO.salvar(novoUsuario);

        resp.sendRedirect("login?cadastroSucesso=true");
    }
}