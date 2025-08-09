package com.ablestrategies.cpu;

public class ExecutorTwoArgs extends ExecutorOneArg {

    protected boolean execute(Opcode opcode, int argument1, int argument2) {
        Register register;
        switch(opcode) {
            case LDI:
                register = getRegister(argument1);
                register.set(getMemoryCell(argument2));
                break;
            case STI:
                register = getRegister(argument1);
                getMemoryCell(argument2).set(register);
        }

        return false;
    }

}
