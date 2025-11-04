package org.sava.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.sava.model.Questao;
import org.sava.util.HibernateUtil;

import java.util.List;

public class QuestaoDAO {

    public void salvar(Questao questao) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.persist(questao);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
    }

    public Questao buscarPorId(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // Inclui fetch join para o formulário e alternativas
            return session.createQuery(
                "from Questao q left join fetch q.alternativas left join fetch q.formulario where q.id = :id", Questao.class)
                .setParameter("id", id)
                .uniqueResult();
        }
    }

    /**
     * CORRIGIDO: Usa FETCH JOIN para carregar as alternativas, evitando LazyInitializationException.
     */
    public List<Questao> listarPorFormularioId(int formularioId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
             return session.createQuery(
                "select distinct q from Questao q " + 
                "left join fetch q.alternativas " + 
                "where q.formulario.id = :id " +
                "order by q.id", Questao.class)
                .setParameter("id", formularioId)
                .list();
        }
    }
    
    // ... (restante do código) ...

    public void atualizar(Questao questao) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.merge(questao);
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
            Questao questao = session.get(Questao.class, id);
            if (questao != null) session.remove(questao);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
    }

    public void salvarOuAtualizar(Questao questao) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.merge(questao);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
    }
}