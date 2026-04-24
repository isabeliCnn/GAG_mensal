package org.example.model;

import jakarta.persistence.*;

@Entity
@Table
public class ItemPedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn
    private Pedido pedido;

    @ManyToOne
    @JoinColumn
    private Produto produto;

    @Column(nullable = false)
    private int quantidadePedida;

    public ItemPedido() {}

    public ItemPedido(Produto produto, int quantidadePedida) {
        this.produto = produto;
        this.quantidadePedida = quantidadePedida;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    public Produto getProduto() {
        return produto;
    }

    public int getQuantidadePedida() {
        return quantidadePedida;
    }

    public double calcularSubtotal() {
        return produto.getPreco() * quantidadePedida;
    }

    @Override
    public String toString() {
        return String.format("%dx %s — R$ %.2f",
                quantidadePedida,
                produto.getNome(),
                calcularSubtotal());
    }
}