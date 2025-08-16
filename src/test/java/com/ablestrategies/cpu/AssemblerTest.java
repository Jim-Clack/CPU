package com.ablestrategies.cpu;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AssemblerTest {

    @Test
    void testBasicOperation() {
        test("""
                      CALL A:
                      JMP 0x10
                C:    0
                A:    LOADIMM 3,0x33
                      STORMEM 3, C:
                      RET
                0x10:
            """,0, 18, 4, 51);
    }

    @Test
    void testLoadStore() {
        test("""
                      LOADIMM 3, 7
                      STORMEM 3, 0x50     # set [0x50] to 7
                      LOADIMM 5, 0x51
                      STORIND 3, 5        # set [0x51] to 7
                      LOADIMM 0$FP, 0x50  # FP = 0x50
                      LOADFRA 6, 1        # set Reg6 to 7 from [0x51]
                      STORMEM 6, 0x52     # set [0x52] to 7
                      STORFRA 6, 3        # set [0x53] to 7
            """,80, 7, 83, 7);
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