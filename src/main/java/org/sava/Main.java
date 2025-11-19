package org.sava;

import org.mindrot.jbcrypt.BCrypt;
import org.sava.dao.*;
import org.sava.model.*;
import org.sava.util.HibernateUtil;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Main {
    public static void main(String[] args) {
        try {
            popularDadosIniciais();
            testarListagem();
        } catch (Exception e) {
            System.err.println("Ocorreu um erro fatal durante a execução do Main.");
            e.printStackTrace();
        } finally {
            HibernateUtil.shutdown();
            System.out.println("\n--- Conexão Hibernate encerrada. ---");
        }
    }

    private static void popularDadosIniciais() {
        System.out.println("\n--- INICIALIZANDO DADOS BÁSICOS E DADOS DE TESTE ---");

        PerfilDAO perfilDAO = new PerfilDAO();
        UsuarioDAO usuarioDAO = new UsuarioDAO();
        CursoDAO cursoDAO = new CursoDAO();
        DisciplinaDAO disciplinaDAO = new DisciplinaDAO();
        ProcessoAvaliativoDAO processoDAO = new ProcessoAvaliativoDAO();
        TurmaDAO turmaDAO = new TurmaDAO();

        try {
            Perfil pAdmin = perfilDAO.buscarPorNome("Administrador");
            Perfil pCoord, pProf, pAluno;

            if (pAdmin == null) {
                System.out.println("-> Criando Perfis Padrões...");
                pAdmin = new Perfil("Administrador");
                pCoord = new Perfil("Coordenador");
                pProf = new Perfil("Professor");
                pAluno = new Perfil("Aluno");

                perfilDAO.salvar(pAdmin);
                perfilDAO.salvar(pCoord);
                perfilDAO.salvar(pProf);
                perfilDAO.salvar(pAluno);
            } else {
                System.out.println("-> Perfis já existem. Carregando...");
                pCoord = perfilDAO.buscarPorNome("Coordenador");
                pProf = perfilDAO.buscarPorNome("Professor");
                pAluno = perfilDAO.buscarPorNome("Aluno");
            }

            String senhaAdmin = BCrypt.hashpw("admin123", BCrypt.gensalt());
            String senhaProf = BCrypt.hashpw("prof123", BCrypt.gensalt());
            String senhaAluno = BCrypt.hashpw("aluno123", BCrypt.gensalt());

            if (usuarioDAO.buscarPorEmail("admin@sava.com") == null) {
                System.out.println("-> Criando Usuário Admin...");
                usuarioDAO.salvar(new Usuario("Admin SAVA", "admin@sava.com", senhaAdmin, pAdmin));
            }

            Usuario prof1 = usuarioDAO.buscarPorEmail("carlos@sava.com");
            Usuario prof2 = usuarioDAO.buscarPorEmail("ana@sava.com");
            Usuario prof3 = usuarioDAO.buscarPorEmail("beatriz@sava.com");

            if (prof1 == null) {
                System.out.println("-> Criando Professores...");
                prof1 = new Usuario("Prof. Carlos Silva", "carlos@sava.com", senhaProf, pProf);
                prof2 = new Usuario("Prof. Ana Costa", "ana@sava.com", senhaProf, pProf);
                prof3 = new Usuario("Prof. Beatriz Lima", "beatriz@sava.com", senhaProf, pProf);
                usuarioDAO.salvar(prof1);
                usuarioDAO.salvar(prof2);
                usuarioDAO.salvar(prof3);
            }

            System.out.println("-> Criando Alunos...");
            Usuario[] alunos = new Usuario[15];
            for (int i = 0; i < 15; i++) {
                String email = "aluno" + (i + 1) + "@sava.com";
                alunos[i] = usuarioDAO.buscarPorEmail(email);
                if (alunos[i] == null) {
                    alunos[i] = new Usuario("Aluno " + (i + 1), email, senhaAluno, pAluno);
                    usuarioDAO.salvar(alunos[i]);
                }
            }
            System.out.println("-> " + alunos.length + " Alunos criados/verificados.");

            List<Curso> cursos = cursoDAO.listar();
            if (cursos.isEmpty()) {
                System.out.println("-> Criando 5 Cursos...");
                cursos.add(new Curso("Ciência da Computação"));
                cursos.add(new Curso("Engenharia de Software"));
                cursos.add(new Curso("Sistemas de Informação"));
                cursos.add(new Curso("Análise e Desenv. de Sistemas"));
                cursos.add(new Curso("Redes de Computadores"));
                
                for (Curso c : cursos) {
                    cursoDAO.salvar(c);
                }
            }

            List<ProcessoAvaliativo> processos = processoDAO.listar();
            if (processos.isEmpty()) {
                System.out.println("-> Criando 5 Processos Avaliativos...");
                processos.add(new ProcessoAvaliativo("Avaliação 2024/1", LocalDate.parse("2024-06-01"), LocalDate.parse("2024-06-15")));
                processos.add(new ProcessoAvaliativo("Avaliação 2024/2", LocalDate.parse("2024-11-01"), LocalDate.parse("2024-11-15")));
                processos.add(new ProcessoAvaliativo("Avaliação 2025/1", LocalDate.parse("2025-06-01"), LocalDate.parse("2025-06-15")));
                processos.add(new ProcessoAvaliativo("Avaliação Docente (Extra) 2025", LocalDate.parse("2025-03-01"), LocalDate.parse("2025-03-10")));
                processos.add(new ProcessoAvaliativo("Avaliação Coordenador 2025", LocalDate.parse("2025-03-01"), LocalDate.parse("2025-03-10")));

                for (ProcessoAvaliativo pa : processos) {
                    processoDAO.salvar(pa);
                }
            }

            List<Disciplina> disciplinas = disciplinaDAO.listar();
            if (disciplinas.isEmpty()) {
                System.out.println("-> Criando 10 Disciplinas...");
                Curso c1 = cursoDAO.listar().stream().filter(c -> c.getNome().equals("Ciência da Computação")).findFirst().get();
                Curso c2 = cursoDAO.listar().stream().filter(c -> c.getNome().equals("Engenharia de Software")).findFirst().get();
                Curso c3 = cursoDAO.listar().stream().filter(c -> c.getNome().equals("Sistemas de Informação")).findFirst().get();
                Curso c4 = cursoDAO.listar().stream().filter(c -> c.getNome().equals("Análise e Desenv. de Sistemas")).findFirst().get();
                Curso c5 = cursoDAO.listar().stream().filter(c -> c.getNome().equals("Redes de Computadores")).findFirst().get();

                disciplinas.add(new Disciplina("Programação I", "2024/2", c1));
                disciplinas.add(new Disciplina("Banco de Dados", "2024/2", c1));
                disciplinas.add(new Disciplina("Engenharia de Software I", "2024/2", c2));
                disciplinas.add(new Disciplina("Requisitos de Software", "2024/2", c2));
                disciplinas.add(new Disciplina("Gestão de Projetos", "2024/2", c3));
                disciplinas.add(new Disciplina("Programação Web", "2024/2", c3));
                disciplinas.add(new Disciplina("Lógica de Programação", "2024/2", c4));
                disciplinas.add(new Disciplina("Modelagem de Sistemas", "2024/2", c4));
                disciplinas.add(new Disciplina("Infraestrutura de Redes", "2024/2", c5));
                disciplinas.add(new Disciplina("Segurança da Informação", "2024/2", c5));

                for (Disciplina d : disciplinas) {
                    disciplinaDAO.salvar(d);
                }
            }

            if (turmaDAO.listar().isEmpty()) {
                System.out.println("-> Criando 7 Turmas...");
                Disciplina d1 = disciplinaDAO.listar().stream().filter(d -> d.getNome().equals("Programação I")).findFirst().get();
                Disciplina d2 = disciplinaDAO.listar().stream().filter(d -> d.getNome().equals("Banco de Dados")).findFirst().get();
                Disciplina d3 = disciplinaDAO.listar().stream().filter(d -> d.getNome().equals("Engenharia de Software I")).findFirst().get();
                Disciplina d4 = disciplinaDAO.listar().stream().filter(d -> d.getNome().equals("Requisitos de Software")).findFirst().get();
                Disciplina d5 = disciplinaDAO.listar().stream().filter(d -> d.getNome().equals("Gestão de Projetos")).findFirst().get();
                Disciplina d6 = disciplinaDAO.listar().stream().filter(d -> d.getNome().equals("Programação Web")).findFirst().get();
                Disciplina d7 = disciplinaDAO.listar().stream().filter(d -> d.getNome().equals("Lógica de Programação")).findFirst().get();

                Turma t1 = new Turma("2024/2", d1);
                t1.getProfessores().add(prof1);
                t1.getAlunos().addAll(Set.of(alunos[0], alunos[1], alunos[2], alunos[3], alunos[4]));
                turmaDAO.salvar(t1);

                Turma t2 = new Turma("2024/2", d2);
                t2.getProfessores().add(prof2);
                t2.getAlunos().addAll(Set.of(alunos[5], alunos[6], alunos[7]));
                turmaDAO.salvar(t2);

                Turma t3 = new Turma("2024/2", d3);
                t3.getProfessores().add(prof1);
                t3.getProfessores().add(prof3);
                t3.getAlunos().addAll(Set.of(alunos[0], alunos[2], alunos[4], alunos[6], alunos[8]));
                turmaDAO.salvar(t3);

                Turma t4 = new Turma("2024/2", d4);
                t4.getProfessores().add(prof3);
                t4.getAlunos().addAll(Set.of(alunos[1], alunos[3], alunos[5], alunos[7], alunos[9]));
                turmaDAO.salvar(t4);

                Turma t5 = new Turma("2024/2", d5);
                t5.getProfessores().add(prof2);
                t5.getAlunos().addAll(Set.of(alunos[10], alunos[11], alunos[12]));
                turmaDAO.salvar(t5);

                Turma t6 = new Turma("2024/2", d6);
                t6.getProfessores().add(prof1);
                t6.getAlunos().addAll(Set.of(alunos[13], alunos[14], alunos[0]));
                turmaDAO.salvar(t6);

                Turma t7 = new Turma("2024/2", d7);
                t7.getProfessores().add(prof3);
                t7.getAlunos().addAll(Set.of(alunos[1], alunos[4], alunos[9], alunos[12], alunos[14]));
                turmaDAO.salvar(t7);
            }
            
            System.out.println("\n--- População de dados concluída. ---");

        } catch (Exception e) {
            System.err.println("ERRO: Falha ao inserir dados iniciais.");
            e.printStackTrace();
        }
    }

    private static void testarListagem() {
        System.out.println("\n--- VERIFICANDO DADOS NO BANCO ---");

        System.out.println("\nCursos:");
        new CursoDAO().listar().forEach(c -> System.out.println("-> " + c.getNome()));

        System.out.println("\nProcessos Avaliativos:");
        new ProcessoAvaliativoDAO().listar().forEach(p -> System.out.println("-> " + p.getNome()));
        
        System.out.println("\nDisciplinas:");
        new DisciplinaDAO().listar().forEach(d -> System.out.println("-> " + d.getNome() + " (Curso: " + d.getCurso().getNome() + ")"));
        
        System.out.println("\nUsuários:");
        new UsuarioDAO().listar().forEach(u -> System.out.println("-> " + u.getNome() + " (Perfil: " + u.getPerfil().getNome() + ")"));

        System.out.println("\nTurmas:");
        new TurmaDAO().listar().forEach(t -> {
            System.out.println("-> Disciplina: " + t.getDisciplina().getNome() + " | Alunos: " + t.getAlunos().size());
        });

        System.out.println("\n--- FIM DA EXECUÇÃO DO MAIN ---");
    }
}