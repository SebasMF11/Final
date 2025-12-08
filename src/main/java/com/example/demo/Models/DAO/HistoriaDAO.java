package com.example.demo.Models.DAO;

import java.util.List;

import com.example.demo.Models.Entity.Historia;

public interface HistoriaDAO {

    Historia findById(long id);

    List<Historia> findAll();

    Historia save(Historia historia);

    void delete(long id);

    List<Historia> findByIdCitaL(List<Long> idsCitas);

    List<Historia> findByIdPaciente(long idPaciente);

    List<Historia> findByIdDoctor(long idDoctor);

    List<Historia> findByIdCita(Long idCita);
}
