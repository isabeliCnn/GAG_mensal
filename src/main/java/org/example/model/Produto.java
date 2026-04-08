package org.example.model;

public class Produto {
        private int id;
        private String nome;
        private double preco;
        private int quantidade; // quantidade em estoque
        private TipoProduto tipo;

        public Produto(String nome, double preco, int quantidade, TipoProduto tipo) {
            this.nome = nome;
            this.preco = preco;
            this.quantidade = quantidade;
            this.tipo = tipo;
        }

        public Produto(int id, String nome, double preco, int quantidade, TipoProduto tipo) {
            this.id = id;
            this.nome = nome;
            this.preco = preco;
            this.quantidade = quantidade;
            this.tipo = tipo;
        }

        public int getId() {
            return id;
        }

        public String getNome(){
            return nome;
        }

        public double getPreco(){
            return preco;
        }

        public int getQuantidade(){
            return quantidade;
        }

        public TipoProduto getTipo(){
            return tipo;
        }


        public void setPreco(double preco) {
            this.preco = preco;
        }

        public void setQuantidade(int quantidade) {
            this.quantidade = quantidade;
        }


        @Override
        public String toString() {
            return String.format("[%d] %s | R$ %.2f | Estoque: %d | Tipo: %s",
                    id, nome, preco, quantidade, tipo);
        }
    }

