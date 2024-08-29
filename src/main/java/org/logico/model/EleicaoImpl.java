package org.logico.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.FileWriter;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

public class EleicaoImpl extends UnicastRemoteObject implements Eleicao {
    private final Map<String, Integer> votos; // Mapeia nome do candidato para votos
    private final CandidatosManager candidatosManager;

    public EleicaoImpl(String caminhoArquivo, Map<String, Integer> votos) throws RemoteException {
        super();
        this.votos = votos != null ? votos : new HashMap<>();
        this.candidatosManager = new CandidatosManager(caminhoArquivo);

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                exibirVotos();
            }
        }, 0, 5000);


        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Salvando resultados antes de encerrar...");
            salvarResultados("C:\\Users\\edufr\\IdeaProjects\\RMi\\src\\main\\java\\org\\logico\\resultados.json");
        }));
    }

    @Override
    public synchronized void recebeVotos(String nomeCandidato, int votos) throws RemoteException {
        if (candidatosManager.candidatoValido(nomeCandidato)) {
            this.votos.put(nomeCandidato, this.votos.getOrDefault(nomeCandidato, 0) + votos);
        } else {
            System.out.println("Nome do candidato inválido.");
        }
    }

    @Override
    public synchronized int getVotos(String nomeCandidato) throws RemoteException {
        return this.votos.getOrDefault(nomeCandidato, 0);
    }

    @Override
    public List<Candidato> listarCandidatos() throws RemoteException {
        return candidatosManager.listarCandidatos();
    }

    private void exibirVotos() {
        System.out.println("Votos atuais:");
        for (Map.Entry<String, Integer> entry : votos.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue() + " votos");
        }
    }

    private void salvarResultados(String caminhoArquivoResultados) {
        try (FileWriter writer = new FileWriter(caminhoArquivoResultados)) {
            JsonObject resultadoJson = new JsonObject();
            JsonArray resultadosArray = new JsonArray();

            // Gera um ID de sessão único para a execução atual
            String sessionId = UUID.randomUUID().toString();

            // Adiciona o ID da sessão no JSON principal
            resultadoJson.addProperty("id_sessao", sessionId);

            // Itera sobre a lista de candidatos e adiciona os resultados
            for (Candidato candidato : candidatosManager.listarCandidatos()) {
                JsonObject candidatoJson = new JsonObject();
                int numeroPartido = candidato.getPartido();
                int totalVotos = votos.getOrDefault(candidato.getNome(), 0);

                candidatoJson.addProperty("nome", candidato.getNome());
                candidatoJson.addProperty("numero_partido", numeroPartido);
                candidatoJson.addProperty("votos", totalVotos);

                resultadosArray.add(candidatoJson);
            }

            // Adiciona o array de resultados ao JSON principal
            resultadoJson.add("resultados", resultadosArray);

            // Serializa o objeto JSON para o arquivo com formatação
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(resultadoJson, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
