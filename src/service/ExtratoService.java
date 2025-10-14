package service;

import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import model.entt.Account;
import model.entt.Movimentacao;

import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ExtratoService {

    public void mostrarCompleto(Account conta) {
        System.out.println("\n=== EXTRATO COMPLETO ===");
        conta.getMovimentacoes().forEach(System.out::println);
    }

    public void mostrarFiltrado(Account conta, LocalDate ini, LocalDate fim) {
        System.out.println("\n=== EXTRATO FILTRADO ===");
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        boolean encontrou = false;
        for (Movimentacao m : conta.getMovimentacoes()) {
            LocalDate dataMov = LocalDate.parse(m.getDataHora().substring(0, 10), fmt);
            if (!dataMov.isBefore(ini) && !dataMov.isAfter(fim)) {
                System.out.println(m);
                encontrou = true;
            }
        }
        if (!encontrou) System.out.println("Nenhuma movimentação encontrada no período.");
    }

    public void exportarExtratoPDF(Account conta) {
        try {
            File pasta = new File("data");
            if (!pasta.exists()) pasta.mkdirs();

            String nomeArquivo = "data/extrato_" + conta.getNumero() + ".pdf";
            Document doc = new Document();
            PdfWriter.getInstance(doc, new FileOutputStream(nomeArquivo));
            doc.open();

            doc.add(new Paragraph("Extrato Bancário"));
            doc.add(new Paragraph("Titular: " + conta.getTitular()));
            doc.add(new Paragraph("Tipo de Conta: " + conta.getTipoConta()));
            doc.add(new Paragraph("Saldo Atual: R$ " + String.format("%.2f", conta.getSaldo())));
            doc.add(new Paragraph(" "));

            PdfPTable tabela = new PdfPTable(3);
            tabela.addCell("Data");
            tabela.addCell("Tipo");
            tabela.addCell("Valor");

            for (Movimentacao m : conta.getMovimentacoes()) {
                tabela.addCell(m.getDataHora());
                tabela.addCell(m.getTipo());
                tabela.addCell(String.format("R$ %.2f", m.getValor()));
            }
            doc.add(tabela);
            doc.close();
            System.out.println("Extrato exportado com sucesso: " + nomeArquivo);
        } catch (Exception e) {
            System.out.println("Erro ao gerar PDF: " + e.getMessage());
        }
    }
}
