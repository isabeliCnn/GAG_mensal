package org.example.model;

import java.util.ArrayList;
import java.util.UUID;

public class Pedido {

    public enum StatusPedido {
        PENDENTE, PREPARANDO, PRONTO, ENTREGUE, CANCELADO
    }

    private UUID id;
    private ArrayList<ItemPedido> itens;
    private StatusPedido status;
    private String observacao;

    public Pedido() {
        this.id = UUID.randomUUID();
        this.itens = new ArrayList<>();
        this.status = StatusPedido.PENDENTE;
    }

    public Pedido(String observacao) {
        this();
        this.observacao = observacao;
    }

    public UUID getId() { return id; }
    public ArrayList<ItemPedido> getItens() { return itens; }
    public StatusPedido getStatus() { return status; }
    public String getObservacao() { return observacao; }

    public void adicionarItem(ItemPedido item) {
        this.itens.add(item);
    }

    public double calcularTotal() {
        double total = 0;
        for (ItemPedido item : itens) {
            total += item.calcularSubtotal();
        }
        return total;
    }

    public void avancarStatus() {
        if (status == StatusPedido.PENDENTE) status = StatusPedido.PREPARANDO;
        else if (status == StatusPedido.PREPARANDO) status = StatusPedido.PRONTO;
        else if (status == StatusPedido.PRONTO) status = StatusPedido.ENTREGUE;
    }

    public void cancelar() {
        this.status = StatusPedido.CANCELADO;
    }

    @Override
    public String toString() {
        return String.format("Pedido [%s] | Status: %s | Total: R$ %.2f",
                id.toString().substring(0, 8), status, calcularTotal());
    }
}