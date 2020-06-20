package correcter.io;

import java.nio.file.*;

public class IOString {
    public static String readFromFile(final String filename) {
        try {
            return new String(Files.readAllBytes(Paths.get(filename)));
        } catch (final Exception e) {
            System.out.println("Error in reading the file: " + filename);
            return "";
        }
    }

    public static void writeToFile(final String filename, final String string) {
        try {
            Files.write(Paths.get(filename), string.getBytes());
        } catch (final Exception e) {
            System.out.println("Error in reading the file: " + filename);
        }
    }

}