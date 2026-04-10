package org.example.repository;

import org.example.database.Conexao;
import org.example.model.ItemPedido;
import org.example.model.Pedido;
import org.example.model.Pedido.StatusPedido;
import org.example.model.Produto;
import org.example.model.TipoProduto;

import java.sql.*;
import java.util.ArrayList;

public class PedidoRepo {

    public boolean salvar(Pedido pedido) {
        String sqlPedido = "INSERT INTO pedidos (total, status) VALUES (?, ?)";
        String sqlItem = "INSERT INTO itens_pedido (pedido_id, produto_id, quantidade, subtotal) VALUES (?, ?, ?, ?)";

        Connection conn = null;
        try {
            conn = Conexao.getConexao();
            conn.setAutoCommit(false);

            int idGerado;
            try (PreparedStatement psPedido = conn.prepareStatement(sqlPedido, Statement.RETURN_GENERATED_KEYS)) {
                psPedido.setDouble(1, pedido.calcularTotal());
                psPedido.setString(2, pedido.getStatus().name());
                psPedido.executeUpdate();

                ResultSet keys = psPedido.getGeneratedKeys();
                if (!keys.next()) throw new SQLException("Nenhum id gerado.");
                idGerado = keys.getInt(1);
                pedido.setId(idGerado);
            }

            try (PreparedStatement psItem = conn.prepareStatement(sqlItem)) {
                for (ItemPedido item : pedido.getItens()) {
                    psItem.setInt(1, idGerado);
                    psItem.setInt(2, item.getProduto().getId());
                    psItem.setInt(3, item.getQuantidadePedida());
                    psItem.setDouble(4, item.calcularSubtotal());
                    psItem.addBatch();
                }
                psItem.executeBatch();
            }

            conn.commit();
            System.out.println("Pedido #" + idGerado + " salvo.");
            return true;

        } catch (SQLException e) {
            System.out.println("Erro ao salvar pedido: " + e.getMessage());
            try { if (conn != null) conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            return false;
        } finally {
            try { if (conn != null) { conn.setAutoCommit(true); conn.close(); } } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    public boolean atualizarStatus(int id, StatusPedido novoStatus) {
        String sql = "UPDATE pedidos SET status = ? WHERE id = ?";
        try (Connection conn = Conexao.getConexao();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, novoStatus.name());
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Erro ao atualizar status: " + e.getMessage());
            return false;
        }
    }

    public Pedido buscarPorId(int id) {
        String sql = """
                SELECT p.id AS pedido_id, p.status,
                       ip.quantidade, ip.subtotal,
                       pr.id AS produto_id, pr.nome AS produto_nome,
                       pr.preco AS produto_preco, pr.quantidade AS produto_qtd,
                       pr.tipo AS produto_tipo
                FROM pedidos p
                LEFT JOIN itens_pedido ip ON p.id = ip.pedido_id
                LEFT JOIN produtos pr ON ip.produto_id = pr.id
                WHERE p.id = ? ORDER BY ip.id
                """;
        try (Connection conn = Conexao.getConexao();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            Pedido pedido = null;
            while (rs.next()) {
                if (pedido == null) pedido = new Pedido(rs.getInt("pedido_id"), StatusPedido.valueOf(rs.getString("status")));
                if (rs.getObject("produto_id") != null) pedido.adicionarItem(reconstituirItem(rs));
            }
            return pedido;

        } catch (SQLException e) {
            System.out.println("Erro ao buscar pedido: " + e.getMessage());
            return null;
        }
    }

    public ArrayList<Pedido> buscarTodos() {
        String sql = """
                SELECT p.id AS pedido_id, p.status,
                       ip.quantidade, ip.subtotal,
                       pr.id AS produto_id, pr.nome AS produto_nome,
                       pr.preco AS produto_preco, pr.quantidade AS produto_qtd,
                       pr.tipo AS produto_tipo
                FROM pedidos p
                LEFT JOIN itens_pedido ip ON p.id = ip.pedido_id
                LEFT JOIN produtos pr ON ip.produto_id = pr.id
                ORDER BY p.id DESC, ip.id
                """;
        ArrayList<Pedido> lista = new ArrayList<>();
        try (Connection conn = Conexao.getConexao();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            Pedido atual = null;
            while (rs.next()) {
                int idAtual = rs.getInt("pedido_id");
                if (atual == null || atual.getId() != idAtual) {
                    atual = new Pedido(idAtual, StatusPedido.valueOf(rs.getString("status")));
                    lista.add(atual);
                }
                if (rs.getObject("produto_id") != null) atual.adicionarItem(reconstituirItem(rs));
            }

        } catch (SQLException e) {
            System.out.println("Erro ao listar pedidos: " + e.getMessage());
        }
        return lista;
    }

    public ArrayList<Pedido> buscarPorStatus(StatusPedido status) {
        String sql = """
                SELECT p.id AS pedido_id, p.status,
                       ip.quantidade, ip.subtotal,
                       pr.id AS produto_id, pr.nome AS produto_nome,
                       pr.preco AS produto_preco, pr.quantidade AS produto_qtd,
                       pr.tipo AS produto_tipo
                FROM pedidos p
                LEFT JOIN itens_pedido ip ON p.id = ip.pedido_id
                LEFT JOIN produtos pr ON ip.produto_id = pr.id
                WHERE p.status = ? ORDER BY p.id, ip.id
                """;
        ArrayList<Pedido> lista = new ArrayList<>();
        try (Connection conn = Conexao.getConexao();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, status.name());
            ResultSet rs = ps.executeQuery();

            Pedido atual = null;
            while (rs.next()) {
                int idAtual = rs.getInt("pedido_id");
                if (atual == null || atual.getId() != idAtual) {
                    atual = new Pedido(idAtual, StatusPedido.valueOf(rs.getString("status")));
                    lista.add(atual);
                }
                if (rs.getObject("produto_id") != null) atual.adicionarItem(reconstituirItem(rs));
            }

        } catch (SQLException e) {
            System.out.println("Erro ao buscar por status: " + e.getMessage());
        }
        return lista;
    }

    private ItemPedido reconstituirItem(ResultSet rs) throws SQLException {
        Produto produto = new Produto(
                rs.getInt("produto_id"),
                rs.getString("produto_nome"),
                rs.getDouble("produto_preco"),
                rs.getInt("produto_qtd"),
                TipoProduto.valueOf(rs.getString("produto_tipo"))
        );
        return new ItemPedido(produto, rs.getInt("quantidade"));
    }
}