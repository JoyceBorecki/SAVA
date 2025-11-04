package org.sava.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "avaliacoes_respondidas")
public class AvaliacaoRespondida {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    // Relacionamento (Muitas-para-Um)
    // Resolve o erro na lista "avaliacoesRespondidas" de Avaliacao.java
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "avaliacao_id", nullable = false)
    private Avaliacao avaliacao; // A qual avaliação esta resposta pertence

    // Relacionamento (Muitas-para-Um)
    // O aluno que respondeu.
    // Importante para RF03, RF11 (mesmo em modo anônimo, registramos quem respondeu)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "aluno_id", nullable = false)
    private Usuario aluno;

    // Relacionamento (Um-para-Muitos) com Resposta
    // (Item 9 do plano - a lista de respostas dadas)
    @OneToMany(mappedBy = "avaliacaoRespondida", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Resposta> respostas = new ArrayList<>(); // <--- ÚLTIMO ERRO ESPERADO AQUI

    // --- Construtores ---
    public AvaliacaoRespondida() {}

    public AvaliacaoRespondida(Avaliacao avaliacao, Usuario aluno) {
        this.avaliacao = avaliacao;
        this.aluno = aluno;
    }

    // --- Getters e Setters ---
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Avaliacao getAvaliacao() {
        return avaliacao;
    }

    public void setAvaliacao(Avaliacao avaliacao) {
        this.avaliacao = avaliacao;
    }

    public Usuario getAluno() {
        return aluno;
    }

    public void setAluno(Usuario aluno) {
        this.aluno = aluno;
    }

    public List<Resposta> getRespostas() {
        return respostas;
    }

    public void setRespostas(List<Resposta> respostas) {
        this.respostas = respostas;
    }
}