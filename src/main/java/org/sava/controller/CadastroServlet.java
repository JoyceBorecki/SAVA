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

    /**
     * O doGet é apenas para redirecionar o usuário para a página de login, 
     * se tentarem acessar /cadastro diretamente.
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.sendRedirect("login"); 
    }

    /**
     * doPost é usado para processar a submissão do formulário de cadastro.
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String nome = req.getParameter("nome");
        String email = req.getParameter("email");
        String senha = req.getParameter("senha");
        int perfilId = Integer.parseInt(req.getParameter("perfilId")); // O ID do Perfil 'Aluno'

        // 1. Validação de existência
        if (usuarioDAO.buscarPorEmail(email) != null) {
            // Se o usuário já existe, envia erro de volta para o login
            req.setAttribute("erro", "Erro: Este e-mail já está cadastrado no sistema.");
            req.setAttribute("perfis", perfilDAO.listar()); // Recarrega os perfis
            req.setAttribute("mostrarCadastro", true); // Mantém o painel de cadastro aberto
            req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
            return;
        }

        // 2. Busca o objeto Perfil (deve ser o 'Aluno')
        Perfil perfil = perfilDAO.buscarPorId(perfilId);
        
        // 3. Cria e salva o novo usuário
        Usuario novoUsuario = new Usuario(nome, email, senha, perfil);
        usuarioDAO.salvar(novoUsuario);

        // 4. Sucesso: Redireciona para o login com uma mensagem de sucesso
        resp.sendRedirect("login?cadastroSucesso=true");
    }
}