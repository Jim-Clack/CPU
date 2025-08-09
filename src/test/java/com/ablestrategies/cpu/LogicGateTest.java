package com.ablestrategies.cpu;

import org.junit.jupiter.api.Test;

import java.util.function.IntBinaryOperator;

import static org.junit.jupiter.api.Assertions.*;

class LogicGateTest {

    @Test
    void testLogicGates() {
        testGate(LogicGate.XOR, (a, b) -> a ^ b);
        testGate(LogicGate.AND, (a, b) -> a & b);
        testGate(LogicGate.OR, (a, b) -> a | b);
        testGate(LogicGate.NAND, (a, b) -> 1 - (a & b));
    }

    void testGate(LogicGate gate, IntBinaryOperator tester) {
        System.out.println("LogicGate Test: " + gate);
        for(int a = 0; a < 2; a++) {
            for (int b = 0; b < 2; b++) {
                Bit bitA = new Bit(a);
                Bit bitB = new Bit(b);
                Bit result = gate.output(bitA, bitB);
                System.out.println(" a=" + a + ", b=" + b + ", ==>" + result);
                int expected = tester.applyAsInt(bitA.getVal(), bitB.getVal());
                assertEquals(expected != 0, result.getVal() != 0);
            }
        }
    }

}