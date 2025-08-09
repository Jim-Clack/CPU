package com.ablestrategies.cpu;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OctetTest {

    @Test
    void testOctet() {
        System.out.println("Octet Tests:");
        testValue(86, new Octet("01010110"));
        testValue(54, new Octet(0x36));
        testValue(-107, new Octet(-107));
        testValue(-108, new Octet(107).onesCompliment());
        testValue( 107, new Octet(-108).onesCompliment());
    }

    void testValue(int expected, Octet actual) {
        System.out.println(" " + expected + " ==> " + actual);
        assertEquals(expected, actual.getIntValue());
    }
}