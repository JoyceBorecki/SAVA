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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "formulario_id", nullable = false)
    private Formulario formulario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "turma_id", nullable = false)
    private Turma turma;

    @OneToMany(mappedBy = "avaliacao", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AvaliacaoRespondida> avaliacoesRespondidas = new ArrayList<>();

    public Avaliacao() {}

    public Avaliacao(Formulario formulario, Turma turma) {
        this.formulario = formulario;
        this.turma = turma;
    }

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