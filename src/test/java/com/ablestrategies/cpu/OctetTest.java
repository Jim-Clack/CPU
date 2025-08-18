package com.ablestrategies.cpu;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OctetTest {

    @Test
    void testOctet() {
        System.out.println("\n-------------\nOctet Tests:");
        testSignedValue(86, new Octet("01010110"));
        testSignedValue(54, new Octet(0x36));
        testSignedValue(-107, new Octet(-107));
        testSignedValue(-108, new Octet(107).onesCompliment());
        testSignedValue( 107, new Octet(-108).onesCompliment());
        testSignedValue( -128, new Octet(128));
        testSignedValue( -2, new Octet(254));
        testSignedValue( -1, new Octet(255));
        testUnsignedValue( 0, new Octet(0));
        testUnsignedValue( 127, new Octet(127));
        testUnsignedValue( 128, new Octet(128));
        testUnsignedValue( 254, new Octet(254));
        testUnsignedValue( 255, new Octet(255));
        testUnsignedValue( 255, new Octet(-1));
    }

    void testSignedValue(int expected, Octet actual) {
        System.out.println(" " + expected + " =?= " + actual.getSignedValue() + " ==> " + actual);
        assertEquals(expected, actual.getSignedValue());
    }

    void testUnsignedValue(int expected, Octet actual) {
        System.out.println(" " + expected + " =?= " + actual.getUnsignedValue() + " ==> " + actual);
        assertEquals(expected, actual.getUnsignedValue());
    }

}