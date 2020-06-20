package correcter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Random;
import java.util.Scanner;

public class Main {
    public static void main(final String[] args) {
        final Scanner scanner = new Scanner(System.in);
        System.out.println("Write a mode: ");
        final String mode = scanner.next();
        scanner.close();
        switch (mode) {
            case "encode":
                encodeMode();
                break;
            case "send":
                sendMode();
                break;
            case "decode":
                decodeMode();
                break;
            default:
        }
    }

    private static void encodeMode() {
        final byte[] readBytes = readFromFile("send.txt");
        final byte[] encodedBytes = encode(readBytes);
        writeToFile("encoded.txt", encodedBytes);
    }

    private static void sendMode() {
        final byte[] encodedBytes = readFromFile("encoded.txt");
        final byte[] jumbledBytes = jumble(encodedBytes);
        writeToFile("received.txt", jumbledBytes);
    }

    private static void decodeMode() {
        final byte[] receivedBytes = readFromFile("received.txt");
        final byte[] decodedBytes = decode(receivedBytes);
        writeToFile("decoded.txt", decodedBytes);
    }

    private static byte[] encode(final byte[] inBytes) {
        final int length = (int) Math.ceil(inBytes.length * 8 / 3.0);
        final byte[] outBytes = new byte[length];
        int l = 0;
        int k = 0;
        int parity = 0;
        int count = 0;
        byte outByte = 0;
        for (final byte inByte : inBytes) {
            for (int j = 0; j < 8; j++) {
                count++;
                count %= 3;
                final int bit = getBitAt(inByte, j);
                parity = parity ^ bit;
                outByte = setBitAt(outByte, k++, bit);
                outByte = setBitAt(outByte, k++, bit);
                if (count == 0) {
                    outByte = setBitAt(outByte, k++, parity);
                    outByte = setBitAt(outByte, k++, parity);
                    outBytes[l++] = outByte;
                    outByte = 0;
                    k = 0;
                    parity = 0;
                }
            }
        }
        if (l != length) {
            outByte = setBitAt(outByte, 6, parity);
            outByte = setBitAt(outByte, 7, parity);
            outBytes[l++] = outByte;
        }
        return outBytes;
    }

    private static int getBitAt(final byte b, final int pos) {
        final int shift = 7 - pos;
        return (b & (1 << shift)) >> shift;
    }

    private static byte setBitAt(final byte b, final int pos, final int bit) {
        if (bit == 1)
            return (byte) (b | (1 << (7 - pos)));
        else
            return (byte) (b & ~(1 << (7 - pos)));
    }

    private static byte[] jumble(final byte[] inBytes) {
        final Random random = new Random();
        final byte[] outBytes = new byte[inBytes.length];
        for (int i = 0; i < inBytes.length; i++) {
            final int num = 1 << random.nextInt(8);
            outBytes[i] = (byte) (inBytes[i] ^ num);
        }
        return outBytes;
    }

    private static byte[] decode(final byte[] inBytes) {
        final byte[] outBytes = new byte[inBytes.length * 3 / 8];
        byte outByte = 0;
        int l = 0;
        int k = 0;
        for (final byte inByte : inBytes) {
            int a = getBitAt(inByte, 0);
            int b = getBitAt(inByte, 2);
            int c = getBitAt(inByte, 4);
            final int p = getBitAt(inByte, 6);
            final int e = (p - a - b - c + 4) % 2;
            if (a != getBitAt(inByte, 1))
                a ^= e;
            else if (b != getBitAt(inByte, 3))
                b ^= e;
            else if (c != getBitAt(inByte, 5))
                c ^= e;
            final int[] bits = { a, b, c };
            for (final int bit : bits) {
                outByte = setBitAt(outByte, k++, bit);
                if (k == 8) {
                    k = 0;
                    outBytes[l++] = outByte;
                    outByte = 0;
                }
            }
        }
        return outBytes;
    }

    private static byte[] readFromFile(final String filename) {
        final File file = new File(filename);
        try (FileInputStream stream = new FileInputStream(file)) {
            return stream.readAllBytes();
        } catch (final Exception e) {
            System.out.println("Error in reading the file: " + file.getName());
            return new byte[] {};
        }
    }

    private static void writeToFile(final String filename, final byte[] bytes) {
        final File file = new File(filename);
        try (FileOutputStream stream = new FileOutputStream(file)) {
            stream.write(bytes);
        } catch (final Exception e) {
            System.out.println("Error in reading the file: " + file.getName());
        }
    }
}
