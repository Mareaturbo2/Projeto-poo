package view;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import model.entt.Account;
import model.excp.DomainException;
import service.BankService;
import service.ExtratoService;

public class MenuContaView {
    private final Scanner sc = new Scanner(System.in);
    private final BankService bank;
    private final String cpf;
    private final Account conta;
    private final ExtratoService extrato = new ExtratoService();

    public MenuContaView(BankService bank, String cpf, Account conta) {
        this.bank = bank;
        this.cpf = cpf;
        this.conta = conta;
    }

    public void exibir() {
        int opcao = -1;
        while (opcao != 0) {
            System.out.println("\n=== MENU DA CONTA ===");
            System.out.println("1 - Consultar saldo");
            System.out.println("2 - Depositar");
            System.out.println("3 - Sacar");
            System.out.println("4 - Extrato (Completo/Período + PDF)");
            System.out.println("5 - Encerrar conta");
            System.out.println("0 - Sair");
            System.out.print("Escolha uma opção: ");
            try {
                opcao = Integer.parseInt(sc.nextLine().trim());
            } catch (Exception e) {
                opcao = -1;
            }

            try {
                switch (opcao) {
                    case 1 -> System.out.printf("Saldo atual: R$ %.2f%n", conta.getSaldo());
                    case 2 -> depositar();
                    case 3 -> sacar();
                    case 4 -> extrato();
                    case 5 -> encerrarConta();
                    case 0 -> System.out.println("Saindo da conta...");
                    default -> System.out.println("Opção inválida.");
                }
            } catch (DomainException e) {
                System.out.println("Erro: " + e.getMessage());
            }
        }
    }

    private void depositar() {
        System.out.print("Valor do depósito: ");
        try {
            double v = Double.parseDouble(sc.nextLine().replace(",", ".").trim());
            bank.depositar(cpf, v);
            System.out.printf("Depósito realizado! Saldo atual: R$ %.2f%n", conta.getSaldo());
        } catch (NumberFormatException ex) {
            System.out.println("Formato inválido de valor.");
        }
    }

    private void sacar() {
        System.out.print("Valor do saque: ");
        try {
            double v = Double.parseDouble(sc.nextLine().replace(",", ".").trim());
            bank.sacar(cpf, v);
            System.out.printf("Saque realizado! Saldo atual: R$ %.2f%n", conta.getSaldo());
        } catch (NumberFormatException ex) {
            System.out.println("Formato inválido de valor.");
        }
    }

    private void extrato() {
        if (conta.getMovimentacoes().isEmpty()) {
            System.out.println("Nenhuma movimentação encontrada.");
            return;
        }

        System.out.print("Deseja filtrar por período? (s/n): ");
        String op = sc.nextLine().trim();
        if (op.equalsIgnoreCase("s")) {
            try {
                DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                System.out.print("Data inicial (dd/MM/yyyy): ");
                LocalDate ini = LocalDate.parse(sc.nextLine(), fmt);
                System.out.print("Data final (dd/MM/yyyy): ");
                LocalDate fim = LocalDate.parse(sc.nextLine(), fmt);
                if (ini.isAfter(fim)) {
                    System.out.println("Intervalo de datas inválido.");
                    return;
                }
                extrato.mostrarFiltrado(conta, ini, fim);
            } catch (Exception e) {
                System.out.println("Formato de data inválido.");
                return;
            }
        } else {
            extrato.mostrarCompleto(conta);
        }

        System.out.print("\nDeseja exportar o extrato em PDF? (s/n): ");
        String exp = sc.nextLine().trim();
        if (exp.equalsIgnoreCase("s")) {
            extrato.exportarExtratoPDF(conta);
        }
    }

    private void encerrarConta() {
        if (conta.getSaldo() > 0) {
            System.out.println("Conta não pode ser encerrada com saldo disponível.");
            return;
        }
        System.out.print("Tem certeza que deseja encerrar sua conta? (s/n): ");
        String confirm = sc.nextLine();
        if (confirm.equalsIgnoreCase("s")) {
            try {
                bank.encerrarConta(cpf);
                System.out.println("Conta encerrada com sucesso.");
                System.out.println("Voltando ao menu principal...");
                System.exit(0);
            } catch (DomainException e) {
                System.out.println("Erro: " + e.getMessage());
            }
        } else {
            System.out.println("Operação cancelada.");
        }
    }
}
