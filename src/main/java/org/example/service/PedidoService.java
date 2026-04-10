package org.example.service;

import org.example.model.ItemPedido;
import org.example.model.Pedido;
import org.example.model.Pedido.StatusPedido;
import org.example.model.Produto;
import org.example.repository.PedidoRepo;

import java.util.ArrayList;

public class PedidoService {

    private static final int    QTDE_MAXIMA_POR_ITEM = 20;
    private static final double VALOR_MAXIMO_PEDIDO  = 500.00;

    private final PedidoRepo pedidoRepository;
    private final EstoqueService   estoqueService;

    public PedidoService() {
        this.pedidoRepository = new PedidoRepo();
        this.estoqueService   = null;
    }

    public PedidoService(EstoqueService estoqueService) {
        this.pedidoRepository = new PedidoRepo();
        this.estoqueService   = estoqueService;
    }

    public Pedido criarPedido() {
        Pedido pedido = new Pedido();
        System.out.println("Pedido criado (ainda não salvo no banco).");
        return pedido;
    }

    public Pedido criarPedido(String observacao) {
        Pedido pedido = new Pedido(observacao);
        System.out.println("Pedido criado | Obs: " + observacao);
        return pedido;
    }

    public boolean adicionarItem(Pedido pedido, Produto produto, int quantidade) {
        StatusPedido status = pedido.getStatus();
        if (status == StatusPedido.ENTREGUE || status == StatusPedido.CANCELADO) {
            System.out.println("Não é possível adicionar itens a um pedido " + status + ".");
            return false;
        }
        if (quantidade <= 0) {
            System.out.println("Quantidade inválida: " + quantidade);
            return false;
        }
        if (quantidade > QTDE_MAXIMA_POR_ITEM) {
            System.out.printf("ANTI-FRAUDE: Quantidade %d acima do máximo (%d).%n",
                    quantidade, QTDE_MAXIMA_POR_ITEM);
            return false;
        }

        ItemPedido item = new ItemPedido(produto, quantidade);
        double totalProjetado = pedido.calcularTotal() + item.calcularSubtotal();
        if (totalProjetado > VALOR_MAXIMO_PEDIDO) {
            System.out.printf("ANTI-FRAUDE: Pedido ultrapassaria R$ %.2f (limite R$ %.2f).%n",
                    totalProjetado, VALOR_MAXIMO_PEDIDO);
            return false;
        }

        pedido.adicionarItem(item);
        System.out.printf("%dx %s | Subtotal: R$ %.2f%n",
                quantidade, produto.getNome(), item.calcularSubtotal());
        return true;
    }

    public boolean confirmarPedido(Pedido pedido) {
        if (pedido.getItens().isEmpty()) {
            System.out.println("Pedido vazio — adicione itens antes de confirmar.");
            return false;
        }

        if (estoqueService != null) {
            for (ItemPedido item : pedido.getItens()) {
                if (!estoqueService.temEstoque(item.getProduto(), item.getQuantidadePedida())) {
                    System.out.printf("Estoque insuficiente: %s (solicitado: %d)%n",
                            item.getProduto().getNome(), item.getQuantidadePedida());
                    return false;
                }
            }
        }

        boolean salvo = pedidoRepository.salvar(pedido);
        if (!salvo) {
            System.out.println("Falha ao salvar no banco — estoque não alterado.");
            return false;
        }

        if (estoqueService != null) {
            for (ItemPedido item : pedido.getItens()) {
                boolean ok = estoqueService.baixarEstoque(item.getProduto(), item.getQuantidadePedida());
                if (!ok) System.out.printf(
                        "INCONSISTÊNCIA: pedido #%d salvo mas estoque não baixado para '%s'!%n",
                        pedido.getId(), item.getProduto().getNome());
            }
        }
        return true;
    }

    public void avancarStatus(Pedido pedido) {
        StatusPedido antes = pedido.getStatus();
        pedido.avancarStatus();
        pedidoRepository.atualizarStatus(pedido.getId(), pedido.getStatus());
        System.out.printf("Pedido #%d: %s → %s%n", pedido.getId(), antes, pedido.getStatus());
    }

    public void cancelarPedido(Pedido pedido) {
        StatusPedido statusAnterior = pedido.getStatus();
        pedido.cancelar();

        if (pedido.getStatus() == StatusPedido.CANCELADO) {
            pedidoRepository.atualizarStatus(pedido.getId(), StatusPedido.CANCELADO);

            // Estorna só se o pedido havia sido confirmado (saiu de ABERTO)
            if (estoqueService != null && statusAnterior != StatusPedido.ABERTO) {
                for (ItemPedido item : pedido.getItens()) {
                    estoqueService.estornarEstoque(item.getProduto(), item.getQuantidadePedida());
                }
            }
        }
    }

    public ArrayList<Pedido> listarTodos()                          { return pedidoRepository.buscarTodos(); }
    public ArrayList<Pedido> listarPorStatus(StatusPedido status)   { return pedidoRepository.buscarPorStatus(status); }
    public Pedido buscarPorId(int id)                               { return pedidoRepository.buscarPorId(id); }

    public double calcularTotalGeral() {
        double total = 0;
        for (Pedido p : listarTodos()) {
            if (p.getStatus() != StatusPedido.CANCELADO) total += p.calcularTotal();
        }
        return total;
    }

    public void exibirTodos() {
        ArrayList<Pedido> lista = listarTodos();
        if (lista.isEmpty()) { System.out.println("Nenhum pedido registrado."); return; }
        System.out.println("═══════════ PEDIDOS ═══════════");
        for (Pedido p : lista) {
            System.out.println(p);
            System.out.println("───────────────────────────────");
        }
        System.out.printf("TOTAL GERAL: R$ %.2f%n", calcularTotalGeral());
    }
}