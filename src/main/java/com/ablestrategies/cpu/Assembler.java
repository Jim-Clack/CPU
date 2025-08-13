package com.ablestrategies.cpu;

import java.util.HashMap;

/**
 * Two-pass Assembler/Linker.
 * Pass in a string that contains a series of the following statements:
 *   [label:] [address:] [opcode] [arg] [label:] ['byte, byte...'] ["string"]
 * These can be newline-delimited or semicolon-delimited. If the address
 * is omitted, it increments starting from zero. Opcodes are not case-
 * sensitive. Spaces are optional and ignored. Comments begin with #. If
 * a label is left-justified then it's a target instead of a reference.
 */
public class Assembler {

    enum PassNumber {
        Pass1FindTargets,
        Pass2ProcessCode;
    }

    private final HashMap<String, Integer> mapOfLabels = new HashMap<>();
    private final CPU cpu;
    private PassNumber passNumber;

    public Assembler(CPU cpu) {
        this.cpu = cpu;
    }

    public void assemble(String program) {
        passNumber = PassNumber.Pass1FindTargets;
        processProgram(program);
        passNumber = PassNumber.Pass2ProcessCode;
        processProgram(program);
    }

    private void processProgram(String program) {
        int address = 0;
        boolean comment = false;
        StringBuilder sb = new StringBuilder();
        for(char ch : program.toCharArray()) {
            if(ch == '\'' && !comment) {
                address = parseBytes(sb.toString(), address);
            } else if(ch == '\"' && !comment) {
                address = parseString(sb.toString(), address);
            } else if(ch == '\n' || ch == '\r' || ch == ';') {
                String deposit = sb.toString();
                sb.setLength(0);
                address = parseLine(deposit, address);
                comment = false;
            } else if (ch == '#') {
                comment = true;
            } else if(ch != '\t' && ch != ' ' && !comment) {
                sb.append(ch);
            }
        }
    }

    private int parseBytes(String data, int address) {
        data = data.substring(1);
        if(data.endsWith("\'")) { // strip quotes
            data = data.substring(0, data.length() - 1);
        }
        String[] bytes = data.split(",");
        for(String b : bytes) {
            int value = Integer.parseInt(b.trim());
            address = emit(address, value);
        }
        return address;
    }

    private int parseString(String str, int address) {
        str = str.substring(1);
        if(str.endsWith("\"")) { // strip quotes
            str = str.substring(0, str.length() - 1);
        }
        for(char ch : str.toCharArray()) {
            int value = (int)ch;
            address = emit(address, value);
        }
        address = emit(address, 0);
        return address;
    }

    private int parseLine(String deposit, int address) {
        if (!deposit.isEmpty()) {
            String[] split = deposit.split(":");
            if (split.length == 2) {
                address = Integer.parseInt(split[0]);
                deposit = split[1];
            }
            if(split[0].endsWith(":") && !Character.isDigit(split[0].charAt(0))) {
                if(passNumber == PassNumber.Pass1FindTargets) {
                    mapOfLabels.put(split[0].trim().toUpperCase(), address);
                }
                ++address;
            } else {
                address = parseOpcodeOrArg(deposit, address);
            }
        }
        return address;
    }

    private int parseOpcodeOrArg(String deposit, int address) {
        int value = 0;
        if(deposit.endsWith(":") && !Character.isDigit(deposit.charAt(0))) {
            Integer valOrNull = mapOfLabels.get(deposit.trim().toUpperCase());
            if(valOrNull == null) {
                System.out.println("ASM ERROR: Cannot find label " + deposit);
            } else {
                value = valOrNull;
            }
        } else {
            try {
                value = Integer.parseInt(deposit);
            } catch (NumberFormatException ex) {
                Opcode opcode = Opcode.opcode(deposit);
                if (opcode.getValue() == Opcode.INVALID.getValue()) {
                    System.out.println("ASM ERROR: Invalid entry at address " + address);
                }
                value = opcode.getValue();
            }
            address = emit(address, value);
        }
        return address;
    }

    private int emit(int address, int value) {
        if(passNumber == PassNumber.Pass2ProcessCode) {
            cpu.getMemoryCell(address).set(value);
        }
       return ++address;
    }

}
