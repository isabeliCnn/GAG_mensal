package org.example;

import org.example.database.Conexao;
import org.example.repository.ProdutoRepo;
import org.example.repository.ProdutoRepositoryJPA;
import org.example.service.EstoqueService;
import org.example.ui.Menu;

public class Main {
    public static void main(String[] args) {

        Conexao.rodarMigrations();

        ProdutoRepositoryJPA jpa = new ProdutoRepositoryJPA();
        System.out.println("\n--- Carga inicial via JPA/Hibernate ---");
        jpa.buscarTodos().forEach(System.out::println);
        System.out.println("---------------------------------------\n");

        ProdutoRepo produtoRepo = new ProdutoRepo();
        EstoqueService estoqueService = new EstoqueService(produtoRepo);
        Menu menu = new Menu(estoqueService);
        menu.iniciar();
    }
}