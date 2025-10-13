package model.srv;

import java.util.Random;

public class NumberGenerator {

    public static int gerarNumeroConta() {
        Random random = new Random();
        return 1000 + random.nextInt(9000); // gera número entre 1000 e 9999
    }
}
