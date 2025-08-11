package com.ablestrategies.cpu;

public class ExecutorNoArg extends Substrate {

    protected boolean execute(Opcode opcode) {
        if(stepping) {
            System.out.printf("{%03d}: {%s}\n",
                    registers[IP].getIntValue() - 1, opcode.toString());
        }
        switch(opcode) {
            case IRET:
                enableInterrupts = true;
                registers[IP].set(pop());
                break;
            case RET:
                registers[IP].set(pop());
                break;
            case LEAVE:
                for(int i = MaxUserReg; i >= 0; i--) {
                    registers[i].set(pop());
                }
                registers[FP].set(pop());
                registers[IP].set(pop());
                break;
            case ILEAVE:
                enableInterrupts = true;
                for(int i = MaxUserReg; i >= 0; i--) {
                    registers[i].set(pop());
                }
                registers[FP].set(pop());
                registers[IP].set(pop());
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
