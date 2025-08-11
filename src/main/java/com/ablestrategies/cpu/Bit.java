package com.ablestrategies.cpu;

public class Bit {

    private int val = 0;

    public Bit(Bit bit) {
        this.val = bit.val;
    }

    public Bit(boolean val) {
        this.val = val ? 1 : 0;
    }

    public int getVal() {
        return val % 2;
    }

    public Bit inverse(){
        return new Bit((val + 1) % 2);
    }

    public Bit(int val) {
        this.val = val != 0 ? 1 : 0;
    }

    @Override
    public String toString() {
        return "" + val;
    }
}
