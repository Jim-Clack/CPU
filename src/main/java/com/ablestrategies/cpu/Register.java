package com.ablestrategies.cpu;

public class Register extends Adder {

    private final FlagRegister flagRegister;

    public Register(FlagRegister flagRegister) {
        this.flagRegister = flagRegister;
    }

    public void zero() {
        super.set(0);
        syncFlagRegister();
    }

    public void invert() {
        onesCompliment();
        super.add(new BByte(1));
        syncFlagRegister();
    }

    public void and(BByte bbyte) {
        for (int i = 0; i < BByte.NumBits; i++) {
            super.setBit(i, LogicGate.AND.gate(getBit(i), bbyte.getBit(i)));
        }
        syncFlagRegister();
    }

    public void or(BByte bbyte) {
        for (int i = 0; i < BByte.NumBits; i++) {
            super.setBit(i, LogicGate.OR.gate(getBit(i), bbyte.getBit(i)));
        }
        syncFlagRegister();
    }

    public void xor(BByte bbyte) {
        for (int i = 0; i < BByte.NumBits; i++) {
            super.setBit(i, LogicGate.XOR.gate(getBit(i), bbyte.getBit(i)));
        }
        syncFlagRegister();
    }

    public BByte add(BByte bbyte) {
        BByte result = super.add(new BByte(bbyte));
        syncFlagRegister();
        return result;
    }

    public BByte add(String bbyte) {
        BByte result = super.add(new BByte(bbyte));
        syncFlagRegister();
        return result;
    }

    public BByte add(int bbyte) {
        BByte result = super.add(new BByte(bbyte));
        syncFlagRegister();
        return result;
    }

    public Bit shiftLeft(int howFar) {
        carry = super.shiftLeft(howFar);
        syncFlagRegister();
        return carry;
    }

    public Bit shiftRight(int howFar) {
        carry = super.shiftRight(howFar);
        syncFlagRegister();
        return carry;
    }

    public void syncFlagRegister() {
        if(flagRegister != null) {
            flagRegister.setFlags(this);
        }
    }

    @Override
    public String toString() {
        return super.toString();
    }

}