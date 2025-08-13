package com.ablestrategies.cpu;

public class ExecutorOneArg extends ExecutorNoArg {

    protected boolean execute(Opcode opcode, int argument1) {
        if(stepping) {
            System.out.printf("{%03d}: {%s} {%d}\n",
                    registers[IP].getSignedValue() - 1, opcode.toString(), argument1);
        }
        switch(opcode) {
            case ENTER: // FP <= SP then SP <= SP+arg1 then Push R0, R1 ... R10, FP
                int initialSP = registers[SP].getUnsignedValue(); // NO!
                for(int i = 0; i <= FP; i++) {
                    push(registers[i]);
                }
                registers[SP].set(registers[SP].getUnsignedValue() + argument1);
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
