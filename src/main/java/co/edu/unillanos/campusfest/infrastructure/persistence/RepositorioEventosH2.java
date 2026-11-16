package co.edu.unillanos.campusfest.infrastructure.persistence;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import co.edu.unillanos.campusfest.application.port.RepositorioEventos;
import co.edu.unillanos.campusfest.domain.Asistencia;
import co.edu.unillanos.campusfest.domain.EstadoReserva;
import co.edu.unillanos.campusfest.domain.Evento;
import co.edu.unillanos.campusfest.domain.Reserva;

public class RepositorioEventosH2 implements RepositorioEventos {

    private final Connection conexion;

    public RepositorioEventosH2(Connection conexion) {
        this.conexion = conexion;
        crearTablas();
    }

    @Override
    public Evento buscar(String eventoId) {
        String sql = "SELECT nombre, fecha, lugar, aforo, bloqueados FROM evento WHERE id = ?";
        try (PreparedStatement consulta = conexion.prepareStatement(sql)) {
            consulta.setString(1, eventoId);
            try (ResultSet fila = consulta.executeQuery()) {
                if (!fila.next()) {
                    return null;
                }
                String nombre = fila.getString("nombre");
                LocalDateTime fecha = fila.getObject("fecha", LocalDateTime.class);
                String lugar = fila.getString("lugar");
                int aforo = fila.getInt("aforo");
                int bloqueados = fila.getInt("bloqueados");
                List<Reserva> reservas = leerReservas(eventoId);
                return new Evento(eventoId, nombre, fecha, lugar, aforo, bloqueados, reservas);
            }
        } catch (SQLException e) {
            throw new IllegalStateException("No se pudo leer el evento " + eventoId, e);
        }
    }

    @Override
    public void guardar(Evento evento) {
        try {
            conexion.setAutoCommit(false);
            guardarEvento(evento);
            for (Reserva reserva : evento.getReservas()) {
                guardarReserva(evento.getId(), reserva);
                if (reserva.getAsistencia() != null) {
                    guardarAsistencia(reserva);
                }
            }
            conexion.commit();
        } catch (SQLException e) {
            deshacer();
            throw new IllegalStateException("No se pudo guardar el evento " + evento.getId(), e);
        } finally {
            restaurarAutoCommit();
        }
    }

    private List<Reserva> leerReservas(String eventoId) throws SQLException {
        String sql = "SELECT r.codigo, r.estudiante_id, r.vence_en, r.estado, a.hora "
                + "FROM reserva r LEFT JOIN asistencia a ON a.codigo_reserva = r.codigo "
                + "WHERE r.evento_id = ?";
        List<Reserva> reservas = new ArrayList<>();
        try (PreparedStatement consulta = conexion.prepareStatement(sql)) {
            consulta.setString(1, eventoId);
            try (ResultSet fila = consulta.executeQuery()) {
                while (fila.next()) {
                    LocalDateTime hora = fila.getObject("hora", LocalDateTime.class);
                    Asistencia asistencia = null;
                    if (hora != null) {
                        asistencia = new Asistencia(hora);
                    }
                    reservas.add(Reserva.reconstruir(
                            fila.getString("codigo"),
                            fila.getString("estudiante_id"),
                            fila.getObject("vence_en", LocalDateTime.class),
                            EstadoReserva.valueOf(fila.getString("estado")),
                            asistencia));
                }
            }
        }
        return reservas;
    }

    private void guardarEvento(Evento evento) throws SQLException {
        String sql = "MERGE INTO evento (id, nombre, fecha, lugar, aforo, bloqueados) "
                + "KEY (id) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setString(1, evento.getId());
            sentencia.setString(2, evento.getNombre());
            sentencia.setObject(3, evento.getFecha());
            sentencia.setString(4, evento.getLugar());
            sentencia.setInt(5, evento.getAforo());
            sentencia.setInt(6, evento.getBloqueados());
            sentencia.executeUpdate();
        }
    }

    private void guardarReserva(String eventoId, Reserva reserva) throws SQLException {
        String sql = "MERGE INTO reserva (codigo, evento_id, estudiante_id, vence_en, estado) "
                + "KEY (codigo) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setString(1, reserva.getCodigo());
            sentencia.setString(2, eventoId);
            sentencia.setString(3, reserva.getEstudianteId());
            sentencia.setObject(4, reserva.getVenceEn());
            sentencia.setString(5, reserva.getEstado().name());
            sentencia.executeUpdate();
        }
    }

    private void guardarAsistencia(Reserva reserva) throws SQLException {
        String sql = "MERGE INTO asistencia (codigo_reserva, hora) KEY (codigo_reserva) VALUES (?, ?)";
        try (PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setString(1, reserva.getCodigo());
            sentencia.setObject(2, reserva.getAsistencia().getHora());
            sentencia.executeUpdate();
        }
    }

    private void crearTablas() {
        try (InputStream entrada = getClass().getResourceAsStream("/db/schema.sql")) {
            if (entrada == null) {
                throw new IllegalStateException("No se encontró db/schema.sql");
            }
            String script = new String(entrada.readAllBytes(), StandardCharsets.UTF_8);
            try (Statement sentencia = conexion.createStatement()) {
                for (String instruccion : script.split(";")) {
                    if (!instruccion.isBlank()) {
                        sentencia.execute(instruccion);
                    }
                }
            }
        } catch (IOException | SQLException e) {
            throw new IllegalStateException("No se pudo crear la base de datos", e);
        }
    }

    private void deshacer() {
        try {
            conexion.rollback();
        } catch (SQLException e) {
            // Si el rollback falla no hay nada más que hacer; sigue la excepción original.
        }
    }

    private void restaurarAutoCommit() {
        try {
            conexion.setAutoCommit(true);
        } catch (SQLException e) {
            // Igual que en deshacer(): la excepción importante ya se lanzó.
        }
    }
}