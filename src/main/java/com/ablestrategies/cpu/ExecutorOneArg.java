package com.ablestrategies.cpu;

public class ExecutorOneArg extends ExecutorNoArg {

    protected boolean execute(Opcode opcode, int argument1) {
        if(stepping) {
            System.out.printf("{%03d}: {%s} {%d}\n",
                    registers[IP].getIntValue() - 1, opcode.toString(), argument1);
        }
        switch(opcode) {
            case Opcode.ENTER:
                for(int i = 0; i <= MaxUserReg; i++) {
                    push(registers[i]);
                } // arg = frame size...
                registers[FP].set(registers[FP].getIntValue() + argument1);
            case Opcode.PUSH:
                push(registers[argument1]);
                break;
            case Opcode.POP:
                registers[argument1].set(pop());
                break;
            case Opcode.JMP:
                registers[IP].set(argument1);
                break;
            case Opcode.CALL:
                push(registers[IP]);
                registers[IP].set(argument1);
                break;
            case Opcode.JMPZE:
                // TODO
                break;
            case Opcode.JMPGT:
                // TODO
                break;
            case Opcode.JMPLT:
                // TODO
                break;
            case Opcode.JMPREG:
                // TODO
                break;
            case Opcode.NEGATE:
                // TODO
                break;
            case Opcode.INVERT:
                // TODO
                break;
            default:
                return true;
        }
        return false;
    }

}
