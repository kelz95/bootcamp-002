package com.bootcamp.helper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class DescuentoCalculatorTest {

    private static final double DELTA = 0.0001;

    @Test
    void shouldCalculateDescWhenPriceIsGreaterThanZero() {
        double valorProducto = 100.0;
        DescuentoCalculator calculator = new DescuentoCalculator(16.0);

        double descCalculado = calculator.calculateDescuento(valorProducto);

        assertEquals(84.0, descCalculado, DELTA);
    }

    @Test
    void shouldReturnZeroWhenPriceIsLessThanZero() {
        double valorProducto = -20.0;
        DescuentoCalculator calculator = new DescuentoCalculator(16.0);

        double descCalculado = calculator.calculateDescuento(valorProducto);

        assertEquals(0, descCalculado, DELTA);
    }

    @Test
    void shouldReturnZeroWhenPriceIsEqualZero() {
        double valorProducto = 0.0;
        DescuentoCalculator calculator = new DescuentoCalculator(16.0);

        double descCalculado = calculator.calculateDescuento(valorProducto);

        assertEquals(0, descCalculado, DELTA);
    }

    @Test
    void shouldReturnPriceWhenDescuentoIsGreaterThan100() {
        double valorProducto = 2000.0;
        DescuentoCalculator calculator = new DescuentoCalculator(101);

        double descCalculado = calculator.calculateDescuento(valorProducto);

        assertEquals(2000.0, descCalculado, DELTA);
    }

    @Test
    void shouldReturnPriceWhenDescuentoIsLessThanZero() {
        double valorProducto = 178.9;
        DescuentoCalculator calculator = new DescuentoCalculator(-16.0);

        double descCalculado = calculator.calculateDescuento(valorProducto);

        assertEquals(178.9, descCalculado, DELTA);
    }

 }