package org.logico.model;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CandidatosManager {
    private final List<Candidato> candidatos;

    public CandidatosManager(String caminhoArquivo) {
        this.candidatos = new ArrayList<>();
        carregarCandidatos(caminhoArquivo);
    }

    private void carregarCandidatos(String caminhoArquivo) {
        try (FileReader reader = new FileReader(caminhoArquivo)) {
            JsonObject jsonObject = JsonParser.parseReader(reader).getAsJsonObject();
            JsonArray jsonArray = jsonObject.getAsJsonArray("candidatos");
            Gson gson = new Gson();
            for (int i = 0; i < jsonArray.size(); i++) {
                Candidato candidato = gson.fromJson(jsonArray.get(i), Candidato.class);
                candidatos.add(candidato);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Candidato> listarCandidatos() {
        return candidatos;
    }

    public boolean candidatoValido(String nome) {
        return candidatos.stream().anyMatch(candidato -> candidato.getNome().equals(nome));
    }

    public int obterNumeroPartido(String nome) {
        return candidatos.stream()
                .filter(candidato -> candidato.getNome().equals(nome))
                .map(Candidato::getPartido)
                .findFirst()
                .orElse(-1); // Retorna -1 se não encontrar o candidato
    }
}
