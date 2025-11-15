package org.sava.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.sava.model.Formulario;
import org.sava.model.Perfil;
import org.sava.util.HibernateUtil;

import java.util.List;

public class FormularioDAO {

    public void salvar(Formulario formulario) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.persist(formulario);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
    }

    public Formulario buscarPorId(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // Busca o formulário e já carrega os perfis (versão padrão)
            return session.createQuery(
                "from Formulario f left join fetch f.perfisDestinados where f.id = :id", Formulario.class)
                .setParameter("id", id)
                .uniqueResult();
        }
    }
    
    /**
     * NOVO MÉTODO CORRIGIDO: Busca o formulário com suas Questões e ProcessoAvaliativo
     * para evitar LazyInitializationException na tela de gerenciamento de questões.
     */
    public Formulario buscarPorIdComQuestoes(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // Usa distinct para evitar duplicação de resultados devido ao join fetch
            return session.createQuery(
                "select distinct f from Formulario f " +
                "join fetch f.processoAvaliativo " + // Carrega o Processo (necessário para o título)
                "left join fetch f.questoes q " + // Carrega as Questões
                "left join fetch q.alternativas " + // Carrega as Alternativas das Questões
                "where f.id = :id", Formulario.class)
                .setParameter("id", id)
                .uniqueResult();
        }
    }

    /**
     * CORRIGIDO: Usa FETCH JOIN para carregar o Processo Avaliativo, evitando LazyInitializationException.
     */
    public List<Formulario> listar() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from Formulario f join fetch f.processoAvaliativo", Formulario.class).list();
        }
    }

    public void atualizar(Formulario formulario) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.merge(formulario);
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
            Formulario formulario = session.get(Formulario.class, id);
            if (formulario != null) session.remove(formulario);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
    }

    public void salvarOuAtualizar(Formulario formulario) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.merge(formulario);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
    }

    public Formulario buscarPorIdComAvaliacoes(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                 "select distinct f from Formulario f " +
                 "left join fetch f.processoAvaliativo " +
                 "left join fetch f.perfisDestinados " +
                 "left join fetch f.avaliacoes a " +
                 "left join fetch a.turma " +
                 "where f.id = :id", Formulario.class)
                    .setParameter("id", id)
                    .uniqueResult();
        }
    }
}