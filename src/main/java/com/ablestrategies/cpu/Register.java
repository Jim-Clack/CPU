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
        super.add(new Octet(1));
        syncFlagRegister();
    }

    public void and(Octet octet) {
        for (int i = 0; i < Octet.NumBits; i++) {
            super.setBit(i, LogicGate.AND.gate(getBit(i), octet.getBit(i)));
        }
        syncFlagRegister();
    }

    public void or(Octet octet) {
        for (int i = 0; i < Octet.NumBits; i++) {
            super.setBit(i, LogicGate.OR.gate(getBit(i), octet.getBit(i)));
        }
        syncFlagRegister();
    }

    public void xor(Octet octet) {
        for (int i = 0; i < Octet.NumBits; i++) {
            super.setBit(i, LogicGate.XOR.gate(getBit(i), octet.getBit(i)));
        }
        syncFlagRegister();
    }

    public Octet add(Octet octet) {
        Octet result = super.add(new Octet(octet));
        syncFlagRegister();
        return result;
    }

    public Octet add(String octet) {
        Octet result = super.add(new Octet(octet));
        syncFlagRegister();
        return result;
    }

    public Octet add(int octet) {
        Octet result = super.add(new Octet(octet));
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