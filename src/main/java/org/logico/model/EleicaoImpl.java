package org.logico.model;

import com.google.gson.Gson;

import java.io.FileWriter;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

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
        }, 0, 5000); // Executa a cada 5 segundos

        // Registro do shutdown hook
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
            Map<Integer, Integer> resultados = new HashMap<>();
            for (Candidato candidato : candidatosManager.listarCandidatos()) {
                int numeroPartido = candidato.getPartido();
                int totalVotos = votos.getOrDefault(candidato.getNome(), 0);
                resultados.put(numeroPartido, totalVotos);
            }
            Gson gson = new Gson();
            gson.toJson(resultados, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
