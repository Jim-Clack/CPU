package com.ablestrategies.cpu;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AssemblerTest {

    @Test
    void testBasicOperation() {
        test("""
                      CALLIMM A:
                      JMPIMM 0x20
             C:       0
             A:       LOADIMM $3, 51    # Func: set [4] to 51
                      STORMEM $3, C:
                      RET
             0x20:
         """,0, 30, 4, 51, 0);
    }

    @Test
    void testLoadStore() {
        test("""
            Loc1:     EQU 0x50
            Loc2:     EQU 0x51
                      LOADIMM $3, 7
                      STORMEM $3, Loc1:   # set [0x50] = 7
                      LOADIMM $5, Loc2:
                      STORIND $3, 5       # set [0x51] = 7
                      LOADREG $6, $3
                      STORMEM $6, 0x52    # set [0x52] = 7
         """,0x51, 7, 0x52, 7, 0);
    }

    @Test
    void testEnterLeave() {
        test("""
                      LOADIMM $SP, 127    # start the stack here
                      LOADIMM $5, 1
                      LOADIMM $6, 2
                      PUSHREG $5          # push Arg 1, val=1
                      PUSHREG $6          # push Arg 2, val=2
                      CALLIMM Func:
                      POPREG $1
                      POPREG $2
                      STORMEM $2, Sum:    # save result to Sum:
                      JMPIMM End:
             Sum:     0                   # 24: result goes here
             Func:    ENTER 1             # allocate 1 local Var
                      LOADFRA $7, 2       # get Arg 2 into Reg 7
                      ADDFRA $7, 1        # add Arg 1 into Reg 7
                      STORFRA $7, 0       # store Sum in local Var
                      STORFRA $7, 2       # store it to Arg 2
                      LEAVE
             127:     0                   # 127: result goes here, too
             End:
         """,127, 3, 24, 3, 0);
    }

    @Test
    void testCmpJZE() {
        test("""
                      JMPIMM Go:
             Pos:     0
             Neg:     0
             Go:      LOADIMM $1, 0
                      CMPIMM $1, 0
             Test:    JZEIMM Skip:        # ZE
                      PUSHREG $FLAGS
                      LOADMEM $2, Pos:
                      INCREG $2
                      STORMEM $2, Pos:
                      POPREG $FLAGS
             Skip:    JNZEIMM Done:       # GT
                      PUSHREG $FLAGS
                      LOADMEM $2, Neg:
                      INCREG $2
                      STORMEM $2, Neg:
                      POPREG $FLAGS
             Done:   
        """,2, 0, 3, 1, 0);
    }

    @Test
    void testCmpJGT() {
        test("""
                      JMPIMM Go:
             Pos:     0
             Neg:     0
             Go:      LOADIMM $1, 1
                      CMPIMM $1, 0
             Test:    JGTIMM Skip:        # ZE
                      PUSHREG $FLAGS
                      LOADMEM $2, Pos:
                      INCREG $2
                      STORMEM $2, Pos:
                      POPREG $FLAGS
             Skip:    JLTEIMM Done:       # GT
                      PUSHREG $FLAGS
                      LOADMEM $2, Neg:
                      INCREG $2
                      STORMEM $2, Neg:
                      POPREG $FLAGS
             Done:
         """,2, 0, 3, 1, 0);
    }

    @Test
    void testCmpJLT() {
        test("""
                      JMPIMM Go:
             Pos:     0
             Neg:     0
             Go:      LOADIMM $1, 0
                      CMPIMM $1, 1
             Test:    JLTIMM Skip:        # ZE
                      PUSHREG $FLAGS
                      LOADMEM $2, Pos:
                      INCREG $2
                      STORMEM $2, Pos:
                      POPREG $FLAGS
             Skip:    JGTEIMM Done:       # GT
                      PUSHREG $FLAGS
                      LOADMEM $2, Neg:
                      INCREG $2
                      STORMEM $2, Neg:
                      POPREG $FLAGS
             Done:
         """,2, 0, 3, 1, 0);
    }

    @Test
    void testBlockingQueue() {
        test("""
            Reg:      EQU 9               # Use this register
                      JMPIMM Test:
            # Here's the one-byte blocking queue...
            Put:      TSWAIT Lock:        # Wait: Spinlock
                      PUSHREG Reg:        # Preserve Reg 9
                      LOADMEM Reg:, Lgt:
                      CMPIMM Reg:, 0      # is Lgt=0?
                      POPREG Reg:
                      ZEROMEM Lock:       # Release
                      JNZEIMM Put:        # Wait til Empty
                      STORMEM Reg:, Que:  # Reg 9 = Data
                      PUSHREG Reg:        # Preserve $9
                      LOADIMM Reg:, 1     # Set Lgt to 1
                      STORMEM Reg:, Lgt:
                      POPREG Reg:
                      ZEROMEM Lock:       # Release
                      RET
            Take:     TSWAIT Lock:        # Wait: Spinlock
                      PUSHREG Reg:        # Preserve $9
                      LOADMEM Reg:, Lgt:
                      CMPIMM Reg:, 0      # is Lgt=0?
                      POPREG Reg:
                      ZEROMEM Lock:       # Release
                      JZEIMM Take:        # Wait til Full
                      LOADMEM Reg:, Que:  # Reg 9 = Data
                      ZEROMEM Lgt:        # Set Lgt to 0
                      ZEROMEM Lock:       # Release
                      RET
            Lock:     0                   # Mutex
            Que:      0                   # The Queue 
            Lgt:      0                   # 1 if Queue full
            # ----------------------------- 
            # Here's the code to test it...
            Ctr:      0                   # Counter 0..9
            Rslt:     0                   # Result 0..10
            Test:     LOADMEM Reg:, Ctr:
                      CALLIMM Put:        # Put Ctr into Queue
                      LOADIMM Reg:, 0
                      CALLIMM Take:       # Get Ctr from Queue
                      INCREG Reg:         # Increment
                      CMPIMM Reg:, 10     # Has it reached 10?
                      JZEIMM Exit:        # If so, all done
                      STORMEM Reg:, Ctr:  # Update Ctr
                      JMPIMM Test:        # Loop
            Exit:     STORMEM Reg:, Rslt:
         """,0x3d, 9, 0x3e, 10, 0);
    }

    @Test
    void testConcurrency() {
        test("""
                      JMPIMM Begin:
             Irq:     EQU 2
             Iters:   EQU 10
             Que:     0
             Sum:     0
                      # Try this with Isr1: -OR- Isr2:
             Begin:   LOADIMM $IV, Isr1:  # Enable interrupts
             Loop:    ZEROREG $1
                      ADDMEM $1, Que:     # Add to set Que
                      JZEIMM Loop:        # Wait for Que != 0
                      ZEROREG $2
                      STORMEM $2, Que:    # Clear the Queue
                      ADDMEM $1, Sum:     # Flag + Sum
                      STORMEM $1, Sum:    # Store it to Sum
                      CMPIMM $1, Iters:   # Do this 10 times
                      JZEIMM Exit:        # Then exit
                      JMPIMM Loop:        # Else keep looping
             # --------------------------------
             # Here's the ISR...
             Isr1:    ENTER 0             # At every RTC tick...
                      CMPIMM $IN, Irq:    # Make sure it's for us
                      JNZEIMM Leave1:     # If not Our IRQ
                      LOADIMM $2, 1
                      STORMEM $2, Que:    # Set Flag to 1
             Leave1:  ILEAVE
             # --------------------------------
             # Here's an alternate ISR...
             Isr2:    PUSHREG $FLAGS      # At every RTC tick...
                      PUSHREG $2
                      CMPIMM $IN, Irq:    # Make sure it's for us
                      JNZEIMM Leave2:     # If not Our IRQ
                      LOADIMM $2, 1
                      STORMEM $2, Que:    # Set Flag to 1
             Leave2:  POPREG $2
                      POPREG $FLAGS
                      IRET
             Exit:
         """,2, 0, 3, 10, 2);
    }

    private void test(String source,
                      int memoryCell1, int expectedValue1, int memoryCell2, int expectedValue2,
                      int m1InterruptNumber) {
        StackTraceElement[] trace = Thread.currentThread().getStackTrace();
        String caller = trace[2].getMethodName();
        System.out.println("\n-------------\nTesting: " + caller);
        CPU cpu = new CPU();
        Assembler assembler = new Assembler(cpu);
        cpu.setTracingDelayMs(0);
        cpu.setTraceCells(memoryCell1, memoryCell2);
        if(m1InterruptNumber > 0) {
            cpu.ActivateM1Clock(20, m1InterruptNumber);
        }
        int errorCount = assembler.assemble(source + " \n TRAP\n");
        assertEquals(0, errorCount);
        CPU.RunMode runMode = cpu.run(false);
        assertEquals(Substrate.RunMode.TRAP, runMode);
        assertEquals(expectedValue1, cpu.getMemoryCell(memoryCell1).getUnsignedValue());
        assertEquals(expectedValue2, cpu.getMemoryCell(memoryCell2).getUnsignedValue());
    }
}