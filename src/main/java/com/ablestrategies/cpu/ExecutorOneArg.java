package com.ablestrategies.cpu;

public class ExecutorOneArg extends ExecutorNoArg {

    protected boolean execute(Opcode opcode, int argument1) {
        if(stepping) {
            System.out.printf("%03d: %s %d\n",
                    registers[IP].getSignedValue() - 2, opcode.getMnemonic(), argument1);
        }
        switch(opcode) {
            case ENTER:
                for(int i = 0; i <= FP; i++) {
                    push(registers[i]);
                }
                registers[SP].set(registers[SP].getUnsignedValue() - argument1);
                registers[FP].add(registers[SP].getUnsignedValue());
                break;
            case PUSH:
                push(registers[argument1]);
                break;
            case POP:
                registers[argument1].set(pop());
                break;
            case JMP:
                registers[IP].set(argument1);
                break;
            case CALL:
                push(registers[IP]);
                registers[IP].set(argument1);
                break;
            case JMPZE:
                // TODO
                break;
            case JMPGT:
                // TODO
                break;
            case JMPLT:
                // TODO
                break;
            case JMPREG:
                // TODO
                break;
            case NEGATE:
                // TODO
                break;
            case INVERT:
                // TODO
                break;
            case INCR:
                // TODO
                break;
            case DECR:
                // TODO
                break;
            default:
                return true;
        }
        return false;
    }

}
