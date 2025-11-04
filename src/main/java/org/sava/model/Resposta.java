package org.sava.model;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "respostas")
public class Resposta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    // Para RF09 (Questões Abertas)
    @Column(name = "texto_resposta", columnDefinition = "TEXT")
    private String textoResposta;

    // Relacionamento (Muitas-para-Um)
    // Resolve o erro na lista "respostas" de AvaliacaoRespondida.java
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "avaliacao_respondida_id", nullable = false)
    private AvaliacaoRespondida avaliacaoRespondida;

    // Relacionamento (Muitas-para-Um)
    // Resolve o erro na lista "respostas" de Questao.java
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "questao_id", nullable = false)
    private Questao questao;

    // Relacionamento (Muitos-para-Muitos)
    // Para RF08 (Questões Única e Múltipla)
    // Resolve o erro no set "respostas" de Alternativa.java
    @ManyToMany
    @JoinTable(
            name = "resposta_alternativas",
            joinColumns = @JoinColumn(name = "resposta_id"),
            inverseJoinColumns = @JoinColumn(name = "alternativa_id")
    )
    private Set<Alternativa> alternativasMarcadas = new HashSet<>();

    // --- Construtores ---
    public Resposta() {}

    public Resposta(AvaliacaoRespondida avaliacaoRespondida, Questao questao) {
        this.avaliacaoRespondida = avaliacaoRespondida;
        this.questao = questao;
    }

    // --- Getters e Setters ---
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTextoResposta() {
        return textoResposta;
    }

    public void setTextoResposta(String textoResposta) {
        this.textoResposta = textoResposta;
    }

    public AvaliacaoRespondida getAvaliacaoRespondida() {
        return avaliacaoRespondida;
    }

    public void setAvaliacaoRespondida(AvaliacaoRespondida avaliacaoRespondida) {
        this.avaliacaoRespondida = avaliacaoRespondida;
    }

    public Questao getQuestao() {
        return questao;
    }

    public void setQuestao(Questao questao) {
        this.questao = questao;
    }

    public Set<Alternativa> getAlternativasMarcadas() {
        return alternativasMarcadas;
    }

    public void setAlternativasMarcadas(Set<Alternativa> alternativasMarcadas) {
        this.alternativasMarcadas = alternativasMarcadas;
    }
}