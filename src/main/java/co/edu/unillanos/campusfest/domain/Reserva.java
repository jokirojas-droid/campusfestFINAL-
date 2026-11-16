package co.edu.unillanos.campusfest.domain;

import java.time.LocalDateTime;

public class Reserva {
    public static Reserva pendiente(String estudianteId, LocalDateTime ahora) {
        throw new UnsupportedOperationException();
    }

    public boolean ocupaCupo(LocalDateTime ahora) {
        throw new UnsupportedOperationException();
    }

    public void confirmar(LocalDateTime ahora) {
        throw new UnsupportedOperationException();
    }

    public void cancelar() {
        throw new UnsupportedOperationException();
    }

    public Asistencia registrarIngreso(LocalDateTime ahora) {
        throw new UnsupportedOperationException();
    }

    public String getCodigo() {
        throw new UnsupportedOperationException();
    }

    public String getEstudianteId() {
        throw new UnsupportedOperationException();
    }

    public LocalDateTime getVenceEn() {
        throw new UnsupportedOperationException();
    }

    public EstadoReserva getEstado() {
        throw new UnsupportedOperationException();
    }

    public Asistencia getAsistencia() {
        throw new UnsupportedOperationException();
    }
    public static Reserva reconstruir(String codigo, String estudianteId, LocalDateTime venceEn,EstadoReserva estado, Asistencia asistencia) {
        throw new UnsupportedOperationException();
    }
}