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
            System.out.println("5. Atualizar estoque");
            System.out.println("6. Atualizar preço");
            System.out.println("7. Alerta estoque baixo");
            System.out.println("0. Sair");
            System.out.print("Escolha: ");

            opcao = lerInt();

            switch (opcao) {
                case 1 -> cadastrarProduto();
                case 2 -> listarProdutos();
                case 3 -> buscarProduto();
                case 4 -> removerProduto();
                case 5 -> atualizarEstoque();
                case 6 -> atualizarPreco();
                case 7 -> alertarEstoqueBaixo();
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

        int tipoOpcao = 0;
        while (tipoOpcao != 1 && tipoOpcao != 2) {
            System.out.println("Tipo: 1-BEBIDA  2-COMIDA");
            System.out.print("Tipo: ");
            tipoOpcao = lerInt();
            if (tipoOpcao != 1 && tipoOpcao != 2)
                System.out.println("Opção inválida, digite 1 ou 2.");
        }
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
        listarProdutos();
        int id = lerInt("ID do produto a remover: ");
        estoqueService.removerProduto(id);
    }

    private void atualizarEstoque() {
        listarProdutos();
        int id = lerInt("ID do produto: ");
        Produto p = estoqueService.buscarPorId(id);
        if (p == null) {
            System.out.println("Produto não encontrado.");
            return;
        }
        System.out.println("Estoque atual de " + p.getNome() + ": " + p.getQuantidade());
        int novaQtd = lerInt("Nova quantidade: ");
        estoqueService.atualizarEstoque(id, novaQtd);
    }

    private void atualizarPreco() {
        listarProdutos();
        int id = lerInt("ID do produto: ");
        Produto p = estoqueService.buscarPorId(id);
        if (p == null) {
            System.out.println("Produto não encontrado.");
            return;
        }
        System.out.println("Preço atual de " + p.getNome() + ": R$ " + p.getPreco());
        System.out.print("Novo preço: R$ ");
        double novoPreco = scanner.nextDouble();
        scanner.nextLine();
        estoqueService.atualizarPreco(id, novoPreco);
    }

    private void alertarEstoqueBaixo() {
        int minimo = lerInt("Quantidade mínima para alerta: ");
        estoqueService.alertarEstoqueBaixo(minimo);
    }

    private void listarProdutos(String cabecalho) {
        System.out.println(cabecalho);
        listarProdutos();
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

    private int lerInt(String mensagem) {
        System.out.print(mensagem);
        return lerInt();
    }
}