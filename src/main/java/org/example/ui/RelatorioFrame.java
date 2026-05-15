package org.example.ui;

import org.example.model.Pedido;
import org.example.service.EstoqueService;
import org.example.service.PedidoService;
import org.example.repository.ProdutoRepo;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class RelatorioFrame extends JFrame {

    private PedidoService pedidoService;
    private DefaultTableModel tableModel;
    private JTable tabela;
    private JScrollPane scrollPane;
    private JLabel lblTitulo;
    private JLabel lblTotal;
    private JLabel lblTotalPedidos;
    private JButton btnAtualizar;
    private JButton btnVoltar;

    public RelatorioFrame() {
        pedidoService = new PedidoService(new EstoqueService(new ProdutoRepo()));
        initComponents();
        carregarRelatorio();
    }

    private void initComponents() {
        setTitle("Relatorio do Dia - Forja Bar");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(700, 480);
        setLocationRelativeTo(null);
        setLayout(null);

        lblTitulo = new JLabel("Relatorio do Dia");
        lblTitulo.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 22));
        lblTitulo.setBounds(230, 10, 300, 35);
        add(lblTitulo);

        String[] colunas = {"Pedido", "Status", "Total (R$)", "Itens"};
        tableModel = new DefaultTableModel(colunas, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };
        tabela = new JTable(tableModel);
        scrollPane = new JScrollPane(tabela);
        scrollPane.setBounds(20, 60, 650, 270);
        add(scrollPane);

        lblTotalPedidos = new JLabel("Total de pedidos: 0");
        lblTotalPedidos.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 13));
        lblTotalPedidos.setBounds(20, 345, 300, 25);
        add(lblTotalPedidos);

        lblTotal = new JLabel("Total geral: R$ 0,00");
        lblTotal.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 16));
        lblTotal.setBounds(20, 375, 350, 30);
        add(lblTotal);

        btnAtualizar = new JButton("Atualizar");
        btnAtualizar.setBounds(420, 360, 120, 35);
        btnAtualizar.addActionListener(e -> carregarRelatorio());
        add(btnAtualizar);

        btnVoltar = new JButton("Voltar ao Menu");
        btnVoltar.setBounds(555, 360, 140, 35);
        btnVoltar.addActionListener(e -> dispose());
        add(btnVoltar);
    }

    private void carregarRelatorio() {
        tableModel.setRowCount(0);
        List<Pedido> pedidos = pedidoService.listarTodos();
        double totalGeral = 0;
        for (Pedido p : pedidos) {
            double total = p.calcularTotal();
            totalGeral += total;
            tableModel.addRow(new Object[]{
                    p.getId(),
                    p.getStatus(),
                    String.format("%.2f", total),
                    p.getItens().size()
            });
        }
        lblTotalPedidos.setText("Total de pedidos: " + pedidos.size());
        lblTotal.setText("Total geral: R$ " + String.format("%.2f", totalGeral));
    }
}