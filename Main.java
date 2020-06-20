package correcter;

import correcter.encoding.bit.*;
import correcter.encoding.hamming.*;
import correcter.encoding.character.*;

public class Main {
    public static void main(final String[] args) {
        if (args.length == 0)
            System.out.println("No encoding mode added.");
        else {
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
                    wrongMode();
            }
        }
    }

    private static void wrongMode() {
        System.out.println("Incorrect encoding mode");
        System.out.println("Choose From:");
        System.out.println("hamming");
        System.out.println("bit");
        System.out.println("char");
    }
}
