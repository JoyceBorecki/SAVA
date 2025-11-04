package org.sava;

import org.sava.dao.CursoDAO;
import org.sava.dao.PerfilDAO;
import org.sava.dao.UsuarioDAO;
import org.sava.model.Perfil;
import org.sava.model.Usuario;
import org.sava.util.HibernateUtil; // Importar

public class Main {
    
    public static void main(String[] args) {
        try {
            // 1. Inicializa o ambiente e carrega dados básicos (Perfís e 1 Admin)
            inicializarDadosBasicos();

            // 2. Executa um pequeno teste de listagem
            testarListagem();

        } catch (Exception e) {
            System.err.println("Ocorreu um erro fatal durante a execução do Main. Verifique o log.");
            e.printStackTrace();
        } finally {
            // 3. Garante que o Hibernate feche a SessionFactory
            HibernateUtil.shutdown(); 
            System.out.println("\n--- Conexão Hibernate encerrada. ---");
        }
    }

    /**
     * Inicializa os perfis obrigatórios (RF01) e o usuário admin (RF02).
     */
    private static void inicializarDadosBasicos() {
        System.out.println("\n--- INICIALIZANDO DADOS BÁSICOS (Perfis e Admin) ---");
        PerfilDAO perfilDAO = new PerfilDAO();
        UsuarioDAO usuarioDAO = new UsuarioDAO();

        try {
            // Tenta buscar o Administrador. Se for nulo, cria todos os perfis.
            Perfil admin = perfilDAO.buscarPorNome("Administrador");
            if (admin == null) {
                admin = new Perfil("Administrador");
                perfilDAO.salvar(admin);
                perfilDAO.salvar(new Perfil("Coordenador"));
                perfilDAO.salvar(new Perfil("Professor"));
                perfilDAO.salvar(new Perfil("Aluno"));
                System.out.println("-> Perfis padrões criados com sucesso.");
            } else {
                 System.out.println("-> Perfis padrões já existem.");
            }

            // Criação do Usuário Administrador (só se não existir um com o mesmo e-mail)
            if (usuarioDAO.buscarPorEmail("admin@sava.com") == null) {
                // Busca o perfil admin novamente (pode ter sido salvo na mesma sessão)
                Perfil perfilAdmin = perfilDAO.buscarPorNome("Administrador"); 
                Usuario adminUser = new Usuario("Admin SAVA", "admin@sava.com", "admin123", perfilAdmin);
                usuarioDAO.salvar(adminUser);
                System.out.println("-> Usuário Admin criado com sucesso: admin@sava.com / 123");
            } else {
                System.out.println("-> Usuário Admin já existe.");
            }
        } catch (Exception e) {
            System.err.println("ERRO: Falha na inserção de dados iniciais.");
        }
    }

    /**
     * Roda as listagens para conferência.
     */
    private static void testarListagem() {
        // Agora o CRUD de listagem funciona com os dados reais
        PerfilDAO perfilDAO = new PerfilDAO();
        UsuarioDAO usuarioDAO = new UsuarioDAO();
        CursoDAO cursoDAO = new CursoDAO();

        System.out.println("\n Lista de Perfis:");
        perfilDAO.listar().forEach(p -> System.out.println("ID: " + p.getId() + ", Nome: " + p.getNome()));

        System.out.println("\n Lista de Usuários:");
        usuarioDAO.listar().forEach(u -> System.out.println("ID: " + u.getId() + ", Nome: " + u.getNome() + ", Perfil: " + u.getPerfil().getNome()));

        System.out.println("\n Lista de Cursos:");
        cursoDAO.listar().forEach(c -> System.out.println("Curso: " + c.getNome()));
        
        System.out.println("\n--- FIM DA EXECUÇÃO ---");
    }
}