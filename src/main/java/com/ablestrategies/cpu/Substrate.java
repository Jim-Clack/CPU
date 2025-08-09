package com.ablestrategies.cpu;

public class Substrate {

    // reserved registers
    public static int IP = 11;
    public static int SP = 12;
    public static int FP = 13;

    protected final MemoryCell[] memoryCells = new MemoryCell[250];
    protected final Register[] registers = new Register[14];
    protected boolean stepping = false;

    public Substrate() {
        for (int i = 0; i < memoryCells.length; i++) {
            memoryCells[i] = new MemoryCell();
        }
        for (int i = 0; i < registers.length; i++) {
            registers[i] = new Register();
        }
        registers[IP].set(0);
        registers[SP].set(memoryCells.length - 1);
        registers[FP].set(memoryCells.length - 1);
    }

    public void setStepping(boolean stepping) {
        this.stepping = stepping;
    }

    protected int getNextOpcode() {
        Opcode opcode = Opcode.Opcode(memoryCells[registers[IP].getIntValue()].getIntValue());
        registers[IP].set(registers[IP].getIntValue() + 1);
        return opcode.getValue();
    }

    protected void push(Octet arg) {
        memoryCells[registers[SP].getIntValue()].clone(arg);
        registers[SP].set(registers[SP].getIntValue() - 1);
    }

    protected Octet pop() {
        registers[SP].set(registers[SP].getIntValue() + 1);
        return registers[SP];
    }

    protected Register getRegister(int registerNum) {
        return registers[registerNum];
    }

    protected MemoryCell getMemoryCell(int cellNum) {
        return memoryCells[cellNum];
    }
}
