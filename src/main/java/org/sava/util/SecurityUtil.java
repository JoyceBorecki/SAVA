package org.sava.util;

import jakarta.servlet.http.HttpServletRequest;
import org.sava.model.Usuario;

public class SecurityUtil {

    public static boolean hasRole(HttpServletRequest req, String role) {
        Usuario u = (Usuario) req.getSession().getAttribute("usuarioLogado");
        return u != null && u.getPerfil().getNome().equals(role);
    }
}
