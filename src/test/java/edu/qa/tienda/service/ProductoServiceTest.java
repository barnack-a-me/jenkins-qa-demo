package edu.qa.tienda.service;

import edu.qa.tienda.model.Producto;
import edu.qa.tienda.repository.ProductoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductoServiceTest {

    @Mock
    private ProductoRepository repository;

    @InjectMocks
    private ProductoService service;

    private Producto teclado(int stock) {
        return new Producto(1L, "Teclado", new BigDecimal("200.00"), stock);
    }

    @Test
    @DisplayName("registrar guarda un producto valido")
    void registrar_guardaProductoValido() {
        Producto nuevo = new Producto(null, "Mouse", new BigDecimal("50.00"), 5);
        when(repository.guardar(nuevo))
                .thenReturn(new Producto(7L, "Mouse", new BigDecimal("50.00"), 5));

        Producto guardado = service.registrar(nuevo);

        assertEquals(7L, guardado.id());
        verify(repository).guardar(nuevo);
    }

    @Test
    @DisplayName("registrar rechaza nombre vacio sin tocar la BD")
    void registrar_rechazaNombreVacio() {
        Producto invalido = new Producto(null, "  ", new BigDecimal("10.00"), 1);
        assertThrows(IllegalArgumentException.class, () -> service.registrar(invalido));
        verifyNoInteractions(repository);
    }

    @Test
    @DisplayName("registrar rechaza stock negativo")
    void registrar_rechazaStockNegativo() {
        Producto invalido = new Producto(null, "Cable", new BigDecimal("10.00"), -1);
        assertThrows(IllegalArgumentException.class, () -> service.registrar(invalido));
        verifyNoInteractions(repository);
    }

    @Test
    @DisplayName("precioFinal de un producto existente (25% desc + IVA)")
    void precioFinal_productoExistente() {
        when(repository.buscarPorId(1L)).thenReturn(Optional.of(teclado(10)));
        assertEquals(new BigDecimal("169.50"), service.precioFinal(1L, 25));
    }

    @Test
    @DisplayName("precioFinal de un producto inexistente lanza excepcion")
    void precioFinal_productoInexistente() {
        when(repository.buscarPorId(99L)).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> service.precioFinal(99L, 0));
    }

    @Test
    @DisplayName("vender descuenta el stock")
    void vender_descuentaStock() {
        when(repository.buscarPorId(1L)).thenReturn(Optional.of(teclado(10)));
        service.vender(1L, 3);
        verify(repository).actualizarStock(1L, 7);
    }

    @Test
    @DisplayName("vender con stock insuficiente no modifica la BD")
    void vender_stockInsuficiente() {
        when(repository.buscarPorId(1L)).thenReturn(Optional.of(teclado(2)));
        assertThrows(IllegalStateException.class, () -> service.vender(1L, 5));
        verify(repository, never()).actualizarStock(anyLong(), anyInt());
    }

    @Test
    @DisplayName("vender rechaza cantidad cero")
    void vender_rechazaCantidadInvalida() {
        assertThrows(IllegalArgumentException.class, () -> service.vender(1L, 0));
        verifyNoInteractions(repository);
    }
}
