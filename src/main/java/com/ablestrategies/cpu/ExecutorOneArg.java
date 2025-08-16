package com.ablestrategies.cpu;

public class ExecutorOneArg extends ExecutorNoArg {

    protected RunMode execute(Opcode opcode, int argument1) {
        if(tracing) {
            System.out.printf("%02x: %s %02x\n",
                    registers[IP].getSignedValue() - 2, opcode.getMnemonic(), argument1);
        }
        switch(opcode) {
            case ENTER:
                for(int i = 0; i <= FP; i++) {
                    push(registers[i]);
                }
                registers[FP].set(registers[SP].getUnsignedValue());
                registers[SP].set(registers[SP].getUnsignedValue() - argument1);
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
                if(getRegister(FLAGS).isZero().getVal() > 0) {
                    registers[IP].set(argument1);
                }
                break;
            case JMPGT:
                if(getRegister(FLAGS).isNegative().getVal() == 0) {
                    registers[IP].set(argument1);
                }
                break;
            case JMPLT:
                if(getRegister(FLAGS).isNegative().getVal() > 0) {
                    registers[IP].set(argument1);
                }
                break;
            case JMPREG:
                registers[IP].set(getRegister(argument1).getUnsignedValue());
                break;
            case NEGATE:
                getRegister(argument1).negate();
                break;
            case INVERT:
                getRegister(argument1).onesCompliment();
                break;
            case INCR:
                getRegister(argument1).increment();
                break;
            case DECR:
                getRegister(argument1).decrement();
                break;
            default:
                return RunMode.FATAL;
        }
        return runMode;
    }

}
