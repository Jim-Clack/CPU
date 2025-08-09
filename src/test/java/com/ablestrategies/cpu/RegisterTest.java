package com.ablestrategies.cpu;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RegisterTest {

    @Test
    void testRegister() {
        System.out.println("Register Tests:");
        Register register = new Register();
        register.clone(new Octet(107));
        register.invert();
        testValue(-107, register);
    }

    void testValue(int expected, Register actual) {
        System.out.println(" " + expected + " ==> " + actual);
        assertEquals(expected, actual.getIntValue());
    }


}