package org.logico;

import org.logico.model.Candidato;
import org.logico.model.Eleicao;

import java.rmi.Naming;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try {
            // Conectar ao servidor RMI
            Eleicao eleicao = (Eleicao) Naming.lookup("rmi://localhost/Eleicao");
            Scanner scanner = new Scanner(System.in);

            // Testar se a conexão está funcionando e listar candidatos
            System.out.println("Testando conexão e listando candidatos...");
            List<Candidato> candidatos = eleicao.listarCandidatos();

            if (candidatos == null || candidatos.isEmpty()) {
                System.out.println("Nenhum candidato disponível.");
                return;
            }

            for (int i = 0; i < candidatos.size(); i++) {
                Candidato candidato = candidatos.get(i);
                System.out.println((i + 1) + ". " + candidato.getNome());
            }

            while (true) {
                System.out.println("Digite o número do candidato para votar ou 0 para sair:");

                int escolha;
                try {
                    escolha = Integer.parseInt(scanner.nextLine());
                } catch (NumberFormatException e) {
                    System.out.println("Entrada inválida. Por favor, digite um número.");
                    continue;
                }

                if (escolha == 0) {
                    break;
                }

                if (escolha < 1 || escolha > candidatos.size()) {
                    System.out.println("Escolha inválida. Tente novamente.");
                    continue;
                }

                Candidato candidatoEscolhido = candidatos.get(escolha - 1);

                System.out.println("Digite o número de votos para " + candidatoEscolhido.getNome() + ":");
                int votos;
                try {
                    votos = Integer.parseInt(scanner.nextLine());
                    if (votos < 0) {
                        System.out.println("O número de votos não pode ser negativo.");
                        continue;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Entrada inválida. Por favor, digite um número.");
                    continue;
                }

                eleicao.recebeVotos(candidatoEscolhido.getNome(), votos);

                int votosAtuais = eleicao.getVotos(candidatoEscolhido.getNome());
                System.out.println("Votos atuais para " + candidatoEscolhido.getNome() + ": " + votosAtuais);
            }

            scanner.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
