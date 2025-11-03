package org.sava.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.sava.model.Perfil;
import org.sava.util.HibernateUtil;

import java.util.List;

public class PerfilDAO {

    public void salvar(Perfil perfil) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.persist(perfil);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
    }

    public Perfil buscarPorId(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Perfil.class, id);
        }
    }

    public List<Perfil> listar() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from Perfil", Perfil.class).list();
        }
    }

    public void atualizar(Perfil perfil) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.merge(perfil);
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
            Perfil perfil = session.get(Perfil.class, id);
            if (perfil != null) session.remove(perfil);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
    }
}
