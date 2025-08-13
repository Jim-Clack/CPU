package com.ablestrategies.cpu;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HalfAdderTest {

    @Test
    public void testHalfAdder() {
        System.out.println("HalfAdder Test");
        test(new Bit(0), new Bit(0), new Bit(0), new Bit(0));
        test(new Bit(0), new Bit(1), new Bit(1), new Bit(0));
        test(new Bit(1), new Bit(0), new Bit(1), new Bit(0));
        test(new Bit(1), new Bit(1), new Bit(0), new Bit(1));
    }

    private void test(Bit a, Bit b, Bit expectedSum, Bit ExpectedCarry) {
        HalfAdder halfAdder = new HalfAdder();
        halfAdder.add(a, b);
        System.out.println(" " + a + " + " + b + " = "  + halfAdder.getSum() + " (carry = " + halfAdder.getCarry() + ")");
        assertEquals(expectedSum.getVal(), halfAdder.getSum().getVal());
        assertEquals(ExpectedCarry.getVal(), halfAdder.getCarry().getVal());
    }

}