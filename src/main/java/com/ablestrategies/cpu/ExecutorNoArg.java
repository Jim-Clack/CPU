package com.ablestrategies.cpu;

public class ExecutorNoArg extends Substrate {

    protected boolean execute(Opcode opcode) {
        if(stepping) {
            System.out.printf("{%03d}: {%s}\n",
                    registers[IP].getIntValue() - 1, opcode.toString());
        }
        switch(opcode) {
            case Opcode.RET:
                registers[IP].set(pop());
                break;
            case Opcode.LEAVE:
                for(int i = MaxUserReg; i >= 0; i--) {
                    registers[i].set(pop());
                }
                registers[FP].set(pop());
                registers[IP].set(pop());
            case Opcode.INVALID:
            case Opcode.NOOP:
                break;
            default:
                return true;
        }

        return false;
    }

}
