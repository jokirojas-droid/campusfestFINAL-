package co.edu.unillanos.campusfest.infrastructure.persistence;

import co.edu.unillanos.campusfest.application.port.RepositorioEventos;
import co.edu.unillanos.campusfest.domain.Evento;

public class RepositorioEventosEnMemoria implements RepositorioEventos {
    public RepositorioEventosEnMemoria() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Evento buscar(String eventoId) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void guardar(Evento evento) {
        throw new UnsupportedOperationException();
    }
}