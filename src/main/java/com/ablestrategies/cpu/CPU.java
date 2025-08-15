package com.ablestrategies.cpu;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * Memory Layout using C/C++ calling convention
 *   Code begins at bottom of memory
 *   Stack begins at top of memory
 *   Dynamic heap can be put in-between
 * Registers:
 *   Register 0-9 - User Registers
 *   Register 10 - Flags: Z, C, S, I + 4 reserved
 *   Register 11 - FP: Frame Pointer
 *   Register 12 - SP: Stack Pointer
 *   Register 13 - IP: Instruction Pointer
 *   Register 14 - IV: Interrupt Vector
 *   Register 15 - IN: Interrupt Number
 * Stack Frame:
 *   Method Parameters (top of stack)
 *   Return IP (next instruction after CALL)
 *   Preserved Previous R0...R9
 *   Preserved Previous Flags
 *   Preserved Previous FP
 *   (Current FP points here)
 *   Local Variables (size = arg to ENTER/LEAVE)
 *   (SP starts out here and grows downward)
 *   Temporaries
 * Call:
 *   Push Params...
 *   CALL xxxx
 * Enter:
 *   PushAll R0...FP
 *   Adjust FP per Arg
 *   Loadreg FP, SP
 * Leave:
 *   Loadreg SP, FP
 *   PopALl FP...R0
 * Return:
 *   RET
 *   ADDIMM SP, ParamsSize
 * FP offsets, assuming there are 2 one-byte parameters and 3 one-byte locals:
 *   PARAM_1    FP+15
 *   PARAM_2    FP+14
 *   OLD_IP     FP+13
 *   OLD_R0     FP+12
 *   ...
 *   OLD_R9     FP+3
 *   OLD_FLAGS  FP+2
 *   OLD_FP     FP+1
 *   LOCAL_3    FP
 *   LOCAL_2    FP-1
 *   LOCAL_1    FP-2
 */
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
                System.out.printf("R0 R1 R2 R3 R4 R5 R6 R7 R8 R9 FL FP SP IP IV IN %02x\n", traceCell);
                for(Register register : registers) {
                    System.out.printf("%02x ", register.getUnsignedValue());
                }
                System.out.printf("%02x\n", memoryCells[traceCell].getUnsignedValue());
                System.out.flush();
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
