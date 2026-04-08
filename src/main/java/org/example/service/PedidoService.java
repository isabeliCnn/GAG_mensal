package org.example.service;

import org.example.model.ItemPedido;
import org.example.model.Pedido;
import org.example.model.Produto;

import java.util.ArrayList;

public class PedidoService {

    private static final int QTDE_MAXIMA_POR_ITEM = 20;
    private static final double VALOR_MAXIMO_PEDIDO = 500.00;

    private EstoqueService estoqueService;

    public PedidoService(EstoqueService estoqueService) {
        this.estoqueService = estoqueService;
    }

    public Pedido criarPedido() {
        Pedido pedido = new Pedido();
        System.out.println("Pedido criado: [" + pedido.getId().toString().substring(0, 8) + "]");
        return pedido;
    }

    public Pedido criarPedido(String observacao) {
        Pedido pedido = new Pedido(observacao);
        System.out.println("Pedido criado: [" + pedido.getId().toString().substring(0, 8) + "]"
                + " | Obs: " + observacao);
        return pedido;
    }

    public boolean adicionarItem(Pedido pedido, Produto produto, int quantidade) {
        Pedido.StatusPedido status = pedido.getStatus();
        if (status == Pedido.StatusPedido.ENTREGUE || status == Pedido.StatusPedido.CANCELADO) {
            System.out.println("Não é possível adicionar itens a um pedido " + status + ".");
            return false;
        }
        if (quantidade <= 0) {
            System.out.println("Quantidade inválida: " + quantidade);
            return false;
        }
        if (quantidade > QTDE_MAXIMA_POR_ITEM) {
            System.out.printf("Quantidade %d acima do máximo (%d).%n",
                    quantidade, QTDE_MAXIMA_POR_ITEM);
            return false;
        }

        ItemPedido item = new ItemPedido(produto, quantidade);
        double totalProjetado = pedido.calcularTotal() + item.calcularSubtotal();

        if (totalProjetado > VALOR_MAXIMO_PEDIDO) {
            System.out.printf("Pedido ultrapassaria R$ %.2f (limite R$ %.2f).%n",
                    totalProjetado, VALOR_MAXIMO_PEDIDO);
            return false;
        }

        pedido.adicionarItem(item);
        System.out.printf("%dx %s | Subtotal: R$ %.2f%n",
                item.getQuantidadePedida(), produto.getNome(), item.calcularSubtotal());
        return true;
    }

    public boolean confirmarPedido(Pedido pedido) {
        if (pedido.getItens().isEmpty()) {
            System.out.println("Pedido vazio — adicione itens antes de confirmar.");
            return false;
        }

        for (ItemPedido item : pedido.getItens()) {
            boolean baixou = estoqueService.baixarEstoque(item.getProduto().getId(), item.getQuantidadePedida());
            if (!baixou) {
                System.out.printf("Estoque insuficiente para '%s'. Pedido não confirmado.%n",
                        item.getProduto().getNome());
                return false;
            }
        }

        System.out.println("Pedido confirmado e estoque atualizado.");
        return true;
    }

    public void avancarStatus(Pedido pedido) {
        Pedido.StatusPedido antes = pedido.getStatus();
        pedido.avancarStatus();
        System.out.printf("[%s] %s → %s%n",
                pedido.getId().toString().substring(0, 8), antes, pedido.getStatus());
    }

    public void cancelarPedido(Pedido pedido) {
        Pedido.StatusPedido statusAnterior = pedido.getStatus();
        pedido.cancelar();

        if (pedido.getStatus() == Pedido.StatusPedido.CANCELADO &&
                statusAnterior != Pedido.StatusPedido.PENDENTE) {
            for (ItemPedido item : pedido.getItens()) {
                Produto p = item.getProduto();
                p.setQuantidade(p.getQuantidade() + item.getQuantidadePedida());
                estoqueService.atualizarPreco(p.getId(), p.getPreco());
            }
            System.out.println("Estoque estornado.");
        }
    }
}