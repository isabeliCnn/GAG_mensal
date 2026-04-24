package org.example.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Persistence;
import org.example.model.Ficha;

import java.util.List;
import java.util.UUID;

public class FichaRepository {

    private EntityManager em;

    public FichaRepository() {
        em = Persistence.createEntityManagerFactory("meuPU").createEntityManager();
    }

    public boolean salvar(Ficha ficha) {
        try {
            em.getTransaction().begin();
            em.persist(ficha);
            em.getTransaction().commit();

            System.out.println("Ficha salva: " + ficha.getId());
            return true;

        } catch (Exception e) {
            System.out.println("Erro ao salvar ficha: " + e.getMessage());
            return false;
        }
    }

    public Ficha buscarPorId(UUID id) {
        return em.find(Ficha.class, id);
    }

    public List<Ficha> listarTodas() {
        return em.createQuery("FROM Ficha", Ficha.class)
                .getResultList();
    }

    public List<Ficha> listarAbertas() {
        return em.createQuery(
                "SELECT f FROM Ficha f WHERE f.usada = false",
                Ficha.class
        ).getResultList();
    }

    public boolean marcarUsada(UUID id) {
        Ficha ficha = buscarPorId(id);

        if (ficha == null) return false;

        em.getTransaction().begin();
        ficha.fechar();
        em.merge(ficha);
        em.getTransaction().commit();

        System.out.println("Ficha fechada: " + id);
        return true;
    }
}