package correcter.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class IOByte {
    public static byte[] readFromFile(final String filename) {
        final File file = new File(filename);
        try (FileInputStream stream = new FileInputStream(file)) {
            return stream.readAllBytes();
        } catch (final Exception e) {
            System.out.println("Error in reading the file: " + file.getName());
            return new byte[] {};
        }
    }

    public static void writeToFile(final String filename, final byte[] bytes) {
        final File file = new File(filename);
        try (FileOutputStream stream = new FileOutputStream(file)) {
            stream.write(bytes);
        } catch (final Exception e) {
            System.out.println("Error in reading the file: " + file.getName());
        }
    }
}