package org.sava.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.sava.model.AvaliacaoRespondida;
import org.sava.util.HibernateUtil;

import java.util.List;

public class AvaliacaoRespondidaDAO {

    public void salvar(AvaliacaoRespondida resposta) {
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

    public AvaliacaoRespondida buscarPorId(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // Busca a resposta e já carrega o aluno e as respostas filhas
             return session.createQuery(
                "from AvaliacaoRespondida ar " +
                "left join fetch ar.aluno " +
                "left join fetch ar.respostas " +
                "where ar.id = :id", AvaliacaoRespondida.class)
                .setParameter("id", id)
                .uniqueResult();
        }
    }

    /**
     * Busca se um aluno específico já respondeu uma avaliação específica.
     * Muito útil para o RF13 (garantir que responda apenas uma vez).
     */
    public AvaliacaoRespondida buscarPorAlunoEAvaliacao(int alunoId, int avaliacaoId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                "from AvaliacaoRespondida ar " +
                "where ar.aluno.id = :alunoId and ar.avaliacao.id = :avaliacaoId", 
                AvaliacaoRespondida.class)
                .setParameter("alunoId", alunoId)
                .setParameter("avaliacaoId", avaliacaoId)
                .uniqueResult(); // Retorna um objeto ou null
        }
    }

    public List<AvaliacaoRespondida> listar() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from AvaliacaoRespondida", AvaliacaoRespondida.class).list();
        }
    }

    public void atualizar(AvaliacaoRespondida resposta) {
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
            AvaliacaoRespondida resposta = session.get(AvaliacaoRespondida.class, id);
            if (resposta != null) session.remove(resposta);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
    }

    /**
     * Novo método para o DashboardServlet.
     * Lista todas as avaliações que um aluno específico JÁ respondeu.
     * RF13 - Garante que o aluno responda apenas uma vez.
     */
    public List<AvaliacaoRespondida> listarPorAlunoId(int alunoId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                "from AvaliacaoRespondida ar " +
                "join fetch ar.avaliacao " + // Opcional, mas bom para pegar o ID
                "where ar.aluno.id = :alunoId", 
                AvaliacaoRespondida.class)
                .setParameter("alunoId", alunoId)
                .list();
        }
    }
}