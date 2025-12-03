package com.example.demo.Models.DAO;

import java.util.List;

import com.example.demo.Models.Entity.Admin;

public interface AdminDAO {

    Admin findById(long id);

    List<Admin> findAll();

    void save(Admin admin);

    void delete(long id);

    Admin findByCorreo(String correo);

    Admin login(String correo, String contraseña);
}
