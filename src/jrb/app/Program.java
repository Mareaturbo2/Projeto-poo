package ProjetoPoo.src.jrb.app;

import ProjetoPoo.src.jrb.model.entt.Account;
import ProjetoPoo.src.jrb.model.srv.NumberGenerator;
import ProjetoPoo.src.jrb.model.excp.DomainException;
import java.util.Scanner;

public class Program {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        try{
            System.out.println("Enter Account data");
            String nAcc= NumberGenerator.numberAccount();
            System.out.print("Holder: ");
            String holder = input.nextLine();
            System.out.print("Age: ");
            int age = input.nextInt();
            if(age<=17){
                throw new DomainException("Age must be between 17 and 18");
            }
            System.out.print("Initial Balance: ");
            double initialBalance = input.nextDouble();

            Account account = new Account(nAcc,holder,age,initialBalance,1000.0);

            System.out.print("Enter Amount for withdrawal: ");
            double amount = input.nextDouble();
            account.withdraw(amount);
            System.out.print("New Balance: "+account.getBalance());

        }catch (DomainException e){
            System.out.println("Withdraw Error: "+ e.getMessage());
        }catch (RuntimeException e){
            System.out.println("Unexpected error occurred. ");
        }
        input.close();
    }
}
