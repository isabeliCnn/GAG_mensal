package org.example.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table
public class Pedido {

    public enum StatusPedido {
        ABERTO, EM_PREPARO, PRONTO, ENTREGUE, CANCELADO
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemPedido> itens = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusPedido status;

    @Column(nullable = false)
    private String observacao;

    public Pedido() {
        this.status = StatusPedido.ABERTO;
        this.observacao = "";
    }

    public Pedido(String observacao) {
        this();
        this.observacao = observacao;
    }

    public Pedido(int id, StatusPedido status) {
        this.id = id;
        this.status = status;
        this.observacao = "";
    }

    public void adicionarItem(Produto produto, int quantidade) {
        ItemPedido item = new ItemPedido(produto, quantidade);
        item.setPedido(this);
        itens.add(item);
    }

    public double calcularTotal() {
        return itens.stream()
                .mapToDouble(ItemPedido::calcularSubtotal)
                .sum();
    }

    public void avancarStatus() {
        switch (status) {
            case ABERTO -> status = StatusPedido.EM_PREPARO;
            case EM_PREPARO -> status = StatusPedido.PRONTO;
            case PRONTO -> status = StatusPedido.ENTREGUE;
            default -> System.out.println("Pedido já finalizado ou cancelado.");
        }
    }

    public void cancelar() {
        if (status == StatusPedido.ENTREGUE) {
            System.out.println("Não é possível cancelar um pedido já entregue.");
            return;
        }
        this.status = StatusPedido.CANCELADO;
    }

    public int getId() { return id; }
    public List<ItemPedido> getItens() { return itens; }
    public StatusPedido getStatus() { return status; }
    public String getObservacao() { return observacao; }

    public void setObservacao(String observacao) { this.observacao = observacao; }
}