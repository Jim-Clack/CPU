package com.ablestrategies.cpu;

public class HalfAdder {

    private Bit sum = new Bit(0);
    private Bit carry = new Bit(0);

    public Bit add(Bit p, Bit q) {
        sum = LogicGate.XOR.gate(p, q);
        carry = LogicGate.AND.gate(p, q);
        return sum;
    }

    public Bit getSum() {
        return sum;
    }

    public Bit getCarry() {
        return carry;
    }
}
