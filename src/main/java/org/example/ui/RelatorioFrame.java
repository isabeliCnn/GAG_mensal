package org.example.ui;

import org.example.model.Pedido;
import org.example.repository.ProdutoRepo;
import org.example.service.EstoqueService;
import org.example.service.PedidoService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class RelatorioFrame extends JFrame {

    private PedidoService pedidoService;
    private DefaultTableModel tableModel;
    private JTable tabela;
    private JLabel lblTotal;
    private JLabel lblTotalPedidos;

    public RelatorioFrame() {
        pedidoService = new PedidoService(new EstoqueService(new ProdutoRepo()));
        initComponents();
        carregarRelatorio();
    }

    private void initComponents() {
        setTitle("Forja Bar — Relatório do Dia");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setMinimumSize(new Dimension(680, 480));
        setLocationRelativeTo(null);

        JPanel painelPrincipal = new JPanel(new BorderLayout());
        painelPrincipal.setBackground(new Color(30, 30, 40));
        setContentPane(painelPrincipal);

        // Cabeçalho
        JPanel cabecalho = new JPanel();
        cabecalho.setBackground(new Color(180, 140, 40));
        cabecalho.setBorder(BorderFactory.createEmptyBorder(14, 20, 14, 20));
        JLabel lblTitulo = new JLabel("📊 Relatório do Dia");
        lblTitulo.setFont(new Font("Segoe UI Black", Font.BOLD, 18));
        lblTitulo.setForeground(Color.WHITE);
        cabecalho.add(lblTitulo);

        // Tabela
        String[] colunas = {"Pedido (ID)", "Status", "Total (R$)", "Itens"};
        tableModel = new DefaultTableModel(colunas, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };
        tabela = new JTable(tableModel);
        tabela.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tabela.setRowHeight(28);
        tabela.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tabela.setBackground(new Color(45, 45, 60));
        tabela.setForeground(new Color(220, 220, 220));
        tabela.getTableHeader().setBackground(new Color(35, 35, 50));
        tabela.getTableHeader().setForeground(new Color(200, 200, 220));
        tabela.setSelectionBackground(new Color(180, 140, 40));
        tabela.setSelectionForeground(Color.WHITE);
        tabela.setGridColor(new Color(60, 60, 80));

        JScrollPane scroll = new JScrollPane(tabela);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(new Color(45, 45, 60));

        // Painel de totais
        JPanel painelTotais = new JPanel(new GridLayout(2, 1, 0, 4));
        painelTotais.setBackground(new Color(35, 35, 50));
        painelTotais.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));

        lblTotalPedidos = new JLabel("Total de pedidos: 0");
        lblTotalPedidos.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblTotalPedidos.setForeground(new Color(180, 180, 200));

        lblTotal = new JLabel("Total geral: R$ 0,00");
        lblTotal.setFont(new Font("Segoe UI Black", Font.BOLD, 16));
        lblTotal.setForeground(new Color(255, 200, 80));

        painelTotais.add(lblTotalPedidos);
        painelTotais.add(lblTotal);

        // Botões
        JPanel rodape = new JPanel(new GridLayout(1, 2, 10, 0));
        rodape.setBackground(new Color(30, 30, 40));
        rodape.setBorder(BorderFactory.createEmptyBorder(10, 20, 16, 20));

        JButton btnAtualizar = criarBotao("🔄 Atualizar", new Color(180, 140, 40));
        btnAtualizar.addActionListener(e -> carregarRelatorio());

        JButton btnVoltar = criarBotao("Voltar ao Menu", new Color(80, 80, 100));
        btnVoltar.addActionListener(e -> dispose());

        rodape.add(btnAtualizar);
        rodape.add(btnVoltar);

        JPanel sul = new JPanel(new BorderLayout());
        sul.setBackground(new Color(30, 30, 40));
        sul.add(painelTotais, BorderLayout.NORTH);
        sul.add(rodape, BorderLayout.SOUTH);

        painelPrincipal.add(cabecalho, BorderLayout.NORTH);
        painelPrincipal.add(scroll, BorderLayout.CENTER);
        painelPrincipal.add(sul, BorderLayout.SOUTH);
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

    private void carregarRelatorio() {
        tableModel.setRowCount(0);
        List<Pedido> pedidos = pedidoService.listarTodos();
        double totalGeral = 0;
        for (Pedido p : pedidos) {
            double total = p.calcularTotal();
            totalGeral += total;
            tableModel.addRow(new Object[]{
                    p.getId(), p.getStatus(),
                    String.format("%.2f", total),
                    p.getItens().size()
            });
        }
        lblTotalPedidos.setText("Total de pedidos: " + pedidos.size());
        lblTotal.setText("Total geral: R$ " + String.format("%.2f", totalGeral));
    }
}
