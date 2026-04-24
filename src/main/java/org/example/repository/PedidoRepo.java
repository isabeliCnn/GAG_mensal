package org.example.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Persistence;
import org.example.model.Pedido;

import java.util.List;

public class PedidoRepo {

    private EntityManager em;

    public PedidoRepo() {
        em = Persistence.createEntityManagerFactory("meuPU").createEntityManager();
    }

    public void salvar(Pedido pedido) {
        em.getTransaction().begin();
        em.persist(pedido);
        em.getTransaction().commit();
    }

    public Pedido buscarPorId(int id) {
        return em.find(Pedido.class, id);
    }

    public List<Pedido> buscarTodos() {
        return em.createQuery("FROM Pedido", Pedido.class).getResultList();
    }

    public void atualizar(Pedido pedido) {
        em.getTransaction().begin();
        em.merge(pedido);
        em.getTransaction().commit();
    }
}