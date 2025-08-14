package com.ablestrategies.cpu;

import java.util.HashMap;

/**
 * Two-pass Assembler/Linker.
 * Pass in a string that contains a series of the following statements:
 *   [label:] [address:] [opcode] [arg] [label:] [byte, byte...] ["string"]
 * These can be newline-delimited or semicolon-delimited. If the address
 * is omitted, it increments starting from zero. Opcodes are not case-
 * sensitive. Spaces are optional and ignored. Comments begin with # and
 * continue to the end of line or a semicolon. If a label is left-justified
 * then it's a target instead of a reference.
 */
public class Assembler {

    enum PassNumber {
        Pass1Assemble,
        Pass2LinkLoad;
    }

    private final HashMap<String, Integer> mapOfLabels = new HashMap<>();
    private final CPU cpu;
    private PassNumber passNumber;

    public Assembler(CPU cpu) {
        this.cpu = cpu;
    }

    public void assemble(String program) {
        passNumber = PassNumber.Pass1Assemble;
        processProgram(program);
        passNumber = PassNumber.Pass2LinkLoad;
        processProgram(program);
    }

    private void processProgram(String program) {
        int address = 0;
        boolean comment = false;
        boolean inString = false;
        StringBuilder sb = new StringBuilder();
        for(char ch : program.toCharArray()) {
            if(ch == '\"' && !comment) {
                if(inString) {
                    address = parseString(sb.toString(), address);
                }
                inString = !inString;
                sb.setLength(0);
            } else if(ch == '\n' || ch == '\r' || ch == ';') {
                String deposit = sb.toString();
                sb.setLength(0);
                address = parseLine(deposit, address);
                comment = false;
            } else if (ch == '#') {
                comment = true;
            } else if(!comment) {
                sb.append(ch);
            }
        }
    }

    private int parseString(String str, int address) {
        if(str.trim().isEmpty()) {
            return address;
        }
        str = str.trim();
        for(char ch : str.toCharArray()) {
            int value = (int)ch;
            address = emit(address, value);
        }
        address = emit(address, 0);
        return address;
    }

    private int parseLine(String deposit, int address) {
        if (!deposit.isEmpty()) {
            deposit = deposit.trim();
            String[] split = deposit.split(":", 2);
            if(!split[0].matches(".*[ ,].*")) {
                split[0] = split[0].trim();
                boolean isTarget = deposit.length() > split[0].length() && deposit.charAt(split[0].length()) == ':';
                if (!split[0].isEmpty() && isTarget) {
                    address = parseTarget(address, split);
                    deposit = split[1].trim();
                }
            }
            if (!deposit.trim().isEmpty()) {
                int spacePos = deposit.indexOf(' ');
                if(spacePos > 0 && spacePos < 8) {
                    deposit = deposit.substring(0, spacePos) + "," +  deposit.substring(spacePos + 1);
                }
                split = deposit.split(",");
                for (String opcodeOrArg : split) {
                    address = parseWord(opcodeOrArg.trim(), address);
                }
            }
        }
        return address;
    }

    private int parseTarget(int address, String[] split) {
        if (Character.isDigit(split[0].charAt(0))) { // addreee followed by colon
            address = Integer.parseInt(split[0]);
        } else if (passNumber == PassNumber.Pass1Assemble) { // left-justified label followed by colon
            mapOfLabels.put(split[0].trim().toUpperCase(), address);
        }
        return address;
    }

    private int parseWord(String deposit, int address) {
        int value = 0;
        if(deposit.trim().isEmpty()) {
            return address;
        }
        if(deposit.endsWith(":") && !Character.isDigit(deposit.charAt(0))) {
            value = parseReference(deposit, value);
        } else {
            value = parseOpcodeOrByte(deposit, address, value);
        }
        address = emit(address, value);
        return address;
    }

    private int parseReference(String deposit, int value) {
        Integer valOrNull = mapOfLabels.get(deposit.substring(0, deposit.length() - 1).trim().toUpperCase());
        if(valOrNull != null) {
            value = valOrNull;
        } else if(passNumber == PassNumber.Pass2LinkLoad) {
            System.out.println("ASM ERROR: Cannot find label " + deposit);
        }
        return value;
    }

    private static int parseOpcodeOrByte(String deposit, int address, int value) {
        if(Character.isDigit(deposit.charAt(0))) {
            try {
                value = Integer.parseInt(deposit);
            } catch (NumberFormatException ex) {
                System.out.println("ASM ERROR: Expected numeric at address " + address);
            }
        } else {
            Opcode opcode = Opcode.opcode(deposit);
            if (opcode.getValue() == Opcode.INVALID.getValue()) {
                System.out.println("ASM ERROR: Invalid opcode at address " + address);
            }
            value = opcode.getValue();
        }
        return value;
    }

    private int emit(int address, int value) {
        if(passNumber == PassNumber.Pass2LinkLoad) {
            System.out.printf(" >>> %04x: %02x \n", address, value); System.out.flush();
            cpu.getMemoryCell(address).set(value);
        }
       return ++address;
    }

}
