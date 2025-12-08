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

    private long IdDoctor;
    private long idCita;
    private String descripcion;

    @CreationTimestamp
    @Column(name = "creation_date", nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date CreateAt;

    public Historia(long IdCita, Date CreateAt, Long IdDoctor,String descripcion) {
        this.IdDoctor = IdDoctor;
        this.CreateAt = CreateAt;
        this.idCita = IdCita;
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
        return idCita;
    }

    public void setIdCita(long IdCita) {
        this.idCita = IdCita;
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

    public long getIdDoctor() {
        return IdDoctor;
    }

    public void setIdDoctor(long IdDoctor) {
        this.IdDoctor = IdDoctor;
    }


}
