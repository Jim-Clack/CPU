package com.ablestrategies.cpu;

public class ExecutorNoArg extends Substrate {

    protected RunMode execute(Opcode opcode) {
        if(tracingDelayMs >= 0) {
            System.out.printf("%02x: %s\n",
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
            case TRAP:
                return RunMode.TRAP;
            case NOOP:
                break;
            case INVALID:
            default:
                return RunMode.FATAL;
        }

        return runMode;
    }

}
