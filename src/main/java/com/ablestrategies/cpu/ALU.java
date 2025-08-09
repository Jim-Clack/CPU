package com.ablestrategies.cpu;

public class ALU extends ExecutorTwoArgs {

    public void ipl(byte[] program) {
        int index = 0;
        for(byte byt : program) {
            memoryCells[index++].unsignedSetInt(byt);
        }
    }

    public void run() {
        boolean fatal = false;
        while(!fatal) {
            Opcode opcode = Opcode.Opcode(getNextOpcode());
            if(stepping) {
                System.out.println(" >>> [" + (registers[IP].getIntValue() - 1) + "]=" + opcode.getMnemonic());
            }
            if (opcode.getArgType().typeNum() < ArgType.ONE_ARG.typeNum()) {
                fatal = execute(opcode);
            } else {
                int argument1 = getNextOpcode();
                if (opcode.getArgType().typeNum() < ArgType.TWO_ARGS.typeNum()) {
                    fatal = execute(opcode, argument1);
                } else {
                    int argument2 = getNextOpcode();
                    fatal = execute(opcode, argument1, argument2);
                }
            }
        }
    }

}
