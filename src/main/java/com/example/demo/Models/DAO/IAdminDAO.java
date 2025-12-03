package com.example.demo.Models.DAO;

import com.example.demo.Models.Entity.Admin;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public class IAdminDAO implements AdminDAO {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Admin findById(long id) {
        return em.find(Admin.class, id);
    }

    @Override
    public List<Admin> findAll() {
        return em.createQuery("FROM Admin", Admin.class).getResultList();
    }

    @Override
    public void save(Admin admin) {
        if (admin.getId() == 0) {
            em.persist(admin);
        } else {
            em.merge(admin);
        }
    }

    @Override
    public void delete(long id) {
        Admin admin = findById(id);
        if (admin != null) {
            em.remove(admin);
        }
    }

    @Override
    public Admin findByCorreo(String correo) {
        return em.createQuery(
                "FROM Admin a WHERE a.correo = :correo", Admin.class)
                .setParameter("correo", correo)
                .getResultStream()
                .findFirst()
                .orElse(null);
    }

    @Override
    public Admin login(String correo, String contraseña) {
        return em.createQuery(
                "FROM Admin a WHERE a.correo = :correo AND a.contraseña = :contraseña",
                Admin.class)
                .setParameter("correo", correo)
                .setParameter("contraseña", contraseña)
                .getResultStream()
                .findFirst()
                .orElse(null);
    }
}
