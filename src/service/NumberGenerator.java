package service;

import java.util.Random;

public class NumberGenerator {
    private static final Random rnd = new Random();
    public static int gerarNumeroConta() {
        return 10000 + rnd.nextInt(90000);
    }
}
