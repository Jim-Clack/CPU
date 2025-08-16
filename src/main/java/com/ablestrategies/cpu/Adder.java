package com.ablestrategies.cpu;

public class Adder extends Octet {

    protected Bit carry = new Bit(0);

    public Octet add(Octet octet, int prevCarry) {
        HalfAdder halfAdder = new HalfAdder();
        carry = new Bit(prevCarry);
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

    public Octet add(Octet octet) {
        return add(octet, 0);
    }

    public Octet adc(Octet octet) {
        return add(octet, carry.getVal());
    }

    public Octet add(String octet) {
        return add(new Octet(octet));
    }

    public Octet add(int octet) {
        return add(new Octet(octet));
    }

    public Octet adc(String octet) {
        return adc(new Octet(octet));
    }

    public Octet adc(int octet) {
        return adc(new Octet(octet));
    }

    public Bit increment() {
        this.add(new Octet(1));
        return this.carry;
    }

    public Bit decrement() {
        this.add(new Octet(-1));
        return this.carry;
    }

    public Bit negate() {
        this.onesCompliment();
        Bit oldCarry = this.carry;
        this.add(new Octet(1));
        this.carry = new Bit(this.carry.getVal() | oldCarry.getVal());
        return this.carry;
    }

    public Bit isCarry() {
        return carry;
    }

    public Bit isNegative() {
        return getBit(Octet.MaxBitNum);
    }

    public Bit shiftLeft(int howFar) { carry = super.shiftLeft(howFar); return carry; }

    public Bit shiftRight(int howFar) { carry = super.shiftRight(howFar); return carry; }

    @Override
    public String toString() {
        return super.toString();
    }

}
