package demo.qe.util;

import java.util.Random;

public class DataGenerator {

    static String[] firstNames = {"John", "Jane", "Alice", "Bob", "Charlie", "David", "Eve", "Frank", "Grace", "Henry"};
    static String[] lastNames = {"Smith", "Johnson", "Williams", "Jones", "Brown", "Davis", "Miller", "Wilson", "Moore", "Taylor"};

    private DataGenerator() {
    }

    public static String generateRandomString(int length) {
        String characters = "0123456789";
        StringBuilder sb = new StringBuilder(length);
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            sb.append(characters.charAt(random.nextInt(characters.length())));
        }
        return sb.toString();
    }

    public static String genFullName() {
        Random random = new Random();
        return firstNames[random.nextInt(firstNames.length)] + " " + lastNames[random.nextInt(lastNames.length)];
    }
    public static String genSSN() {
        return "987" + "-" + generateRandomString(2) + "-" + generateRandomString(4);
    }
    public static String genCCN() {
        return "439" + "-" + generateRandomString(4) + "-" + generateRandomString(4)+ "-" + generateRandomString(4);
    }
}
