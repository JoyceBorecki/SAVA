package org.sava.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.sava.model.Avaliacao;
import org.sava.util.HibernateUtil;

import java.util.List;

public class AvaliacaoDAO {

    public void salvar(Avaliacao avaliacao) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.persist(avaliacao);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
    }

    public Avaliacao buscarPorId(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Avaliacao.class, id);
        }
    }

    public List<Avaliacao> listar() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // Traz a avaliação já com o formulário e a turma
            return session.createQuery("from Avaliacao a join fetch a.formulario join fetch a.turma", Avaliacao.class).list();
        }
    }

    public void atualizar(Avaliacao avaliacao) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.merge(avaliacao);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
    }

    public void excluir(int id) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            Avaliacao avaliacao = session.get(Avaliacao.class, id);
            if (avaliacao != null) session.remove(avaliacao);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
    }

    public void salvarOuAtualizar(Avaliacao avaliacao) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.merge(avaliacao);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
    }

    /**
     * Novo método para o DashboardServlet.
     * Lista todas as avaliações (Formulario + Turma)
     * às quais um aluno específico está vinculado (via matrícula na turma).
     * RF12 - Aluno deve ter acesso apenas às avaliações das turmas em que está matriculado.
     */
    public List<Avaliacao> listarPorAlunoId(int alunoId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // Este HQL navega:
            // 1. Começa em Avaliacao (a)
            // 2. Entra em Turma (a.turma t)
            // 3. Entra na lista de alunos da turma (t.alunos aluno)
            // 4. Filtra onde o id do aluno bate
            return session.createQuery(
                "select distinct a from Avaliacao a " +
                "join fetch a.formulario " +
                "join fetch a.turma t " +
                "join t.alunos aluno " +
                "where aluno.id = :alunoId", Avaliacao.class)
                .setParameter("alunoId", alunoId)
                .list();
        }
    }
}