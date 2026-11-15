package co.edu.unillanos.campusfest.infrastructure.persistence;

import co.edu.unillanos.campusfest.application.port.RepositorioEventos;
import co.edu.unillanos.campusfest.domain.Evento;
import java.util.HashMap;
import java.util.Map;

public class RepositorioEventosEnMemoria implements RepositorioEventos {

    private final Map<String, Evento> eventos = new HashMap<>();

    public RepositorioEventosEnMemoria() {
    }

    @Override
    public Evento buscar(String eventoId) {
        if (eventoId == null) {
            return null;
        }
        return eventos.get(eventoId);
    }

    @Override
    public void guardar(Evento evento) {
        if (evento == null) {
            throw new IllegalArgumentException("El evento no puede ser nulo");
        }
        eventos.put(evento.getId(), evento);
    }
}