package org.sava.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.mindrot.jbcrypt.BCrypt;
import org.sava.dao.PerfilDAO;
import org.sava.dao.UsuarioDAO;
import org.sava.model.Perfil;
import org.sava.model.Usuario;

import java.io.IOException;
import java.util.List;

@WebServlet("/usuarios")
public class UsuarioServlet extends HttpServlet {

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
        req.setAttribute("usuarios", usuarioDAO.listar());
        req.getRequestDispatcher("/WEB-INF/views/usuario/lista.jsp").forward(req, resp);
    }

    private void carregarPerfis(HttpServletRequest req) {
        req.setAttribute("perfis", perfilDAO.listar());
    }

    private void mostrarFormularioNovo(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        carregarPerfis(req);
        req.setAttribute("usuario", new Usuario());
        req.getRequestDispatcher("/WEB-INF/views/usuario/form.jsp").forward(req, resp);
    }

    private void mostrarFormularioEditar(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        Integer id = Integer.parseInt(req.getParameter("id"));
        Usuario usuario = usuarioDAO.buscarPorId(id);

        carregarPerfis(req);
        req.setAttribute("usuario", usuario);

        req.getRequestDispatcher("/WEB-INF/views/usuario/form.jsp").forward(req, resp);
    }

    private void salvarUsuario(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException {

        String idParam = req.getParameter("id");
        String nome = req.getParameter("nome");
        String email = req.getParameter("email");
        String senha = req.getParameter("senha");
        String confirmarSenha = req.getParameter("confirmarSenha");
        String perfilParam = req.getParameter("perfilId");

        boolean criando = (idParam == null || idParam.isBlank());

        if (nome == null || nome.isBlank()) {
            enviarErro(req, resp, "O campo nome é obrigatório.", nome, email, perfilParam);
            return;
        }

        if (!nome.matches("^[A-Za-zÀ-ÖØ-öø-ÿ ]+$")) {
            enviarErro(req, resp, "O nome não pode conter números ou símbolos.", nome, email, perfilParam);
            return;
        }

        if (email == null || email.isBlank()) {
            enviarErro(req, resp, "O campo e-mail é obrigatório.", nome, email, perfilParam);
            return;
        }

        if (perfilParam == null || perfilParam.isBlank()) {
            enviarErro(req, resp, "Selecione um perfil.", nome, email, perfilParam);
            return;
        }

        if (criando && (senha == null || senha.isBlank())) {
            enviarErro(req, resp, "A senha é obrigatória para novos usuários.", nome, email, perfilParam);
            return;
        }

        if (senha != null && !senha.isBlank()) {
            if (senha.length() < 8) {
                enviarErro(req, resp, "A senha deve ter no mínimo 8 caracteres.", nome, email, perfilParam);
                return;
            }

            if (senha.length() > 50) {
                enviarErro(req, resp, "A senha deve ter no máximo 50 caracteres.", nome, email, perfilParam);
                return;
            }

            if (!senha.equals(confirmarSenha)) {
                enviarErro(req, resp, "As senhas não conferem.", nome, email, perfilParam);
                return;
            }
        }

        Usuario existente = usuarioDAO.buscarPorEmail(email);

        if (existente != null && (criando || !existente.getId().equals(Integer.valueOf(idParam)))) {
            enviarErro(req, resp, "Este e-mail já está sendo usado por outro usuário.", nome, email, perfilParam);
            return;
        }

        Integer perfilId = Integer.parseInt(perfilParam);
        Perfil perfil = perfilDAO.buscarPorId(perfilId);

        Usuario usuario;

        if (!criando) {
            Integer id = Integer.parseInt(idParam);
            usuario = usuarioDAO.buscarPorId(id);

            usuario.setNome(nome);
            usuario.setEmail(email);
            usuario.setPerfil(perfil);

            if (senha != null && !senha.isBlank()) {
                usuario.setSenha(BCrypt.hashpw(senha, BCrypt.gensalt()));
            }

        } else {
            String senhaHash = BCrypt.hashpw(senha, BCrypt.gensalt());
            usuario = new Usuario(nome, email, senhaHash, perfil);
        }

        usuarioDAO.salvarOuAtualizar(usuario);
        resp.sendRedirect("usuarios?action=listar");
    }

    private void excluirUsuario(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        Integer id = Integer.parseInt(req.getParameter("id"));
        usuarioDAO.excluir(id);
        resp.sendRedirect("usuarios?action=listar");
    }

    private void enviarErro(HttpServletRequest req, HttpServletResponse resp, String mensagem, String nome, String email, String perfilId)
            throws ServletException, IOException {

        req.setAttribute("erro", mensagem);

        Usuario u = new Usuario();
        u.setNome(nome);
        u.setEmail(email);

        if (perfilId != null && !perfilId.isBlank()) {
            u.setPerfil(perfilDAO.buscarPorId(Integer.parseInt(perfilId)));
        }

        req.setAttribute("usuario", u);
        carregarPerfis(req);

        req.getRequestDispatcher("/WEB-INF/views/usuario/form.jsp").forward(req, resp);
    }
}