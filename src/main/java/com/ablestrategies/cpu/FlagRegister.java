package com.ablestrategies.cpu;

public class FlagRegister extends Register {

    private final String errorMsg = "Illegal Operation on FlagRegister.zero";
    private Substrate substrate = null;

    public FlagRegister(Substrate substrate) {
        super(null);
        this.substrate = substrate;
    }

    public void set(int intVal) {
        super.set(intVal);
        Register lastRegister = getLastRegister();
        if(lastRegister != null) {
            lastRegister.carry = this.getBit(Flags.CARRY.getBitNum());
        }
    }

    public void set(String bits) {
        super.set(bits);
        Register lastRegister = getLastRegister();
        if(lastRegister != null) {
            lastRegister.carry = this.getBit(Flags.CARRY.getBitNum());
        }
    }

    public void setBit(int bitNum, Bit bit) {
        super.setBit(bitNum, bit);
        Register lastRegister = getLastRegister();
        if(lastRegister != null) {
            lastRegister.carry = this.getBit(Flags.CARRY.getBitNum());
        }
    }

    public Bit isZero() {
        syncToSubstrate();
        return super.isZero();
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
        if(register instanceof FlagRegister) {
            return;
        }
        super.zero();
        setBit(Flags.ZERO.getBitNum(), new Bit(register.isZero()));
        setBit(Flags.CARRY.getBitNum(), new Bit(register.carry));
        setBit(Flags.SIGN.getBitNum(), new Bit(register.isNegative()));
        setLastRegister(register);
    }

    private void setLastRegister(Register register) {
        if(substrate == null) {
            return;
        }
        for(int i = 0; i < 16; i++) {
            if(register == substrate.registers[i]) {
                setBit(Flags.LASTREG1.getBitNum(), new Bit((i & 1) > 0));
                setBit(Flags.LASTREG2.getBitNum(), new Bit((i & 2) > 0));
                setBit(Flags.LASTREG4.getBitNum(), new Bit((i & 4) > 0));
                setBit(Flags.LASTREG8.getBitNum(), new Bit((i & 8) > 0));
            }
        }
    }

    private void syncToSubstrate() {
        if(substrate != null) {
            super.setBit(Flags.IRQENAB.getBitNum(), new Bit(substrate.enableInterrupts));
        }
    }

    private Register getLastRegister() {
        if(substrate == null) {
            return null;
        }
        return substrate.registers[(super.getUnsignedValue() & 0x0F) >> 4];
    }

    ///  The following methods do not make sense for a destination FlagRegister

    public Octet onesCompliment() {
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

    public void set(Octet from) {
        throw new RuntimeException(errorMsg);
    }

}