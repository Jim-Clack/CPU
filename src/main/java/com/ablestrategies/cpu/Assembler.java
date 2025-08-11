package com.ablestrategies.cpu;

public class Assembler {

    private final CPU cpu;

    public Assembler(CPU cpu) {
        this.cpu = cpu;
    }

    public void assemble(String program) {
        int address = 0;
        StringBuilder sb = new StringBuilder();
        for(char ch : program.toCharArray()) {
            if(ch == '\n' || ch == '\r' || ch == ',' || ch == ';') {
                String deposit = sb.toString();
                sb.setLength(0);
                if(!deposit.isEmpty()) {
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
            } else if(ch != '\t' && ch != ' ' && ch != '#') {
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
