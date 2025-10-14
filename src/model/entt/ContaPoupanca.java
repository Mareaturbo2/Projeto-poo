package model.entt;

public class ContaPoupanca extends Account {
    public ContaPoupanca(Integer numero, String titular, String senha, Double saldo) {
        super(numero, titular, senha, saldo);
    }
    @Override
    public String getTipoConta() { return "Conta Poupança"; }
}
