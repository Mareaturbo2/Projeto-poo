package ProjetoPoo.src.jrb.model.srv;

import java.util.Random;

public class NumberGenerator {

    public static String numberAccount(){
        Random rand = new Random();
        StringBuilder sb = new StringBuilder(6);
        for (int i = 0; i < 6; i++) {
            sb.append(rand.nextInt(10));
        }
        return sb.toString();
    }
}

