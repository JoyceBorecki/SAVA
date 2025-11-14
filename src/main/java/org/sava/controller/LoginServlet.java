package org.sava.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.mindrot.jbcrypt.BCrypt;
import org.sava.dao.PerfilDAO;
import org.sava.dao.UsuarioDAO;
import org.sava.model.Usuario;

import java.io.IOException;
import java.util.List;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private UsuarioDAO usuarioDAO;

    @Override
    public void init() {
        usuarioDAO = new UsuarioDAO();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String email = req.getParameter("email");
        String senha = req.getParameter("senha");

        if (email == null || senha == null || email.isBlank() || senha.isBlank()) {
            req.setAttribute("erro", "E-mail e senha são obrigatórios.");
            req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
            return;
        }

        Usuario usuario = usuarioDAO.buscarPorEmail(email);

        if (usuario == null) {
            req.setAttribute("erro", "E-mail não encontrado.");
            req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
            return;
        }

        if (!BCrypt.checkpw(senha, usuario.getSenha())) {
            req.setAttribute("erro", "Senha incorreta.");
            req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
            return;
        }

        HttpSession session = req.getSession(true);
        session.setAttribute("usuarioLogado", usuario);
        session.setAttribute("role", usuario.getPerfil().getNome());

        resp.sendRedirect("dashboard");
    }
}