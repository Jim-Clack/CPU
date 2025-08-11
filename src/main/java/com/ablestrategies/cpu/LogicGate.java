package com.ablestrategies.cpu;

public enum LogicGate {
    AND("0001"),
    OR("0111"),
    XOR("0110"),
    NAND("1110");

    private final Bit[] truthTable = new Bit[4];

    LogicGate(String truthTable) {
        for(int i = 0; i < truthTable.length(); i++) {
            this.truthTable[i] = new Bit(truthTable.charAt(i) % 2);
        }
    }

    public Bit gate(Bit a, Bit b) {
        return truthTable[(a.getVal() * 2) + b.getVal()];
    }

    @Override
    public String toString() {
        return this.name() + "[" + truthTable[0] +  ", " + truthTable[1] + ", " + truthTable[2] + ", " + truthTable[3] + "]";
    }

}
