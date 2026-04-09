package org.example;

import org.example.database.Conexao;
import org.example.repository.ProdutoRepo;
import org.example.service.EstoqueService;
import org.example.ui.Menu;

public class Main {
    public static void main(String[] args) {

        Conexao.rodarMigrations();

        ProdutoRepo produtoRepo = new ProdutoRepo();
        EstoqueService estoqueService = new EstoqueService(produtoRepo);
        Menu menu = new Menu(estoqueService);

        menu.iniciar();
    }
}