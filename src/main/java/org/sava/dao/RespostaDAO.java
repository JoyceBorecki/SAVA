package org.sava.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.sava.model.Resposta;
import org.sava.util.HibernateUtil;

import java.util.List;

public class RespostaDAO {

    public void salvar(Resposta resposta) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.persist(resposta);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
    }

    public Resposta buscarPorId(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
             return session.createQuery(
                "from Resposta r " +
                "left join fetch r.alternativasMarcadas " +
                "where r.id = :id", Resposta.class)
                .setParameter("id", id)
                .uniqueResult();
        }
    }

    public List<Resposta> listar() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from Resposta", Resposta.class).list();
        }
    }

    public void atualizar(Resposta resposta) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.merge(resposta);
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
            Resposta resposta = session.get(Resposta.class, id);
            if (resposta != null) session.remove(resposta);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
    }
}