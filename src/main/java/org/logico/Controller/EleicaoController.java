package org.logico.Controller;

import org.logico.View.EleicaoView;
import org.logico.model.Eleicao;

import java.rmi.RemoteException;

public class EleicaoController {
    private final EleicaoView view;
    private final Eleicao model;

    public EleicaoController(EleicaoView view, Eleicao model) {
        this.view = view;
        this.model = model;
    }

    public void iniciar() {
        while (true) {
            String nomeCandidato = view.getNomeCandidato();
            if ("0".equals(nomeCandidato)) {
                break;
            }

            int votos = view.getNumeroVotos();

            try {
                model.recebeVotos(nomeCandidato, votos);
                view.mostrarMensagem("Votos foram contabilizados");
                int totalVotos = model.getVotos(nomeCandidato);
                view.mostrarVotos(nomeCandidato, totalVotos);
            } catch (RemoteException e) {
                view.mostrarMensagem("Erro ao se comunicar com o servidor.");
                e.printStackTrace();
            }
        }
    }
}