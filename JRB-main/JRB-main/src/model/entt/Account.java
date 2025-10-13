package model.entt;

import model.excp.DomainException;

public class Account {

    private Integer numero;
    private String titular;
    private String senha;     
    private Double saldo;
    private Double limiteDeSaque;

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
        if (quantia <= 0.0) {
            throw new DomainException("Valor inválido para depósito");
        }
        saldo += quantia;
    }

    public void sacar(Double quantia) {
        if (quantia == null || quantia <= 0.0) {
            throw new DomainException("Valor inválido para saque");
        }

        if (quantia > saldo) {
            throw new DomainException("Saldo insuficiente");
        }

        saldo -= quantia;

        System.out.printf("Saque de R$ %.2f realizado com sucesso.%n", quantia);
        System.out.printf("Saldo atual: R$ %.2f%n", saldo);
    }
}
