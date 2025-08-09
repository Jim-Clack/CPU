package com.ablestrategies.cpu;

// TODO - extend Octet instead of declaring content - get rid of IDataCell

public class Adder {

    protected Octet content = new Octet();
    protected Bit carry = new Bit(0);

    public void set(Octet octet) {
        content = octet;
        carry = new Bit(0);
    }

    public Octet getValue() {
        return content;
    }

    public Octet add(Octet octet) {
        HalfAdder halfAdder = new HalfAdder();
        carry = new Bit(0);
        for(int bitNum = 0; bitNum < Octet.NumBits; bitNum++) {
            halfAdder.add(content.getBit(bitNum), octet.getBit(bitNum));
            Bit step1Carry = halfAdder.getCarry();
            halfAdder.add(halfAdder.getSum(), carry);
            carry = Gate.OR.output(halfAdder.getCarry(), step1Carry);
            content.setBit(bitNum, halfAdder.getSum());
        }
        carry = halfAdder.getCarry();
        return content;
    }

    public Bit isCarry() {
        return carry;
    }

    public Bit isNegative() {
        return content.getBit(Octet.MaxBitNum);
    }

    public Bit isZero() {
        return content.isZero();
    }

    ////////////////////////////////// I/O ///////////////////////////////////

    public void set(String octet) {
        set(new Octet(octet));
    }

    public void set(int octet) {
        set(new Octet(octet));
    }

    public Octet add(String octet) {
        return add(new Octet(octet));
    }

    public Octet add(int octet) {
        return add(new Octet(octet));
    }

}
