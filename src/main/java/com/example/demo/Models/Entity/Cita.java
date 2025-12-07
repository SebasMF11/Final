package com.example.demo.Models.Entity;

import java.time.LocalDate;
import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table (name = "Citas")
public class Cita {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private long id;
    private long IdPaciente;
    private long IdDoctor;

    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate fecha;

    @JsonFormat(pattern = "HH:mm")
    private LocalTime hora;

    public Cita() {
    }

    public Cita(long IdDoctor, long IdPaciente, LocalDate fecha, LocalTime hora) {
        this.IdDoctor = IdDoctor;
        this.IdPaciente = IdPaciente;
        this.fecha = fecha;
        this.hora = hora;
    }

    public long getIdPaciente() {
        return IdPaciente;
    }

    public void setIdPaciente(long idPaciente) {
        IdPaciente = idPaciente;
    }

    public long getIdDoctor() {
        return IdDoctor;
    }

    public void setIdDoctor(long idDoctor) {
        IdDoctor = idDoctor;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public LocalTime getHora() {
        return hora;
    }

    public void setHora(LocalTime hora) {
        this.hora = hora;
    }
}
