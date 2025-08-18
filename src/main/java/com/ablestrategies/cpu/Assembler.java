package com.ablestrategies.cpu;

import java.util.HashMap;
import java.util.Map;

/**
 * Quick two-pass Assembler/LinkLoader.
 *   See AssemblerTest.java for examples.
 * Meaning of opcode Mnemonib:
 *   Operation[Condition][AddrMode]
 * Condition:
 *   ZE = Zero or Equal         NZE = Not Zero or Equal
 *   LT = Less than             LTE = Less Than or Equal
 *   GT = Greater than          GTE = Greater Than or Equal
 * AddrMode:
 *   IMM = Immediate value - value is Arg2 literally
 *   REG = Register number - designated register Arg2
 *   IND = Indirect - memory as specified by the designated register Arg2
 *   MEM = Memory address - memory as specified by Arg2
 *   FRA = Stack frame offset from FP (only after ENTER opcode) see Note3
 *     Note1: for 2 Arg Ops: Arg1 is always REG so the AddrMode is for Arg2
 *     Note2: AddrMode for source, except STORxxx where it's destination
 *     Note3: FRA AddrMode 1, 2, 3... for params; 0, -1, -2... for locals
 * Pass in a string that contains a series of the following statements:
 *  [label:] [address:] [[opcode[,]] [arg[,]...] [label:] [byte[,]...] ["string"]
 * These can be newline-delimited or semicolon-delimited.
 * - If the address is omitted, it increments starting from zero.
 * - Opcodes are not case-sensitive.
 * - Spaces are optional and ignored.
 * - Comments begin with # and continue to the end of line or a semicolon.
 * - If a label is left-justified then it's a target instead of a reference.
 * - ConstantArgs/Bytes can start with "0x" if they are hex instead of decimal.
 * - Register numbers start with "0$" or "$", required if it's a register name.
 * - Math/logic operations on System registers do not set ZE/GT/LT flags.
 * - There is a directive/pseudo-op EQU for creating numeric constants as well.
 */
public class Assembler {

    enum PassNumber {
        Pass1Assemble,
        Pass2LinkLoad;
    }

    private final HashMap<String, Integer> mapOfLabels = new HashMap<>();
    private final HashMap<String, Integer> mapOfConstants = new HashMap<>();
    private final CPU cpu;
    private String lastLabel = "";
    private int address;
    private int errorCount;
    private int maxAddress;
    private PassNumber passNumber;
    private boolean listHexCodes = true;
    private int expectArgCount = 0;

    public Assembler(CPU cpu) {
        this.cpu = cpu;
    }

    public void setListHexCodes(boolean listHexCodes) {
        this.listHexCodes = listHexCodes;
    }

    public byte[] getMachineCode() {
        byte[] code = new byte[maxAddress];
        for(int i = 0; i < maxAddress; i++) {
            code[i] = (byte)cpu.getMemoryCell(i).getSignedValue();
        }
        return code;
    }

    public void loadMachineCode(byte[] code) {
        for(int i = 0; i < code.length; i++) {
            cpu.memoryCells[i].set(code[i]);
        }
    }

    public int assemble(String program) {
        mapOfLabels.clear();
        mapOfConstants.clear();
        System.out.println("ASM Assembling...");
        passNumber = PassNumber.Pass1Assemble;
        boolean ok = processProgram(program);
        if(ok) {
            System.out.println("ASM LinkLoading...");
            passNumber = PassNumber.Pass2LinkLoad;
            ok = processProgram(program);
        }
        System.out.println("Done.");
        return errorCount;
    }

    private boolean processProgram(String program) {
        boolean comment = false;
        boolean inString = false;
        StringBuilder sb = new StringBuilder();
        address = 0;
        maxAddress = 0;
        errorCount = 0;
        for(char ch : program.toCharArray()) {
            if(ch == '\"' && !comment) { // quoted string
                if(inString) {
                    address = parseString(sb.toString(), address);
                } else if(!sb.isEmpty()) {
                    address = parseLine(sb.toString(), address);
                }
                inString = !inString;
                sb.setLength(0);
            } else if(ch == '\n' || ch == '\r' || ch == ';') { // eoln
                String deposit = sb.toString();
                sb.setLength(0);
                address = parseLine(deposit, address);
                comment = false;
            } else if (ch == '#') { // comment
                comment = true;
            } else if(!comment) { // else accumulate chars
                sb.append(ch);
            }
            if(errorCount > 10) {
                showError("Too Many Errors, Aborting");
                return false;
            }
        }
        return true;
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
        if(expectArgCount > 0 && passNumber == PassNumber.Pass2LinkLoad) {
            showError("ASM ERROR: Prior opcode had bad or missing args");
            expectArgCount = 0;
        }
        if (!deposit.isEmpty()) {
            deposit = deposit.trim();
            String[] split = deposit.split(":", 2);
            if(!split[0].matches(".*[ ,].*")) { // if it's a target label (left justified)
                split[0] = split[0].trim();
                boolean isTarget = deposit.length() > split[0].length() && deposit.charAt(split[0].length()) == ':';
                if (!split[0].isEmpty() && isTarget) {
                    address = parseTarget(address, split);
                    deposit = split[1].trim();
                }
            }
            if (!deposit.trim().isEmpty()) { // parse remainder of line
                int spacePos = deposit.indexOf(' ');
                if(spacePos > 0 && spacePos <= Opcode.LongestMnemonicLgt) { // insert missing comma
                    deposit = deposit.substring(0, spacePos) + "," +  deposit.substring(spacePos + 1);
                }
                split = deposit.split(",");
                for (String opcodeOrArg : split) {
                    String opcode = opcodeOrArg.trim();
                    if(opcode.equalsIgnoreCase("EQU")) {
                        address = parseConstant(split[1], address);
                        break;
                    } else {
                        address = parseToken(opcode, address);
                    }
                }
            }
        }
        return address;
    }

