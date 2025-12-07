package com.example.demo.Models.DAO;

import org.springframework.stereotype.Repository;

import com.example.demo.Models.Entity.Usuario;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@Repository
@Transactional
public class UsuarioDAOImpl implements UsuarioDAO {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Usuario findById(Long id) {
        return em.find(Usuario.class, id);
    }

    @Override
    public Usuario findByCorreo(String correo) {
        return em.createQuery(
                "FROM Usuario u WHERE u.correo = :correo", Usuario.class)
                .setParameter("correo", correo)
                .getResultStream()
                .findFirst()
                .orElse(null);
    }

    @Override
    public Usuario login(String correo, String contraseña) {
        return em.createQuery(
                "FROM Usuario u WHERE u.correo = :correo AND u.contraseña = :contraseña",
                Usuario.class
        )
        .setParameter("correo", correo)
        .setParameter("contraseña", contraseña)
        .getResultStream()
        .findFirst()
        .orElse(null);
    }

    @Override
    public Usuario save(Usuario usuario) {
        if (usuario.getId() == null) {
            em.persist(usuario);
            return usuario;
        } else {
            return em.merge(usuario);
        }
    }

    @Override
    public void delete(Long id) {
        Usuario u = findById(id);
        if (u != null) {
            em.remove(u);
        }
    }
}
