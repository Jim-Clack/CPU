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
 *   Method Parameters (top of stack) [FP+13+arg] arg=1, 2, ...
 *   Return IP (next instruction after CALL)
 *   Preserved Previous R0...R9
 *   Preserved Previous Flags
 *   Preserved Previous FP
 *   (Current FP points here)
 *   Local Variables (size=ENTER Arg1) [FP-var] var=0, -1, -2, ...
 *   (SP starts out here and grows downward)
 *   Temporaries
 * Call:
 *   Push Params...
 *   CALL xxxx
 * Enter:
 *   PushAll R0...FP
 *   Loadreg FP, SP
 *   Adjust SP per Arg
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
    private int m1Interval = 0;
    private int m1InterruptNumber = 0;
    private int m1Ticks = 0;
    private int traceCell1 = 0;
    private int traceCell2 = memoryCells.length-1;

    public CPU() {
        initialize(this);
        runMode = RunMode.IDLE;
    }

    public RunMode run(boolean resume) {
        if(resume && runMode != RunMode.TRAP) {
            System.out.println("CPU cannot resume as it was not paused by a TRAP opcode");
            return runMode;
        }
        System.out.println("EXECUTING...");
        runMode = RunMode.RUNNING;
        while(runMode == RunMode.RUNNING) {
            handleInterrupts();
            Opcode opcode = Opcode.opcode(getNextProgramByte());
            if (opcode.getNumArgs() < 1) {
                runMode = execute(opcode);
            } else {
                int argument1 = getNextProgramByte();
                if (opcode.getNumArgs() < 2) {
                    runMode = execute(opcode, argument1);
                } else {
                    int argument2 = getNextProgramByte();
                    runMode = execute(opcode, argument1, argument2);
                }
            }
            if(tracingDelayMs >= 0) {
                System.out.printf("R0 R1 R2 R3 R4 R5 R6 R7 R8 R9 FL FP SP IP IV IN SC %02x %02x\n",
                        traceCell1, traceCell2);
                for(Register register : registers) {
                    System.out.printf("%02x ", register.getUnsignedValue());
                }
                System.out.printf("%02x ", memoryCells[traceCell1].getUnsignedValue());
                System.out.printf("%02x\n", memoryCells[traceCell2].getUnsignedValue());
                System.out.flush();
                try {
                    Thread.sleep(tracingDelayMs);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        System.out.printf("%02x: %s - EXECUTION STOPPED\n", registers[IP].getSignedValue(), runMode.name());
        return runMode;
    }

    public void sendInterrupt(int interruptNumber) {
        try {
            this.interruptNumbers.put(interruptNumber);
        } catch (InterruptedException e) {
            System.out.printf("FAILURE to put interrupt " + interruptNumber + "\n" + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void setTraceCells(int traceCell1, int traceCell2) {
        this.traceCell1 = traceCell1;
        this.traceCell2 = traceCell2;
    }

    public void ActivateM1Clock(int rtcInterval, int rtcInterruptNumber) {
        this.m1Interval = Math.max(rtcInterval, 20); // never less than 20
        this.m1InterruptNumber = rtcInterruptNumber;
        this.m1Ticks = 0;
    }

    private void handleInterrupts() {
        int interruptNumber = 0;
        if(!enableInterrupts || getRegister(IV).isZero().getVal() > 0) {
            return;
        }
        if(m1InterruptNumber > 0) {
            if(m1Ticks++ > m1Interval) {
                m1Ticks = 0;
                sendInterrupt(m1InterruptNumber);
            }
        }
        if(!interruptNumbers.isEmpty()) {
            try {
                interruptNumber = interruptNumbers.take();
            } catch (InterruptedException e) {
                System.out.printf("FAILURE to take interrupt " + interruptNumber + "\n" + e.getMessage());
                throw new RuntimeException(e);
            }
        }
        if(interruptNumber <= 0) {
            return;
        }
        if(tracingDelayMs >= 0) {
            System.out.printf("%02x: INTERRUPT %d\n", registers[IP].getSignedValue() - 1, interruptNumber);
        }
        push(registers[IP]);
        registers[IN].set(interruptNumber);
        registers[IP].set(registers[IV].getSignedValue());
        enableInterrupts = false;
    }

}
