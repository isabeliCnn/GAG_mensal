package org.example.model;

import java.util.ArrayList;

public class Pedido {

    public enum StatusPedido {
        ABERTO, EM_PREPARO, PRONTO, ENTREGUE, CANCELADO
    }

    private int id;
    private ArrayList<ItemPedido> itens;
    private StatusPedido status;
    private String observacao;


    public Pedido() {
        this.id         = 0;
        this.itens      = new ArrayList<>();
        this.status     = StatusPedido.ABERTO;
        this.observacao = "";
    }

    public Pedido(String observacao) {
        this();
        this.observacao = observacao;
    }

    public Pedido(int id, StatusPedido status) {
        this.id         = id;
        this.status     = status;
        this.itens      = new ArrayList<>();
        this.observacao = "";
    }

    public void adicionarItem(ItemPedido item) {
        itens.add(item);
    }

    public double calcularTotal() {
        double total = 0;
        for (ItemPedido item : itens) total += item.calcularSubtotal();
        return total;
    }

    public void avancarStatus() {
        switch (status) {
            case ABERTO:     status = StatusPedido.EM_PREPARO; break;
            case EM_PREPARO: status = StatusPedido.PRONTO;     break;
            case PRONTO:     status = StatusPedido.ENTREGUE;   break;
            default: System.out.println("Pedido já finalizado ou cancelado.");
        }
    }

    public void cancelar() {
        if (status == StatusPedido.ENTREGUE) {
            System.out.println("Não é possível cancelar um pedido já entregue.");
            return;
        }
        this.status = StatusPedido.CANCELADO;
        System.out.println("Pedido #" + id + " cancelado.");
    }

    public int getId()                         { return id; }
    public void setId(int id)                  { this.id = id; } // usado pelo repo após INSERT
    public ArrayList<ItemPedido> getItens()    { return itens; }
    public StatusPedido getStatus()            { return status; }
    public String getObservacao()              { return observacao; }
    public void setObservacao(String obs)      { this.observacao = obs; }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Pedido #%d | Status: %s%n", id, status));
        for (ItemPedido item : itens) sb.append("   • ").append(item).append("\n");
        sb.append(String.format("TOTAL: R$ %.2f", calcularTotal()));
        if (!observacao.isEmpty()) sb.append(" | Obs: ").append(observacao);
        return sb.toString();
    }
}