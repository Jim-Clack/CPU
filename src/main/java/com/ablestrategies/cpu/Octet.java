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

    public Octet(Octet octet) {
        set(octet);
    }

    public Octet(int intVal) {
        set(intVal);
    }

    public Octet(String bits) {
        set(bits);
    }

    public void set(Octet from) {
        for (int i = 0; i < NumBits; i++) {
            this.content[i] = new Bit(from.content[i].getVal());
        }
    }

    public void set(int intVal) {
        for (int i = 0; i < NumBits; i++) {
            setBit(i, new Bit((intVal & (1 << i)) > 0));
        }
    }

    public void set(String bits) {
        int bitNum = 0;
        for (int i = NumBits - 1; i >= 0; i--) {
            int bit = 0;
            if(bitNum < bits.length()) {
                bit = bits.charAt(bitNum) % 2;
            }
            setBit(i, new Bit(bit));
            bitNum++;
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

    public int getSignedValue() {
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

    public String getHexValue(int places) {
        int value = getUnsignedValue();
        return String.format("%02X", value);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (Bit bit : content) {
            sb.append(bit);
        }
        sb.append("](");
        sb.append(getSignedValue());
        sb.append(")");
        return sb.toString();
    }

}
