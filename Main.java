package correcter;

import correcter.encoding.bit.*;
import correcter.encoding.hamming.*;
import correcter.encoding.character.*;

public class Main {
    public static void main(final String[] args) {
        try {
            if (args.length == 0) {
                System.out.println("No encoding mode added.");
                wrongMode();
            } else {
                switch (args[0].toLowerCase()) {
                    case "hamming":
                        EncodeHamming.all();
                        break;
                    case "bit":
                        EncodeBit.all();
                        break;
                    case "char":
                        EncodeChar.all();
                        break;
                    default:
                        System.out.println("Incorrect encoding mode");
                        wrongMode();
                }
            }
        } catch (Exception e) {
            System.out.println("Error");
        }
    }

    private static void wrongMode() {
        System.out.println("Choose From:");
        System.out.println("hamming");
        System.out.println("bit");
        System.out.println("char");
    }
}
