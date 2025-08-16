package com.ablestrategies.cpu;

public abstract class Substrate {

    public enum RunMode {
        IDLE,
        RUNNING,
        TRAP,
        FATAL;
    };

    // reserved registers
    public static int FLAGS = 10; // 1=zero, 2=carry, 4=sign, 8=enableIrq
    public static int FP = 11; // frame pointer (within stack)
    public static int SP = 12; // stack pointer (grows downward)
    public static int IP = 13; // instruction pointer (program counter)
    public static int IV = 14; // interrupt vector (page 0 code pointer)
    public static int IN = 15; // interrupt number (set by interrupter)

    protected final MemoryCell[] memoryCells = new MemoryCell[256];
    protected final Register[] registers = new Register[16];
    protected final IOPort[] ioPorts = new IOPort[256];
    protected boolean enableInterrupts = true;
    protected boolean tracing = false;
    protected RunMode runMode = RunMode.IDLE;

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

    public void setTracing(boolean tracing) {
        this.tracing = tracing;
    }

    protected int getNextProgramByte() {
        int arg = memoryCells[registers[IP].getUnsignedValue()].getUnsignedValue();
        registers[IP].increment();
        return arg;
    }

    protected void push(Octet arg) {
        memoryCells[registers[SP].getUnsignedValue()].set(arg.getUnsignedValue());
        registers[SP].decrement();
    }

    protected void push(int arg) {
        memoryCells[registers[SP].getUnsignedValue()].set(arg);
        registers[SP].decrement();
    }

    protected Octet pop() {
        registers[SP].increment();
        return getMemoryCell(registers[SP].getUnsignedValue());
    }

    public Register getRegister(int registerNum) {
        return registers[registerNum];
    }

    public int getRegisterValue(int registerNum) {
        return getRegister(registerNum).getUnsignedValue();
    }

    public MemoryCell getMemoryCell(int cellNum) {
        return memoryCells[cellNum];
    }

    public int getMemoryCellValue(int cellNum) {
        return memoryCells[cellNum].getUnsignedValue();
    }

    public MemoryCell getMemoryCellRegIndirect(int regNum) {
        return getMemoryCell(getRegister(regNum).getUnsignedValue());
    }

    public int getMemoryCellValueRegIndirect(int regNum) {
        return getMemoryCellValue(getRegister(regNum).getUnsignedValue());
    }

    public MemoryCell getMemoryCellFrameLocal(int offset) {
        if(offset > 0) {
            offset += 13 * Octet.NumBits/8;
        }
        return getMemoryCell(getRegister(FP).getUnsignedValue() + offset);
    }

    public int getMemoryCellValueFrameLocal(int offset) {
        if(offset > 0) {
            offset += 13 * Octet.NumBits/8;
        }
        return getMemoryCellValue(getRegister(FP).getUnsignedValue() + offset);
    }

    public IOPort getIoPort(int portNum) {
        return ioPorts[portNum];
    }

}
