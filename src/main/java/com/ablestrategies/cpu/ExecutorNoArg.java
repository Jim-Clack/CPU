package com.ablestrategies.cpu;

public class ExecutorNoArg extends Substrate {

    protected boolean execute(Opcode opcode) {
        if(stepping) {
            System.out.printf("%03d: %s\n",
                    registers[IP].getUnsignedValue() - 1, opcode.getMnemonic());
        }
        switch(opcode) {
            case IRET:
                registers[IP].set(pop().getUnsignedValue());
                enableInterrupts = true;
                break;
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
