package org.sava.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.sava.dao.PerfilDAO;
import org.sava.dao.UsuarioDAO;
import org.sava.model.Usuario;

import java.io.IOException;
import java.util.List;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private UsuarioDAO usuarioDAO;
    private PerfilDAO perfilDAO;

    @Override
    public void init() {
        usuarioDAO = new UsuarioDAO();
        perfilDAO = new PerfilDAO();
    }

    /**
     * doGet é usado para mostrar a página de login.
     * GARANTE que o painel de cadastro está OCULTO por padrão.
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // --- CORREÇÃO FINAL: GARANTE O ESTADO PADRÃO DE VISUALIZAÇÃO ---
        // Ao renderizar a tela de login via GET, o painel de cadastro deve estar oculto.
        req.setAttribute("mostrarCadastro", false);
        // --- FIM CORREÇÃO ---
        
        // Carrega os perfis (boa prática)
        try {
            // Assumindo que listar retorna List<Perfil> ou similar
            List<?> perfis = perfilDAO.listar(); 
            req.setAttribute("perfis", perfis);
        } catch (Exception e) {
            System.err.println("Erro ao carregar perfis: " + e.getMessage());
        }

        req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
    }

    /**
     * doPost é usado para processar a tentativa de login, 
     * diferenciando erro de senha e erro de e-mail não existente.
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String email = req.getParameter("email");
        String senha = req.getParameter("senha");

        if (email == null || senha == null || email.isEmpty() || senha.isEmpty()) {
            req.setAttribute("erro", "E-mail e senha são obrigatórios.");
            req.setAttribute("mostrarCadastro", false);
            req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
            return;
        }

        Usuario usuario = usuarioDAO.buscarPorEmail(email);

        if (usuario != null) {
            // Usuário ENCONTRADO. Verifica a senha.

            if (usuario.getSenha().equals(senha)) { 
                // SUCESSO!
                HttpSession session = req.getSession(true);
                session.setAttribute("usuarioLogado", usuario);
                resp.sendRedirect("dashboard");

            } else {
                // FALHA! Senha incorreta.
                req.setAttribute("erro", "Senha incorreta. Verifique e tente novamente.");
                req.setAttribute("mostrarCadastro", false); // NÂO mostra o painel de cadastro
                req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
            }

        } else {
            // FALHA! Usuário NÃO encontrado. Sugere cadastro.
            req.setAttribute("erro", "E-mail não encontrado em nosso sistema. Por favor, cadastre-se ao lado.");
            req.setAttribute("emailParaCadastro", email);
            req.setAttribute("mostrarCadastro", true); // ACIONA o painel de cadastro no JSP

            req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
        }
    }
}