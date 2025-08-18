package com.ablestrategies.cpu;

public enum Flags {
    ZERO(0),       // not settable
    CARRY(1),      // only settable flag
    SIGN(2),       // not settable
    IRQENAB(3);    // not settable

    private final int bitNum;

    Flags(int bitNum) {
        this.bitNum = bitNum;
    }

    public int getBitNum() {
        return bitNum;
    }

    public int getBitWgt() {
        return 1 << bitNum;
    }

    public boolean getBit(Octet octet) {
        return(octet.getUnsignedValue() & getBitWgt()) != 0;
    }
}
