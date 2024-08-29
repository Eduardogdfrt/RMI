package org.logico.View;

import java.util.Scanner;

public class EleicaoView {
    private final Scanner scanner;

    public EleicaoView() {
        this.scanner = new Scanner(System.in);
    }

    public String getNomeCandidato() {
        System.out.print("Digite o nome do candidato (ou 0 para sair): ");
        return scanner.nextLine();
    }

    public int getNumeroVotos() {
        System.out.print("Digite o número de votos: ");
        while (!scanner.hasNextInt()) {
            System.out.println("Entrada inválida. Por favor, digite um número.");
            scanner.next(); // Consome a entrada inválida
        }
        return scanner.nextInt();
    }

    public void mostrarMensagem(String mensagem) {
        System.out.println(mensagem);
    }

    public void mostrarVotos(String nomeCandidato, int totalVotos) {
        System.out.println("Total de votos para " + nomeCandidato + ": " + totalVotos);
    }
}
