package co.edu.unillanos.campusfest.application.usecase;

import co.edu.unillanos.campusfest.application.port.Reloj;
import co.edu.unillanos.campusfest.application.port.RepositorioEventos;
import co.edu.unillanos.campusfest.domain.Reserva;

public class ReservarEntrada {
    public ReservarEntrada(RepositorioEventos repositorio, Reloj reloj) {
        throw new UnsupportedOperationException();
    }

    public Reserva solicitarReserva(String eventoId, String estudianteId) {
        throw new UnsupportedOperationException();
    }

    public Reserva confirmarReserva(String eventoId, String codigoReserva) {
        throw new UnsupportedOperationException();
    }
}