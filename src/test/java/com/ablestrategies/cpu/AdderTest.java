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
        BByte bbyteP = new BByte(p);
        BByte bbyteQ = new BByte(q);
        Adder adder = new Adder();
        adder.set(bbyteP);
        System.out.println("\n-------------\nAdder Test: " + bbyteP + " + " + bbyteQ);
        System.out.println(" Result=" + adder.add(bbyteQ));
        System.out.println("  Carry=" + adder.isCarry());
        System.out.println("   Zero=" + adder.isZero());
        System.out.println("    Neg=" + adder.isNegative());
        assertEquals(expected, adder.getSignedValue());
    }

}