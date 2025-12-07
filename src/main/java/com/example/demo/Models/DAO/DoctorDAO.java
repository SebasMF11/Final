package com.example.demo.Models.DAO;

import java.time.LocalTime;
import java.util.List;

import com.example.demo.Models.Entity.Doctor;

public interface DoctorDAO {

    Doctor findById(long id);

    List<Doctor> findAll();

    Doctor save(Doctor doctor);

    void delete(long id);

    Doctor findByCorreo(String correo);

    Doctor login(String correo, String contraseña);

    List<Doctor> findByEspecialidad(String especialidad);

    List<Doctor> findByEstado(String estado);

    List<Doctor> findAvailableBetween(LocalTime inicio, LocalTime fin);
}
