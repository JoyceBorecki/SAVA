package org.sava;

import org.mindrot.jbcrypt.BCrypt;
import org.sava.dao.CursoDAO;
import org.sava.dao.PerfilDAO;
import org.sava.dao.UsuarioDAO;
import org.sava.model.Perfil;
import org.sava.model.Usuario;
import org.sava.util.HibernateUtil;

public class Main {
    public static void main(String[] args) {
        try {
            inicializarDadosBasicos();
            testarListagem();
        } catch (Exception e) {
            System.err.println("Ocorreu um erro fatal durante a execução do Main.");
            e.printStackTrace();
        } finally {
            HibernateUtil.shutdown();
            System.out.println("\n--- Conexão Hibernate encerrada. ---");
        }
    }

    private static void inicializarDadosBasicos() {
        System.out.println("\n--- INICIALIZANDO DADOS BÁSICOS (Perfis e Admin) ---");

        PerfilDAO perfilDAO = new PerfilDAO();
        UsuarioDAO usuarioDAO = new UsuarioDAO();

        try {
            Perfil admin = perfilDAO.buscarPorNome("Administrador");
            if (admin == null) {
                perfilDAO.salvar(new Perfil("Administrador"));
                perfilDAO.salvar(new Perfil("Coordenador"));
                perfilDAO.salvar(new Perfil("Professor"));
                perfilDAO.salvar(new Perfil("Aluno"));
                System.out.println("-> Perfis padrões criados.");
            } else {
                System.out.println("-> Perfis já existem.");
            }

            if (usuarioDAO.buscarPorEmail("admin@sava.com") == null) {
                Perfil perfilAdmin = perfilDAO.buscarPorNome("Administrador");

                String senhaHash = BCrypt.hashpw("admin123", BCrypt.gensalt());
                Usuario adminUser = new Usuario(
                        "Admin",
                        "admin@sava.com",
                        senhaHash,
                        perfilAdmin
                );

                usuarioDAO.salvar(adminUser);
                System.out.println("-> Usuário Admin criado: admin@sava.com / senha: admin123");
            } else {
                System.out.println("-> Usuário Admin já existe.");
            }

        } catch (Exception e) {
            System.err.println("ERRO: Falha ao inserir dados iniciais.");
            e.printStackTrace();
        }
    }

    private static void testarListagem() {
        System.out.println("\n--- Listagens ---");

        PerfilDAO perfilDAO = new PerfilDAO();
        UsuarioDAO usuarioDAO = new UsuarioDAO();
        CursoDAO cursoDAO = new CursoDAO();

        System.out.println("\nPerfis:");
        perfilDAO.listar().forEach(p ->
                System.out.println("ID: " + p.getId() + " | Perfil: " + p.getNome())
        );

        System.out.println("\nUsuários:");
        usuarioDAO.listar().forEach(u ->
                System.out.println("ID: " + u.getId() + " | Nome: " + u.getNome() + " | Perfil: " + u.getPerfil().getNome())
        );

        System.out.println("\nCursos:");
        cursoDAO.listar().forEach(c ->
                System.out.println("Curso: " + c.getNome())
        );

        System.out.println("\n--- FIM DA EXECUÇÃO ---");
    }
}