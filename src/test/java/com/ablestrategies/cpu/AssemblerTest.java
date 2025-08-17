package com.ablestrategies.cpu;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AssemblerTest {

    @Test
    void testBasicOperation() {
        test("""
                      CALLIMM A:
                      JMPIMM 0x20
                C:    0
                A:    LOADIMM $3, 51         # Func: set [4] to 51
                      STORMEM $3, C:
                      RET
                0x20:
            """,0, 18, 4, 51);
    }

    @Test
    void testLoadStore() {
        test("""
                      LOADIMM $3, 7
                      STORMEM $3, 0x50         # set [0x50] = 7
                      LOADIMM $5, 0x51
                      STORIND $3, 5            # set [0x51] = 7
                      LOADREG $6, $3
                      STORMEM $6, 0x52         # set [0x52] = 7
            """,0x51, 7, 0x52, 7);
    }

    @Test
    void testEnterLeave() {
        test("""
                      LOADIMM 0$SP, 127  # start the stack here
                      LOADIMM $5, 1
                      LOADIMM $6, 2
                      PUSHREG $5          # push Arg 1, val=1
                      PUSHREG $6          # push Arg 2, val=2
                      CALLIMM Func:
                      POPREG $1
                      POPREG $2
                      STORMEM $2, Sum:    # save result to Sum:
                      JMPIMM End:
                Sum:  0                   # 24: result goes here
                Func: ENTER 1             # allocate 1 local Var
                      LOADFRA $7, 2       # get Arg 2 into Reg 7
                      ADDFRA $7, 1        # add Arg 1 into Reg 7
                      STORFRA $7, 0       # store Sum in local Var
                      STORFRA $7, 2       # store it to Arg 2
                      LEAVE
                127:   0                  # 127: result goes here, too
                End:
            """,127, 3, 24, 3);
    }

    @Test
    void testBlockingQueue() {
        test("""
                      JMPIMM Test:
                # Here's the one-byte blocking queue...
                Put:  TESTSET Lock:       # Wait: Spinlock
                      STORMEM $9, Que:    # Reg 9 = Data
                      ZEROMEM Lock:       # Release
                      RET
                Take: TESTSET Lock:       # Wait: Spinlock
                      LOADMEM $9, Que:    # Reg 9 = Data
                      ZEROMEM Lock:       # Release
                      RET
                Lock: 0                   # Mutex
                Que:  0                   # The Queue 
                Ctr:  0                   # Counter 0...10
                Rslt: 0                   # Final Count
                # Here's the code to test it...
                Test: LOADMEM $9, Ctr:
                      CALLIMM Put:        # Put Ctr into Queue
                      INCREG $9           # Increment
                      CMPIMM $9, 10       # Has it reached 10?
                      JZEIMM Exit:        # If so, all done
                      STORMEM $9, Ctr:    # Update Ctr
                      JMPIMM Test:        # Loop
                Exit: STORMEM $9, Rslt:
            """,20, 9, 21, 10);
    }

    private void test(String source, int memoryCell1, int expectedValue1, int memoryCell2, int expectedValue2) {
        CPU cpu = new CPU();
        Assembler assembler = new Assembler(cpu);
        cpu.setTracing(true);
        cpu.setTraceCells(memoryCell1, memoryCell2);
        int errorCount = assembler.assemble(source + " \n TRAP\n");
     // assertEquals(0, errorCount);
        CPU.RunMode runMode = cpu.run(false);
        assertEquals(Substrate.RunMode.TRAP, runMode);
        assertEquals(expectedValue1, cpu.getMemoryCell(memoryCell1).getUnsignedValue());
        if(memoryCell2 != memoryCell1) {
            assertEquals(expectedValue2, cpu.getMemoryCell(memoryCell2).getUnsignedValue());
        }
    }
}