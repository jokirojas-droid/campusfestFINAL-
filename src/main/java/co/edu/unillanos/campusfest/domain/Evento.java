package co.edu.unillanos.campusfest.domain;

import java.time.LocalDateTime;

public class Evento {
    public Evento(String id, String nombre, LocalDateTime fecha, String lugar,
                  int aforo, int bloqueados) {
        throw new UnsupportedOperationException();
    }

    public int cuposDisponibles(LocalDateTime ahora) {
        throw new UnsupportedOperationException();
    }

    public Reserva reservar(String estudianteId, LocalDateTime ahora) {
        throw new UnsupportedOperationException();
    }

    public Reserva buscarReservaPorCodigo(String codigo) {
        throw new UnsupportedOperationException();
    }

    public void cambiarDatos(String nombre, LocalDateTime fecha, String lugar) {
        throw new UnsupportedOperationException();
    }

    public void cambiarAforo(int nuevoAforo, LocalDateTime ahora) {
        throw new UnsupportedOperationException();
    }

    public String getId() {
        throw new UnsupportedOperationException();
    }

    public String getNombre() {
        throw new UnsupportedOperationException();
    }

    public LocalDateTime getFecha() {
        throw new UnsupportedOperationException();
    }

    public String getLugar() {
        throw new UnsupportedOperationException();
    }

    public int getAforo() {
        throw new UnsupportedOperationException();
    }
}