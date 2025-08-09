package com.ablestrategies.cpu;

public class Register extends Adder implements IDataCell {

    public void shiftLeft(int howFar) {
        carry = content.shiftLeft(howFar);
    }

    public void shiftRight(int howFar) {
        carry = content.shiftRight(howFar);
    }

    public void zero() {
        set(new Octet());
    }

    public void invert() {
        content.onesCompliment();
        this.add(new Octet(1));
    }

    public void and(Register register) {
        for(int i = 0; i < Octet.NumBits; i++) {
            content.setBit(i, Gate.AND.output(content.getBit(i), register.content.getBit(i)));
        }
    }

    public void or(Register register) {
        for(int i = 0; i < Octet.NumBits; i++) {
            content.setBit(i, Gate.OR.output(content.getBit(i), register.content.getBit(i)));
        }
    }
    public void xor(Register register) {
        for(int i = 0; i < Octet.NumBits; i++) {
            content.setBit(i, Gate.XOR.output(content.getBit(i), register.content.getBit(i)));
        }
    }

    ////////////////////////////////// IData /////////////////////////////////

    @Override
    public int getIntValue() {
        return content.getIntValue();
    }

    @Override
    public void setIntValue(int val) {

    }

    @Override
    public IDataCell get() {
        return content;
    }

    @Override
    public void set(IDataCell value) {
        content.set(value);
    }

    @Override
    public void unsignedSetInt(int val) {

    }

    @Override
    public int unsignedGetInt() {
        return 0; // TODO
    }
}