    private int parseTarget(int address, String[] split) {
        if (Character.isDigit(split[0].charAt(0))) { // address followed by colon
            address = parseNumeric(split[0], false);
        } else if (passNumber == PassNumber.Pass1Assemble) { // left-justified label followed by colon
            String label = split[0].trim().toUpperCase();
            this.lastLabel = label;
            if(mapOfLabels.containsKey(label)) {
                showError("Same label used in more than one place: " + label);
            }
            mapOfLabels.put(label, address);
            System.out.printf(" >>> %04x: %s (label)\n", address, label);
        }
        return address;
    }

    private int parseConstant(String label, int address) {
        int intVal = this.parseNumeric(label, false);
        if (passNumber == PassNumber.Pass1Assemble) { // left-justified label followed by colon
            mapOfConstants.put(this.lastLabel, intVal);
            System.out.printf(" >>> %04x: %s (constant)\n", intVal, this.lastLabel);
        }
        return address;
    }

    private int parseToken(String deposit, int address) {
        int value = 0;
        if(deposit.trim().isEmpty()) {
            return address;
        }
        if(deposit.endsWith(":") && Character.isAlphabetic(deposit.charAt(0))) {
            value = parseReference(deposit, value);
        } else {
            value = parseOpcodeOrByte(deposit, address, value);
        }
        address = emit(address, value);
        return address;
    }

    private int parseReference(String deposit, int value) {
        // Always check for a constant before a label, as constants are also labelled
        Integer valOrNull1 = mapOfConstants.get(deposit.substring(0, deposit.length() - 1).trim().toUpperCase());
        Integer valOrNull2 = mapOfLabels.get(deposit.substring(0, deposit.length() - 1).trim().toUpperCase());
        if(valOrNull1 != null) {
            value = valOrNull1;
        } else if(valOrNull2 != null) {
            value =  valOrNull2;
        } else if(passNumber == PassNumber.Pass2LinkLoad) {
            showError("ASM ERROR: Cannot find label " + deposit);
        }
        expectArgCount--;
        return value;
    }

    private int parseOpcodeOrByte(String deposit, int address, int value) {
        if(!Character.isAlphabetic(deposit.charAt(0))) {
            try {
                value = parseNumeric(deposit, true);
            } catch (NumberFormatException ex) {
                showError("ASM ERROR: Expected numeric");
            }
            expectArgCount--;
        } else {
            Opcode opcode = Opcode.opcode(deposit);
            if (opcode.getValue() == Opcode.INVALID.getValue()) {
                showError("ASM ERROR: Invalid opcode");
            }
            value = opcode.getValue();
            expectArgCount = opcode.getNumArgs();
        }
        return value;
    }

    private int emit(int address, int value) {
        if(passNumber == PassNumber.Pass2LinkLoad) {
            if(address < maxAddress) {
                showError("ASM ERROR: Reduction in address may overwrite code");
            }
            if(listHexCodes) {
                System.out.printf(" >>> %02x: %02x\n", address, value);
                System.out.flush();
            }
            cpu.getMemoryCell(address).set(value);
        }
        maxAddress = Math.max(maxAddress, ++address);
        return address;
    }

    private void showError(String message) {
        System.out.println(message + ", at: " + toHex(address));
        errorCount++;
    }

    private int parseNumeric(String stringVal, boolean isRegisterNum) throws NumberFormatException {
        if (stringVal.trim().isEmpty()) {
            return 0;
        }
        int value = 0;
        if(isRegisterNum && stringVal.contains("$")) {
            stringVal = stringVal.substring(stringVal.indexOf('$') + 1).trim();
            switch (stringVal) {
                case "FLAGS": value = Substrate.FLAGS; break;
                case "FP": value = Substrate.FP; break;
                case "SP": value = Substrate.SP; break;
                case "IP": value = Substrate.IP; break;
                case "IV": value = Substrate.IV; break;
                case "IN": value = Substrate.IN; break;
                default:   value = Integer.parseInt(stringVal); break;
            }
        } else if(stringVal.toUpperCase().startsWith("0X")) {
            stringVal = stringVal.substring(2);
            if(stringVal.endsWith(":")) {
                stringVal = stringVal.substring(0, stringVal.length() - 1);
            }
            value = Integer.parseInt(stringVal, 16);
        } else {
            value = Integer.parseInt(stringVal);
        }
        return value;
    }

    private String toHex(int value) {
        return String.format("%x", value);
    }

}
