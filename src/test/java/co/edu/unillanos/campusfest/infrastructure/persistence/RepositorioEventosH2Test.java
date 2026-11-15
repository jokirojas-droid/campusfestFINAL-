package co.edu.unillanos.campusfest.infrastructure.persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.UUID;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import co.edu.unillanos.campusfest.domain.EstadoReserva;
import co.edu.unillanos.campusfest.domain.Evento;
import co.edu.unillanos.campusfest.domain.Reserva;

class RepositorioEventosH2Test {

    private static final LocalDateTime AHORA = LocalDateTime.of(2026, 11, 20, 18, 0);

    private Connection conexion;
    private RepositorioEventosH2 repositorio;

    @BeforeEach
    void abrirBaseEnMemoria() throws SQLException {
        String nombre = UUID.randomUUID().toString().replace("-", "");
        conexion = DriverManager.getConnection("jdbc:h2:mem:" + nombre);
        repositorio = new RepositorioEventosH2(conexion);
    }

    @AfterEach
    void cerrarBase() throws SQLException {
        conexion.close();
    }

    private Evento nuevoEvento() {
        return new Evento("e1", "CampusFest", AHORA.plusDays(5), "Auditorio", 10, 2);
    }

    @Test
    void buscarDevuelveNullSiElEventoNoExiste() {
        assertNull(repositorio.buscar("no-existe"));
    }

    @Test
    void guardarYBuscarConservaLosDatosDelEvento() {
        repositorio.guardar(nuevoEvento());

        Evento leido = repositorio.buscar("e1");

        assertNotNull(leido);
        assertEquals("CampusFest", leido.getNombre());
        assertEquals("Auditorio", leido.getLugar());
        assertEquals(10, leido.getAforo());
        assertEquals(8, leido.cuposDisponibles(AHORA));
    }

    @Test
    void guardarYBuscarConservaLasReservas() {
        Evento evento = nuevoEvento();
        Reserva reserva = evento.reservar("est-1", AHORA);
        repositorio.guardar(evento);

        Evento leido = repositorio.buscar("e1");

        assertEquals(7, leido.cuposDisponibles(AHORA));
        assertEquals(EstadoReserva.PENDIENTE,
                leido.buscarReservaPorCodigo(reserva.getCodigo()).getEstado());
    }

    @Test
    void guardarDosVecesNoDuplicaLasReservas() {
        Evento evento = nuevoEvento();
        evento.reservar("est-1", AHORA);
        repositorio.guardar(evento);
        repositorio.guardar(evento);

        assertEquals(1, repositorio.buscar("e1").getReservas().size());
    }

    @Test
    void conservaLaConfirmacionYLaAsistencia() {
        Evento evento = nuevoEvento();
        Reserva reserva = evento.reservar("est-1", AHORA);
        reserva.confirmar(AHORA.plusMinutes(2));
        reserva.registrarIngreso(AHORA.plusDays(5));
        repositorio.guardar(evento);

        Reserva leida = repositorio.buscar("e1").buscarReservaPorCodigo(reserva.getCodigo());

        assertEquals(EstadoReserva.CONFIRMADA, leida.getEstado());
        assertEquals(AHORA.plusDays(5), leida.getAsistencia().getHora());
    }
}