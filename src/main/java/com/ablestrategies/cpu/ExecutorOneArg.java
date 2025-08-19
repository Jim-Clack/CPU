package com.ablestrategies.cpu;

public class ExecutorOneArg extends ExecutorNoArg {

    protected RunMode execute(Opcode opcode, int argument1) {
        if(tracingDelayMs >= 0) {
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
            case TSWAIT:
                while(getMemoryCellValue(argument1) > 0) {
                    try {
                        Thread.sleep(100);
                    } catch (Exception e) {
                        // ignore
                    }
                }
                getMemoryCell(argument1).set(1);
                break;
            case ZEROREG:
                getRegister(argument1).set(0);
                break;
            case ZEROMEM:
                getMemoryCell(argument1).set(0);
                break;
            case PUSHREG:
                push(registers[argument1]);
                break;
            case POPREG:
                registers[argument1].set(pop());
                break;
            case JMPIMM:
                registers[IP].set(argument1);
                break;
            case JMPREG:
                registers[IP].set(getRegister(argument1).getUnsignedValue());
                break;
            case CALLIMM:
                push(registers[IP]);
                registers[IP].set(argument1);
                break;
            case JZEIMM:
                if(FlagBit.ZERO.getBit(getRegister(FLAGS))) {
                    registers[IP].set(argument1);
                }
                break;
            case JGTIMM:
                if(!FlagBit.SIGN.getBit(getRegister(FLAGS))) {
                    registers[IP].set(argument1);
                }
                break;
            case JLTIMM:
                if(FlagBit.SIGN.getBit(getRegister(FLAGS))) {
                    registers[IP].set(argument1);
                }
                break;
            case JNZEIMM:
                if(!FlagBit.ZERO.getBit(getRegister(FLAGS))) {
                    registers[IP].set(argument1);
                }
                break;
            case JGTEIMM:
                if(FlagBit.ZERO.getBit(getRegister(FLAGS)) || !FlagBit.SIGN.getBit(getRegister(FLAGS))) {
                    registers[IP].set(argument1);
                }
                break;
            case JLTEIMM:
                if(FlagBit.ZERO.getBit(getRegister(FLAGS)) || FlagBit.SIGN.getBit(getRegister(FLAGS))) {
                    registers[IP].set(argument1);
                }
                break;
            case JCYIMM:
                if(FlagBit.CARRY.getBit(getRegister(FLAGS))) {
                    registers[IP].set(argument1);
                }
                break;
            case JNCYIMM:
                if(!FlagBit.CARRY.getBit(getRegister(FLAGS))) {
                    registers[IP].set(argument1);
                }
                break;
            case NEGATE:
                getRegister(argument1).negate();
                break;
            case INVERT:
                getRegister(argument1).onesCompliment();
                break;
            case INCREG:
                getRegister(argument1).increment();
                break;
            case DECREG:
                getRegister(argument1).decrement();
                break;
            default:
                return RunMode.FATAL;
        }
        return runMode;
    }

}
