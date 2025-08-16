package com.ablestrategies.cpu;

public class ExecutorTwoArgs extends ExecutorOneArg {

    protected RunMode execute(Opcode opcode, int argument1, int argument2) {
        if(tracing) {
            System.out.printf("%02x: %s %02x, %02x\n",
                    registers[IP].getSignedValue() - 3,
                        opcode.getMnemonic(), argument1, argument2);
        }
        switch(opcode) {
            case LOADIMM:
                getRegister(argument1).set(argument2);
                break;
            case LOADMEM:
                getRegister(argument1).set(getMemoryCellValue(argument2));
                break;
            case LOADREG:
                getRegister(argument1).set(getRegisterValue(argument2));
                break;
            case LOADFRA:
                String s1 = "Load " + argument1 + " from mem " + getMemoryCellValueFrameLocal(argument2);
                getRegister(argument1).set(getMemoryCellValueFrameLocal(argument2));
                break;
            case LOADIND:
                getRegister(argument1).set(getMemoryCellValueRegIndirect(argument2));
                break;
            case STORMEM:
                getMemoryCell(argument2).set(getRegisterValue(argument1));
                break;
            case STORFRA:
                getMemoryCellFrameLocal(argument2).set(getRegisterValue(argument1));
                break;
            case STORIND:
                getMemoryCellRegIndirect(argument2).set(getRegisterValue(argument1));
                break;
            case ADDIMM:
                getRegister(argument1).add(argument2);
                break;
            case ADDREG:
                getRegister(argument1).add(getRegisterValue(argument2));
                break;
            case ADDFRA:
                String s2 = "From " + argument1 + " add mem " + getMemoryCellValueFrameLocal(argument2);
                getRegister(argument1).add(getMemoryCellValueFrameLocal(argument2));
                break;
            case ADDIND:
                getRegister(argument1).add(getMemoryCellValueRegIndirect(argument2));
                break;
            case ADCIMM:
                getRegister(argument1).adc(argument2);
                break;
            case ADCREG:
                getRegister(argument1).adc(getRegisterValue(argument2));
                break;
            case SHFLREG:
                getRegister(argument1).shiftLeft(getRegisterValue(argument2));
                break;
            case SHFRREG:
                getRegister(argument1).shiftRight(getRegisterValue(argument2));
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
                getRegister(argument1).add(-getRegisterValue(argument2));
                break;
            case INPUT:
                registers[argument1].set(getIoPort(argument2).getUnsignedValue());
                break;
            case OUTPUT:
                ioPorts[argument1].set(getRegister(argument2).getUnsignedValue());
                break;
            default:
                return RunMode.FATAL;
        }
        return runMode;
    }

}
