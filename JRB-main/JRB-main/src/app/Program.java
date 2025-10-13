package app;

import java.util.Locale;
import java.util.Scanner;

import model.entt.Account;
import model.excp.DomainException;
import model.srv.Bank;

public class Program {

    public static void main(String[] args) {
        Locale.setDefault(Locale.US);
        Scanner sc = new Scanner(System.in);
        Bank bank = new Bank();

        int opcao = -1;
        while (opcao != 0) {
            try {
                System.out.println("\n===== MENU BANCO DIGITAL =====");
                System.out.println("1 - Criar conta");
                System.out.println("2 - Acessar conta");
                System.out.println("0 - Sair");
                System.out.print("Escolha uma opção: ");
                opcao = sc.nextInt();
                sc.nextLine(); 

                switch (opcao) {
                    case 1:
                        criarConta(sc, bank);
                        break;
                    case 2:
                        acessarConta(sc, bank);
                        break;
                    case 0:
                        System.out.println("Encerrando o sistema. Até logo!");
                        break;
                    default:
                        System.out.println("Opção inválida. Tente novamente.");
                }

            } catch (DomainException e) {
                System.out.println("Erro: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("Erro inesperado: " + e.getMessage());
                sc.nextLine(); 
            }
        }

        sc.close();
    }

    private static void criarConta(Scanner sc, Bank bank) {
        System.out.println("\n=== CRIAÇÃO DE CONTA ===");
        System.out.print("Nome: ");
        String nome = sc.nextLine();

        System.out.print("CPF: ");
        String cpf = sc.nextLine();

        if (bank.buscarConta(cpf) != null) {
            throw new DomainException("CPF já vinculado a uma conta");
        }

        System.out.print("Crie uma senha de acesso: ");
        String senha = sc.nextLine();

        System.out.print("Deseja informar saldo inicial? (s/n): ");
        char opc = sc.next().charAt(0);

        Double saldoInicial = null;
        if (opc == 's' || opc == 'S') {
            System.out.print("Saldo inicial: ");
            saldoInicial = sc.nextDouble();
        }

        Account novaConta = bank.criarConta(nome, cpf, senha, saldoInicial);

        System.out.println("\nConta criada com sucesso!");
        System.out.println("Número da conta: " + novaConta.getNumero());
        System.out.printf("Saldo inicial: R$ %.2f%n", novaConta.getSaldo());
    }

    private static void acessarConta(Scanner sc, Bank bank) {
        System.out.println("\n=== ACESSO À CONTA ===");
        System.out.print("CPF: ");
        String cpf = sc.nextLine();

        Account conta = bank.buscarConta(cpf);
        if (conta == null) {
            System.out.println("Conta não encontrada.");
            return;
        }

        System.out.print("Senha: ");
        String senha = sc.nextLine();

        try {
            
            if (!conta.validarSenha(senha)) {
                System.out.println("Senha incorreta.");
                return;
            }
        } catch (DomainException e) {
            
            System.out.println("Erro: " + e.getMessage());
            return;
        }

        System.out.println("\nLogin realizado com sucesso!");
        menuDaConta(sc, conta, bank);
    }
    private static void menuDaConta(Scanner sc, Account conta, Bank bank) {
        int opcao = -1;
        while (opcao != 0) {
            System.out.println("\n=== MENU DA CONTA ===");
            System.out.println("1 - Consultar saldo");
            System.out.println("2 - Depositar");
            System.out.println("3 - Sacar");
            System.out.println("0 - Sair da conta");
            System.out.print("Escolha uma opção: ");
            opcao = sc.nextInt();
            sc.nextLine();

            try {
                switch (opcao) {
                    case 1:
                        System.out.println("Titular: " + conta.getTitular());
                        System.out.printf("Saldo atual: R$ %.2f%n", conta.getSaldo());
                        break;

                    case 2:
                        System.out.print("Valor do depósito: ");
                        double dep = sc.nextDouble();
                        conta.depositar(dep);
                        bank.atualizarContas(); 
                        System.out.printf("Depósito realizado! Saldo atual: R$ %.2f%n", conta.getSaldo());
                        break;

                    case 3:
                        System.out.print("Valor do saque: ");
                        double saque = sc.nextDouble();
                        conta.sacar(saque);     
                        bank.atualizarContas(); 
                        if (conta.getSaldo() == 0.0) {
                            System.out.println("Saque realizado! O saldo foi zerado.");
                        } else {
                            System.out.printf("Saque realizado! Saldo atual: R$ %.2f%n", conta.getSaldo());
                        }
                        break;

                    case 0:
                        System.out.println("Saindo da conta...");
                        break;

                    default:
                        System.out.println("Opção inválida.");
                }
            } catch (DomainException e) {
                System.out.println("Erro: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("Erro inesperado: " + e.getMessage());
                sc.nextLine();
            }
        }
    }
}
