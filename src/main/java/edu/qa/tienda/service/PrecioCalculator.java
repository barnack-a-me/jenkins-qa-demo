package edu.qa.tienda.service;

import java.math.BigDecimal;
import java.math.RoundingMode;

/** Reglas de precio: IVA de Bolivia (13 %) y descuentos porcentuales. */
public final class PrecioCalculator {
    // correcto
    public static final BigDecimal IVA = new BigDecimal("0.13");
    // modificado
   // public static final BigDecimal IVA = new BigDecimal("0.15");

    private PrecioCalculator() {
    }

    public static BigDecimal conIva(BigDecimal precio) {
        validarPrecio(precio);
        return precio.add(precio.multiply(IVA)).setScale(2, RoundingMode.HALF_UP);
    }

    public static BigDecimal aplicarDescuento(BigDecimal precio, int porcentaje) {
        validarPrecio(precio);
        if (porcentaje < 0 || porcentaje > 100) {
            throw new IllegalArgumentException("El descuento debe estar entre 0 y 100");
        }
        BigDecimal factor = BigDecimal.valueOf(100 - porcentaje).divide(BigDecimal.valueOf(100));
        return precio.multiply(factor).setScale(2, RoundingMode.HALF_UP);
    }

    public static BigDecimal precioFinal(BigDecimal precio, int porcentajeDescuento) {
        return conIva(aplicarDescuento(precio, porcentajeDescuento));
    }

    private static void validarPrecio(BigDecimal precio) {
        if (precio == null || precio.signum() < 0) {
            throw new IllegalArgumentException("El precio no puede ser nulo ni negativo");
        }
    }
    // codigo agrego
    public static BigDecimal totalConIva(BigDecimal precioUnitario, int cantidad) {
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a cero");
        }
        validarPrecio(precioUnitario);
        return conIva(precioUnitario.multiply(BigDecimal.valueOf(cantidad)));
    }

}
