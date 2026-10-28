package edu.qa.tienda.model;

import java.math.BigDecimal;

/** Producto de la tienda. El id es null mientras no se haya guardado en la BD. */
public record Producto(Long id, String nombre, BigDecimal precio, int stock) {
}
