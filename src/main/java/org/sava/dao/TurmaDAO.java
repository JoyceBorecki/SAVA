package org.sava.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.sava.model.Turma;
import org.sava.util.HibernateUtil;

import java.util.List;

public class TurmaDAO {

    public void salvar(Turma turma) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.persist(turma);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
    }

    public Turma buscarPorId(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                "select t from Turma t " +
                   "left join fetch t.professores " +
                   "left join fetch t.alunos " +
                   "left join fetch t.disciplina " +
                   "where t.id = :id", Turma.class)
                   .setParameter("id", id)
                   .uniqueResult();
        }
    }

    public List<Turma> listar() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                "select distinct t from Turma t " +
                "left join fetch t.disciplina " +
                "left join fetch t.alunos",
                 Turma.class
            ).list();
        }
    }

    public void atualizar(Turma turma) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.merge(turma);
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
            Turma turma = session.get(Turma.class, id);
            if (turma != null) session.remove(turma);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
    }

    public void salvarOuAtualizar(Turma turma) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.merge(turma);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
    }
}