package com.ablestrategies.cpu;

import java.util.HashMap;

public enum Opcode {

    NOOP(0, "NOOP", 0),
    INVALID(1, "INVALID", 0),
    TRAP(2, "TRAP", 0),
    RET(5, "RET", 0),
    IRET(6, "IRET", 0),
    LEAVE(8, "LEAVE", 0),
    ILEAVE(9, "ILEAVE", 0),

    ENTER(10, "ENTER", 1),
    JMPREG(11, "JMPREG", 1),
    JMPIMM(14, "JMPIMM", 1),
    JZEIMM(15, "JZEIMM", 1),
    JGTIMM(16, "JGTIMM", 1),
    JLTIMM(17, "JLTIMM", 1),
    JNZEIMM(18, "JNZEIMM", 1),
    JGTEIMM(19, "JGTEIMM", 1),
    JLTEIMM(20, "JLTEIMM", 1),
    CALLIMM(30, "CALLIMM", 1),
    PUSHREG(31, "PUSHREG", 1),
    POPREG(32, "POPREG", 1),
    NEGATE(35, "NEGATE", 1),
    INVERT(36, "INVERT", 1),
    INCREG(38, "INCREG", 1),
    DECREG(39, "DECREG", 1),
    TSWAIT(50, "TSWAIT", 1 ),
    ZEROMEM(51, "ZEROMEM", 1),

    SHFLREG(100, "SHFLREG", 2),
    SHFRREG(101, "SHFRREG", 2),
    ANDREG(102, "ANDREG", 2),
    XORREG(103, "XORREG", 2),
    ORREG(104, "ORREG", 2),
    ADDIMM(111, "ADDIMM", 2),
    ADDREG(112, "ADDREG", 2),
    ADDMEM(113, "ADDMEM", 2),
    ADDFRA(114, "ADDFRA", 2),
    ADDIND(115, "ADDIND", 2),
    ADCIMM(121, "ADCIMM", 2),
    ADCREG(122, "ADCREG", 2),
    SUBIMM(131, "SUBIMM", 2),
    SUBREG(132, "SUBREG", 2),
    CMPIMM(135, "CMPIMM", 2),
    CMPREG(136, "CMPREG", 2),
    LOADIMM(141, "LOADIMM", 2),
    LOADREG(142, "LOADREG", 2),
    LOADMEM(143, "LOADMEM", 2),
    LOADFRA(144, "LOADFRA", 2),
    LOADIND(145, "LOADIND", 2),
    STORMEM(151, "STORMEM", 2),
    STORFRA(154, "STORFRA", 2),
    STORIND(155, "STORIND", 2),
    INPUT(201, "INPUT", 2),
    OUTPUT(211, "OUTPUT", 2);

    public static final int LongestMnemonicLgt = 7;
    private final int value;
    private final String mnemonic;
    private final int numArgs;

    private static final HashMap<Integer, Opcode> mapByValue = new HashMap<>();
    private static final HashMap<String, Opcode> mapByMnemonic = new HashMap<>();

    Opcode(int value, String mnemonic, int numArgs) {
        this.value = value;
        this.mnemonic = mnemonic;
        this.numArgs = numArgs;
    }

    public static Opcode opcode(int val) {
        ensureMapsAreInitialized();
        Opcode opcode = mapByValue.get(val);
        if (opcode == null) {
            opcode = INVALID;
        }
        return opcode;
    }

    public static Opcode opcode(String mnemonic) {
        ensureMapsAreInitialized();
        Opcode opcode = mapByMnemonic.get(mnemonic.trim().toUpperCase());
        if (opcode == null) {
            opcode = INVALID;
        }
        return opcode;
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
        return mnemonic;
    }

    private static void ensureMapsAreInitialized() {
        if (!mapByValue.isEmpty()) {
            return;
        }
        for (Opcode opcode : Opcode.values()) {
            mapByValue.put(opcode.value, opcode);
            mapByMnemonic.put(opcode.mnemonic.trim().toUpperCase(), opcode);
        }
    }

}
