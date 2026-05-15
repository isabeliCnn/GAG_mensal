package org.example.ui;

import org.example.model.Produto;
import org.example.model.TipoProduto;
import org.example.repository.ProdutoRepo;
import org.example.service.EstoqueService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class ProdutoFrame extends JFrame {

    private EstoqueService estoqueService;
    private DefaultTableModel tableModel;

    private JTable tabelaProdutos;
    private JScrollPane scrollPane;
    private JButton btnAtualizar;
    private JButton btnRemover;
    private JButton btnVoltar;
    private JLabel lblTitulo;

    public ProdutoFrame() {
        estoqueService = new EstoqueService(new ProdutoRepo());
        initComponents();
        carregarProdutos();
    }

    private void initComponents() {
        setTitle("Produtos — Forja Bar");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(700, 450);
        setLocationRelativeTo(null);
        setLayout(null);

        lblTitulo = new JLabel("Lista de Produtos");
        lblTitulo.setFont(new java.awt.Font("Segoe UI Black", 1, 20));
        lblTitulo.setBounds(230, 10, 300, 35);
        add(lblTitulo);

        String[] colunas = {"ID", "Nome", "Preço (R$)", "Estoque", "Tipo"};
        tableModel = new DefaultTableModel(colunas, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };
        tabelaProdutos = new JTable(tableModel);
        tabelaProdutos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        scrollPane = new JScrollPane(tabelaProdutos);
        scrollPane.setBounds(20, 60, 650, 280);
        add(scrollPane);

        btnAtualizar = new JButton("Atualizar Lista");
        btnAtualizar.setBounds(20, 360, 150, 35);
        btnAtualizar.addActionListener(e -> carregarProdutos());
        add(btnAtualizar);

        btnRemover = new JButton("Remover Produto");
        btnRemover.setBounds(200, 360, 160, 35);
        btnRemover.addActionListener(e -> removerProduto());
        add(btnRemover);

        btnVoltar = new JButton("Voltar ao Menu");
        btnVoltar.setBounds(530, 360, 140, 35);
        btnVoltar.addActionListener(e -> dispose());
        add(btnVoltar);
    }

    private void carregarProdutos() {
        tableModel.setRowCount(0);
        List<Produto> lista = estoqueService.listarTodos();
        for (Produto p : lista) {
            tableModel.addRow(new Object[]{
                    p.getId(),
                    p.getNome(),
                    String.format("%.2f", p.getPreco()),
                    p.getQuantidade(),
                    p.getTipo()
            });
        }
    }

    private void removerProduto() {
        int linhaSelecionada = tabelaProdutos.getSelectedRow();
        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um produto na tabela.");
            return;
        }
        int id = (int) tableModel.getValueAt(linhaSelecionada, 0);
        String nome = (String) tableModel.getValueAt(linhaSelecionada, 1);
        int confirm = JOptionPane.showConfirmDialog(this,
                "Remover o produto '" + nome + "'?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            estoqueService.removerProduto(id);
            carregarProdutos();
            JOptionPane.showMessageDialog(this, "Produto removido!");
        }
    }
}
