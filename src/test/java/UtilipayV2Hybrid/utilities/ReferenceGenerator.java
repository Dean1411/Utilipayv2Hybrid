package UtilipayV2Hybrid.utilities;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;


public class ReferenceGenerator {

    private static final String FILE_PATH = "src/test/resources/refCounter.txt";
    private static final String PREFIX = "10003lookup";

    public static String generateNextReference() {
        int nextNumber = 1;

        // Read the current value from file
        try {
            File file = new File(FILE_PATH);
            if (file.exists()) {
                List<String> lines = Files.readAllLines(file.toPath());
                if (!lines.isEmpty()) {
                    nextNumber = Integer.parseInt(lines.get(0)) + 1;
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.out.println("Could not read reference counter. Starting from 1.");
        }

        // Save the new value back to file
        try {
            Files.write(Paths.get(FILE_PATH), String.valueOf(nextNumber).getBytes());
        } catch (IOException e) {
            System.out.println("Could not save reference counter.");
        }

        // Return formatted reference number like 10003lookup0003
        return PREFIX + String.format("%04d", nextNumber);
    }
}
