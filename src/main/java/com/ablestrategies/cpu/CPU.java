package com.ablestrategies.cpu;

public class CPU extends ALU {

    public void assemble(String program) {
        int index = 0;
        StringBuilder sb = new StringBuilder();
        for(char ch : program.toCharArray()) {
            if(ch == '\n' || ch == '\r' || ch == ',' || ch == ';') {
                String deposit = sb.toString();
                sb.setLength(0);
                if(!deposit.isEmpty()) {
                    int value = 0;
                    String[] split = deposit.split(":");
                    if (split.length == 2) {
                        index = Integer.parseInt(split[0]);
                        deposit = split[1];
                    }
                    try {
                        value = Integer.parseInt(deposit);
                    } catch (NumberFormatException ex) {
                        index = Opcode.opcode(deposit).getValue(); // ASM
                    }
                    if (value >= Octet.MaxBitWgt) {
                        value = value - Octet.NextBitWgt;
                    }
                    memoryCells[index++].set(value);
                }
            } else if(ch != '\t' && ch != ' ' && ch != '#') {
                sb.append(ch);
            }
        }
    }

}
