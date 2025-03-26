package demo.qe.util;

import java.util.Random;

import demo.qe.models.Patient;
import demo.qe.models.PatientBilling;
import demo.qe.models.PatientRecord;

public class DataGenerator {

    static String[] firstNames = {"John", "Jane", "Alice", "Bob", "Charlie", "David", "Eve", "Frank", "Grace", "Henry"};
    static String[] lastNames = {"Smith", "Johnson", "Williams", "Jones", "Brown", "Davis", "Miller", "Wilson", "Moore", "Taylor"};

    private DataGenerator() {
    }

    private static String generateRandomString(int length) {
        String characters = "0123456789";
        StringBuilder sb = new StringBuilder(length);
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            sb.append(characters.charAt(random.nextInt(characters.length())));
        }
        return sb.toString();
    }

    private static String genFullName() {
        Random random = new Random();
        return firstNames[random.nextInt(firstNames.length)] + " " + lastNames[random.nextInt(lastNames.length)];
    }
    private static String genSSN() {
        return "987" + "-" + generateRandomString(2) + "-" + generateRandomString(4);
    }

    private static String genCCN() {
        return "439" + "-" + generateRandomString(4) + "-" + generateRandomString(4)+ "-" + generateRandomString(4);
    }

    public static Patient genPatient() {
        PatientBilling patientBilling = new PatientBilling("Visa", DataGenerator.genCCN());
        PatientRecord patientRecord = new PatientRecord(DataGenerator.genSSN(), patientBilling, new Random().nextInt(1800) + 200);
        return new Patient(DataGenerator.genFullName(), patientRecord);
    }
}
