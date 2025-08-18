package com.ablestrategies.cpu;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WWordTest {

    @Test
    void testWWord() {
        System.out.println("\n-------------\nWWord Tests:");
        testSignedValue(86, new WWord("0000000001010110"));
        testSignedValue(54, new WWord(0x36));
        testSignedValue(-107, new WWord(-107));
        testSignedValue(-108, new WWord(107).onesCompliment());
        testSignedValue( 107, new WWord(-108).onesCompliment());
        testSignedValue( -128, new WWord(WWord.NextBitWgt-128));
        testSignedValue( -2, new WWord(WWord.NextBitWgt-2));
        testSignedValue( -1, new WWord(WWord.NextBitWgt-1));
        testUnsignedValue( 0, new WWord(0));
        testUnsignedValue( 127, new WWord(127));
        testUnsignedValue( 128, new WWord(128));
        testUnsignedValue( 254, new WWord(254));
        testUnsignedValue( 255, new WWord(255));
        testUnsignedValue( 65535, new WWord(-1));
    }

    void testSignedValue(int expected, WWord actual) {
        System.out.println(" " + expected + " =?= " + actual.getSignedValue() + " ==> " + actual);
        assertEquals(expected, actual.getSignedValue());
    }

    void testUnsignedValue(int expected, WWord actual) {
        System.out.println(" " + expected + " =?= " + actual.getUnsignedValue() + " ==> " + actual);
        assertEquals(expected, actual.getUnsignedValue());
    }

}