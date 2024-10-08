package org.logico;

import org.logico.model.Candidato;
import org.logico.model.Eleicao;

import java.rmi.Naming;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try {

            Eleicao eleicao = (Eleicao) Naming.lookup("rmi://localhost/Eleicao");
            Scanner scanner = new Scanner(System.in);

            while (true) {

                System.out.println("Listando candidatos...");
                List<Candidato> candidatos = eleicao.listarCandidatos();

                if (candidatos == null || candidatos.isEmpty()) {
                    System.out.println("Nenhum candidato disponível.");
                    break;
                }


                for (Candidato candidato : candidatos) {
                    System.out.println("Número do partido: " + candidato.getPartido() + " - " + candidato.getNome());
                }


                System.out.println("Digite o número do partido para votar ou qualquer outro número para não votar (0 para sair):");

                int escolha;
                try {
                    escolha = Integer.parseInt(scanner.nextLine());
                } catch (NumberFormatException e) {
                    System.out.println("Entrada inválida. Por favor, digite um número.");
                    continue;
                }


                if (escolha == 0) {
                    System.out.println("Saindo...");
                    break;
                }


                Candidato candidatoEscolhido = candidatos.stream()
                        .filter(candidato -> candidato.getPartido() == escolha)
                        .findFirst()
                        .orElse(null);

                if (candidatoEscolhido == null) {
                    System.out.println("Número de partido inválido. Nenhum voto registrado.");
                    continue;
                }


                eleicao.recebeVotos(candidatoEscolhido.getNome(), 1); // Conta um voto por vez


                int votosAtuais = eleicao.getVotos(candidatoEscolhido.getNome());
                System.out.println("Voto registrado para " + candidatoEscolhido.getNome() + ". Total de votos: " + votosAtuais);
            }

            scanner.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
