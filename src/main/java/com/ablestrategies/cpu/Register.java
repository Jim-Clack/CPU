package com.ablestrategies.cpu;

public class Register extends Adder {

    public void zero() {
        clone(new Octet());
    }

    public void invert() {
        onesCompliment();
        this.add(new Octet(1));
    }

    public void and(Register register) {
        for (int i = 0; i < Octet.NumBits; i++) {
            setBit(i, LogicGate.AND.gate(getBit(i), register.getBit(i)));
        }
    }

    public void or(Register register) {
        for (int i = 0; i < Octet.NumBits; i++) {
            setBit(i, LogicGate.OR.gate(getBit(i), register.getBit(i)));
        }
    }

    public void xor(Register register) {
        for (int i = 0; i < Octet.NumBits; i++) {
            setBit(i, LogicGate.XOR.gate(getBit(i), register.getBit(i)));
        }
    }

}