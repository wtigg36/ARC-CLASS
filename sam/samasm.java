import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Scanner;

class samasm {
    static final int MEMSIZE = 4096;
    static final int OPTABSIZE = 16;
    static int codeLen = 0;
    static String[] lab = new String[MEMSIZE];
    static String[] op = new String[MEMSIZE];
    static String[] addr = new String[MEMSIZE];
    static int[] code = new int[MEMSIZE];
    static String[] opTab = {"lda", "add", "sub", "sta", "jmp", "jaz", "jan", "mul",
                             "div", "rem", "", "", "", "in", "out", "hlt"};

    public static void main(String args[]) throws FileNotFoundException {
        if (args.length < 1) {
            System.out.println("Usage: java samasm asmfile.txt");
        } else {
            File outCodeFile = new File(args[0] + ".cod");
            PrintStream outcode = new PrintStream(outCodeFile);

            File outListFile = new File(args[0] + ".codlst");
            PrintStream outlist = new PrintStream(outListFile);

            File asmcode = new File(args[0]);
            Scanner filescan = new Scanner(asmcode);
            filescan.useRadix(16);

            parse(filescan);
            genCode();
            showAsmCode();
            dumpCode(outcode);
            dumpListing(outlist);
        }
    }

    public static void parse(Scanner filescan) {
        String codeStr;
        String[] codeline;
        int codeIndex = 0;

        while (filescan.hasNext()) {
            if (codeIndex >= MEMSIZE) {
                System.out.println("Program is too big for SAM memory - abort");
                System.exit(-1);
            }

            codeStr = filescan.nextLine();
            codeline = codeStr.split("\\s+");

            if (codeline.length == 0) {
                System.out.println("Blank line; ignore");
                continue;
            }

            if (codeline.length == 1) {
                System.out.println("Lone label; ignore");
                continue;
            }

            lab[codeIndex] = codeline[0];
            op[codeIndex] = codeline[1];

            if (codeline.length == 2) {
                addr[codeIndex] = "";
            } else {
                addr[codeIndex] = codeline[2];
            }

            codeIndex++;
        }

        codeLen = codeIndex;
    }

    public static void showAsmCode() {
        System.out.println("\nCode Length: " + codeLen);
        for (int i = 0; i < MEMSIZE && i < codeLen; i++) {
            System.out.println(lab[i] + "\t" + op[i] + "\t" + addr[i] + " \t"
                    + String.format("%03x", i) + " "
                    + String.format("%04x", (code[i] & 0xffff)));
        }
    }

    public static void dumpCode(PrintStream outcode) {
        for (int i = 0; i < codeLen; i++) {
            outcode.println(String.format("%04x", (code[i] & 0xffff)));
        }
    }

    public static void dumpListing(PrintStream outlist) {
        for (int i = 0; i < codeLen; i++) {
            outlist.println(String.format("%04x", (code[i] & 0xffff)) + "\t"
                    + String.format("%03x", i) + "\t"
                    + lab[i] + "\t" + op[i] + "\t" + addr[i]);
        }
    }

    public static int getOpNum(String opStr) {
        int i;
        for (i = 0; i < OPTABSIZE; i++) {
            if (opStr.toLowerCase().equals(opTab[i]))
                return i;
        }
        return -1;
    }

    public static int getLabNum(String labStr) {
        int i;
        if (labStr.equals(""))
            return 0;

        for (i = 0; i < codeLen; i++) {
            if (labStr.equals(lab[i]))
                return i;
        }
        return -1;
    }

    public static void genCode() {
        int theOp, theAddr;
        int i;
        for (i = 0; i < codeLen && !op[i].toLowerCase().equals("dat"); i++) {
            if (getOpNum(op[i]) < 0) {
                System.out.println("Unknown instruction " + op[i] + " - aborting");
                return;
            }
            if (getLabNum(addr[i]) < 0) {
                System.out.println("Missing label in Column 1: " + addr[i] + " - aborting");
                return;
            }
            code[i] = (getOpNum(op[i])) * 4096 + getLabNum(addr[i]);
        }
        for (; i < codeLen; i++) {
            if (!op[i].toLowerCase().equals("dat")) {
                System.out.println("Data only section " + op[i] + " - aborting");
                return;
            }
            code[i] = Integer.parseInt(addr[i], 16);
        }
    }
}
