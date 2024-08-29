package org.logico.model;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EleicaoImpl extends UnicastRemoteObject implements Eleicao {
    private final Map<String, Integer> votos;
    private final CandidatosManager candidatosManager;

    public EleicaoImpl() throws RemoteException {
        super();
        this.votos = new HashMap<>();
        // Atualize o caminho para o arquivo JSON
        String caminhoAbsoluto = "C:/Users/edufr/IdeaProjects/RMi/src/main/java/org/logico/data.json";
        this.candidatosManager = new CandidatosManager(caminhoAbsoluto);
    }

    @Override
    public synchronized void recebeVotos(String nomeCandidato, int votos) throws RemoteException {
        if (candidatosManager.candidatoValido(nomeCandidato)) {
            this.votos.put(nomeCandidato, this.votos.getOrDefault(nomeCandidato, 0) + votos);
        } else {
            System.out.println("Obrigado por votar branco.");
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
}
