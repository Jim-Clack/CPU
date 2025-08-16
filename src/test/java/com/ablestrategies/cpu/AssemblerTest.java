package com.ablestrategies.cpu;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AssemblerTest {

    @Test
    void testBasicOperation() {
        test("""
                      CALL A:
                      JMP 0x20
                C:    0
                A:    LOADIMM 3, 51          # Func: set [4] to 51
                      STORMEM 3, C:
                      RET
                0x20:
            """,0, 18, 4, 51);
    }

    @Test
    void testLoadStore() {
        test("""
                      LOADIMM 3, 7
                      STORMEM 3, 0x50         # set [0x50] = 7
                      LOADIMM 5, 0x51
                      STORIND 3, 5            # set [0x51] = 7
                      LOADREG 6, 3
                      STORMEM 6, 0x52         # set [0x52] = 7
            """,0x51, 7, 0x52, 7);
    }

    @Test
    void testEnterLeave() {
        test("""
                      LOADIMM 0$SP, 127   # start the stack here
                      LOADIMM 5, 1
                      LOADIMM 6, 2
                      PUSH 5              # push Arg 1, val=1
                      PUSH 6              # push Arg 2, val=2
                      CALL Func:
                      POP 1
                      POP 2
                      STORMEM 2, Sum:     # save result to Sum:
                      JMP End:
                Sum:  0                   # 24: result goes here
                Func: ENTER 1             # allocate 1 local Var
                      LOADFRA 7, 2        # get Arg 2 into Reg 7
                      ADDFRA 7, 1         # add Arg 1 into Reg 7
                      STORFRA 7, 0        # store Sum in local Var
                      STORFRA 7, 2        # store it to Arg 2
                      LEAVE
                127:   0                   # 127: result goes here, too
                End:
            """,127, 3, 24, 3);
    }

    private void test(String source, int memoryCell1, int expectedValue1, int memoryCell2, int expectedValue2) {
        CPU cpu = new CPU();
        Assembler assembler = new Assembler(cpu);
        cpu.setTracing(true);
        cpu.setTraceCells(memoryCell1, memoryCell2);
        assembler.assemble(source + " \n TRAP\n");
        CPU.RunMode runMode = cpu.run(false);
        assertEquals(Substrate.RunMode.TRAP, runMode);
        assertEquals(expectedValue1, cpu.getMemoryCell(memoryCell1).getUnsignedValue());
        if(memoryCell2 != memoryCell1) {
            assertEquals(expectedValue2, cpu.getMemoryCell(memoryCell2).getUnsignedValue());
        }
    }
}