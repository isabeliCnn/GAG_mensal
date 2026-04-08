package org.example.model;

public class ItemPedido {

    private Produto produto;
    private int quantidadePedida;

    public ItemPedido(Produto produto, int quantidadePedida) {
        this.produto = produto;
        this.quantidadePedida = quantidadePedida;
    }

    public double calcularSubtotal() {
        return produto.getPreco() * quantidadePedida;
    }

    public Produto getProduto(){
        return produto;
    }

    public int getQuantidadePedida(){
        return quantidadePedida;
    }

    @Override
    public String toString() {
        return String.format("%dx %s — R$ %.2f",
                quantidadePedida, produto.getNome(), calcularSubtotal());
    }
}
