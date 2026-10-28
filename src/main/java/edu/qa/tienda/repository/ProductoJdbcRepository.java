package edu.qa.tienda.repository;

import edu.qa.tienda.model.Producto;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/** Implementación JDBC contra PostgreSQL (tabla productos). */
public class ProductoJdbcRepository implements ProductoRepository {

    private final String url;
    private final String user;
    private final String password;

    public ProductoJdbcRepository(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;
    }

    private Connection conectar() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

    @Override
    public Producto guardar(Producto p) {
        String sql = "INSERT INTO productos (nombre, precio, stock) VALUES (?, ?, ?)";
        try (Connection c = conectar();
             PreparedStatement ps = c.prepareStatement(sql, new String[]{"id"})) {
            ps.setString(1, p.nombre());
            ps.setBigDecimal(2, p.precio());
            ps.setInt(3, p.stock());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                keys.next();
                return new Producto(keys.getLong(1), p.nombre(), p.precio(), p.stock());
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Error al guardar el producto", e);
        }
    }

    @Override
    public Optional<Producto> buscarPorId(Long id) {
        String sql = "SELECT id, nombre, precio, stock FROM productos WHERE id = ?";
        try (Connection c = conectar(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(mapear(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Error al buscar el producto", e);
        }
    }

    @Override
    public List<Producto> listar() {
        String sql = "SELECT id, nombre, precio, stock FROM productos ORDER BY id";
        List<Producto> lista = new ArrayList<>();
        try (Connection c = conectar();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(mapear(rs));
            }
            return lista;
        } catch (SQLException e) {
            throw new IllegalStateException("Error al listar productos", e);
        }
    }

    @Override
    public void actualizarStock(Long id, int nuevoStock) {
        String sql = "UPDATE productos SET stock = ? WHERE id = ?";
        try (Connection c = conectar(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, nuevoStock);
            ps.setLong(2, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException("Error al actualizar el stock", e);
        }
    }

    private Producto mapear(ResultSet rs) throws SQLException {
        return new Producto(rs.getLong("id"), rs.getString("nombre"),
                rs.getBigDecimal("precio"), rs.getInt("stock"));
    }
}
