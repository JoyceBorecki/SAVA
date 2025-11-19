package org.sava.dao;

import org.hibernate.Session;
import org.sava.dto.EstatisticaQuestao;
import org.sava.model.Questao;
import org.sava.util.HibernateUtil;

import java.util.ArrayList;
import java.util.List;

public class RelatorioDAO {

    public List<EstatisticaQuestao> gerarEstatisticas(int formularioId) {
        List<EstatisticaQuestao> resultado = new ArrayList<>();

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<Questao> questoes = session.createQuery(
                "from Questao q where q.formulario.id = :fid order by q.id", Questao.class)
                .setParameter("fid", formularioId)
                .list();

            for (Questao q : questoes) {
                EstatisticaQuestao est = new EstatisticaQuestao(q.getEnunciado(), q.getTipo().toString());

                if (q.getTipo() == Questao.TipoQuestao.ABERTA) {
                    List<Object[]> respostas = session.createQuery(
                        "select r.avaliacaoRespondida.aluno.nome, r.textoResposta " +
                        "from Resposta r where r.questao.id = :qid", Object[].class)
                        .setParameter("qid", q.getId())
                        .list();
                    
                    for (Object[] row : respostas) {
                        String nomeAluno = (String) row[0];
                        String texto = (String) row[1];
                        est.adicionarRespostaAberta(nomeAluno, texto);
                    }

                } else {
                    List<Object[]> contagem = session.createQuery(
                        "select alt.texto, count(r.id) " +
                        "from Resposta r join r.alternativasMarcadas alt " +
                        "where r.questao.id = :qid " +
                        "group by alt.texto", Object[].class)
                        .setParameter("qid", q.getId())
                        .list();

                    for (Object[] row : contagem) {
                        String alternativa = (String) row[0];
                        Long qtd = (Long) row[1];
                        est.adicionarVoto(alternativa, qtd);
                    }
                }
                resultado.add(est);
            }
        }
        return resultado;
    }
}