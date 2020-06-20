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
        final byte[] outBytes = new byte[inBytes.length * 2];
        for (int i = 0; i < inBytes.length; i++) {
            for (int j = 0; j < 2; j++) {
                int[] bits = new int[4];
                for (int k = 0; k < 4; k++)
                    bits[k] = getBitAt(inBytes[i], j * 4 + k);
                outBytes[2 * i + j] = encodeByte(bits);
            }
        }
        return outBytes;
    }

    private static byte encodeByte(final int[] bits) {
        byte outByte = 0;
        outByte = setBitAt(outByte, 1, bits[0] ^ bits[1] ^ bits[3]);
        outByte = setBitAt(outByte, 2, bits[0] ^ bits[2] ^ bits[3]);
        outByte = setBitAt(outByte, 3, bits[0]);
        outByte = setBitAt(outByte, 4, bits[1] ^ bits[2] ^ bits[3]);
        outByte = setBitAt(outByte, 5, bits[1]);
        outByte = setBitAt(outByte, 6, bits[2]);
        outByte = setBitAt(outByte, 7, bits[3]);
        return outByte;
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
        final byte[] outBytes = new byte[inBytes.length / 2];
        int outByteCount = 0;
        byte bitCount = 0;
        byte outByte = 0;
        for (final byte inByte : inBytes) {
            int[] bits = decodeByte(inByte);
            for (byte i = 0; i < 4; i++) {
                outByte = setBitAt(outByte, bitCount, bits[bitCount % 4]);
                bitCount++;
            }
            if (bitCount == 8) {
                outBytes[outByteCount++] = outByte;
                bitCount = 0;
                outByte = 0;
            }
        }
        return outBytes;
    }

    private static int[] decodeByte(byte inByte) {
        int p1 = getBitAt(inByte, 1);
        int p2 = getBitAt(inByte, 2);
        int p4 = getBitAt(inByte, 4);
        int a = getBitAt(inByte, 3);
        int b = getBitAt(inByte, 5);
        int c = getBitAt(inByte, 6);
        int d = getBitAt(inByte, 7);
        boolean c1 = (p1 == (a ^ b ^ d));
        boolean c2 = (p2 == (a ^ c ^ d));
        boolean c4 = (p4 == (b ^ c ^ d));
        if (!(c1 && c2 || c2 && c4 || c4 && c1))
            if (c1)
                c = flipInt(c);
            else if (c2)
                b = flipInt(b);
            else if (c4)
                a = flipInt(a);
            else
                d = flipInt(d);
        return new int[] { a, b, c, d };
    }

    private static int flipInt(int x) {
        return x == 1 ? 0 : 1;
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
