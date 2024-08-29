package org.logico.model;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface Eleicao extends Remote {
    void recebeVotos(String nomeCandidato, int votos) throws RemoteException;
    int getVotos(String nomeCandidato) throws RemoteException;
    List<Candidato> listarCandidatos() throws RemoteException;
}
