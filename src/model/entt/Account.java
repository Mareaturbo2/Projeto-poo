package model.entt;

import model.excp.DomainException;
import java.util.ArrayList;
import java.util.List;

public abstract class Account {
    protected Integer numero;
    protected String titular;
    protected String senha;
    protected Double saldo;
    protected boolean encerrada = false;
    protected List<Movimentacao> movimentacoes = new ArrayList<>();

    protected Account(Integer numero, String titular, String senha, Double saldo) {
        this.numero = numero;
        this.titular = titular;
        this.senha = senha;
        this.saldo = saldo == null ? 0.0 : saldo;
    }

    public Integer getNumero() { return numero; }
    public String getTitular() { return titular; }
    public Double getSaldo() { return saldo; }
    public boolean isEncerrada() { return encerrada; }
    public List<Movimentacao> getMovimentacoes() {
        if (movimentacoes == null) movimentacoes = new ArrayList<>();
        return movimentacoes;
    }

    public void setMovimentacoes(List<Movimentacao> movs) {
        this.movimentacoes = movs == null ? new ArrayList<>() : movs;
    }

    public boolean validarSenha(String s) { return senha != null && senha.equals(s); }
    public String getSenhaMaskless() { return senha; }

    public void depositar(Double v) {
        if (encerrada) throw new DomainException("Conta encerrada não permite operações.");
        if (v == null || v <= 0) throw new DomainException("Valor inválido para depósito.");
        saldo += v;
        registrar("Depósito", v);
    }

    public void sacar(Double v) {
        if (encerrada) throw new DomainException("Conta encerrada não permite operações.");
        if (v == null || v <= 0) throw new DomainException("Valor inválido para saque.");
        if (v > saldo) throw new DomainException("Saldo insuficiente.");
        saldo -= v;
        registrar("Saque", -v);
    }

    public void encerrar() {
        this.encerrada = true;
        registrar("Encerramento de conta", 0.0);
    }

    protected void registrar(String tipo, Double valor) {
        getMovimentacoes().add(Movimentacao.of(tipo, valor));
    }

    public abstract String getTipoConta();
}
