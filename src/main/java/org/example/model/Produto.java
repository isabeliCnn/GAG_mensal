package org.example.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "produtos")
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(nullable = false)
    private double preco;

    @Column(nullable = false)
    private int quantidade;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoProduto tipo;

    @Column(nullable = false)
    private boolean ativo;

    @OneToMany(mappedBy = "produto")
    private List<ItemPedido> itens;

    public Produto() {}

    public Produto(String nome, double preco, int quantidade, TipoProduto tipo) {
        this.nome = nome;
        this.preco = preco;
        this.quantidade = quantidade;
        this.tipo = tipo;
        this.ativo = true;
    }

    public Produto(int id, String nome, double preco, int quantidade, TipoProduto tipo, boolean ativo) {
        this.id = id;
        this.nome = nome;
        this.preco = preco;
        this.quantidade = quantidade;
        this.tipo = tipo;
        this.ativo = ativo;
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
        return String.format("[%d] %s | R$ %.2f | Estoque: %d | %s",
                id, nome, preco, quantidade, tipo);
    }}