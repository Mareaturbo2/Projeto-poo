package ProjetoPoo.src.jrb.model.entt;

import ProjetoPoo.src.jrb.model.excp.DomainException;

public class Account {
    private String numberAccount;
    private String holder;
    private Double balance;
    private Double withdrawLimit;

    public Account(String number, String holder, Double balance, Double withdrawLimit) {
        this.numberAccount = number;
        this.holder = holder;
        this.balance = balance;
        this.withdrawLimit = withdrawLimit;
    }

    public String getNumberAccount() {
        return numberAccount;
    }

    public void setNumberAccount(String numberAccount) {
        this.numberAccount = numberAccount;
    }

    public String getHolder() {
        return holder;
    }

    public void setHolder(String holder) {
        this.holder = holder;
    }

    public Double getBalance() {
        return balance;
    }


    public Double getWithdrawLimit() {
        return withdrawLimit;
    }

    public void withdraw(Double withdraw){
        if(withdraw>withdrawLimit){
            throw new DomainException("The amount exceeds withdraw limit . ");
        }else if(withdraw>balance){
            throw new DomainException("not enough balance. ");

        }else{
            balance -= withdraw;
        }
    }
    public void deposit(Double deposit){
        balance += deposit;
    }



}
