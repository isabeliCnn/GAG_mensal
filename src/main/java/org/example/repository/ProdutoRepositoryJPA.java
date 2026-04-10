package org.example.repository;

import org.example.database.HibernateUtil;
import org.example.model.Produto;
import org.hibernate.Session;
import java.util.List;

public class ProdutoRepositoryJPA {

    public List<Produto> buscarTodos() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Produto", Produto.class).list();
        }
    }

    public Produto buscarPorId(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Produto.class, id);
        }
    }

    public void salvar(Produto p) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            session.persist(p);
            session.getTransaction().commit();
        }
    }

    public void atualizar(Produto p) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            session.merge(p);
            session.getTransaction().commit();
        }
    }

    public void deletar(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            Produto p = session.get(Produto.class, id);
            if (p != null) session.remove(p);
            session.getTransaction().commit();
        }
    }
}