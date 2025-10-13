package model.srv;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import model.entt.Account;
import model.excp.DomainException;

public class Bank {

    private static final String FILE_PATH = "contas.json";
    private Map<String, Account> contas = new HashMap<>();
    private Gson gson = new Gson();

    public Bank() {
        carregarContas();
    }

    public Account criarConta(String nome, String cpf, String senha, Double saldoInicial) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new DomainException("Nome inválido");
        }

        if (cpf == null || cpf.trim().isEmpty()) {
            throw new DomainException("CPF inválido");
        }

        if (contas.containsKey(cpf)) {
            throw new DomainException("CPF já vinculado a uma conta");
        }

        if (senha == null || senha.trim().isEmpty()) {
            throw new DomainException("Senha inválida");
        }

        if (saldoInicial == null) {
            saldoInicial = 0.0;
        }

        int numero = NumberGenerator.gerarNumeroConta();
        Account novaConta = new Account(numero, nome, senha, saldoInicial, 1000.0);
        contas.put(cpf, novaConta);
        salvarContas();

        return novaConta;
    }

    public Account buscarConta(String cpf) {
        return contas.get(cpf);
    }

    public void atualizarContas() {
        salvarContas(); 
    }

    private void salvarContas() {
        try (FileWriter writer = new FileWriter(FILE_PATH)) {
            gson.toJson(contas, writer);
        } catch (IOException e) {
            System.out.println("Erro ao salvar contas: " + e.getMessage());
        }
    }

    private void carregarContas() {
        try (FileReader reader = new FileReader(FILE_PATH)) {
            java.lang.reflect.Type type = new TypeToken<Map<String, Account>>() {}.getType();
            contas = gson.fromJson(reader, type);
            if (contas == null) contas = new HashMap<>();
        } catch (IOException e) {
            contas = new HashMap<>();
        }
    }
}
