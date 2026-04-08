package org.example.repository;

import org.example.model.Produto;
import org.example.model.TipoProduto;
import java.util.ArrayList;

public class ProdutoRepo {

    private ArrayList<Produto> produtos = new ArrayList<>();
    private int proximoId = 1;

    public void salvar(Produto p) {

        if (p.getId() == 0) {
            Produto comId = new Produto(proximoId++, p.getNome(), p.getPreco(),
                    p.getQuantidade(), p.getTipo());
            produtos.add(comId);
        } else {
            produtos.add(p);
        }
    }

    public ArrayList<Produto> buscarTodos() {
        return produtos;
    }

    public Produto buscarPorId(int id) {
        for (Produto p : produtos) {
            if (p.getId() == id) return p;
        }
        return null; // não encontrado
    }

    public Produto buscarPorNome(String nome) {
        for (Produto p : produtos) {
            if (p.getNome().equalsIgnoreCase(nome)) return p;
        }
        return null;
    }

    public ArrayList<Produto> buscarPorTipo(TipoProduto tipo) {
        ArrayList<Produto> resultado = new ArrayList<>();
        for (Produto p : produtos) {
            if (p.getTipo().equals(tipo)) resultado.add(p);
        }
        return resultado;
    }

    public boolean atualizar(Produto atualizado) {
        for (int i = 0; i < produtos.size(); i++) {
            if (produtos.get(i).getId() == atualizado.getId()) {
                produtos.set(i, atualizado);
                return true;
            }
        }
        return false;
    }

    public boolean deletar(int id) {
        return produtos.removeIf(p -> p.getId() == id);
    }
}
