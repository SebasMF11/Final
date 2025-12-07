package com.example.demo.Models.DAO;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.example.demo.Models.Entity.Historia;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@Repository
@Transactional
public class HistoriaDAOImpl implements HistoriaDAO {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Historia findById(long id) {
        return em.find(Historia.class, id);
    }

    @Override
    public List<Historia> findAll() {
        return em.createQuery("FROM Historia", Historia.class).getResultList();
    }

    @Override
    public void save(Historia historia) {
        if (historia.getId() == 0) {
            em.persist(historia);
        } else {
            em.merge(historia);
        }
    }

    @Override
    public void delete(long id) {
        Historia h = findById(id);
        if (h != null) em.remove(h);
    }

    @Override
    public List<Historia> findByIdPaciente(long idPaciente) {
        return em.createQuery(
                "FROM Historia h WHERE h.IdPaciente = :idPaciente", Historia.class)
                .setParameter("idPaciente", idPaciente)
                .getResultList();
    }

    @Override
    public List<Historia> findByIdDoctor(long idDoctor) {
        return em.createQuery(
                "FROM Historia h WHERE h.IdDoctor = :idDoctor", Historia.class)
                .setParameter("idDoctor", idDoctor)
                .getResultList();
    }

    @Override
    public List<Historia> searchByDescripcion(String texto) {
        return em.createQuery(
                "FROM Historia h WHERE h.descripcion LIKE :texto", Historia.class)
                .setParameter("texto", "%" + texto + "%")
                .getResultList();
    }
}
