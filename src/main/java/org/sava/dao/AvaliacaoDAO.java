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
            return session.createQuery(
                 "select distinct a from Avaliacao a " +
                 "join fetch a.formulario f " +
                 "join fetch a.turma t " +
                 "join fetch t.disciplina d " +
                 "left join fetch t.professores prof " +
                 "left join fetch t.alunos aluno " +
                 "where a.id = :id",
                 Avaliacao.class
            )
                 .setParameter("id", id)
                 .uniqueResult();
        }
    }

    public List<Avaliacao> listar() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                "select distinct a from Avaliacao a " +
                "join fetch a.formulario f " +
                "join fetch a.turma t " +
                "join fetch t.disciplina d",
                Avaliacao.class
            ).list();
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

    public List<Avaliacao> listarPorAlunoId(int alunoId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                 "select distinct a from Avaliacao a " +
                 "join fetch a.formulario f " +
                 "join fetch f.processoAvaliativo pa " +
                 "left join f.perfisDestinados perfis " +
                 "join fetch a.turma t " +
                 "join fetch t.disciplina d " +
                 "left join fetch t.professores prof " +
                 "join t.alunos aluno " +
                 "where aluno.id = :alunoId " +
                 "and (perfis is null or perfis.id = aluno.perfil.id)",
                    Avaliacao.class
            )
            .setParameter("alunoId", alunoId)
           .list();
        }
    }
}
