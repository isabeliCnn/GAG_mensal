package org.example.ui;

import org.example.model.Produto;
import org.example.model.TipoProduto;
import org.example.service.EstoqueService;
import java.util.ArrayList;
import java.util.Scanner;

public class Menu {

    private EstoqueService estoqueService;
    private Scanner scanner;

    public Menu(EstoqueService estoqueService) {
        this.estoqueService = estoqueService;
        this.scanner = new Scanner(System.in);
    }

    public void iniciar() {
        int opcao = -1;
        while (opcao != 0) {
            System.out.println("\n=== BAR/RESTAURANTE — ESTOQUE ===");
            System.out.println("1. Cadastrar produto");
            System.out.println("2. Listar todos");
            System.out.println("3. Buscar por nome");
            System.out.println("4. Remover produto");
            System.out.println("0. Sair");
            System.out.print("Escolha: ");

            opcao = lerInt();

            switch (opcao) {
                case 1 -> cadastrarProduto();
                case 2 -> listarProdutos();
                case 3 -> buscarProduto();
                case 4 -> removerProduto();
                case 0 -> System.out.println("Encerrando...");
                default -> System.out.println("Opção inválida.");
            }
        }
    }

    private void cadastrarProduto() {
        System.out.print("Nome: ");
        String nome = scanner.nextLine();

        System.out.print("Preço: ");
        double preco = scanner.nextDouble();
        scanner.nextLine();

        System.out.print("Quantidade em estoque: ");
        int quantidade = scanner.nextInt();
        scanner.nextLine();

        System.out.println("Tipo: 1-BEBIDA  2-COMIDA");
        System.out.print("Tipo: ");
        int tipoOpcao = scanner.nextInt();
        scanner.nextLine();
        TipoProduto tipo = (tipoOpcao == 1) ? TipoProduto.BEBIDA : TipoProduto.COMIDAS;

        Produto p = new Produto(nome, preco, quantidade, tipo);
        estoqueService.adicionarProduto(p);
    }

    private void listarProdutos() {
        ArrayList<Produto> lista = estoqueService.listarTodos();
        if (lista.isEmpty()) {
            System.out.println("Nenhum produto cadastrado.");
            return;
        }
        System.out.println("\n--- Produtos ---");
        for (Produto p : lista) {
            System.out.println(p);
        }
    }

    private void buscarProduto() {
        System.out.print("Nome: ");
        String nome = scanner.nextLine();
        Produto p = estoqueService.buscarPorNome(nome);
        if (p != null) System.out.println("Encontrado: " + p);
    }

    private void removerProduto() {
        System.out.print("ID do produto: ");
        int id = lerInt();
        estoqueService.removerProduto(id);
    }

    private int lerInt() {
        while (!scanner.hasNextInt()) {
            System.out.print("Digite um número: ");
            scanner.next();
        }
        int valor = scanner.nextInt();
        scanner.nextLine();
        return valor;
    }
}
