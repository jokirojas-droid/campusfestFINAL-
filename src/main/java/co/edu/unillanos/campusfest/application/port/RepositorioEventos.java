package co.edu.unillanos.campusfest.application.port;

import co.edu.unillanos.campusfest.domain.Evento;

public interface RepositorioEventos {
    Evento buscar(String eventoId);
    void guardar(Evento evento);
}