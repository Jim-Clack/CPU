package com.ablestrategies.cpu;

import java.util.concurrent.LinkedBlockingQueue;

public class CPU extends ExecutorTwoArgs implements IInterruptable {

    private final LinkedBlockingQueue<Integer> interruptNumbers = new LinkedBlockingQueue<>();
    private int traceCell = 0;

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
            if(stepping) {
                System.out.printf("ADDR INTS R0 R1 R2 R3 R4 R5 R6 R7 R8 R9 RT FP SP IP IV IN %02x\n",
                        memoryCells[traceCell].getUnsignedValue());
                System.out.printf("%04x %4b ",
                        registers[IP].getUnsignedValue() - 1, enableInterrupts);
                for(Register register : registers) {
                    System.out.printf(" %02x", register.getUnsignedValue());
                }
                System.out.println();
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

    private void setTraceCell(int traceCell) {
        this.traceCell = traceCell;
    }

    private void handleInterrupts() {
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
