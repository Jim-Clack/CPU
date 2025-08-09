package com.ablestrategies.cpu;

public class ExecutorTwoArgs extends ExecutorOneArg {

    protected boolean execute(Opcode opcode, int argument1, int argument2) {
        switch(opcode) {
            case Opcode.LDI:
                break;
        }

        return false;
    }

}
