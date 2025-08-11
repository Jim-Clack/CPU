package com.ablestrategies.cpu;

/**
 * Opcode Key:
 *   Operation + [Condition] + [AddrMode]
 * Conditions:
 *   ZE
 *   LT
 *   GT
 * AddrModes:
 *   IMM = Immediate value
 *   REG = Register number
 *   MEM = Memory address
 *   FRA = Memory offset from FP
 *   (Note1: for 2 Arg Ops: Arg1 is always REG so the AddrMode is for Arg2)
 *   (Note2: for 1 Arg Ops: JMP/CALL defaults to IMM, others to REG)
 */
public enum Opcode {

    INVALID(0, "INVALID", 0),
    NOOP(1, "NOOP", 0),
    RET(5, "RET", 0),
    IRET(6, "IRET", 0),
    LEAVE(8, "LEAVE", 0),
    ILEAVE(9, "ILEAVE", 0),

    ENTER(11, "ENTER", 1),
    JMP(14, "JMP", 1),
    JMPZE(15, "JMPZE", 1),
    JMPGT(16, "JMPGT", 1),
    JMPLT(17, "JMPLT", 1),
    CALL(18, "CALL", 1),
    JMPREG(21, "JMPREG", 1),
    PUSH(31, "PUSH", 1),
    POP(32, "POP", 1),
    NEGATE(35, "NEGATE", 1),
    INVERT(36, "INVERT", 1),

    SHFTREG(101, "SHFTREG", 2),
    ANDREG(104, "ANDREG", 2),
    XORREG(107, "XORREG", 2),
    ORREG(110, "ORREG", 2),
    ADDIMM(114, "ADDIMM", 2),
    ADDREG(115, "ADDREG", 2),
    ADCIMM(116, "ADCIMM", 2),
    ADCREG(117, "ADCREG", 2),
    SUBREG(125, "SUBREG", 2),
    LOADIMM(204, "LOADIMM", 2),
    LOADREG(205, "LOADREG", 2),
    LOADMEM(206, "LOADFRA", 2),
    LOADFRA(207, "LOADFRA", 2),
    STORMEM(208, "STORMEM", 2),
    STORFRA(209, "STORFRA", 2),
    INPUT(211, "INPUT", 2),
    OUTPUT(215, "OUTPUT", 2);

    private final int value;
    private final String mnemonic;
    private final int numArgs;

    Opcode(int value, String mnemonic, int numArgs) {
        this.value = value;
        this.mnemonic = mnemonic;
        this.numArgs = numArgs;
    }

    public static Opcode opcode(int val) {
        for (Opcode opcode : Opcode.values()) {
            if(opcode.value == val) {
                return opcode;
            }
        }
        return INVALID;
    }

    public static Opcode opcode(String mnemonic) {
        for (Opcode opcode : Opcode.values()) {
            if(opcode.mnemonic.equalsIgnoreCase(mnemonic)) {
                return opcode;
            }
        }
        return INVALID;
    }

    public int getValue() {
        return value;
    }

    public String getMnemonic() {
        return mnemonic;
    }

    public int getNumArgs() {
        return numArgs;
    }

    @Override
    public String toString() {
        return mnemonic + "(" + value + ")";
    }

}
