package correcter.encoding;

import correcter.io.IOString;
import java.util.Random;

public class Char {

    final private static int SIZE = 3;

    public static void all() {
        encodeMode();
        sendMode();
        decodeMode();
    }

    public static void encodeMode() {
        final String inputString = IOString.readFromFile("send.txt");
        final String encodedString = encode(inputString);
        IOString.writeToFile("encoded.txt", encodedString);
    }

    public static void sendMode() {
        final String encodedString = IOString.readFromFile("encoded.txt");
        final String jumbledString = jumble(encodedString);
        IOString.writeToFile("received.txt", jumbledString);
    }

    public static void decodeMode() {
        final String receivedString = IOString.readFromFile("received.txt");
        final String decodedString = decode(receivedString);
        IOString.writeToFile("decoded.txt", decodedString);
    }

    private static String decode(String string) {
        final StringBuilder in = new StringBuilder(string);
        final String[] parts = in.toString().split("(?<=\\G.{" + SIZE + "})");
        final StringBuilder out = new StringBuilder();
        for (final String part : parts)
            for (int i = 0; i < SIZE; i++)
                if (part.charAt(i) == part.charAt((i + 1) % SIZE)) {
                    out.append(part.charAt(i));
                    break;
                }
        return out.toString();
    }

    private static String jumble(String string) {
        final StringBuilder in = new StringBuilder(string);
        final Random random = new Random();
        final String[] parts = in.toString().split("(?<=\\G.{" + SIZE + "})");
        final String pattern = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ 0123456789";
        final StringBuilder out = new StringBuilder();
        for (final String part : parts) {
            final StringBuilder temp = new StringBuilder(part);
            while (true) {
                final int randIndex = random.nextInt(SIZE);
                final char randChar = pattern.charAt(random.nextInt(pattern.length()));
                if (temp.charAt(randIndex) != randChar) {
                    temp.setCharAt(randIndex, randChar);
                    break;
                }
            }
            out.append(temp);
        }
        return out.toString();
    }

    private static String encode(final String in) {
        final StringBuilder out = new StringBuilder();
        for (int i = 0; i < in.length(); i++)
            for (int j = 0; j < SIZE; j++)
                out.append(in.charAt(i));
        return out.toString();
    }
}
