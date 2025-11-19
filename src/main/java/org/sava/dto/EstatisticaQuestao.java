package org.sava.dto;

import java.util.HashMap;
import java.util.Map;

public class EstatisticaQuestao {
    private String enunciado;
    private String tipo;
    private int totalRespostas;
    // Mapa: "Texto da Alternativa" -> Quantidade de votos
    private Map<String, Long> contagemAlternativas = new HashMap<>();
    // Lista de respostas textuais (para questões abertas)
    private Map<String, String> respostasAbertas = new HashMap<>(); // Aluno -> Texto

    public EstatisticaQuestao(String enunciado, String tipo) {
        this.enunciado = enunciado;
        this.tipo = tipo;
    }

    public void adicionarVoto(String alternativa, Long qtd) {
        this.contagemAlternativas.put(alternativa, qtd);
        this.totalRespostas += qtd;
    }

    public void adicionarRespostaAberta(String aluno, String texto) {
        this.respostasAbertas.put(aluno, texto);
        this.totalRespostas++;
    }

    // Getters
    public String getEnunciado() { return enunciado; }
    public String getTipo() { return tipo; }
    public int getTotalRespostas() { return totalRespostas; }
    public Map<String, Long> getContagemAlternativas() { return contagemAlternativas; }
    public Map<String, String> getRespostasAbertas() { return respostasAbertas; }

    // Método auxiliar para calcular porcentagem na JSP
    public int getPorcentagem(String alternativa) {
        if (totalRespostas == 0) return 0;
        long votos = contagemAlternativas.getOrDefault(alternativa, 0L);
        return (int) ((votos * 100) / totalRespostas);
    }
}