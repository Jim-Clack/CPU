package com.ablestrategies.cpu;

public class Octet {

    public static int NumBits = 8;
    public static int MaxBitNum = 7;
    public static int MaxBitWgt = 128;
    public static int NextBitWgt = 256;

    private final Bit[] content = new Bit[NumBits];

    public Octet() {
        for (int i = 0; i < NumBits; i++) {
            setBit(i, new Bit(0));
        }
    }

    public Octet clone(Octet other) {
        set(other);
        return this;
    }

    public void set(Octet from) {
        for (int i = 0; i < NumBits; i++) {
            this.content[i] = new Bit(from.content[i].getVal());
        }
    }

    public void setUnsignedValue(int intVal) {
        for (int i = 0; i < NumBits; i++) {
            setBit(i, new Bit((intVal & (1 << i)) > 0));
        }
    }

    public void setBit(int bitNum, Bit bit) {
        content[MaxBitNum - bitNum] = bit;
    }

    public Bit getBit(int bitNum) {
        return content[MaxBitNum - bitNum];
    }

    public Bit isZero() {
        for (int i = 0; i < Octet.MaxBitNum; i++) {
            if(content[i].getVal() != 0) {
                return new Bit(0);
            }
        }
        return new Bit(1);
    }

    public Octet onesCompliment() {
        for (int i = 0; i < NumBits; i++) {
            content[i] = new Bit(content[i].inverse());
        }
        return this;
    }

    public Bit shiftLeft(int howFar) {
        Bit carry = new Bit(0);
        for (int i = 0; i < howFar; i++) {
            for (int j = NumBits; j > 0; j--) {
                Bit temp =  content[j - 1];
                content[j - 1] = carry;
                carry = temp;
            }
        }
        return carry;
    }

    public Bit shiftRight(int howFar) {
        Bit carry = new Bit(0);
        for (int i = 0; i < howFar; i++) {
            for (int j = 0; j < NumBits; j++) {
                Bit temp =  content[j];
                content[j] = carry;
                carry = temp;
            }
        }
        return carry;
    }

    public void set(int from) {
        set(new Octet(from));
    }

    public void set(String from) {
        set(new Octet(from));
    }

    public int getIntValue() {
        int accumulator = getUnsignedValue();
        if(accumulator >= MaxBitWgt) { // negatives
            accumulator = accumulator - NextBitWgt;
        }
        return accumulator;
    }

    public int getUnsignedValue() {
        int accumulator = 0;
        int multiplier = 1;
        for (int i = NumBits; i > 0; i--) {
            accumulator += content[i-1].getVal() * multiplier;
            multiplier *= 2;
        }
        return accumulator;
    }

    public Octet(String bits) {
        for (int i = 0; i < NumBits; i++) {
            int bitNum = MaxBitNum - i;
            if(bitNum >= bits.length()) {
                break;
            }
            setBit(bitNum, new Bit(bits.charAt(i) % 2));
        }
    }

    public Octet(int intVal) {
        if(intVal < 0) { // negative
            intVal = NextBitWgt + intVal;
        }
        for (int i = 0; i < NumBits; i++) {
            setBit(i, new Bit((intVal & (1 << i)) > 0));
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (Bit bit : content) {
            sb.append(bit);
        }
        sb.append("](");
        sb.append(getIntValue());
        sb.append(")");
        return sb.toString();
    }

}
