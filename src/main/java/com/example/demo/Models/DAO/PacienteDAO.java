package com.example.demo.Models.DAO;

import java.util.List;

import com.example.demo.Models.Entity.Paciente;

public interface PacienteDAO {

    Paciente findById(long id);

    List<Paciente> findAll();

    Paciente save(Paciente paciente);

    void delete(long id);

    Paciente findByCorreo(String correo);

    Paciente login(String correo, String contraseña);

    List<Paciente> findByNombre(String nombre);

    List<Paciente> findByApellido(String apellido);
}
