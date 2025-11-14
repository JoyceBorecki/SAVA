package org.sava.model;

import jakarta.persistence.*;
import java.util.HashSet;
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
    private boolean anonimo = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "processo_avaliativo_id", nullable = false)
    private ProcessoAvaliativo processoAvaliativo;

    @OneToMany(mappedBy = "formulario", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Questao> questoes = new HashSet<>();

    @OneToMany(mappedBy = "formulario", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Avaliacao> avaliacoes = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "formulario_perfis",
            joinColumns = @JoinColumn(name = "formulario_id"),
            inverseJoinColumns = @JoinColumn(name = "perfil_id")
    )
    private Set<Perfil> perfisDestinados = new HashSet<>();

    public Formulario() {}

    public Formulario(String titulo, boolean anonimo, ProcessoAvaliativo processoAvaliativo) {
        this.titulo = titulo;
        this.anonimo = anonimo;
        this.processoAvaliativo = processoAvaliativo;
    }

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

    public Set<Questao> getQuestoes() {
        return questoes;
    }

    public void setQuestoes(Set<Questao> questoes) {
        this.questoes = questoes;
    }

    public Set<Avaliacao> getAvaliacoes() {
        return avaliacoes;
    }

    public void setAvaliacoes(Set<Avaliacao> avaliacoes) {
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