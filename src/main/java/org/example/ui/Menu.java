package org.example.ui;

import org.example.model.*;
import org.example.repository.FichaRepository;
import org.example.service.*;

import java.util.ArrayList;
import java.util.Scanner;

public class Menu {

    private EstoqueService estoqueService;
    private PedidoService pedidoService;
    private FichaService fichaService;
    private Scanner scanner;

    public Menu(EstoqueService estoqueService) {
        this.estoqueService = estoqueService;
        this.pedidoService = new PedidoService(estoqueService);
        this.fichaService = new FichaService(new FichaRepository(), pedidoService);
        this.scanner = new Scanner(System.in);
    }

    public void iniciar() {
        int opcao = -1;

        while (opcao != 0) {
            System.out.println("\n=== SISTEMA BAR/RESTAURANTE ===");
            System.out.println("1. Cadastrar produto");
            System.out.println("2. Listar produtos");
            System.out.println("3. Buscar produto");
            System.out.println("4. Remover produto");

            System.out.println("\n--- PEDIDOS ---");
            System.out.println("5. Iniciar atendimento (Novo Pedido)");

            System.out.println("0. Sair");
            System.out.print("Escolha: ");

            opcao = lerInt();

            switch (opcao) {
                case 1 -> cadastrarProduto();
                case 2 -> listarProdutos();
                case 3 -> buscarProduto();
                case 4 -> removerProduto();
                case 5 -> fluxoPedidoCompleto();
                case 0 -> System.out.println("Encerrando...");
                default -> System.out.println("Opção inválida.");
            }
        }
    }

    // ================= FLUXO DE PEDIDO =================

    private void fluxoPedidoCompleto() {
        Pedido pedido = pedidoService.criarPedido();

        boolean continuar = true;

        while (continuar) {
            mostrarCardapio();

            System.out.print("\nDigite o ID do produto: ");
            int idProduto = lerInt();

            Produto produto = estoqueService.buscarPorId(idProduto);
            if (produto == null) {
                System.out.println("Produto não encontrado.");
                continue;
            }

            System.out.print("Quantidade: ");
            int qtd = lerInt();

            boolean ok = pedidoService.adicionarItem(pedido, produto, qtd);

            if (!ok) continue;

            System.out.print("Deseja adicionar mais itens? (s/n): ");
            String resp = scanner.nextLine();

            continuar = resp.equalsIgnoreCase("s");
        }

        boolean confirmado = pedidoService.confirmarPedido(pedido);

        if (confirmado) {
            System.out.println("Pedido confirmado!");

            // 🔥 CRIA FICHA AUTOMATICAMENTE
            fichaService.abrirFicha(pedido.getId());
        }
    }

    // ================= CARDÁPIO BONITO =================

    private void mostrarCardapio() {
        ArrayList<Produto> produtos = estoqueService.listarTodos();

        System.out.println("\n========== CARDÁPIO ==========");
        System.out.printf("%-40s | %-40s%n", "COMIDAS", "BEBIDAS");
        System.out.println("--------------------------------------------------------------------------");

        ArrayList<Produto> comidas = new ArrayList<>();
        ArrayList<Produto> bebidas = new ArrayList<>();

        for (Produto p : produtos) {
            if (p.getTipo() == TipoProduto.COMIDAS) comidas.add(p);
            else bebidas.add(p);
        }

        int max = Math.max(comidas.size(), bebidas.size());

        for (int i = 0; i < max; i++) {

            String comidaStr = "";
            String bebidaStr = "";

            if (i < comidas.size()) {
                Produto c = comidas.get(i);
                comidaStr = String.format("[%d] %s (R$ %.2f | %d)",
                        c.getId(), c.getNome(), c.getPreco(), c.getQuantidade());
            }

            if (i < bebidas.size()) {
                Produto b = bebidas.get(i);
                bebidaStr = String.format("[%d] %s (R$ %.2f | %d)",
                        b.getId(), b.getNome(), b.getPreco(), b.getQuantidade());
            }

            System.out.printf("%-40s | %-40s%n", comidaStr, bebidaStr);
        }

        System.out.println("--------------------------------------------------------------------------");
    }

    // ================= PRODUTOS =================

    private void cadastrarProduto() {
        System.out.print("Nome: ");
        String nome = scanner.nextLine();

        System.out.print("Preço: ");
        double preco = scanner.nextDouble();
        scanner.nextLine();

        System.out.print("Quantidade: ");
        int quantidade = scanner.nextInt();
        scanner.nextLine();

        System.out.println("Tipo: 1-BEBIDA  2-COMIDA");
        int tipoOpcao = scanner.nextInt();
        scanner.nextLine();

        TipoProduto tipo = (tipoOpcao == 1) ? TipoProduto.BEBIDA : TipoProduto.COMIDAS;

        Produto p = new Produto(nome, preco, quantidade, tipo);
        estoqueService.adicionarProduto(p);
    }

    private void listarProdutos() {
        ArrayList<Produto> lista = estoqueService.listarTodos();

        if (lista.isEmpty()) {
            System.out.println("Nenhum produto.");
            return;
        }

        for (Produto p : lista) {
            System.out.println(p);
        }
    }

    private void buscarProduto() {
        System.out.print("Buscar por (1-ID | 2-Nome): ");
        int opcao = lerInt();

        if (opcao == 1) {
            System.out.print("ID: ");
            int id = lerInt();

            Produto p = estoqueService.buscarPorId(id);

            if (p != null) {
                System.out.println(p);
            } else {
                System.out.println("❌ Produto não encontrado.");
            }

        } else if (opcao == 2) {
            System.out.print("Nome: ");
            String nome = scanner.nextLine();

            Produto p = estoqueService.buscarPorNome(nome);

            if (p != null) {
                System.out.println(p);
            }
        } else {
            System.out.println("Opção inválida.");
        }
    }

    private void removerProduto() {
        System.out.println("\n=== REMOVER PRODUTO ===");

        listarProdutos(); // 👈 MOSTRA TODOS ANTES

        System.out.print("\nDigite o ID do produto: ");
        int id = lerInt();

        Produto produto = estoqueService.buscarPorId(id);

        if (produto == null) {
            System.out.println("❌ Produto não encontrado.");
            return;
        }

        System.out.println("Produto encontrado:");
        System.out.println(produto);

        System.out.print("Tem certeza que deseja remover? (s/n): ");
        String confirmacao = scanner.nextLine();

        if (confirmacao.equalsIgnoreCase("s")) {
            estoqueService.removerProduto(id);
            System.out.println("✅ Produto removido com sucesso!");
        } else {
            System.out.println("❌ Remoção cancelada.");
        }
    }

    // ================= AUX =================

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