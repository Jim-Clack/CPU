package com.ablestrategies.cpu;

public class WWord {

    public static int NumBits = 16;
    public static int MaxBitNum = 15;
    public static int MaxBitWgt = 32768;
    public static int NextBitWgt = 65536;

    private final Bit[] content = new Bit[NumBits];

    public WWord() {
        for (int i = 0; i < NumBits; i++) {
            setBit(i, new Bit(0));
        }
    }

    public WWord(WWord wWord) {
        set(wWord);
    }

    public WWord(int intVal) {
        set(intVal);
    }

    public WWord(String bits) {
        set(bits);
    }

    public void set(WWord from) {
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
        for (int i = 0; i < WWord.NumBits; i++) {
            if(content[i].getVal() != 0) {
                return new Bit(0);
            }
        }
        return new Bit(1);
    }

    public WWord onesCompliment() {
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
