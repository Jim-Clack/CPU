package com.ablestrategies.cpu;

/**
 * Sort of an mini-assembler that does not do fixups (references).
 * Pass in a string that contains a series of the following:
 *    address:value
 *    address:opcode
 *    value
 *    opcode
 * These can be newline-delimited or comma-delimited. If the address
 * is omitted, it increments starting from zero. Opcodes are not case-
 * sensitive. Spaces are optional and ignored. Comments begin with #.
 */
public class Assembler {

    private final CPU cpu;

    public Assembler(CPU cpu) {
        this.cpu = cpu;
    }

    public void assemble(String program) {
        int address = 0;
        boolean comment = false;
        StringBuilder sb = new StringBuilder();
        for(char ch : program.toCharArray()) {
            if(ch == '\n' || ch == '\r' || ch == ',' || ch == ';') {
                String deposit = sb.toString();
                sb.setLength(0);
                if (!deposit.isEmpty()) {
                    String[] split = deposit.split(":");
                    if (split.length == 2) {
                        address = Integer.parseInt(split[0]);
                        deposit = split[1];
                    }
                    if (parseOpcodeOrArg(deposit, address)) {
                        break;
                    }
                    address++;
                }
                comment = false;
            } else if (ch == '#') {
                comment = true;
            } else if(ch != '\t' && ch != ' ' && !comment) {
                sb.append(ch);
            }
        }
    }

    private boolean parseOpcodeOrArg(String deposit, int address) {
        int value;
        try {
            value = Integer.parseInt(deposit);
        } catch (NumberFormatException ex) {
            Opcode  opcode = Opcode.opcode(deposit);
            if(opcode.getValue() == Opcode.INVALID.getValue()) {
                System.out.println("ASM ERROR: Invalid entry at address " + address);
                return true;
            }
            value = opcode.getValue();
        }
        if (value >= Octet.MaxBitWgt) {
            value = value - Octet.NextBitWgt;
        }
        cpu.getMemoryCell(address).set(value);
        return false;
    }

}
