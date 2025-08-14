package com.ablestrategies.cpu;

public enum Flags {
    ZERO(0),       // not settable
    CARRY(1),      // only settable flag
    SIGN(2),       // not settable
    IRQENAB(3),    // not settable
    LASTREG1(4),  // Note:
    LASTREG2(5),  // These 4 bits store the register number 0-15 of the
    LASTREG4(6),  // last register that performed an arithmetic/logical
    LASTREG8(7); // operation so that a carry can be propagated.

    private final int bitNum;

    Flags(int bitNum) {
        this.bitNum = bitNum;
    }

    public int getBitNum() {
        return bitNum;
    }
}
