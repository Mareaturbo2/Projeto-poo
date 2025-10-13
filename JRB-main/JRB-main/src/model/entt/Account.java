package model.entt;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import model.excp.DomainException;

public class Account {

    private Integer numero;
    private String titular;
    private String senha;
    private Double saldo;
    private Double limiteDeSaque;
    private List<Movimentacao> movimentacoes = new ArrayList<>();

    public Account(Integer numero, String titular, String senha, Double saldo, Double limiteDeSaque) {
        this.numero = numero;
        this.titular = titular;
        this.senha = senha;
        this.saldo = saldo;
        this.limiteDeSaque = limiteDeSaque;
    }

    public Integer getNumero() {
        return numero;
    }

    public String getTitular() {
        return titular;
    }

    public Double getSaldo() {
        return saldo;
    }

    public Double getLimiteDeSaque() {
        return limiteDeSaque;
    }

    public boolean validarSenha(String senhaDigitada) {
        return senha.equals(senhaDigitada);
    }
    public void depositar(Double quantia) {
        if (quantia == null || quantia <= 0.0) {
            throw new DomainException("Valor inválido para depósito");
        }
        saldo += quantia;
        registrarMovimentacao("Depósito", quantia);
    }
    public void sacar(Double quantia) {
        if (quantia == null || quantia <= 0.0) {
            throw new DomainException("Valor inválido para saque");
        }
        if (quantia > saldo) {
            throw new DomainException("Saldo insuficiente");
        }
        saldo -= quantia;
        registrarMovimentacao("Saque", -quantia);
    }
    private void registrarMovimentacao(String tipo, Double valor) {
        movimentacoes.add(new Movimentacao(tipo, valor, LocalDateTime.now()));
    }

    public List<Movimentacao> getMovimentacoes() {
        if (movimentacoes == null) {
        movimentacoes = new ArrayList<>();
    }
        return movimentacoes;
    }

    public static class Movimentacao {
        private String tipo;
        private Double valor;
        private String dataHora; 
        public Movimentacao(String tipo, Double valor, LocalDateTime dataHora) {
            this.tipo = tipo;
            this.valor = valor;
            this.dataHora = dataHora.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
        }

        public String getTipo() {
            return tipo;
        }

        public Double getValor() {
            return valor;
        }

        public String getDataHora() {
            return dataHora;
        }

        @Override
        public String toString() {
            return String.format("%s | %s de R$ %.2f", dataHora, tipo, valor);
        }
    }
}
