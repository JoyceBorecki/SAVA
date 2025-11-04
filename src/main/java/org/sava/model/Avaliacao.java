package org.sava.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "avaliacoes")
public class Avaliacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    // Relacionamento (Muitas-para-Um)
    // Resolve o erro na lista "avaliacoes" de Formulario.java
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "formulario_id", nullable = false)
    private Formulario formulario; // O formulário que foi aplicado

    // Relacionamento (Muitas-para-Um)
    // Resolve o erro na lista "avaliacoes" de Turma.java
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "turma_id", nullable = false)
    private Turma turma; // A turma que foi avaliada

    // Relacionamento (Um-para-Muitos) com AvaliacaoRespondida
    // (Item 8 do plano)
    @OneToMany(mappedBy = "avaliacao", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AvaliacaoRespondida> avaliacoesRespondidas = new ArrayList<>(); // <--- NOVO ERRO ESPERADO AQUI

    // --- Construtores ---
    public Avaliacao() {}

    public Avaliacao(Formulario formulario, Turma turma) {
        this.formulario = formulario;
        this.turma = turma;
    }

    // --- Getters e Setters ---
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Formulario getFormulario() {
        return formulario;
    }

    public void setFormulario(Formulario formulario) {
        this.formulario = formulario;
    }

    public Turma getTurma() {
        return turma;
    }

    public void setTurma(Turma turma) {
        this.turma = turma;
    }

    public List<AvaliacaoRespondida> getAvaliacoesRespondidas() {
        return avaliacoesRespondidas;
    }

    public void setAvaliacoesRespondidas(List<AvaliacaoRespondida> avaliacoesRespondidas) {
        this.avaliacoesRespondidas = avaliacoesRespondidas;
    }
}