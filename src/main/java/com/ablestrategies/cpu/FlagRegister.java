package com.ablestrategies.cpu;

public class FlagRegister extends Register {

    private final String errorMsg = "Illegal Operation on FlagRegister.zero";
    private Substrate substrate = null;

    public FlagRegister(Substrate substrate) {
        super(null);
        this.substrate = substrate;
    }

    public void set(BByte from) {
        for(int i = 0; i < BByte.NumBits; i++) {
            setBit(i, new Bit(from.getBit(i)));
        }
    }

    public void setBit(int bitNum, Bit bit) {
        if(bitNum == Flags.IRQENAB.getBitNum()) {
            // should we allow this?
            // super.setBit(bitNum, bit);
            // substrate.enableInterrupts = bit.getVal() > 0;
        } else {
            super.setBit(bitNum, bit);
        }
    }

    public Bit getBit(int bitNum) {
        syncToSubstrate();
        return super.getBit(bitNum);
    }

    public int getUnsignedValue() {
        syncToSubstrate();
        return super.getUnsignedValue();
    }

    public String getHexValue(int places) {
        syncToSubstrate();
        return super.getHexValue(places);
    }

    protected void setFlags(Register register) {
        if(register == this) {
            return;
        }
      //  super.zero();
        setBit(Flags.ZERO.getBitNum(), new Bit(register.isZero()));
        setBit(Flags.CARRY.getBitNum(), new Bit(register.isCarry()));
        setBit(Flags.SIGN.getBitNum(), new Bit(register.isNegative()));
    }

    private void syncToSubstrate() {
        if(substrate != null) {
            super.setBit(Flags.IRQENAB.getBitNum(), new Bit(substrate.enableInterrupts));
        }
    }

    @Override
    public String toString() {
        // syncToSubstrate();
        return super.toString();
    }

    ///  The following methods do not make sense for a destination FlagRegister

    public BByte onesCompliment() {
        throw new RuntimeException(errorMsg);
    }

    public Bit shiftLeft(int howFar) {
        throw new RuntimeException(errorMsg);
    }

    public Bit shiftRight(int howFar) {
        throw new RuntimeException(errorMsg);
    }

    public int getSignedValue() {
        throw new RuntimeException(errorMsg);
    }

    public void zero() {
        throw new RuntimeException(errorMsg);
    }

    public void invert() {
        throw new RuntimeException(errorMsg);
    }

    public void and(Register register) {
        throw new RuntimeException(errorMsg);
    }

    public void or(Register register) {
        throw new RuntimeException(errorMsg);
    }

    public void xor(Register register) {
        throw new RuntimeException(errorMsg);
    }

}