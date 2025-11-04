package org.sava.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "formularios")
public class Formulario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String titulo;

    @Column(nullable = false)
    private boolean anonimo = false; // RF11: Configuração de anonimato

    // Relacionamento (Muitas-para-Um)
    // Resolve o erro em ProcessoAvaliativo.java
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "processo_avaliativo_id", nullable = false)
    private ProcessoAvaliativo processoAvaliativo;

    // Relacionamento (Um-para-Muitos) com Questao
    // (Item 5 do plano)
    @OneToMany(mappedBy = "formulario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Questao> questoes = new ArrayList<>(); // <--- NOVO ERRO ESPERADO AQUI

    // Relacionamento (Um-para-Muitos) com Avaliacao
    // (Item 7 do plano)
    @OneToMany(mappedBy = "formulario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Avaliacao> avaliacoes = new ArrayList<>(); // <--- NOVO ERRO ESPERADO AQUI

    // Relacionamento (Muitos-para-Muitos) com Perfil
    // RF07: Para quais perfis o formulário é destinado
    @ManyToMany
    @JoinTable(
            name = "formulario_perfis",
            joinColumns = @JoinColumn(name = "formulario_id"),
            inverseJoinColumns = @JoinColumn(name = "perfil_id")
    )
    private Set<Perfil> perfisDestinados = new HashSet<>();

    // --- Construtores ---
    public Formulario() {}

    public Formulario(String titulo, boolean anonimo, ProcessoAvaliativo processoAvaliativo) {
        this.titulo = titulo;
        this.anonimo = anonimo;
        this.processoAvaliativo = processoAvaliativo;
    }

    // --- Getters e Setters ---
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public boolean isAnonimo() {
        return anonimo;
    }

    public void setAnonimo(boolean anonimo) {
        this.anonimo = anonimo;
    }

    public ProcessoAvaliativo getProcessoAvaliativo() {
        return processoAvaliativo;
    }

    public void setProcessoAvaliativo(ProcessoAvaliativo processoAvaliativo) {
        this.processoAvaliativo = processoAvaliativo;
    }

    public List<Questao> getQuestoes() {
        return questoes;
    }

    public void setQuestoes(List<Questao> questoes) {
        this.questoes = questoes;
    }

    public List<Avaliacao> getAvaliacoes() {
        return avaliacoes;
    }

    public void setAvaliacoes(List<Avaliacao> avaliacoes) {
        this.avaliacoes = avaliacoes;
    }

    public Set<Perfil> getPerfisDestinados() {
        return perfisDestinados;
    }

    public void setPerfisDestinados(Set<Perfil> perfisDestinados) {
        this.perfisDestinados = perfisDestinados;
    }

    @Override
    public String toString() {
        return titulo;
    }
}