package com.ablestrategies.cpu;

public enum Opcode {
    INVALID(255, "INVALID", ArgType.NONE),
    NOOP(0, "NOOP", ArgType.NONE),
    RET(10, "RET", ArgType.NONE),
    ENTER(11, "ENTER", ArgType.NONE),
    LEAVE(12, "LEAVE", ArgType.NONE),
    SHLI(20, "SHLI", ArgType.REG_AND_VALUE),
    SHLR(21, "SHLR", ArgType.REG_AND_REG),
    SHRI(22, "SHRI", ArgType.REG_AND_VALUE),
    SHRR(23, "SHRR", ArgType.REG_AND_REG),
    ANDI(24, "ANDI", ArgType.REG_AND_VALUE),
    ANDR(25, "ANDR", ArgType.REG_AND_REG),
    XORI(26, "XORI", ArgType.REG_AND_VALUE),
    XORR(27, "XORR", ArgType.REG_AND_REG),
    ORI(28, "ORI", ArgType.REG_AND_VALUE),
    ORR(29, "ORR", ArgType.REG_AND_REG),
    ADDI(101, "ADDI", ArgType.REG_AND_VALUE),
    ADDR(102, "ADDR", ArgType.REG_AND_REG),
    ADDM(103, "ADDM", ArgType.REG_AND_MEM),
    ADDL(104, "ADDL", ArgType.REG_AND_LOCAL),
    ADCI(105, "ADCI", ArgType.REG_AND_VALUE),
    ADCR(106, "ADCR", ArgType.REG_AND_REG),
    ADCM(107, "ADCM", ArgType.REG_AND_VALUE),
    ADCL(108, "ADCL", ArgType.REG_AND_LOCAL),
    SUBI(109, "SUBI", ArgType.REG_AND_VALUE),
    SUBR(110, "SUBP", ArgType.REG_AND_REG),
    SUBM(111, "SUBM", ArgType.REG_AND_MEM),
    SUBL(112, "SUBL", ArgType.REG_AND_LOCAL),
    JI(113, "JI", ArgType.MEMORY),
    JR(114, "JR", ArgType.REGISTER),
    JZE(115, "JZE", ArgType.MEMORY),
    JGT(116, "JGT", ArgType.MEMORY),
    JLT(117, "JLT", ArgType.MEMORY),
    CALL(120, "CALL", ArgType.MEMORY),
    PUSH(128, "PUSH", ArgType.REGISTER),
    POP(131, "POP", ArgType.REGISTER),
    CPLR(132, "CMPR", ArgType.REGISTER),
    NEGR(133, "NEGR", ArgType.REGISTER),
    LDI(201, "LDI", ArgType.REG_AND_VALUE),
    LDR(202, "LDR", ArgType.REG_AND_REG),
    LDM(203, "LDM", ArgType.REG_AND_MEM),
    STI(206, "STI", ArgType.REG_AND_VALUE),
    STR(206, "STR", ArgType.REG_AND_REG),
    STM(206, "STM", ArgType.REG_AND_MEM);

    private final int value;
    private final String mnemonic;
    private final ArgType argType;

    Opcode(int value, String mnemonic, ArgType argType) {
        this.value = value;
        this.mnemonic = mnemonic;
        this.argType = argType;
    }

    public static Opcode Opcode(int val) {
        for (Opcode opcode : Opcode.values()) {
            if(opcode.value == val) {
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

    public ArgType getArgType() {
        return argType;
    }

}
