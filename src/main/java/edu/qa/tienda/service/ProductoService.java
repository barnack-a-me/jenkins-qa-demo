package edu.qa.tienda.service;

import edu.qa.tienda.model.Producto;
import edu.qa.tienda.repository.ProductoRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;

public class ProductoService {

    private final ProductoRepository repository;

    public ProductoService(ProductoRepository repository) {
        this.repository = repository;
    }

    public Producto registrar(Producto producto) {
        if (producto.nombre() == null || producto.nombre().isBlank()) {
            throw new IllegalArgumentException("El nombre es obligatorio");
        }
        if (producto.precio() == null || producto.precio().signum() <= 0) {
            throw new IllegalArgumentException("El precio debe ser mayor a cero");
        }
        if (producto.stock() < 0) {
            throw new IllegalArgumentException("El stock no puede ser negativo");
        }
        return repository.guardar(producto);
    }

    public List<Producto> listar() {
        return repository.listar();
    }

    public BigDecimal precioFinal(Long id, int porcentajeDescuento) {
        Producto p = buscar(id);
        return PrecioCalculator.precioFinal(p.precio(), porcentajeDescuento);
    }

    public void vender(Long id, int cantidad) {
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a cero");
        }
        Producto p = buscar(id);
        if (p.stock() < cantidad) {
            throw new IllegalStateException("Stock insuficiente para el producto " + id);
        }
        repository.actualizarStock(id, p.stock() - cantidad);
    }

    private Producto buscar(Long id) {
        return repository.buscarPorId(id)
                .orElseThrow(() -> new NoSuchElementException("Producto no encontrado: " + id));
    }
}
