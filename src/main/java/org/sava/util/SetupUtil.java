package org.sava.util;

import org.mindrot.jbcrypt.BCrypt;
import org.sava.dao.PerfilDAO;
import org.sava.dao.UsuarioDAO;
import org.sava.model.Perfil;
import org.sava.model.Usuario;

public class SetupUtil {

    public static void inicializarDados() {
        System.out.println("--- Iniciando Setup de Perfis e Admin ---");

        PerfilDAO perfilDAO = new PerfilDAO();
        UsuarioDAO usuarioDAO = new UsuarioDAO();

        try {
            Perfil admin = new Perfil("Administrador");
            Perfil coord = new Perfil("Coordenador");
            Perfil prof = new Perfil("Professor");
            Perfil aluno = new Perfil("Aluno");

            perfilDAO.salvar(admin);
            perfilDAO.salvar(coord);
            perfilDAO.salvar(prof);
            perfilDAO.salvar(aluno);

            System.out.println("Perfis padrões criados.");

            String hash = BCrypt.hashpw("admin123", BCrypt.gensalt());

            Usuario adminUser = new Usuario(
                    "Admin",
                    "admin@sava.com",
                    hash,
                    admin
            );

            usuarioDAO.salvar(adminUser);

            System.out.println("Usuário Admin criado: admin@sava.com / senha: admin123");

        } catch (Exception e) {
            System.err.println("ERRO: Ao inserir dados iniciais (provavelmente duplicados).");
            e.printStackTrace();
        }
    }
}