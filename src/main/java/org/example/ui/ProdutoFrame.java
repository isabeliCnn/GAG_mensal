package org.example.ui;

import org.example.model.Produto;
import org.example.repository.ProdutoRepo;
import org.example.service.EstoqueService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ProdutoFrame extends JFrame {

    private EstoqueService estoqueService;
    private DefaultTableModel tableModel;
    private JTable tabelaProdutos;

    public ProdutoFrame() {
        estoqueService = new EstoqueService(new ProdutoRepo());
        initComponents();
        carregarProdutos();
    }

    private void initComponents() {
        setTitle("Forja Bar — Produtos");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setMinimumSize(new Dimension(680, 450));
        setLocationRelativeTo(null);

        JPanel painelPrincipal = new JPanel(new BorderLayout());
        painelPrincipal.setBackground(new Color(30, 30, 40));
        setContentPane(painelPrincipal);

        // Cabeçalho
        JPanel cabecalho = new JPanel();
        cabecalho.setBackground(new Color(60, 150, 160));
        cabecalho.setBorder(BorderFactory.createEmptyBorder(14, 20, 14, 20));
        JLabel lblTitulo = new JLabel("📋 Lista de Produtos");
        lblTitulo.setFont(new Font("Segoe UI Black", Font.BOLD, 18));
        lblTitulo.setForeground(Color.WHITE);
        cabecalho.add(lblTitulo);

        // Tabela
        String[] colunas = {"ID", "Nome", "Preço (R$)", "Estoque", "Tipo"};
        tableModel = new DefaultTableModel(colunas, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };
        tabelaProdutos = new JTable(tableModel);
        tabelaProdutos.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tabelaProdutos.setRowHeight(28);
        tabelaProdutos.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tabelaProdutos.setSelectionBackground(new Color(60, 150, 160));
        tabelaProdutos.setSelectionForeground(Color.WHITE);
        tabelaProdutos.setBackground(new Color(45, 45, 60));
        tabelaProdutos.setForeground(new Color(220, 220, 220));
        tabelaProdutos.getTableHeader().setBackground(new Color(35, 35, 50));
        tabelaProdutos.getTableHeader().setForeground(new Color(200, 200, 220));
        tabelaProdutos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabelaProdutos.setGridColor(new Color(60, 60, 80));

        JScrollPane scroll = new JScrollPane(tabelaProdutos);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(new Color(45, 45, 60));

        // Botões
        JPanel rodape = new JPanel(new GridLayout(1, 3, 10, 0));
        rodape.setBackground(new Color(30, 30, 40));
        rodape.setBorder(BorderFactory.createEmptyBorder(10, 20, 16, 20));

        JButton btnAtualizar = criarBotao("🔄 Atualizar", new Color(60, 150, 160));
        btnAtualizar.addActionListener(e -> carregarProdutos());

        JButton btnRemover = criarBotao("🗑️ Remover Produto", new Color(160, 60, 60));
        btnRemover.addActionListener(e -> removerProduto());

        JButton btnVoltar = criarBotao("Voltar ao Menu", new Color(80, 80, 100));
        btnVoltar.addActionListener(e -> dispose());

        rodape.add(btnAtualizar);
        rodape.add(btnRemover);
        rodape.add(btnVoltar);

        painelPrincipal.add(cabecalho, BorderLayout.NORTH);
        painelPrincipal.add(scroll, BorderLayout.CENTER);
        painelPrincipal.add(rodape, BorderLayout.SOUTH);
        pack();
    }

    private JButton criarBotao(String texto, Color cor) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBackground(cor);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void carregarProdutos() {
        tableModel.setRowCount(0);
        List<Produto> lista = estoqueService.listarTodos();
        for (Produto p : lista) {
            tableModel.addRow(new Object[]{
                    p.getId(), p.getNome(),
                    String.format("%.2f", p.getPreco()),
                    p.getQuantidade(), p.getTipo()
            });
        }
    }

    private void removerProduto() {
        int linha = tabelaProdutos.getSelectedRow();
        if (linha == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um produto na tabela.");
            return;
        }
        int id = (int) tableModel.getValueAt(linha, 0);
        String nome = (String) tableModel.getValueAt(linha, 1);
        int confirm = JOptionPane.showConfirmDialog(this,
                "Remover o produto '" + nome + "'?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            estoqueService.removerProduto(id);
            carregarProdutos();
            JOptionPane.showMessageDialog(this, "Produto removido!");
        }
    }
}
