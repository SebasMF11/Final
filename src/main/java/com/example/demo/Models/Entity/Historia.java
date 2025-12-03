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
@Table (name = "Historias")
public class Historia {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private long id;

    private long IdPaciente;
    private long IdDoctor;
    private String descripcion;

    @CreationTimestamp
    @Column(name = "creation_date", nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date CreateAt;

    public Historia(long IdPaciente, long IdDoctor, Date CreateAt, String descripcion) {
        this.CreateAt = CreateAt;
        this.IdDoctor = IdDoctor;
        this.IdPaciente = IdPaciente;
        this.descripcion = descripcion;
    }

    public Historia() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getIdPaciente() {
        return IdPaciente;
    }

    public void setIdPaciente(long IdPaciente) {
        this.IdPaciente = IdPaciente;
    }

    public long getIdDoctor() {
        return IdDoctor;
    }

    public void setIdDoctor(long IdDoctor) {
        this.IdDoctor = IdDoctor;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Date getCreateAt() {
        return CreateAt;
    }

    public void setCreateAt(Date CreateAt) {
        this.CreateAt = CreateAt;
    }


}
