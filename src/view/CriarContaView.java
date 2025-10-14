package view;

import java.util.Scanner;
import model.entt.Account;
import model.excp.DomainException;
import service.BankService;

public class CriarContaView {
    private final Scanner sc = new Scanner(System.in);
    private final BankService bank;

    public CriarContaView(BankService bank) {
        this.bank = bank;
    }

    public void exibir() {
        try {
            System.out.println("\n=== CRIAÇÃO DE CONTA ===");
            System.out.print("Nome: ");
            String nome = sc.nextLine();
            System.out.print("CPF: ");
            String cpf = sc.nextLine();
            if (bank.buscarConta(cpf) != null) {
                System.out.println("Erro: CPF já vinculado a uma conta");
                return;
            }
            System.out.print("Senha: ");
            String senha = sc.nextLine();

            System.out.print("Tipo de conta (corrente/poupanca): ");
            String tipo = sc.nextLine().trim().toLowerCase();

            System.out.print("Deseja informar saldo inicial? (s/n): ");
            String op = sc.nextLine().trim();
            Double saldoInicial = 0.0;
            if (op.equalsIgnoreCase("s")) {
                System.out.print("Saldo inicial: ");
                try {
                    saldoInicial = Double.parseDouble(sc.nextLine().replace(",", ".").trim());
                } catch (Exception e) {
                    System.out.println("Formato inválido de valor.");
                    return;
                }
            }

            Account conta = bank.criarConta(nome, cpf, senha, saldoInicial, tipo);
            System.out.println("\nConta criada com sucesso!");
            System.out.println("Número da conta: " + conta.getNumero());
            System.out.printf("Saldo inicial: R$ %.2f%n", conta.getSaldo());
        } catch (DomainException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }
}
