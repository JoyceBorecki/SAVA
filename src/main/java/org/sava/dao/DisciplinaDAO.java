package org.sava.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.sava.model.Disciplina;
import org.sava.util.HibernateUtil;

import java.util.List;

public class DisciplinaDAO {

    public void salvar(Disciplina disciplina) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.persist(disciplina);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
    }

    public Disciplina buscarPorId(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Disciplina.class, id);
        }
    }

    public List<Disciplina> listar() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                "from Disciplina d join fetch d.curso", Disciplina.class)
                .list();
        }
    }

    public void atualizar(Disciplina disciplina) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.merge(disciplina);
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
            Disciplina disciplina = session.get(Disciplina.class, id);
            if (disciplina != null) session.remove(disciplina);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
    }

    public void salvarOuAtualizar(Disciplina disciplina) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.merge(disciplina);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
    }
}