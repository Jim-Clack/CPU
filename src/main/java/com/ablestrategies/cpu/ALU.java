package com.ablestrategies.cpu;

public class ALU extends ExecutorTwoArgs implements IInterruptable {

    private Integer interruptNumber = 0;

    public ALU() {
        initialize(this);
    }

    public void run() {
        boolean fatal = false;
        while(!fatal) {
            handleInterrupts();
            Opcode opcode = Opcode.opcode(getNextOpcode());
            if (opcode.getNumArgs() < 1) {
                fatal = execute(opcode);
            } else {
                int argument1 = getNextOpcode();
                if (opcode.getNumArgs() < 2) {
                    fatal = execute(opcode, argument1);
                } else {
                    int argument2 = getNextOpcode();
                    fatal = execute(opcode, argument1, argument2);
                }
            }
        }
        System.out.printf("{%03d}: FATAL\n", registers[IP].getIntValue() - 1);
    }

    public synchronized void sendInterrupt(int interruptNumber) {
        this.interruptNumber = interruptNumber;
    }

    public synchronized void handleInterrupts() {
        if(interruptNumber == 0) {
            return;
        }
        if(stepping) {
            System.out.printf("{%03d}: INTERRUPT {%d}\n", registers[IP].getIntValue() - 1, interruptNumber);
        }
        push(registers[IP]);
        registers[IN].set(interruptNumber);
        registers[IP].set(registers[IV].getIntValue());
        interruptNumber = 0;
    }

}
