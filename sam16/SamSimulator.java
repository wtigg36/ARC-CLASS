import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class SamSimulator {
    // SAM ISA machine state (global)
    static int MEMSIZE = 4096; // 2^12 = 16^3
    static int acc = 0;
    static int pc = 0;
    static int out = 0;
    static int codeLen;
    static int mem[] = new int[MEMSIZE];
    static String asm[] = new String[MEMSIZE];

    public static void main(String args[]) throws FileNotFoundException {
        if (args.length < 1) {
            System.out.println("Usage: java SamSimulator codeFile");
            System.exit(-1);
        }

        String codeFilePath = args[0];
        String listFilePath = codeFilePath + ".codlst";

        // Set up Sam console read
        Scanner scan = new Scanner(System.in);

        // Open code file and asm listing file
        File codeFile = new File(codeFilePath);
        File listFile = new File(listFilePath);

        Scanner codeScan = new Scanner(codeFile);
        Scanner listScan = new Scanner(listFile);

        // Initialize the Sam machine state (registers and memory)
        initSamState(codeScan, listScan);

        // Print the initial SAM machine state
        display();

        // Sam ISA simulation loop
        System.out.print("Enter cmd (run, step, or quit): ");
        char cmd = scan.next().charAt(0);

        while ((cmd != 'r') && (cmd != 's') && (cmd != 'q')) {
            System.out.print("Enter cmd (run, step, or quit): ");
            cmd = scan.next().charAt(0);
        }

        while (cmd != 'q') {
            // Decode the instruction at pc
            int opcode = mem[pc] / 4096;
            int addr = mem[pc] % 4096;

            // Perform the instruction and display machine state
            step(scan, opcode, addr);

            // If cmd == 'r' and opcode != 15 (hlt), we do not display the state
            if (cmd == 'r' && opcode == 15) {
                return;
            }

            if (cmd == 's') {
                display();
                scan.nextLine(); // Eat up newline
                System.out.print("Enter cmd (run, step, or quit): ");
                cmd = scan.next().charAt(0);
            }
        }
    }

    // Sam Instruction Decode and Execution
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
            if (acc == 0) {
                pc = addr;
            } else {
                pc++;
            }
        } else if (opcode == 6) { // Jan
            if (acc >= 128) {
                pc = addr;
            } else {
                pc++;
            }
        } else if (opcode == 7) { // Out
            pc++;
            out = acc;
            System.out.println("OUT: " + out);
        } else if (opcode == 8) { // In
            pc++;
            System.out.print("Enter input: ");
            int input = scan.nextInt();
            acc = input;
        } else if (opcode == 9) { // Hlt
            System.out.println("HALT");
            display();
            System.exit(0);
        } else {
            System.out.println("Invalid opcode: " + opcode);
            display();
            System.exit(-1);
        }
    }

    // Initialize the Sam machine state
    public static void initSamState(Scanner codeScan, Scanner listScan) {
        // Read the code file and populate memory
        codeLen = 0;
        while (codeScan.hasNextLine()) {
            String line = codeScan.nextLine().trim();
            if (line.isEmpty()) {
                continue;
            }
            mem[codeLen] = Integer.parseInt(line, 16);
            asm[codeLen] = listScan.nextLine().trim();
            codeLen++;
        }

        codeScan.close();
        listScan.close();
    }

    // Display the current state of the Sam machine
    public static void display() {
        System.out.println();
        System.out.println("ACC: " + acc);
        System.out.println("PC: " + pc);
        System.out.println("OUT: " + out);
        System.out.println("CODE:");
        for (int i = 0; i < codeLen; i++) {
            System.out.println(i + ": " + asm[i]);
        }
    }
}

