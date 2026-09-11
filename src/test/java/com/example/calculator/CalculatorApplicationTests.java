package com.example.calculator;

import com.example.calculator.service.CalculatorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CalculatorApplicationTests {

    private CalculatorService calculatorService;

    @BeforeEach
    void setUp() {
        calculatorService = new CalculatorService();
    }

    @Test
    void testAdd() {
        assertEquals(5.0, calculatorService.add(2, 3));
    }

    @Test
    void testSubtract() {
        assertEquals(6.0, calculatorService.subtract(10, 4));
    }

    @Test
    void testMultiply() {
        assertEquals(42.0, calculatorService.multiply(6, 7));
    }

    @Test
    void testDivide() {
        assertEquals(5.0, calculatorService.divide(20, 4));
    }

    @Test
    void testDivideByZero() {
        assertThrows(
                IllegalArgumentException.class,
                () -> calculatorService.divide(20, 0)
        );
    }
}