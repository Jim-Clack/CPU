package com.ablestrategies.cpu;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BByteTest {

    @Test
    void testBByte() {
        System.out.println("\n-------------\nBByte Tests:");
        testSignedValue(86, new BByte("01010110"));
        testSignedValue(54, new BByte(0x36));
        testSignedValue(-107, new BByte(-107));
        testSignedValue(-108, new BByte(107).onesCompliment());
        testSignedValue( 107, new BByte(-108).onesCompliment());
        testSignedValue( -128, new BByte(128));
        testSignedValue( -2, new BByte(254));
        testSignedValue( -1, new BByte(255));
        testUnsignedValue( 0, new BByte(0));
        testUnsignedValue( 127, new BByte(127));
        testUnsignedValue( 128, new BByte(128));
        testUnsignedValue( 254, new BByte(254));
        testUnsignedValue( 255, new BByte(255));
        testUnsignedValue( 255, new BByte(-1));
    }

    void testSignedValue(int expected, BByte actual) {
        System.out.println(" " + expected + " =?= " + actual.getSignedValue() + " ==> " + actual);
        assertEquals(expected, actual.getSignedValue());
    }

    void testUnsignedValue(int expected, BByte actual) {
        System.out.println(" " + expected + " =?= " + actual.getUnsignedValue() + " ==> " + actual);
        assertEquals(expected, actual.getUnsignedValue());
    }

}