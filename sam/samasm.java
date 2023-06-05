import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

class samasm {
    static int MEMSIZE = 4096;
    static int codeLen;
    static int[] mem = new int[MEMSIZE];
    static String[] asm = new String[MEMSIZE];

    public static void main(String args[]) throws FileNotFoundException {
        if (args.length < 1) {
            System.out.println("Usage: java samasm codeFile");
            System.exit(-1);
        }

        System.out.println("Assembling SAM assembly code from file: " + args[0]);
        File asmcode = new File(args[0]);
        Scanner asmscan = new Scanner(asmcode);

        initSamState(asmscan);

        System.out.println("Code Length: " + codeLen);
        for (int i = 0; i < codeLen; i++) {
            System.out.println("\t" + asm[i]);
        }
    }

    public static void initSamState(Scanner asmscan) {
        int i = 0;

        asmscan.useDelimiter("\t|\r\n");

        while (asmscan.hasNext()) {
            String line = asmscan.next();
            String[] parts = line.trim().split("\\s+", 4);
            if (parts.length < 4)
                continue;

            mem[i] = Integer.parseInt(parts[2], 16);
            asm[i] = parts[0] + "\t" + parts[1] + "\t" + parts[2] + "\t" + parts[3];

            i++;
        }

        codeLen = i;
    }
}
