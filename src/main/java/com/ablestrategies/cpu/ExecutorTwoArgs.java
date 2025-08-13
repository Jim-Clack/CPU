package com.ablestrategies.cpu;

public class ExecutorTwoArgs extends ExecutorOneArg {

    protected boolean execute(Opcode opcode, int argument1, int argument2) {
        if(stepping) {
            System.out.printf("{%03d}: {%s} {%d} {%d}\n",
                    registers[IP].getSignedValue() - 1, opcode.toString(), argument1, argument2);
        }
        Register register;
        IOPort ioPort;
        MemoryCell memoryCell;
        switch(opcode) {
            case LOADIMM:
                getRegister(argument1).set(argument2);
                break;
            case LOADMEM:
                getRegister(argument1).set(getMemoryCell(argument2).getUnsignedValue());
                break;
            case STORMEM:
                getMemoryCell(argument2).set(getRegister(argument1).getUnsignedValue());
            case SHFTREG:
                // TODO
                break;
            case ANDREG:
                // TODO
                break;
            case XORREG:
                // TODO
                break;
            case ORREG:
                // TODO
                break;
            case ADDIMM:
                // TODO
                break;
            case ADDREG:
                // TODO
                break;
            case ADCIMM:
                // TODO
                break;
            case ADCREG:
                // TODO
                break;
            case SUBREG:
                // TODO
                break;
            case LOADREG:
                // TODO
                break;
            case LOADFRA:
                // TODO
                break;
            case STORFRA:
                // TODO
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
