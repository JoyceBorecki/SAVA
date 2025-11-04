package org.sava.model;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "alternativas")
public class Alternativa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, length = 500)
    private String texto;

    // Relacionamento (Muitas-para-Um)
    // Resolve o erro na lista "alternativas" de Questao.java
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "questao_id", nullable = false)
    private Questao questao;

    // Relacionamento (Muitos-para-Muitos) com Resposta
    // (Item 9 do plano)
    @ManyToMany(mappedBy = "alternativasMarcadas")
    private Set<Resposta> respostas = new HashSet<>(); // <--- NOVO ERRO ESPERADO AQUI

    // --- Construtores ---
    public Alternativa() {}

    public Alternativa(String texto, Questao questao) {
        this.texto = texto;
        this.questao = questao;
    }

    // --- Getters e Setters ---
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public Questao getQuestao() {
        return questao;
    }

    public void setQuestao(Questao questao) {
        this.questao = questao;
    }

    public Set<Resposta> getRespostas() {
        return respostas;
    }

    public void setRespostas(Set<Resposta> respostas) {
        this.respostas = respostas;
    }

    @Override
    public String toString() {
        return texto;
    }
}