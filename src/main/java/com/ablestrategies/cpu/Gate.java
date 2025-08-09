package com.ablestrategies.cpu;

public enum Gate {
    AND("0001"),
    XOR("0110"),
    OR("0111"),
    NAND("1110");

    private final Bit[] tt = new Bit[4];

    Gate(String tt) {
        for(int i = 0; i < tt.length(); i++) {
            this.tt[i] = new Bit(tt.charAt(i) % 2);
        }
    }

    public Bit output(Bit a, Bit b) {
        return tt[(a.getVal() * 2) + b.getVal()];
    }

    @Override
    public String toString() {
        return this.name() + "[" + tt[0] +  ", " + tt[1] + ", " + tt[2] + ", " + tt[3] + "]";
    }

}
