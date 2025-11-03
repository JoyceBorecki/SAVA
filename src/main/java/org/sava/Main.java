package org.sava;

import org.sava.dao.CursoDAO;
import org.sava.dao.PerfilDAO;
import org.sava.dao.UsuarioDAO;
import org.sava.model.Curso;
import org.sava.model.Perfil;
import org.sava.model.Usuario;

public class Main {
    public static void main(String[] args) {
        PerfilDAO perfilDAO = new PerfilDAO();
        UsuarioDAO usuarioDAO = new UsuarioDAO();
        CursoDAO cursoDAO = new CursoDAO();

        Perfil p1 = new Perfil("Administrador");
        perfilDAO.salvar(p1);

        Curso c1 = new Curso("Análise e Desenvolvimento de Sistemas");
        cursoDAO.salvar(c1);

        Usuario u1 = new Usuario("Teste", "teste@sava.com", "123456", p1);
        usuarioDAO.salvar(u1);

        System.out.println("\n Lista de Perfis:");
        perfilDAO.listar().forEach(System.out::println);

        System.out.println("\n Lista de Usuários:");
        usuarioDAO.listar().forEach(System.out::println);

        System.out.println("\n Lista de Cursos:");
        cursoDAO.listar().forEach(System.out::println);

        u1.setNome("Joyce Atualizada");
        usuarioDAO.atualizar(u1);

        cursoDAO.excluir(c1.getId());

        System.out.println("\n Executado com sucesso!");
    }
}
