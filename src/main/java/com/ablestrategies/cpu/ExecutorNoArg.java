package com.ablestrategies.cpu;

public class ExecutorNoArg extends Substrate {

    protected boolean execute(Opcode opcode) {
        if(stepping) {
            System.out.printf("{%03d}: {%s}\n",
                    registers[IP].getUnsignedValue() - 1, opcode.toString());
        }
        switch(opcode) {
            case IRET:
                enableInterrupts = true;
                registers[IP].set(pop().getUnsignedValue());
                break;
            case RET:
                registers[IP].set(pop().getUnsignedValue());
                break;
            case LEAVE: // leave: Pop FP, R10, R9 ... R0, then SP <= FP, then Pop IP (RET)
                for(int i = FP; i >= 0; i--) { // NO!
                    registers[i].set(pop().getUnsignedValue());
                }
                registers[SP].set(registers[FP].getUnsignedValue());
                registers[IP].set(pop());
                break;
            case ILEAVE:
                enableInterrupts = true;
                for(int i = MaxUserReg; i >= 0; i--) {
                    registers[i].set(pop().getUnsignedValue());
                }
                registers[FP].set(pop().getUnsignedValue());
                registers[IP].set(pop().getUnsignedValue());
                break;
            case INVALID:
            case NOOP:
                break;
            default:
                return true;
        }

        return false;
    }

}
