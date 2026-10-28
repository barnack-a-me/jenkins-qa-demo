package edu.qa.tienda.repository;

import edu.qa.tienda.model.Producto;

import java.util.List;
import java.util.Optional;

public interface ProductoRepository {

    Producto guardar(Producto producto);

    Optional<Producto> buscarPorId(Long id);

    List<Producto> listar();

    void actualizarStock(Long id, int nuevoStock);
}
