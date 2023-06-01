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
        File asmcode = new File(args[0] + ".codlst");
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

    public static void step(Scanner scan, int opcode, int addr) {
        if (opcode == 0) { // Lda
            pc++;
            acc = mem[addr];
        } else if (opcode == 1) { // Add
            pc++;
            acc = (acc + mem[addr]) & 0xffff;
        } else if (opcode == 2) { // Sub
            pc++;
            acc = (acc - mem[addr]) & 0xffff;
        } else if (opcode == 3) { // Sta
            pc++;
            mem[addr] = acc;
        } else if (opcode == 4) { // Jmp
            pc = addr;
        } else if (opcode == 5) { // Jaz
            if (acc == 0)
                pc = addr;
            else
                pc++;
        } else if (opcode == 6) { // Jan
            if (acc < 0)
                pc = addr;
            else
                pc++;
        } else if (opcode == 7) { // Mul
            pc++;
            acc = (acc * mem[addr]) & 0xffff;
        } else if (opcode == 8) { // Div
            pc++;
            if (mem[addr] != 0)
                acc = (acc / mem[addr]) & 0xffff;
        } else if (opcode == 9) { // Rem
            pc++;
            if (mem[addr] != 0)
                acc = (acc % mem[addr]) & 0xffff;
        } else if (opcode == 13) { // In
            pc++;
            System.out.print("In: ");
            acc = Integer.parseInt(scan.next());
        } else if (opcode == 14) { // Out
            pc++;
            out = acc;
            System.out.println("Out: " + out);
        } else if (opcode == 15) { // Hlt
            System.out.println("Hlt");
        } else {
            System.out.println("Illegal Instruction at pc = " + pc);
            System.exit(-1);
        }
    }

    public static void display() {
        System.out.println("SAM machine state");
        System.out.println(String.format("Pc: %03x Acc: %04x Out: %04x", pc, acc, out));
        String pad;
        for (int i = 0; i < codeLen; i++) {
            if (i == pc)
                pad = "> ";
            else
                pad = " ";
            System.out.println(String.format(pad + "%03x %04x\t%s", i, mem[i], asm[i]));
        }
    }

    public static void initSamState(Scanner asmscan) {
        int i = 0;

        pc = 0;
        acc = 0;

        asmscan.useDelimiter("\t|\r\n");

        while (asmscan.hasNext()) {
            String line = asmscan.next();
            String[] parts = line.trim().split("\\s+");

            mem[i] = Integer.parseInt(parts[2], 16);
            asm[i] = parts[0] + "\t" + parts[1] + "\t" + parts[2] + "\t" + parts[3];

            i++;
        }

        codeLen = i;
    }
}
