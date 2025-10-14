package service;

import com.google.gson.*;
import model.entt.*;
import model.excp.DomainException;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class BankService {
    private static final String FILE_PATH = "data/contas.json";
    private final Map<String, Account> contas = new HashMap<>();
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public BankService() {
        carregar();
    }

    public Account criarConta(String nome, String cpf, String senha, Double saldoInicial, String tipo) {
        if (nome == null || nome.isBlank()) throw new DomainException("Nome inválido");
        if (cpf == null || cpf.isBlank()) throw new DomainException("CPF inválido");
        if (senha == null || senha.isBlank()) throw new DomainException("Senha inválida");
        if (contas.containsKey(cpf)) throw new DomainException("CPF já vinculado a uma conta");
        if (saldoInicial == null) saldoInicial = 0.0;

        int numero = NumberGenerator.gerarNumeroConta();
        Account conta;
        switch (tipo.toLowerCase()) {
            case "corrente" -> conta = new ContaCorrente(numero, nome, senha, saldoInicial);
            case "poupanca" -> conta = new ContaPoupanca(numero, nome, senha, saldoInicial);
            default -> throw new DomainException("Tipo de conta inválido (use: corrente/poupanca)");
        }

        contas.put(cpf, conta);
        salvar();
        return conta;
    }

    public Account buscarConta(String cpf) {
        return contas.get(cpf);
    }

    public boolean validarSenha(Account conta, String senhaDigitada) {
        return conta != null && conta.validarSenha(senhaDigitada);
    }

    public void depositar(String cpf, double valor) {
        Account c = contas.get(cpf);
        if (c == null) throw new DomainException("Conta não encontrada");
        c.depositar(valor);
        salvar();
    }

    public void sacar(String cpf, double valor) {
        Account c = contas.get(cpf);
        if (c == null) throw new DomainException("Conta não encontrada");
        c.sacar(valor);
        salvar();
    }

    public void encerrarConta(String cpf) {
        Account conta = contas.get(cpf);
        if (conta == null) throw new DomainException("Conta não encontrada");
        if (conta.isEncerrada()) throw new DomainException("Conta já está encerrada.");
        if (conta.getSaldo() > 0) throw new DomainException("Conta não pode ser encerrada com saldo disponível.");
        conta.encerrar();
        salvar();
    }

    public void salvar() {
        try {
            File pasta = new File("data");
            if (!pasta.exists()) pasta.mkdirs();

            JsonObject root = new JsonObject();
            for (Map.Entry<String, Account> e : contas.entrySet()) {
                String cpf = e.getKey();
                Account c = e.getValue();
                JsonObject obj = new JsonObject();
                obj.addProperty("cpf", cpf);
                obj.addProperty("tipo", c.getTipoConta().equals("Conta Corrente") ? "corrente" : "poupanca");
                obj.addProperty("conta", c.getNumero());
                obj.addProperty("titular", c.getTitular());
                obj.addProperty("senha", c.getSenhaMaskless());
                obj.addProperty("saldo", c.getSaldo());
                obj.addProperty("encerrada", c.isEncerrada());

                JsonArray movs = new JsonArray();
                for (Movimentacao m : c.getMovimentacoes()) {
                    JsonObject jm = new JsonObject();
                    jm.addProperty("tipo", m.getTipo());
                    jm.addProperty("valor", m.getValor());
                    jm.addProperty("dataHora", m.getDataHora());
                    movs.add(jm);
                }
                obj.add("movimentacoes", movs);
                root.add(cpf, obj);
            }

            try (Writer w = new OutputStreamWriter(new FileOutputStream(FILE_PATH), StandardCharsets.UTF_8)) {
                gson.toJson(root, w);
            }
        } catch (IOException ex) {
            System.out.println("Erro ao salvar contas: " + ex.getMessage());
        }
    }

    private void carregar() {
        File f = new File(FILE_PATH);
        if (!f.exists()) return;
        try (Reader r = new InputStreamReader(new FileInputStream(f), StandardCharsets.UTF_8)) {
            JsonObject root = JsonParser.parseReader(r).getAsJsonObject();
            for (Map.Entry<String, JsonElement> entry : root.entrySet()) {
                String cpf = entry.getKey();
                JsonObject obj = entry.getValue().getAsJsonObject();

                String tipo = obj.get("tipo").getAsString();
                int numero = obj.has("conta") ? obj.get("conta").getAsInt() : obj.get("numero").getAsInt();

                String titular = obj.get("titular").getAsString();
                String senha = obj.get("senha").getAsString();
                double saldo = obj.get("saldo").getAsDouble();

                Account conta = switch (tipo) {
                    case "corrente" -> new ContaCorrente(numero, titular, senha, saldo);
                    case "poupanca" -> new ContaPoupanca(numero, titular, senha, saldo);
                    default -> null;
                };
                if (conta == null) continue;

                if (obj.has("encerrada") && obj.get("encerrada").getAsBoolean()) conta.encerrar();

                List<Movimentacao> movsList = new ArrayList<>();
                JsonArray movs = obj.has("movimentacoes") ? obj.get("movimentacoes").getAsJsonArray() : new JsonArray();
                for (JsonElement el : movs) {
                    JsonObject jm = el.getAsJsonObject();
                    String mtipo = jm.get("tipo").getAsString();
                    double mvalor = jm.get("valor").getAsDouble();
                    String dataHora = jm.get("dataHora").getAsString();
                    movsList.add(new Movimentacao(mtipo, mvalor, dataHora));
                }
                conta.setMovimentacoes(movsList);
                contas.put(cpf, conta);
            }
        } catch (Exception ex) {
            System.out.println("Erro ao carregar contas: " + ex.getMessage());
        }
    }
}
