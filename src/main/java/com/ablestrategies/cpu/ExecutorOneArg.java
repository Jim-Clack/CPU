package com.ablestrategies.cpu;

public class ExecutorOneArg extends ExecutorNoArg {

    protected boolean execute(Opcode opcode, int argument1) {
        switch(opcode) {
            case Opcode.PUSH:
                push(registers[argument1]);
                break;
            case Opcode.POP:
                registers[argument1].set(pop());
                break;
            case Opcode.JI:
                registers[IP].set(argument1);
                break;
        }

        return false;
    }

}
