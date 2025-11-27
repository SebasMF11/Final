package com.example.demo.Models.Entity;

import java.util.Date;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table (name = "Citas")
public class Cita {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private long id;
    private String estado;
    private long IdPaciente;
    private long IdDoctor;

    @CreationTimestamp
    @Column(name = "creation_date", nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date CreateAt;

    public Cita(long IdPaciente, long IdDoctor, String estado, Date CreateAt) {
        this.CreateAt = CreateAt;
        this.IdDoctor = IdDoctor;
        this.IdPaciente = IdPaciente;
        this.estado = estado;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
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

    public Date getCreateAt() {
        return CreateAt;
    }

    public void setCreateAt(Date createAt) {
        CreateAt = createAt;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
