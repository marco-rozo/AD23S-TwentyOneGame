package utfpr.edu.br.twentyone.Utils;
import java.util.Random;

public class RandomNumber {

    public static int generateRandomNumber() {
        Random generate = new Random();
        return generate.nextInt(13) + 1;
    }
}
