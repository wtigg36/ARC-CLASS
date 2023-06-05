import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

class samsim {
    static int MEMSIZE = 4096;
    static int acc = 0;
    static int pc = 0;
    static int out = 0;
    static int codeLen;
    static int[] mem = new int[MEMSIZE];
    static String[] asm = new String[MEMSIZE];

    public static void main(String args[]) throws FileNotFoundException {
        if (args.length < 1) {
            System.out.println("Usage: java samsim codeFile");
            System.exit(-1);
        }

        Scanner scan = new Scanner(System.in);

        System.out.println("Reading SAM assembly code and listing file " + args[0] + ".codlst");
        File asmcode = new File(args[0]);
        Scanner asmscan = new Scanner(asmcode);

        initSamState(asmscan);

        display();

        System.out.print("Enter cmd (run, step, or quit): ");
        char cmd = scan.next().charAt(0);

        while ((cmd != 'r') && (cmd != 's') && (cmd != 'q')) {
            System.out.print("Enter cmd (run, step, or quit): ");
            cmd = scan.next().charAt(0);
        }

        while (cmd != 'q') {
            int opcode = mem[pc] / 4096;
            int addr = mem[pc] % 4096;

            step(scan, opcode, addr);

            if (cmd == 'r' && opcode == 15) {
                return;
            }

            if (cmd == 's') {
                display();
                scan.nextLine();
                System.out.print("Enter cmd (run, step, or quit): ");
                cmd = scan.next().charAt(0);
            }
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

    public static void step(Scanner scan, int opcode, int addr) {
        if (opcode == 1) {
            acc = mem[addr];
            pc++;
        } else if (opcode == 2) {
            mem[addr] = acc;
            pc++;
        } else if (opcode == 3) {
            acc += mem[addr];
            pc++;
        } else if (opcode == 4) {
            acc -= mem[addr];
            pc++;
        } else if (opcode == 5) {
            if (acc == 0) {
                pc = addr;
            } else {
                pc++;
            }
        } else if (opcode == 6) {
            pc = addr;
        } else if (opcode == 7) {
            out = acc;
            System.out.println("Output: " + out);
            pc++;
        } else if (opcode == 15) {
            System.out.println("Program halted.");
        } else {
            System.out.println("Unknown instruction - aborting");
            System.exit(-1);
        }
    }

    public static void display() {
        System.out.println("SAM machine state");
        System.out.println("Pc: " + String.format("%03d", pc) +
                " Acc: " + String.format("%04d", acc) +
                " Out: " + String.format("%04d", out));
    }
}
