package com.ablestrategies.cpu;

public class Adder extends BByte {

    protected Bit carry = new Bit(0);

    public BByte add(BByte bbyte, int prevCarry) {
        HalfAdder halfAdder = new HalfAdder();
        carry = new Bit(prevCarry);
        for(int bitNum = 0; bitNum < BByte.NumBits; bitNum++) {
            halfAdder.add(getBit(bitNum), bbyte.getBit(bitNum));
            Bit step1Carry = halfAdder.getCarry();
            halfAdder.add(halfAdder.getSum(), carry);
            carry = LogicGate.OR.gate(halfAdder.getCarry(), step1Carry);
            setBit(bitNum, halfAdder.getSum());
        }
        carry = halfAdder.getCarry();
        return this;
    }

    public BByte add(BByte bbyte) {
        return add(bbyte, 0);
    }

    public BByte adc(BByte bbyte, int carry) {
        return add(bbyte, carry);
    }

    public BByte add(String bbyte) {
        return add(new BByte(bbyte));
    }

    public BByte add(int bbyte) {
        return add(new BByte(bbyte));
    }

    public BByte adc(String bbyte, int carry) {
        return adc(new BByte(bbyte), carry);
    }

    public BByte adc(int bbyte, int carry) {
        return adc(new BByte(bbyte), carry);
    }

    public Bit increment() {
        this.add(new BByte(1));
        return this.carry;
    }

    public Bit decrement() {
        this.add(new BByte(-1));
        return this.carry;
    }

    public Bit negate() {
        this.onesCompliment();
        Bit oldCarry = this.carry;
        this.add(new BByte(1));
        this.carry = new Bit(this.carry.getVal() | oldCarry.getVal());
        return this.carry;
    }

    public Bit isCarry() {
        return carry;
    }

    public Bit isNegative() {
        return getBit(BByte.MaxBitNum);
    }

    public Bit shiftLeft(int howFar) { carry = super.shiftLeft(howFar); return carry; }

    public Bit shiftRight(int howFar) { carry = super.shiftRight(howFar); return carry; }

    @Override
    public String toString() {
        return super.toString();
    }

}
