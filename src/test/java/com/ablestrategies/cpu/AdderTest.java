package com.ablestrategies.cpu;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AdderTest {

    @Test
    void testAdder() {
        testAdd(32, -1, 33);
        testAdd(0, 0, 0);
        testAdd(55, 17, 38);
        testAdd(117, 106, 11);
        testAdd(-21, 97, -118);
    }

    public void testAdd(int expected, int p, int q) {
        Octet octetP = new Octet(p);
        Octet octetQ = new Octet(q);
        Adder adder = new Adder();
        adder.clone(octetP);
        System.out.println("Adder Test: " + octetP + " + " + octetQ);
        System.out.println(" Result=" + adder.add(octetQ));
        System.out.println("  Carry=" + adder.isCarry());
        System.out.println("   Zero=" + adder.isZero());
        System.out.println("    Neg=" + adder.isNegative());
        assertEquals(expected, adder.getIntValue());
    }

}