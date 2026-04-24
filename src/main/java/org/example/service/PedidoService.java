package org.example.service;

import org.example.model.ItemPedido;
import org.example.model.Pedido;
import org.example.model.Produto;
import org.example.repository.PedidoRepo;

import java.util.List;

public class PedidoService {

    private final PedidoRepo pedidoRepo;
    private final EstoqueService estoqueService;

    public PedidoService(EstoqueService estoqueService) {
        this.pedidoRepo = new PedidoRepo();
        this.estoqueService = estoqueService;
    }

    public Pedido criarPedido() {
        return new Pedido();
    }

    public boolean adicionarItem(Pedido pedido, Produto produto, int quantidade) {
        pedido.adicionarItem(produto, quantidade);
        return false;
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

        pedidoRepo.salvar(pedido);

        for (ItemPedido item : pedido.getItens()) {
            estoqueService.baixarEstoque(item.getProduto(), item.getQuantidadePedida());
        }

        return true;
    }

    public Pedido buscarPorId(int id) {
        return pedidoRepo.buscarPorId(id);
    }

    public List<Pedido> listarTodos() {
        return pedidoRepo.buscarTodos();
    }
}