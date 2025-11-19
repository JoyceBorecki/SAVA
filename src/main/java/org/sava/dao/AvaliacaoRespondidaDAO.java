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
             return session.createQuery(
                "from AvaliacaoRespondida ar " +
                "left join fetch ar.aluno " +
                "left join fetch ar.respostas " +
                "where ar.id = :id", AvaliacaoRespondida.class)
                .setParameter("id", id)
                .uniqueResult();
        }
    }

    public AvaliacaoRespondida buscarPorAlunoEAvaliacao(int alunoId, int avaliacaoId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                "from AvaliacaoRespondida ar " +
                "where ar.aluno.id = :alunoId and ar.avaliacao.id = :avaliacaoId", 
                AvaliacaoRespondida.class)
                .setParameter("alunoId", alunoId)
                .setParameter("avaliacaoId", avaliacaoId)
                .uniqueResult();
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

    public List<AvaliacaoRespondida> listarPorAlunoId(int alunoId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                 "select distinct ar from AvaliacaoRespondida ar " +
                 "join fetch ar.avaliacao a " +
                 "join fetch a.formulario f " +
                 "join fetch a.turma t " +
                 "left join fetch t.professores " +
                 "where ar.aluno.id = :alunoId",
                    AvaliacaoRespondida.class)
                    .setParameter("alunoId", alunoId)
                    .list();
        }
    }
}