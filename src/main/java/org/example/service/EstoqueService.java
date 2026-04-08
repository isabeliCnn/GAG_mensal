package org.example.service;

import org.example.model.Produto;
import org.example.model.TipoProduto;
import org.example.repository.ProdutoRepo;
import java.util.ArrayList;

public class EstoqueService {
    private ProdutoRepo repo;

    public EstoqueService(ProdutoRepo repo) {
        this.repo = repo;
    }

    public void adicionarProduto(Produto p) {
        // Validação
        if (p.getNome() == null || p.getNome().isBlank()) {
            System.out.println("Erro: nome do produto não pode ser vazio.");
            return;
        }
        if (p.getPreco() <= 0) {
            System.out.println("Erro: preço deve ser maior que zero.");
            return;
        }
        if (p.getQuantidade() < 0) {
            System.out.println("Erro: quantidade não pode ser negativa.");
            return;
        }
        repo.salvar(p);
        System.out.println("Produto '" + p.getNome() + "' cadastrado com sucesso!");
    }

    public ArrayList<Produto> listarTodos() {
        return repo.buscarTodos();
    }

    public ArrayList<Produto> listarPorTipo(TipoProduto tipo) {
        return repo.buscarPorTipo(tipo);
    }

    public Produto buscarPorNome(String nome) {
        Produto p = repo.buscarPorNome(nome);
        if (p == null) System.out.println("Produto '" + nome + "' não encontrado.");
        return p;
    }

    public Produto buscarPorId(int id) {
        return repo.buscarPorId(id);
    }

    // Baixa estoque quando uma venda é feita — chamado pelo PedidoService
    public boolean baixarEstoque(int idProduto, int quantidade) {
        Produto p = repo.buscarPorId(idProduto);
        if (p == null) {
            System.out.println("Erro: produto não encontrado.");
            return false;
        }
        if (p.getQuantidade() < quantidade) {
            System.out.println("Estoque insuficiente para: " + p.getNome());
            return false;
        }
        p.setQuantidade(p.getQuantidade() - quantidade);
        repo.atualizar(p);
        return true;
    }

    public void removerProduto(int id) {
        if (repo.deletar(id)) {
            System.out.println("Produto removido.");
        } else {
            System.out.println("Produto com id " + id + " não encontrado.");
        }
    }

    public void atualizarPreco(int id, double novoPreco) {
        Produto p = repo.buscarPorId(id);
        if (p == null) {
            System.out.println("Produto não encontrado.");
            return;
        }
        p.setPreco(novoPreco);
        repo.atualizar(p);
        System.out.println("Preço atualizado.");
    }
}
