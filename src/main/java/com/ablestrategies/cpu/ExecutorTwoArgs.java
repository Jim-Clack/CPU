package com.ablestrategies.cpu;

public class ExecutorTwoArgs extends ExecutorOneArg {

    protected boolean execute(Opcode opcode, int argument1, int argument2) {
        if(stepping) {
            System.out.printf("{%03d}: {%s} {%d} {%d}\n",
                    registers[IP].getIntValue() - 1, opcode.toString(), argument1, argument2);
        }
        Register register;
        switch(opcode) {
            case LOADIMM:
                register = getRegister(argument1);
                register.set(argument2);
                break;
            case LOADMEM:
                register = getRegister(argument1);
                register.set(getMemoryCell(argument2));
                break;
            case STORMEM:
                register = getRegister(argument1);
                getMemoryCell(argument2).set(register);
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
                // TODO
                break;
            case OUTPUT:
                // TODO
                break;
            default:
                return true;
        }

        return false;
    }

}
