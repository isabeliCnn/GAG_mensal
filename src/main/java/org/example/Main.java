package org.example;

import org.example.repository.ProdutoRepo;
import org.example.service.EstoqueService;
import org.example.ui.Menu;

public class Main {
    public static void main(String[] args) {

        // Cria o repository (aqui é ArrayList; no Mensal 2 vira JDBC)
        ProdutoRepo produtoRepo = new ProdutoRepo();

        // Cria o service passando o repository
        EstoqueService estoqueService = new EstoqueService(produtoRepo);

        // Cria o menu passando o service
        Menu menu = new Menu(estoqueService);

        // Liga tudo
        menu.iniciar();
    }
}