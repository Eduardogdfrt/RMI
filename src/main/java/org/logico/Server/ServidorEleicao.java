package org.logico.Server;

import org.logico.model.EleicaoImpl;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

public class ServidorEleicao {
    public static void main(String[] args) {
        try {
            LocateRegistry.createRegistry(1099);
            EleicaoImpl eleicao = new EleicaoImpl();
            Naming.rebind("rmi://localhost:1099/Eleicao", eleicao);
            System.out.println("Servidor de Eleição RMI iniciado.");
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}