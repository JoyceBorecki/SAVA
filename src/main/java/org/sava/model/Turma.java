package org.sava.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "turmas")
public class Turma {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(length = 20)
    private String semestre;

    // Relacionamento (Muitas-para-Um)
    // A "dona" do relacionamento com Disciplina (resolve o erro em Disciplina.java)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "disciplina_id", nullable = false)
    private Disciplina disciplina;

    // Relacionamento (Muitos-para-Muitos) com Professores
    // (Professores são Usuarios com Perfil "Professor")
    @ManyToMany
    @JoinTable(
            name = "turma_professores", // Tabela pivo
            joinColumns = @JoinColumn(name = "turma_id"),
            inverseJoinColumns = @JoinColumn(name = "professor_id") // ID de Usuario
    )
    private Set<Usuario> professores = new HashSet<>();

    // Relacionamento (Muitos-para-Muitos) com Alunos
    // (Alunos são Usuarios com Perfil "Aluno")
    @ManyToMany
    @JoinTable(
            name = "turma_alunos", // Tabela pivo
            joinColumns = @JoinColumn(name = "turma_id"),
            inverseJoinColumns = @JoinColumn(name = "aluno_id") // ID de Usuario
    )
    private Set<Usuario> alunos = new HashSet<>();

    // Relacionamento (Um-para-Muitos) com Avaliacao
    // (Item 7 do plano)
    @OneToMany(mappedBy = "turma", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Avaliacao> avaliacoes = new ArrayList<>(); // <--- NOVO ERRO ESPERADO AQUI

    // --- Construtores ---
    public Turma() {}

    public Turma(String semestre, Disciplina disciplina) {
        this.semestre = semestre;
        this.disciplina = disciplina;
    }

    // --- Getters e Setters ---
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSemestre() {
        return semestre;
    }

    public void setSemestre(String semestre) {
        this.semestre = semestre;
    }

    public Disciplina getDisciplina() {
        return disciplina;
    }

    public void setDisciplina(Disciplina disciplina) {
        this.disciplina = disciplina;
    }

    public Set<Usuario> getProfessores() {
        return professores;
    }

    public void setProfessores(Set<Usuario> professores) {
        this.professores = professores;
    }

    public Set<Usuario> getAlunos() {
        return alunos;
    }

    public void setAlunos(Set<Usuario> alunos) {
        this.alunos = alunos;
    }

    public List<Avaliacao> getAvaliacoes() {
        return avaliacoes;
    }

    public void setAvaliacoes(List<Avaliacao> avaliacoes) {
        this.avaliacoes = avaliacoes;
    }

    @Override
    public String toString() {
        return disciplina.getNome() + " - Semestre: " + semestre;
    }
}