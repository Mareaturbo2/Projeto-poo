package ProjetoPoo;

public class OperacoesNaConta {
    private double saldo;

    public OperacoesNaConta(double saldo){

        this.saldo = saldo;
    }

    //---------------- OPERAÇÕES ----------------

    public void sacar(double valorSaque){
        
        if (valorSaque <= 0){
            System.out.println("Ação inválida, tente novamente...");
        } else if  (valorSaque > this.saldo){
            System.out.println("Saldo Insuficiente");
        }

        this.saldo -= valorSaque;
    }

    public void depositar(double valorDeposito){

        if (valorDeposito <= 0){
            System.out.println("Ação inválida, tente novamente...");
        }

        this.saldo += valorDeposito;
    }

    // ---------------- GET -------------------
    public double getSaldo(){
        return this.saldo;
    }

    // ---------------- SET -------------------
    public void setSaldo(double saldo){
        this.saldo = saldo;
    }

   

}
