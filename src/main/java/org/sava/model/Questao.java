package org.sava.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "questoes")
public class Questao {

    // Enum para RF08 e RF09 (Tipo da questão)
    public enum TipoQuestao {
        ABERTA, // Resposta textual
        UNICA,  // Múltipla escolha, resposta única
        MULTIPLA // Múltipla escolha, várias respostas
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, length = 1000)
    private String enunciado;

    @Column(nullable = false)
    private boolean obrigatoria = true; // RF10

    @Enumerated(EnumType.STRING) // Salva o nome ("ABERTA") no BD
    @Column(nullable = false, length = 20)
    private TipoQuestao tipo;

    // Relacionamento (Muitas-para-Um)
    // Resolve o erro em Formulario.java
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "formulario_id", nullable = false)
    private Formulario formulario;

    // Relacionamento (Um-para-Muitos) com Alternativa
    // (Item 6 do plano)
    @OneToMany(mappedBy = "questao", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Alternativa> alternativas = new ArrayList<>();

    // Relacionamento (Um-para-Muitos) com Resposta
    // (Item 9 do plano)
    @OneToMany(mappedBy = "questao", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Resposta> respostas = new ArrayList<>(); // <--- NOVO ERRO ESPERADO AQUI

    // --- Construtores ---
    public Questao() {}

    public Questao(String enunciado, boolean obrigatoria, TipoQuestao tipo, Formulario formulario) {
        this.enunciado = enunciado;
        this.obrigatoria = obrigatoria;
        this.tipo = tipo;
        this.formulario = formulario;
    }

    // --- Getters e Setters ---
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEnunciado() {
        return enunciado;
    }

    public void setEnunciado(String enunciado) {
        this.enunciado = enunciado;
    }

    public boolean isObrigatoria() {
        return obrigatoria;
    }

    public void setObrigatoria(boolean obrigatoria) {
        this.obrigatoria = obrigatoria;
    }

    public TipoQuestao getTipo() {
        return tipo;
    }

    public void setTipo(TipoQuestao tipo) {
        this.tipo = tipo;
    }

    public Formulario getFormulario() {
        return formulario;
    }

    public void setFormulario(Formulario formulario) {
        this.formulario = formulario;
    }

    public List<Alternativa> getAlternativas() {
        return alternativas;
    }

    public void setAlternativas(List<Alternativa> alternativas) {
        this.alternativas = alternativas;
    }

    public List<Resposta> getRespostas() {
        return respostas;
    }

    public void setRespostas(List<Resposta> respostas) {
        this.respostas = respostas;
    }

    @Override
    public String toString() {
        return enunciado;
    }
}