package org.sava.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.sava.model.Usuario;
import java.io.IOException;

@WebFilter("/*")
public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        String path = req.getRequestURI();
        HttpSession session = req.getSession(false);

        Usuario usuario = (session != null) ? (Usuario) session.getAttribute("usuarioLogado") : null;

        // Rotas públicas
        boolean rotaPublica =
                path.endsWith("login") ||
                    path.contains("/assets/") ||
                    path.contains("css") ||
                    path.contains("js");

        if (rotaPublica) {
            chain.doFilter(request, response);
            return;
        }

        // Não logado -> manda pro login
        if (usuario == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String role = usuario.getPerfil().getNome();

        // ADMINISTRADOR   (somente ele pode /usuarios)
        if (path.contains("/usuarios") && !role.equals("Administrador")) {
            req.setAttribute("mensagem403", "Apenas administradores podem acessar esta área.");
            req.getRequestDispatcher("/WEB-INF/views/erro/403.jsp").forward(req, resp);
            return;
        }

        // ADMINISTRADOR ou COORDENADOR → /cursos
        if (path.contains("/cursos") && !(role.equals("Administrador") || role.equals("Coordenador"))) {
            req.setAttribute("mensagem403", "Acesso restrito a administradores e coordenadores.");
            req.getRequestDispatcher("/WEB-INF/views/erro/403.jsp").forward(req, resp);
            return;
        }

        // ADMINISTRADOR ou COORDENADOR → /disciplinas
        if (path.contains("/disciplinas") && !(role.equals("Administrador") || role.equals("Coordenador"))) {
            req.setAttribute("mensagem403", "Acesso restrito a administradores e coordenadores.");
            req.getRequestDispatcher("/WEB-INF/views/erro/403.jsp").forward(req, resp);
            return;
        }

        // ADMINISTRADOR / COORDENADOR / PROFESSOR → /turmas
        if (path.contains("/turmas") && !(role.equals("Administrador") || role.equals("Coordenador") || role.equals("Professor"))) {
            req.setAttribute("mensagem403", "Acesso permitido apenas para administradores, coordenadores e professores.");
            req.getRequestDispatcher("/WEB-INF/views/erro/403.jsp").forward(req, resp);
            return;
        }

        // ADMINISTRADOR / COORDENADOR → /processos
        if (path.contains("/processos") && !(role.equals("Administrador") || role.equals("Coordenador"))) {
            req.setAttribute("mensagem403", "Acesso permitido apenas para administradores e coordenadores.");
            req.getRequestDispatcher("/WEB-INF/views/erro/403.jsp").forward(req, resp);
            return;
        }

        // FORMULÁRIOS → proibido para professor e aluno
        if (path.contains("/formularios")) {
            if (role.equals("Professor") || role.equals("Aluno")) {
                req.setAttribute("mensagem403", "Seu perfil não permite acessar formulários.");
                req.getRequestDispatcher("/WEB-INF/views/erro/403.jsp").forward(req, resp);
                return;
            }
        }

        chain.doFilter(request, response);
    }
}
