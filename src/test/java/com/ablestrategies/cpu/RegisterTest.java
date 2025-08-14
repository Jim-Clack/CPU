package com.ablestrategies.cpu;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RegisterTest {

    @Test
    void testRegister() {
        System.out.println("Register Tests:");
        FlagRegister flagRegister = new FlagRegister(null);
        Register register = new Register(flagRegister);
        Register register2 = new Register(flagRegister);

        register.set(107);
        register.invert();
        testValue(-107, register);
        testFlags(0, 0, 1, register);
        register.invert();
        testValue(107, register);
        register.add(2);
        testValue(109, register);
        testFlags(0, 0, 0, register);

        register.set(Octet.MaxBitWgt - 1);
        register2.set(Octet.MaxBitWgt - 1);
        register.add(register2);
        testFlags(0, 0, 1, register);
        register.add(register2);
        testFlags(0, 1, 0, register);

        register.set("00011010");
        int expected = register.getSignedValue() << 3;
        register.shiftLeft(3);
        testValue(expected - Octet.NextBitWgt, register);
        testFlags(0, 0, 1, register);

        register.set("01011000");
        expected = register.getSignedValue() >> 3;
        register.shiftRight(3);
        testValue(expected, register);
        testFlags(0, 0, 0, register);

        register.set("11001010");
        register2.set(register.getSignedValue() ^ (Octet.NextBitWgt - 1));
        register.onesCompliment();
        testValue(register2.getSignedValue(), register);

        register2.set("01010101");
        register.set("00111100");
        expected = register.getSignedValue() & register2.getSignedValue();
        register.and(register2);
        testValue(expected, register);

        register2.set("01010101");
        register.set("00111100");
        expected = register.getSignedValue() | register2.getSignedValue();
        register.or(register2);
        testValue(expected, register);

        register2.set("01010101");
        register.set("00111100");
        expected = register.getSignedValue() ^ register2.getSignedValue();
        register.xor(register2);
        testValue(expected, register);

    }

    void testValue(int expected, Register actual) {
        System.out.println(" " + expected + " ==> " + actual);
        assertEquals(expected, actual.getSignedValue());
    }

    void testFlags(int expZero, int expCarry, int expNegative, Register register) {
        assertEquals(expZero, register.isZero().getVal());
        assertEquals(expCarry, register.isCarry().getVal());
        assertEquals(expNegative, register.isNegative().getVal());
    }

}