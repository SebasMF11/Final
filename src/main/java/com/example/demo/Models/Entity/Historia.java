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

    private long IdCita;
    private String descripcion;

    @CreationTimestamp
    @Column(name = "creation_date", nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date CreateAt;

    public Historia(long IdPaciente, long IdCita, Date CreateAt, String descripcion) {
        this.CreateAt = CreateAt;
        this.IdCita = IdCita;
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

    public long getIdCita() {
        return IdCita;
    }

    public void setIdPaciente(long IdCita) {
        this.IdCita = IdCita;
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
