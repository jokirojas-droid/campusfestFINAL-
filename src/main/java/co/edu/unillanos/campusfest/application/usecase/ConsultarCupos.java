package co.edu.unillanos.campusfest.application.usecase;

import co.edu.unillanos.campusfest.application.port.Reloj;
import co.edu.unillanos.campusfest.application.port.RepositorioEventos;
import co.edu.unillanos.campusfest.domain.Evento;
import java.time.LocalDateTime;

public class ConsultarCupos {

    private final RepositorioEventos repositorio;
    private final Reloj reloj;

    public ConsultarCupos(RepositorioEventos repositorio, Reloj reloj) {
        this.repositorio = repositorio;
        this.reloj = reloj;
    }

    public int consultar(String eventoId) {
        Evento evento = repositorio.buscar(eventoId);
        if (evento == null) {
            throw new IllegalArgumentException("Evento no encontrado: " + eventoId);
        }
        LocalDateTime ahora = reloj.ahora();
        return evento.cuposDisponibles(ahora);
    }
}