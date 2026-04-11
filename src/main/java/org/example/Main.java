package org.example;

import org.example.repository.ProdutoRepo;
import org.example.service.EstoqueService;
import org.example.service.PedidoService;
import org.example.ui.Menu;

public class Main {
    public static void main(String[] args) {

        ProdutoRepo produtoRepo = new ProdutoRepo();
        EstoqueService estoqueService = new EstoqueService(produtoRepo);

        PedidoService pedidoService = new PedidoService(estoqueService);

        Menu menu = new Menu(estoqueService);
        menu.iniciar();
    }
}