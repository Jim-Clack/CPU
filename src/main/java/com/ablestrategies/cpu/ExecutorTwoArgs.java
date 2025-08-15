package com.ablestrategies.cpu;

public class ExecutorTwoArgs extends ExecutorOneArg {

    protected boolean execute(Opcode opcode, int argument1, int argument2) {
        if(tracing) {
            System.out.printf("%03d: %s %d, %d\n",
                    registers[IP].getSignedValue() - 3,
                        opcode.getMnemonic(), argument1, argument2);
        }
        switch(opcode) {
            case LOADIMM:
                getRegister(argument1).set(argument2);
                break;
            case LOADMEM:
                getRegister(argument1).set(getMemoryCell(argument2).getUnsignedValue());
                break;
            case LOADREG:
                getRegister(argument1).set(getRegister(argument2).getUnsignedValue());
                break;
            case LOADFRA:
                getRegister(argument1).set(getRegister(FP).getUnsignedValue() + argument2);
                break;
            case LOADIND:
                getRegister(argument1).set(getMemoryCell(getRegister(argument2).getUnsignedValue()));
                break;
            case STORMEM:
                getMemoryCell(argument2).set(getRegister(argument1).getUnsignedValue());
                break;
            case STORFRA:
                getMemoryCell(argument2).set(getMemoryCell(getRegister(FP).getUnsignedValue() + argument1));
                break;
            case STORIND:
                getMemoryCell(argument2).set(getMemoryCell(getRegister(argument1).getUnsignedValue()));
                break;
            case ADDIMM:
                getRegister(argument1).add(argument2);
                break;
            case ADDREG:
                getRegister(argument1).add(getRegister(argument2).getUnsignedValue());
                break;
            case ADDFRA:
                getRegister(argument1).add(getRegister(FP).getUnsignedValue() + argument2);
                break;
            case ADDIND:
                getRegister(argument1).add(getMemoryCell(getRegister(argument2).getUnsignedValue()));
                break;
            case ADCIMM:
                getRegister(argument1).adc(argument2);
                break;
            case ADCREG:
                getRegister(argument1).adc(getRegister(argument2).getUnsignedValue());
                break;
            case SHFLREG:
                getRegister(argument1).shiftLeft(getRegister(argument2).getUnsignedValue());
                break;
            case SHFRREG:
                getRegister(argument1).shiftRight(getRegister(argument2).getUnsignedValue());
                break;
            case ANDREG:
                getRegister(argument1).and(getRegister(argument2));
                break;
            case XORREG:
                getRegister(argument1).xor(getRegister(argument2));
                break;
            case ORREG:
                getRegister(argument1).or(getRegister(argument2));
                break;
            case SUBIMM:
                getRegister(argument1).add(-argument2);
                break;
            case SUBREG:
                getRegister(argument1).add(-getRegister(argument2).getUnsignedValue());
                break;
            case INPUT:
                registers[argument1].set(getIoPort(argument2).getUnsignedValue());
                break;
            case OUTPUT:
                ioPorts[argument1].set(getRegister(argument2).getUnsignedValue());
                break;
            default:
                return true;
        }

        return false;
    }

}
