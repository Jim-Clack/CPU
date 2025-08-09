package com.ablestrategies.cpu;

public class ExecutorNoArg extends Substrate {

    protected boolean execute(Opcode opcode) {
        switch(opcode) {
            case Opcode.RET:
                registers[IP].set(pop());
                break;
            case Opcode.NOOP:
                break;
            case Opcode.INVALID:
            default:
                return true;
        }

        return false;
    }

}
