package co.edu.unillanos.campusfest.domain;

import java.time.LocalDateTime;

public class Asistencia {
    private final LocalDateTime hora;

    public Asistencia(LocalDateTime hora) {
        this.hora = hora;
    }

    public LocalDateTime getHora() {
        return hora;
    }
}
