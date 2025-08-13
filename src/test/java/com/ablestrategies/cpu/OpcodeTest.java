package com.ablestrategies.cpu;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OpcodeTest {

    @Test
    public void testOpcodes() {
        System.out.println("Test Opcodes:");
        for(Opcode opcode : Opcode.values()) {
            System.out.println(" " + opcode.getMnemonic());
            assertEquals(opcode.getMnemonic(), opcode.name());
            assertEquals(opcode.name(), Opcode.opcode(opcode.getValue()).name());
        }
        System.out.println(" INVALID 3333");
        assertEquals(Opcode.INVALID.getValue(), Opcode.opcode(3333).getValue());
    }
}