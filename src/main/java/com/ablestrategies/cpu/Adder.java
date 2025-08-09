package com.ablestrategies.cpu;

public class Adder extends Octet {

    protected Bit carry = new Bit(0);

    @Override
    public Octet clone(Octet octet) {
        super.clone(octet);
        carry = new Bit(0);
        return this;
    }

    public Octet add(Octet octet) {
        HalfAdder halfAdder = new HalfAdder();
        carry = new Bit(0);
        for(int bitNum = 0; bitNum < Octet.NumBits; bitNum++) {
            halfAdder.add(getBit(bitNum), octet.getBit(bitNum));
            Bit step1Carry = halfAdder.getCarry();
            halfAdder.add(halfAdder.getSum(), carry);
            carry = LogicGate.OR.gate(halfAdder.getCarry(), step1Carry);
            setBit(bitNum, halfAdder.getSum());
        }
        carry = halfAdder.getCarry();
        return this;
    }

    public Octet add(String octet) {
        return add(new Octet(octet));
    }

    public Octet add(int octet) {
        return add(new Octet(octet));
    }

    public Bit isCarry() {
        return carry;
    }

    public Bit isNegative() {
        return getBit(Octet.MaxBitNum);
    }

}
