package app;

import static spark.Spark.*;
import com.google.gson.Gson;
import java.util.Map;

import model.entt.Account;
import service.BankService;

import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class ApiServer {
    public static void main(String[] args) {
        port(8080);
        Gson gson = new Gson();
        BankService bankService = new BankService();

      
        before((req, res) -> {
            res.header("Access-Control-Allow-Origin", "*");
            res.header("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS");
            res.header("Access-Control-Allow-Headers", "Content-Type");
        });

        options("/*", (req, res) -> {
            res.status(200);
            return "OK";
        });

    
        get("/", (req, res) -> "Servidor ativo em http://localhost:8080 🚀");

       
        post("/api/login", (req, res) -> {
            res.type("application/json");
            Map<String, Object> data = gson.fromJson(req.body(), Map.class);

            String cpf = (String) data.get("cpf");
            String senha = String.valueOf(data.get("senha"));

            Account conta = bankService.buscarConta(cpf);
            if (conta != null && conta.validarSenha(senha)) {
                return gson.toJson(conta);
            } else {
                res.status(401);
                return gson.toJson("CPF ou senha incorretos");
            }
        });

           post("/api/contas", (req, res) -> {
            res.type("application/json");
            Map<String, Object> data = gson.fromJson(req.body(), Map.class);

            String titular = (String) data.get("titular");
            String cpf = (String) data.get("cpf");
            String senha = String.valueOf(data.get("senha"));
            Double saldoInicial = data.get("saldoInicial") == null ? 0.0 : ((Number) data.get("saldoInicial")).doubleValue();
            String tipo = (String) data.get("tipo");

            try {
                bankService.criarConta(titular, cpf, senha, saldoInicial, tipo);
                return gson.toJson("Conta criada com sucesso!");
            } catch (Exception e) {
                res.status(400);
                return gson.toJson("Erro ao criar conta: " + e.getMessage());
            }
        });

     
        get("/api/contas/:cpf", (req, res) -> {
            res.type("application/json");
            String cpf = req.params("cpf");

            Account conta = bankService.buscarConta(cpf);
            if (conta != null) {
                return gson.toJson(conta);
            } else {
                res.status(404);
                return gson.toJson("Conta não encontrada");
            }
        });

        put("/api/contas/:cpf/deposito", (req, res) -> {
            res.type("application/json");
            String cpf = req.params("cpf");
            double valor = Double.parseDouble(req.queryParams("valor"));

            try {
                bankService.depositar(cpf, valor);
                return gson.toJson("Depósito de R$ " + valor + " realizado com sucesso!");
            } catch (Exception e) {
                res.status(400);
                return gson.toJson("Erro no depósito: " + e.getMessage());
            }
        });

        put("/api/contas/:cpf/saque", (req, res) -> {
            res.type("application/json");
            String cpf = req.params("cpf");
            double valor = Double.parseDouble(req.queryParams("valor"));

            try {
                bankService.sacar(cpf, valor);
                return gson.toJson("Saque de R$ " + valor + " realizado com sucesso!");
            } catch (Exception e) {
                res.status(400);
                return gson.toJson("Erro no saque: " + e.getMessage());
            }
        });

        
        get("/api/contas/:cpf/extrato", (req, res) -> {
            res.type("application/json");
            String cpf = req.params("cpf");
            Account conta = bankService.buscarConta(cpf);

            if (conta != null) {
                return gson.toJson(conta.getMovimentacoes());
            } else {
                res.status(404);
                return gson.toJson("Conta não encontrada");
            }
        });

        
        get("/api/contas/:cpf/extrato/pdf", (req, res) -> {
            String cpf = req.params("cpf");
            Account conta = bankService.buscarConta(cpf);

            if (conta == null) {
                res.status(404);
                return "Conta não encontrada";
            }

            res.raw().setContentType("application/pdf");
            res.raw().setHeader("Content-Disposition", "attachment; filename=extrato_" + cpf + ".pdf");

            Document document = new Document(PageSize.A4);
            PdfWriter.getInstance(document, res.raw().getOutputStream());
            document.open();

            Font tituloFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
            document.add(new Paragraph("Extrato Bancário\n\n", tituloFont));

            Font infoFont = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL);
            document.add(new Paragraph("Titular: " + conta.getTitular(), infoFont));
            document.add(new Paragraph("CPF: " + cpf, infoFont));
            document.add(new Paragraph("Emitido em: " +
                    java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
                            .format(java.time.LocalDateTime.now()), infoFont));
            document.add(new Paragraph("\n\n"));

            PdfPTable table = new PdfPTable(3);
            table.setWidthPercentage(100);
            table.addCell("Data");
            table.addCell("Tipo");
            table.addCell("Valor (R$)");

            conta.getMovimentacoes().forEach(mov -> {
                table.addCell(mov.getDataHora());
                table.addCell(mov.getTipo());
                table.addCell(String.format("%.2f", mov.getValor()));
            });

            document.add(table);
            document.close();

            return res.raw();
        });
    }
}
