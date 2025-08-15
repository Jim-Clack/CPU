package com.ablestrategies.cpu;

public class ExecutorNoArg extends Substrate {

    protected boolean execute(Opcode opcode) {
        if(tracing) {
            System.out.printf("%03d: %s\n",
                    registers[IP].getUnsignedValue() - 1, opcode.getMnemonic());
        }
        switch(opcode) {
            case ILEAVE:
                enableInterrupts = true;
                // Fall thru...
            case LEAVE:
                registers[SP].set(registers[FP].getUnsignedValue());
                for(int i = FP; i >= 0; i--) { // NO!
                    registers[i].set(pop().getUnsignedValue());
                }
                registers[IP].set(pop());
                break;
            case IRET:
                enableInterrupts = true;
                // Fall thru...
            case RET:
                registers[IP].set(pop().getUnsignedValue());
                break;
            case NOOP:
                break;
            case INVALID:
            default:
                return true;
        }

        return false;
    }

}
