package org.example.model;

public class Produto {

    private int id;
    private String nome;
    private double preco;
    private int quantidade;
    private TipoProduto tipo;
    private boolean ativo;

    public Produto(int id, String nome, double preco, int quantidade, TipoProduto tipo, boolean ativo) {
        this.id = id;
        this.nome = nome;
        this.preco = preco;
        this.quantidade = quantidade;
        this.tipo = tipo;
        this.ativo = ativo;
    }

    public Produto(String nome, double preco, int quantidade, TipoProduto tipo) {
        this.nome = nome;
        this.preco = preco;
        this.quantidade = quantidade;
        this.tipo = tipo;
        this.ativo = true;
    }

    public int getId() { return id; }
    public String getNome() { return nome; }
    public double getPreco() { return preco; }
    public int getQuantidade() { return quantidade; }
    public TipoProduto getTipo() { return tipo; }
    public boolean isAtivo() { return ativo; }

    public void setQuantidade(int quantidade) { this.quantidade = quantidade; }
    public void setPreco(double preco) { this.preco = preco; }
    public void setAtivo(boolean ativo) { this.ativo = ativo; }

    @Override
    public String toString() {
        return String.format("[%d] %s | R$ %.2f | Estoque: %d | Tipo: %s",
                id, nome, preco, quantidade, tipo);
    }
}