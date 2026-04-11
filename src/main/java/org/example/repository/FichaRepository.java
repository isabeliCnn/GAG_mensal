package org.example.repository;

import org.example.database.Conexao;
import org.example.model.Ficha;
import java.sql.*;
import java.util.ArrayList;
import java.util.UUID;

public class FichaRepository {

    public boolean salvar(Ficha ficha) {
        String sql = "INSERT INTO fichas (id, pedido_id, usada) VALUES (?, ?, ?)";
        try (Connection conn = Conexao.getConexao();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, ficha.getId().toString());
            ps.setInt(2, ficha.getPedidoId());
            ps.setBoolean(3, ficha.isUsada());
            ps.executeUpdate();
            System.out.println("Ficha salva: [" + ficha.getId().toString().substring(0, 8) + "]"
                    + " → Pedido #" + ficha.getPedidoId());
            return true;

        } catch (SQLException e) {
            System.out.println("Erro ao salvar ficha: " + e.getMessage());
            return false;
        }
    }

    public Ficha buscarPorId(UUID id) {
        String sql = "SELECT * FROM fichas WHERE id = ?";
        try (Connection conn = Conexao.getConexao();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, id.toString());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return montarFicha(rs);

        } catch (SQLException e) {
            System.out.println("Erro ao buscar ficha: " + e.getMessage());
        }
        return null;
    }

    public Ficha buscarPorIdParcial(String idParcial) {
        String sql = "SELECT * FROM fichas WHERE id LIKE ?";
        try (Connection conn = Conexao.getConexao();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, idParcial + "%");
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return montarFicha(rs);

        } catch (SQLException e) {
            System.out.println("Erro ao buscar ficha parcial: " + e.getMessage());
        }
        return null;
    }

    public ArrayList<Ficha> listarAbertas() {
        return listarPorUsada(false);
    }

    public ArrayList<Ficha> listarTodas() {
        String sql = "SELECT * FROM fichas ORDER BY id";
        ArrayList<Ficha> lista = new ArrayList<>();
        try (Connection conn = Conexao.getConexao();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) lista.add(montarFicha(rs));

        } catch (SQLException e) {
            System.out.println("Erro ao listar fichas: " + e.getMessage());
        }
        return lista;
    }

    public boolean marcarUsada(UUID id) {
        String sql = "UPDATE fichas SET usada = TRUE WHERE id = ?";
        try (Connection conn = Conexao.getConexao();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, id.toString());
            boolean ok = ps.executeUpdate() > 0;
            if (ok) System.out.println("Ficha fechada: [" + id.toString().substring(0, 8) + "]");
            return ok;

        } catch (SQLException e) {
            System.out.println("Erro ao fechar ficha: " + e.getMessage());
            return false;
        }
    }

    private ArrayList<Ficha> listarPorUsada(boolean usada) {
        String sql = "SELECT * FROM fichas WHERE usada = ?";
        ArrayList<Ficha> lista = new ArrayList<>();
        try (Connection conn = Conexao.getConexao();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setBoolean(1, usada);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) lista.add(montarFicha(rs));

        } catch (SQLException e) {
            System.out.println("Erro ao listar fichas: " + e.getMessage());
        }
        return lista;
    }

    private Ficha montarFicha(ResultSet rs) throws SQLException {
        return new Ficha(
                UUID.fromString(rs.getString("id")),
                rs.getInt("pedido_id"),
                rs.getBoolean("usada")
        );
    }
}