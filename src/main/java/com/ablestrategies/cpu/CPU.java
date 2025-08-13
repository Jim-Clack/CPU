package com.ablestrategies.cpu;

import java.util.concurrent.LinkedBlockingQueue;

public class CPU extends ExecutorTwoArgs implements IInterruptable {

    private LinkedBlockingQueue<Integer> interruptNumbers = new LinkedBlockingQueue<>();

    public CPU() {
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
        System.out.printf("{%03d}: FATAL\n", registers[IP].getSignedValue() - 1);
    }

    public void sendInterrupt(int interruptNumber) {
        try {
            this.interruptNumbers.put(interruptNumber);
        } catch (InterruptedException e) {
            System.out.printf("FAILURE to put interrupt " + interruptNumber + "\n" + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void handleInterrupts() {
        if(interruptNumbers.isEmpty() || !enableInterrupts) {
            return;
        }
        int interruptNumber = 0;
        try {
            interruptNumber = interruptNumbers.take();
        } catch (InterruptedException e) {
            System.out.printf("FAILURE to take interrupt " + interruptNumber + "\n" + e.getMessage());
            throw new RuntimeException(e);
        }
        if(stepping) {
            System.out.printf("{%03d}: INTERRUPT {%d}\n", registers[IP].getSignedValue() - 1, interruptNumber);
        }
        push(registers[IP]);
        registers[IN].set(interruptNumber);
        registers[IP].set(registers[IV].getSignedValue());
        enableInterrupts = false;
    }

}
