package org.sava.util;

import org.sava.dao.PerfilDAO;
import org.sava.dao.UsuarioDAO;
import org.sava.model.Perfil;
import org.sava.model.Usuario;

public class SetupUtil {

    public static void inicializarDados() {
        System.out.println("--- Iniciando Setup de Perfis e Admin ---");
        
        PerfilDAO perfilDAO = new PerfilDAO();
        UsuarioDAO usuarioDAO = new UsuarioDAO();

        // 1. Criação dos Perfis (RF01)
        try {
            Perfil admin = new Perfil("Administrador");
            Perfil coord = new Perfil("Coordenador");
            Perfil prof = new Perfil("Professor");
            Perfil aluno = new Perfil("Aluno");
            
            perfilDAO.salvar(admin);
            perfilDAO.salvar(coord);
            perfilDAO.salvar(prof);
            perfilDAO.salvar(aluno);
            
            System.out.println("Perfis padrões criados: 4.");
            
            // 2. Criação do Usuário Administrador (RF02)
            Usuario adminUser = new Usuario("Admin SAVA", "admin@sava.com", "admin123", admin);
            usuarioDAO.salvar(adminUser);
            System.out.println("Usuário Admin criado: admin@sava.com / senha: admin123");
            
        } catch (Exception e) {
            System.err.println("ERRO: Ocorreu um erro ao inserir dados iniciais. Provavelmente os dados já existem.");
            // O erro mais comum aqui é tentar inserir um Perfil com o mesmo nome duas vezes.
        }
    }
}