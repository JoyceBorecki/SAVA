package org.sava.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.sava.dao.UsuarioDAO;
import org.sava.model.Perfil;
import org.sava.model.Usuario;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.sava.util.HibernateUtil;

import java.io.IOException;
import java.util.List;

@WebServlet("/usuarios")
public class UsuarioServlet extends HttpServlet {

    private UsuarioDAO usuarioDAO;

    @Override
    public void init() {
        usuarioDAO = new UsuarioDAO();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String action = req.getParameter("action");

        if (action == null) action = "listar";

        switch (action) {
            case "novo" -> req.getRequestDispatcher("/usuario/form.jsp").forward(req, resp);
            case "editar" -> {
                int id = Integer.parseInt(req.getParameter("id"));
                Usuario usuario = usuarioDAO.buscarPorId(id);
                req.setAttribute("usuario", usuario);
                req.getRequestDispatcher("/usuario/form.jsp").forward(req, resp);
            }
            case "excluir" -> {
                int id = Integer.parseInt(req.getParameter("id"));
                usuarioDAO.excluir(id);
                resp.sendRedirect("usuarios?action=listar");
            }
            default -> {
                List<Usuario> lista = usuarioDAO.listar();
                req.setAttribute("usuarios", lista);
                req.getRequestDispatcher("/usuario/lista.jsp").forward(req, resp);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String nome = req.getParameter("nome");
        String email = req.getParameter("email");
        String senha = req.getParameter("senha");
        int perfilId = Integer.parseInt(req.getParameter("perfilId"));

        Perfil perfil = new Perfil();
        perfil.setId(perfilId);

        Usuario usuario = new Usuario(nome, email, senha, perfil);
        usuarioDAO.salvarOuAtualizar(usuario);

        resp.sendRedirect("usuarios?action=listar");
    }
}
