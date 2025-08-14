package com.ablestrategies.cpu;

public abstract class Substrate {

    // reserved registers
    public static int FLAGS = 10; // 1=zero, 2=carry, 4=sign, 8=enableIrq
    public static int FP = 11; // frame pointer (within stack)
    public static int SP = 12; // stack pointer (grows downward)
    public static int IP = 13; // instruction pointer (program counter)
    public static int IV = 14; // interrupt vector (page 0 code pointer)
    public static int IN = 15; // interrupt number (set by interrupter)

    protected final MemoryCell[] memoryCells = new MemoryCell[250];
    protected final Register[] registers = new Register[16];
    protected final IOPort[] ioPorts = new IOPort[250];
    protected boolean enableInterrupts = true;
    protected boolean stepping = false;

    public void initialize(IInterruptable interruptableALU) {
        for (int i = 0; i < memoryCells.length; i++) {
            memoryCells[i] = new MemoryCell();
        }
        FlagRegister flagRegister = new FlagRegister(this);
        for (int i = 0; i < registers.length; i++) {
            registers[i] = new Register(flagRegister);
        }
        registers[FLAGS] = flagRegister;
        for (int i = 0; i < ioPorts.length; i++) {
            ioPorts[i] = new IOPort(interruptableALU);
        }
        registers[IP].set(0);
        registers[SP].set(memoryCells.length - 1);
        registers[FP].set(memoryCells.length - 1);
        registers[IV].set(0);
        registers[IN].set(0);
    }

    public void setStepping(boolean stepping) {
        this.stepping = stepping;
    }

    protected int getNextOpcode() {
        Opcode opcode = Opcode.opcode(memoryCells[registers[IP].getUnsignedValue()].getUnsignedValue());
        registers[IP].set(registers[IP].getUnsignedValue() + 1);
        return opcode.getValue();
    }

    protected void push(Octet arg) {
        memoryCells[registers[SP].getUnsignedValue()].set(arg.getUnsignedValue());
        registers[SP].set(registers[SP].getUnsignedValue() - 1);
    }

    protected void push(int arg) {
        memoryCells[registers[SP].getSignedValue()].set(arg);
        registers[SP].set(registers[SP].getSignedValue() - 1);
    }

    protected Octet pop() {
        registers[SP].set(registers[SP].getSignedValue() + 1);
        return registers[SP];
    }

    public Register getRegister(int registerNum) {
        return registers[registerNum];
    }

    public MemoryCell getMemoryCell(int cellNum) {
        return memoryCells[cellNum];
    }

    public IOPort getIoPort(int portNum) {
        return ioPorts[portNum];
    }

}
