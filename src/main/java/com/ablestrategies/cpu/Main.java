package com.ablestrategies.cpu;

// TOPICS for discussion
// bits numbered right-to-left (units, twos, fours, etc.) i.e. 7 6 5 4 3 2 1 0
// little endian (versus big endian)
// logic, shift, move, test - bit operations only
// no math except on real-world conversions (strings, decimal values)
// note: iterating over bits at a low level is really "selection" not addition
// two's compliment notation (negative values, numeric invert b boolean invert)
// flip-flops vs logic gates, clock signal latches output

public class Main {
    public static void main(String[] args) {
        Main main = new Main();
        main.test(Gate.XOR);
        main.test(Gate.AND);
        main.test(Gate.OR);
        main.test(Gate.NAND);
        main.test();
        main.test(-1, 33);
        main.test(0, 0);
        main.test(17, 38);
        main.test(106, 11);
        main.test(97, -118);
    }

    public void test() {
        System.out.println("Octet Tests:");
        System.out.println("   86 = " + new Octet("01010110"));
        System.out.println("   54 = " + new Octet(0x36));
        System.out.println(" -107 = " + new Octet(-107));
        Register register = new Register();
        register.set(new Octet(107));
        register.invert();
        System.out.println(" -107 = " + register.getValue());
        System.out.println(" -108 = " + new Octet(107).onesCompliment());
        System.out.println("  107 = " + new Octet(-108).onesCompliment());
    }

    public void test(Gate gate) {
        System.out.println("Gate Test: " + gate);
        for(int a = 0; a < 2; a++) {
            for (int b = 0; b < 2; b++) {
                Bit result = gate.output(new Bit(a), new Bit(b));
                System.out.println(" a=" + a + ", b=" + b + ", ==>" + result);
            }
        }
    }

    public void test(int p, int q) {
        Octet octetP = new Octet(p);
        Octet octetQ = new Octet(q);
        Adder adder = new Adder();
        adder.set(octetP);
        System.out.println("Adder Test: " + octetP + " + " + octetQ);
        System.out.println(" Result=" + adder.add(octetQ));
        System.out.println("  Carry=" + adder.isCarry());
        System.out.println("   Zero=" + adder.isZero());
        System.out.println("    Neg=" + adder.isNegative());
    }
}