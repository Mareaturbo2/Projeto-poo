package model.entt;

public class ContaCorrente extends Account {
    public ContaCorrente(Integer numero, String titular, String senha, Double saldo) {
        super(numero, titular, senha, saldo);
    }
    @Override
    public String getTipoConta() { return "Conta Corrente"; }
}
