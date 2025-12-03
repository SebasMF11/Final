package com.example.demo.Models.DAO;

import java.util.List;

import com.example.demo.Models.Entity.Cita;

public interface CitaDAO {

    Cita findById(long id);

    List<Cita> findAll();

    void save(Cita cita);

    void delete(long id);

    List<Cita> findByIdPaciente(long idPaciente);

    List<Cita> findByIdDoctor(long idDoctor);

    List<Cita> findByEstado(String estado);
}
