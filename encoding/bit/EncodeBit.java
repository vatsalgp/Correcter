package correcter.encoding.bit;

import correcter.io.IOByte;
import java.util.Random;

public class EncodeBit {
    public static void all() {
        encodeMode();
        sendMode();
        decodeMode();
    }

    public static void encodeMode() {
        final byte[] readBytes = IOByte.readFromFile("send.txt");
        final byte[] encodedBytes = encode(readBytes);
        IOByte.writeToFile("encoded.txt", encodedBytes);
    }

    public static void sendMode() {
        final byte[] encodedBytes = IOByte.readFromFile("encoded.txt");
        final byte[] jumbledBytes = jumble(encodedBytes);
        IOByte.writeToFile("received.txt", jumbledBytes);
    }

    public static void decodeMode() {
        final byte[] receivedBytes = IOByte.readFromFile("received.txt");
        final byte[] decodedBytes = decode(receivedBytes);
        IOByte.writeToFile("decoded.txt", decodedBytes);
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
}