package app;

import java.util.Locale;
import java.util.Scanner;
import java.time.format.DateTimeFormatter;
import java.time.LocalDate;

import model.entt.Account;
import model.excp.DomainException;
import model.srv.Bank;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileOutputStream;

public class Program {

    public static void main(String[] args) {
        Locale.setDefault(Locale.US);
        System.setProperty("file.encoding", "UTF-8");


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

        if (!conta.validarSenha(senha)) {
            System.out.println("Senha incorreta.");
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
            System.out.println("4 - Extrato (visualizar/exportar PDF)");
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
                        try {
                            double dep = sc.nextDouble();
                            conta.depositar(dep);
                            bank.atualizarContas();
                            System.out.printf("Depósito realizado! Saldo atual: R$ %.2f%n", conta.getSaldo());
                        } catch (java.util.InputMismatchException e) {
                            System.out.println("Formato inválido de valor.");
                            sc.nextLine();
                        }
                        break;
                    case 3:
                        System.out.print("Valor do saque: ");
                        try {
                            double saque = sc.nextDouble();
                            conta.sacar(saque);
                            bank.atualizarContas();
                            if (conta.getSaldo() == 0.0) {
                                System.out.println("Saque realizado! O saldo foi zerado.");
                            } else {
                                System.out.printf("Saque realizado! Saldo atual: R$ %.2f%n", conta.getSaldo());
                            }
                        } catch (java.util.InputMismatchException e) {
                            System.out.println("Formato inválido de valor.");
                            sc.nextLine();
                        }
                        break;
                    case 4:
                        mostrarExtrato(sc, conta);
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

    private static void mostrarExtrato(Scanner sc, Account conta) {
        System.out.println("\n=== EXTRATO BANCÁRIO ===");

        if (conta.getMovimentacoes().isEmpty()) {
            System.out.println("Nenhuma movimentação encontrada.");
            return;
        }

        System.out.print("Deseja filtrar por período? (s/n): ");
        char opcao = sc.next().charAt(0);
        sc.nextLine();

        if (opcao == 's' || opcao == 'S') {
            try {
                System.out.print("Data inicial (dd/MM/yyyy): ");
                String dataInicioStr = sc.nextLine();
                System.out.print("Data final (dd/MM/yyyy): ");
                String dataFimStr = sc.nextLine();

                DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                LocalDate dataInicio = LocalDate.parse(dataInicioStr, fmt);
                LocalDate dataFim = LocalDate.parse(dataFimStr, fmt);

                if (dataInicio.isAfter(dataFim)) {
                    System.out.println("Intervalo de datas inválido.");
                    return;
                }

                boolean encontrou = false;
                System.out.println("\n=== EXTRATO FILTRADO ===");
                for (Account.Movimentacao mov : conta.getMovimentacoes()) {
                    LocalDate dataMov = LocalDate.parse(mov.getDataHora().substring(0, 10),
                            DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                    if (!dataMov.isBefore(dataInicio) && !dataMov.isAfter(dataFim)) {
                        System.out.println(mov);
                        encontrou = true;
                    }
                }

                if (!encontrou) {
                    System.out.println("Nenhuma movimentação encontrada no período.");
                }

            } catch (java.time.format.DateTimeParseException e) {
                System.out.println("Formato de data inválido.");
            }
        } else {
            System.out.println("\n=== EXTRATO COMPLETO ===");
            for (Account.Movimentacao mov : conta.getMovimentacoes()) {
                System.out.println(mov);
            }
        }

        System.out.print("\nDeseja exportar o extrato em PDF? (s/n): ");
        char exportar = sc.next().charAt(0);
        sc.nextLine();

        if (exportar == 's' || exportar == 'S') {
            exportarExtratoPDF(conta);
        }
    }

    private static void exportarExtratoPDF(Account conta) {
        try {
            String nomeArquivo = "data/extrato_" + conta.getNumero() + ".pdf";
            Document documento = new Document();
            PdfWriter.getInstance(documento, new FileOutputStream(nomeArquivo));
            documento.open();

            documento.add(new Paragraph("Extrato Bancário"));
            documento.add(new Paragraph("Titular: " + conta.getTitular()));
            documento.add(new Paragraph("Número da Conta: " + conta.getNumero()));
            documento.add(new Paragraph("Saldo Atual: R$ " + String.format("%.2f", conta.getSaldo())));
            documento.add(new Paragraph(" "));
            documento.add(new Paragraph("Movimentações:"));
            documento.add(new Paragraph(" "));

            if (conta.getMovimentacoes().isEmpty()) {
                documento.add(new Paragraph("Nenhuma movimentação encontrada."));
            } else {
                PdfPTable tabela = new PdfPTable(3);
                tabela.addCell("Data");
                tabela.addCell("Tipo");
                tabela.addCell("Valor");

                for (Account.Movimentacao mov : conta.getMovimentacoes()) {
                    tabela.addCell(mov.getDataHora());
                    tabela.addCell(mov.getTipo());
                    tabela.addCell(String.format("R$ %.2f", mov.getValor()));
                }
                documento.add(tabela);
            }

            documento.close();
            System.out.println("Extrato exportado com sucesso: " + nomeArquivo);

        } catch (DocumentException | java.io.IOException e) {
            System.out.println("Erro ao gerar PDF: " + e.getMessage());
        }
    }
}
