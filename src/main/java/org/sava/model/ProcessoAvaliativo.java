package org.sava.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "processos_avaliativos")
public class ProcessoAvaliativo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String nome;

    @Column(name = "data_inicio")
    private LocalDate dataInicio;

    @Column(name = "data_fim")
    private LocalDate dataFim;

    @OneToMany(mappedBy = "processoAvaliativo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Formulario> formularios = new ArrayList<>();

    public ProcessoAvaliativo() {}

    public ProcessoAvaliativo(String nome, LocalDate dataInicio, LocalDate dataFim) {
        this.nome = nome;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public LocalDate getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(LocalDate dataInicio) {
        this.dataInicio = dataInicio;
    }

    public LocalDate getDataFim() {
        return dataFim;
    }

    public void setDataFim(LocalDate dataFim) {
        this.dataFim = dataFim;
    }

    public List<Formulario> getFormularios() {
        return formularios;
    }

    public void setFormularios(List<Formulario> formularios) {
        this.formularios = formularios;
    }

    @Override
    public String toString() {
        return nome;
    }
}