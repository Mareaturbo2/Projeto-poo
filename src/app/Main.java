package app;

import service.BankService;
import view.MenuPrincipalView;

public class Main {
    public static void main(String[] args) {
        System.setProperty("file.encoding", "UTF-8");
        BankService bank = new BankService();
        new MenuPrincipalView(bank).exibir();
    }
}
