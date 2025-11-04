package org.sava.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.sava.model.Alternativa;
import org.sava.util.HibernateUtil;

import java.util.List;

public class AlternativaDAO {

    public void salvar(Alternativa alternativa) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.persist(alternativa);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
    }

    public Alternativa buscarPorId(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Alternativa.class, id);
        }
    }

    public List<Alternativa> listarPorQuestaoId(int questaoId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from Alternativa where questao.id = :id", Alternativa.class)
                    .setParameter("id", questaoId)
                    .list();
        }
    }

    public void atualizar(Alternativa alternativa) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.merge(alternativa);
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
            Alternativa alternativa = session.get(Alternativa.class, id);
            if (alternativa != null) session.remove(alternativa);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
    }
}