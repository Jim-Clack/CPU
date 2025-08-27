package com.ablestrategies.cpu;

import java.util.HashMap;

public enum Opcode {

    NOOP(0, 0),
    INVALID(1, 0),
    TRAP(2, 0),
    RET(5, 0),
    IRET(6, 0),
    LEAVE(8, 0),
    ILEAVE(9, 0),

    ENTER(10, 1),
    JMPREG(11, 1),
    JMPIMM(14, 1),
    JZEIMM(15, 1),
    JGTIMM(16, 1),
    JLTIMM(17, 1),
    JNZEIMM(18, 1),
    JGTEIMM(19, 1),
    JLTEIMM(20, 1),
    JCYIMM(21, 1),
    JNCYIMM(22, 1),
    CALLREG(29, 1),
    CALLIMM(30, 1),
    PUSHREG(31, 1),
    POPREG(32, 1),
    NEGATE(35, 1),
    INVERT(36, 1),
    INCREG(38, 1),
    DECREG(39, 1),
    TSWAIT(50, 1 ),
    ZEROREG(51, 1),
    ZEROMEM(52, 1),

    SHFLREG(100, 2),
    SHFRREG(101, 2),
    ANDREG(102, 2),
    XORREG(103, 2),
    ORREG(104, 2),
    ADDIMM(111, 2),
    ADDREG(112, 2),
    ADDMEM(113, 2),
    ADDFRA(114, 2),
    ADDIND(115, 2),
    ADCIMM(121, 2),
    ADCREG(122, 2),
    SUBIMM(131, 2),
    SUBREG(132, 2),
    CMPIMM(135, 2),
    CMPREG(136, 2),
    LOADIMM(141, 2),
    LOADREG(142, 2),
    LOADMEM(143, 2),
    LOADFRA(144, 2),
    LOADIND(145, 2),
    STORMEM(151, 2),
    STORFRA(154, 2),
    STORIND(155, 2),
    INPREG(201, 2),
    OUTREG(211, 2);

    public static final int LongestMnemonicLgt = 7;
    private final int value;
    private final int numArgs;

    private static final HashMap<Integer, Opcode> mapByValue = new HashMap<>();
    private static final HashMap<String, Opcode> mapByMnemonic = new HashMap<>();

    Opcode(int value, int numArgs) {
        this.value = value;
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
        return this.value;
    }

    public String getMnemonic() {
        return this.name();
    }

    public int getNumArgs() {
        return numArgs;
    }

    @Override
    public String toString() {
        return name();
    }

    private static void ensureMapsAreInitialized() {
        if (!mapByValue.isEmpty()) {
            return;
        }
        for (Opcode opcode : Opcode.values()) {
            mapByValue.put(opcode.value, opcode);
            mapByMnemonic.put(opcode.name(), opcode);
        }
    }

}
