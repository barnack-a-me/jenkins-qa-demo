package edu.qa.tienda.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PrecioCalculatorTest {

    @ParameterizedTest(name = "precio {0} + IVA 13% = {1}")
    @CsvSource({"100.00,113.00", "10.50,11.87", "0.00,0.00", "250.00,282.50"})
    @DisplayName("conIva calcula y redondea a 2 decimales")
    void conIva_calculaCorrectamente(String precio, String esperado) {
        assertEquals(new BigDecimal(esperado), PrecioCalculator.conIva(new BigDecimal(precio)));
    }

    @Test
    @DisplayName("conIva rechaza precios negativos")
    void conIva_rechazaPrecioNegativo() {
        assertThrows(IllegalArgumentException.class,
                () -> PrecioCalculator.conIva(new BigDecimal("-1.00")));
    }

    @Test
    @DisplayName("conIva rechaza precio nulo")
    void conIva_rechazaPrecioNulo() {
        assertThrows(IllegalArgumentException.class, () -> PrecioCalculator.conIva(null));
    }

    @Test
    @DisplayName("aplicarDescuento del 25% sobre 200.00 = 150.00")
    void aplicarDescuento_veinticincoPorCiento() {
        assertEquals(new BigDecimal("150.00"),
                PrecioCalculator.aplicarDescuento(new BigDecimal("200.00"), 25));
    }

    @ParameterizedTest(name = "descuento invalido: {0}")
    @ValueSource(ints = {-1, 101})
    @DisplayName("aplicarDescuento rechaza porcentajes fuera de 0..100")
    void aplicarDescuento_rechazaPorcentajeInvalido(int porcentaje) {
        assertThrows(IllegalArgumentException.class,
                () -> PrecioCalculator.aplicarDescuento(new BigDecimal("100.00"), porcentaje));
    }

    @Test
    @DisplayName("precioFinal aplica descuento y luego IVA")
    void precioFinal_descuentoMasIva() {
        assertEquals(new BigDecimal("169.50"),
                PrecioCalculator.precioFinal(new BigDecimal("200.00"), 25));
    }
}
