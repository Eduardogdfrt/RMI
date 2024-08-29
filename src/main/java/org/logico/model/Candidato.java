package org.logico.model;

import com.google.gson.Gson;

import java.io.Serializable;

public class Candidato implements Serializable {
    private String nome;
    private int partido;
    private static final long serialVersionUID = 1L;


    public Candidato(String nome, int partido) {
        this.nome = nome;
        this.partido = partido;
    }

    public String getNome() {
        return nome;
    }

    public int getPartido() {
        return partido;
    }

    @Override
    public String toString() {
        return "Candidato{" +
                "nome='" + nome + '\'' +
                ", partido=" + partido +
                '}';
    }

    public static String toJson(Candidato[] candidatos) {
        Gson gson = new Gson();
        return gson.toJson(candidatos);
    }
}
