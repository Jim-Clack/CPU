package com.ablestrategies.cpu;

public class BByte {

    public static int NumBits = 8;
    public static int MaxBitNum = 7;
    public static int MaxBitWgt = 128;
    public static int NextBitWgt = 256;

    private final Bit[] content = new Bit[NumBits];

    public BByte() {
        for (int i = 0; i < NumBits; i++) {
            setBit(i, new Bit(0));
        }
    }

    public BByte(BByte bbyte) {
        set(bbyte);
    }

    public BByte(int intVal) {
        set(intVal);
    }

    public BByte(String bits) {
        set(bits);
    }

    public void set(BByte from) {
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
        int bitNum = bits.length() - 1;
        for (int i = 0; i < NumBits; i++) {
            int bit = 0;
            if(bitNum >= 0) {
                bit = bits.charAt(bitNum) % 2;
            }
            setBit(i, new Bit(bit));
            bitNum--;
        }
    }

    public void setBit(int bitNum, Bit bit) {
        content[bitNum] = bit;
    }

    public Bit getBit(int bitNum) {
        return content[bitNum];
    }

    public Bit isZero() {
        for (int i = 0; i < BByte.NumBits; i++) {
            if(content[i].getVal() != 0) {
                return new Bit(0);
            }
        }
        return new Bit(1);
    }

    public BByte onesCompliment() {
        for (int i = 0; i < NumBits; i++) {
            content[i] = new Bit(content[i].inverse());
        }
        return this;
    }

    public Bit shiftLeft(int howFar) {
        Bit carry = new Bit(0);
        for (int i = 0; i < howFar; i++) {
            for (int j = 0; j < NumBits; j++) {
                Bit temp =  new Bit(content[j]);
                content[j] = carry;
                carry = temp;
            }
            carry = new Bit(0);
        }
        return carry;
    }

    public Bit shiftRight(int howFar) {
        Bit carry = new Bit(0);
        for (int i = 0; i < howFar; i++) {
            for (int j = MaxBitNum; j >= 0; j--) {
                Bit temp =  new Bit(content[j]);
                content[j] = carry;
                carry = temp;
            }
            carry = new Bit(0);
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
        for (int i = 0; i < NumBits; i++) {
            accumulator += content[i].getVal() * multiplier;
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
            sb.insert(0, bit);
        }
        sb.append("](");
        sb.append(getSignedValue());
        sb.append(")");
        return sb.toString();
    }

}
