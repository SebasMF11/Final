package com.example.demo.Models.DAO;

import com.example.demo.Models.Entity.Usuario;

public interface UsuarioDAO {

    Usuario findById(Long id);

    Usuario findByCorreo(String correo);

    Usuario login(String correo, String contraseña);

    Usuario save(Usuario usuario);

    void delete(Long id);
}
