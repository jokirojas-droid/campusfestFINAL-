package co.edu.unillanos.campusfest.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Evento {
    private final String id;
    private String nombre;
    private LocalDateTime fecha;
    private String lugar;
    private int aforo;
    private final int bloqueados;
    private final List<Reserva> reservas;

    public Evento(String id, String nombre, LocalDateTime fecha, String lugar,
                  int aforo, int bloqueados) {
        this(id, nombre, fecha, lugar, aforo, bloqueados, List.of());
    }

    public Evento(String id, String nombre, LocalDateTime fecha, String lugar,
                  int aforo, int bloqueados, List<Reserva> reservas) {
        this.id = id;
        this.nombre = nombre;
        this.fecha = fecha;
        this.lugar = lugar;
        this.aforo = aforo;
        this.bloqueados = bloqueados;
        this.reservas = new ArrayList<>(reservas);
    }

    public int cuposDisponibles(LocalDateTime ahora) {
        return aforo - cuposOcupados(ahora);
    }

    public Reserva reservar(String estudianteId, LocalDateTime ahora) {
        if (cuposDisponibles(ahora) <= 0) {
            throw new IllegalStateException("El evento no tiene cupos disponibles.");
        }
        Reserva reserva = Reserva.pendiente(estudianteId, ahora);
        reservas.add(reserva);
        return reserva;
    }

    public Reserva buscarReservaPorCodigo(String codigo) {
        return reservas.stream()
                .filter(reserva -> reserva.getCodigo().equals(codigo))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("El código no pertenece al evento."));
    }

    public void cambiarDatos(String nombre, LocalDateTime fecha, String lugar) {
        this.nombre = nombre;
        this.fecha = fecha;
        this.lugar = lugar;
    }

    public void cambiarAforo(int nuevoAforo, LocalDateTime ahora) {
        int ocupados = cuposOcupados(ahora);
        if (nuevoAforo < ocupados) {
            throw new IllegalStateException("El aforo debe cubrir " + (ocupados - bloqueados)
                    + " cupos ocupados por reservas y " + bloqueados + " cupos bloqueados.");
        }
        aforo = nuevoAforo;
    }

    private int cuposOcupados(LocalDateTime ahora) {
        int ocupados = bloqueados;
        for (Reserva reserva : reservas) {
            if (reserva.ocupaCupo(ahora)) {
                ocupados++;
            }
        }
        return ocupados;
    }

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public String getLugar() {
        return lugar;
    }

    public int getAforo() {
        return aforo;
    }

    public int getBloqueados() {
        return bloqueados;
    }

    public List<Reserva> getReservas() {
        return List.copyOf(reservas);
    }
}
