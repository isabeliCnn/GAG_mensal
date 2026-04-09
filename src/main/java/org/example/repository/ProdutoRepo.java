package org.example.repository;

import org.example.database.Conexao;
import org.example.model.Produto;
import org.example.model.TipoProduto;

import java.sql.*;
import java.util.ArrayList;

public class ProdutoRepo {

    public void salvar(Produto p) {
        String sql = "INSERT INTO produtos (nome, preco, quantidade, tipo) VALUES (?, ?, ?, ?)";
        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, p.getNome());
            stmt.setDouble(2, p.getPreco());
            stmt.setInt(3, p.getQuantidade());
            stmt.setString(4, p.getTipo().name());
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Erro ao salvar produto: " + e.getMessage());
        }
    }

    public ArrayList<Produto> buscarTodos() {
        ArrayList<Produto> lista = new ArrayList<>();
        String sql = "SELECT * FROM produtos";
        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                lista.add(montarProduto(rs));
            }

        } catch (SQLException e) {
            System.out.println("Erro ao buscar produtos: " + e.getMessage());
        }
        return lista;
    }

    public Produto buscarPorId(int id) {
        String sql = "SELECT * FROM produtos WHERE id = ?";
        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return montarProduto(rs);

        } catch (SQLException e) {
            System.out.println("Erro ao buscar por id: " + e.getMessage());
        }
        return null;
    }

    public Produto buscarPorNome(String nome) {
        String sql = "SELECT * FROM produtos WHERE LOWER(nome) = LOWER(?)";
        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nome);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return montarProduto(rs);

        } catch (SQLException e) {
            System.out.println("Erro ao buscar por nome: " + e.getMessage());
        }
        return null;
    }

    public ArrayList<Produto> buscarPorTipo(TipoProduto tipo) {
        ArrayList<Produto> lista = new ArrayList<>();
        String sql = "SELECT * FROM produtos WHERE tipo = ?";
        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, tipo.name());
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) lista.add(montarProduto(rs));

        } catch (SQLException e) {
            System.out.println("Erro ao buscar por tipo: " + e.getMessage());
        }
        return lista;
    }

    public boolean atualizar(Produto p) {
        String sql = "UPDATE produtos SET nome=?, preco=?, quantidade=?, tipo=? WHERE id=?";
        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, p.getNome());
            stmt.setDouble(2, p.getPreco());
            stmt.setInt(3, p.getQuantidade());
            stmt.setString(4, p.getTipo().name());
            stmt.setInt(5, p.getId());
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Erro ao atualizar produto: " + e.getMessage());
        }
        return false;
    }

    public boolean deletar(int id) {
        String sql = "DELETE FROM produtos WHERE id = ?";
        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Erro ao deletar produto: " + e.getMessage());
        }
        return false;
    }

    // monta um objeto Produto a partir de uma linha do banco
    private Produto montarProduto(ResultSet rs) throws SQLException {
        return new Produto(
                rs.getInt("id"),
                rs.getString("nome"),
                rs.getDouble("preco"),
                rs.getInt("quantidade"),
                TipoProduto.valueOf(rs.getString("tipo"))
        );
    }
}