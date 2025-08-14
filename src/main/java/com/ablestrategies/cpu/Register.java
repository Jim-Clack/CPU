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

    public void and(Register register) {
        for (int i = 0; i < Octet.NumBits; i++) {
            super.setBit(i, LogicGate.AND.gate(getBit(i), register.getBit(i)));
        }
        syncFlagRegister();
    }

    public void or(Register register) {
        for (int i = 0; i < Octet.NumBits; i++) {
            super.setBit(i, LogicGate.OR.gate(getBit(i), register.getBit(i)));
        }
        syncFlagRegister();
    }

    public void xor(Register register) {
        for (int i = 0; i < Octet.NumBits; i++) {
            super.setBit(i, LogicGate.XOR.gate(getBit(i), register.getBit(i)));
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

}