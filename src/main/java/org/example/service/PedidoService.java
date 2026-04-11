package org.example.service;

import org.example.model.ItemPedido;
import org.example.model.Pedido;
import org.example.model.Pedido.StatusPedido;
import org.example.model.Produto;
import org.example.repository.PedidoRepo;

import java.util.ArrayList;

public class PedidoService {

    private static final int QTDE_MAXIMA_POR_ITEM = 20;

    private final PedidoRepo pedidoRepository;
    private final EstoqueService estoqueService;

    public PedidoService(EstoqueService estoqueService) {
        this.pedidoRepository = new PedidoRepo();
        this.estoqueService   = estoqueService;
    }

    public Pedido criarPedido() {
        Pedido pedido = new Pedido();
        System.out.println("Pedido criado.");
        return pedido;
    }

    public boolean adicionarItem(Pedido pedido, Produto produto, int quantidade) {
        if (quantidade <= 0 || quantidade > QTDE_MAXIMA_POR_ITEM) {
            System.out.println("Quantidade inválida.");
            return false;
        }

        ItemPedido item = new ItemPedido(produto, quantidade);


        pedido.adicionarItem(item);
        System.out.println("Item adicionado!");
        return true;
    }

    public boolean confirmarPedido(Pedido pedido) {
        if (pedido.getItens().isEmpty()) {
            System.out.println("Pedido vazio.");
            return false;
        }

        for (ItemPedido item : pedido.getItens()) {
            if (!estoqueService.temEstoque(item.getProduto(), item.getQuantidadePedida())) {
                System.out.println("Estoque insuficiente: " + item.getProduto().getNome());
                return false;
            }
        }

        boolean salvo = pedidoRepository.salvar(pedido);

        if (!salvo) return false;

        for (ItemPedido item : pedido.getItens()) {
            estoqueService.baixarEstoque(item.getProduto(), item.getQuantidadePedida());
        }

        return true;
    }
    public Pedido buscarPorId(int id) {
        return pedidoRepository.buscarPorId(id);
    }
    public ArrayList<Pedido> listarTodos() {
        return pedidoRepository.buscarTodos();
    }

    public void exibirTodos() {
        ArrayList<Pedido> lista = listarTodos();

        if (lista.isEmpty()) {
            System.out.println("Nenhum pedido.");
            return;
        }

        for (Pedido p : lista) {
            System.out.println(p);
            System.out.println("----------------------");
        }
    }
}