package co.edu.unillanos.campusfest.infrastructure.persistence;

import co.edu.unillanos.campusfest.application.port.RepositorioEventos;
import co.edu.unillanos.campusfest.domain.Evento;
import java.sql.Connection;

public class RepositorioEventosH2 implements RepositorioEventos {

    public RepositorioEventosH2(Connection conexion) {
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