package org.example.ui;

import org.example.model.Produto;
import org.example.model.Pedido;
import org.example.repository.ProdutoRepo;
import org.example.repository.PedidoRepo;
import org.example.service.EstoqueService;
import org.example.service.PedidoService;
import org.example.service.FichaService;
import org.example.repository.FichaRepository;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class PedidoFrame extends JFrame {

    private EstoqueService estoqueService;
    private PedidoService pedidoService;
    private FichaService fichaService;
    private Pedido pedidoAtual;
    private DefaultTableModel modelCardapio;
    private DefaultTableModel modelItens;

    private JTable tabelaCardapio;
    private JTable tabelaItens;
    private JScrollPane scrollCardapio;
    private JScrollPane scrollItens;
    private JLabel lblTitulo;
    private JLabel lblCardapio;
    private JLabel lblItens;
    private JLabel lblTotal;
    private JSpinner spinQuantidade;
    private JButton btnAdicionar;
    private JButton btnConfirmar;
    private JButton btnVoltar;

    public PedidoFrame() {
        estoqueService = new EstoqueService(new ProdutoRepo());
        pedidoService = new PedidoService(estoqueService);
        fichaService = new FichaService(new FichaRepository(), pedidoService);
        pedidoAtual = pedidoService.criarPedido();
        initComponents();
        carregarCardapio();
    }

    private void initComponents() {
        setTitle("Novo Pedido — Forja Bar");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(850, 550);
        setLocationRelativeTo(null);
        setLayout(null);

        lblTitulo = new JLabel("Novo Pedido");
        lblTitulo.setFont(new java.awt.Font("Segoe UI Black", 1, 20));
        lblTitulo.setBounds(330, 10, 250, 35);
        add(lblTitulo);

        // Cardápio
        lblCardapio = new JLabel("Cardápio");
        lblCardapio.setFont(new java.awt.Font("Segoe UI", 1, 14));
        lblCardapio.setBounds(20, 55, 100, 25);
        add(lblCardapio);

        String[] colsCardapio = {"ID", "Nome", "Preço", "Estoque", "Tipo"};
        modelCardapio = new DefaultTableModel(colsCardapio, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tabelaCardapio = new JTable(modelCardapio);
        tabelaCardapio.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        scrollCardapio = new JScrollPane(tabelaCardapio);
        scrollCardapio.setBounds(20, 80, 400, 280);
        add(scrollCardapio);

        // Quantidade
        JLabel lblQtd = new JLabel("Quantidade:");
        lblQtd.setBounds(20, 370, 100, 25);
        add(lblQtd);

        spinQuantidade = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
        spinQuantidade.setBounds(120, 370, 60, 25);
        add(spinQuantidade);

        btnAdicionar = new JButton("Adicionar ao Pedido");
        btnAdicionar.setBounds(200, 368, 180, 30);
        btnAdicionar.addActionListener(e -> adicionarItem());
        add(btnAdicionar);

        // Itens do pedido
        lblItens = new JLabel("Itens do Pedido");
        lblItens.setFont(new java.awt.Font("Segoe UI", 1, 14));
        lblItens.setBounds(440, 55, 200, 25);
        add(lblItens);

        String[] colsItens = {"Produto", "Qtd", "Subtotal"};
        modelItens = new DefaultTableModel(colsItens, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tabelaItens = new JTable(modelItens);
        scrollItens = new JScrollPane(tabelaItens);
        scrollItens.setBounds(440, 80, 380, 280);
        add(scrollItens);

        lblTotal = new JLabel("Total: R$ 0,00");
        lblTotal.setFont(new java.awt.Font("Segoe UI Black", 1, 16));
        lblTotal.setBounds(440, 370, 300, 30);
        add(lblTotal);

        btnConfirmar = new JButton("Confirmar Pedido");
        btnConfirmar.setBounds(550, 460, 160, 40);
        btnConfirmar.setBackground(new java.awt.Color(0, 153, 0));
        btnConfirmar.setForeground(java.awt.Color.WHITE);
        btnConfirmar.addActionListener(e -> confirmarPedido());
        add(btnConfirmar);

        btnVoltar = new JButton("Voltar");
        btnVoltar.setBounds(730, 460, 90, 40);
        btnVoltar.addActionListener(e -> dispose());
        add(btnVoltar);
    }

    private void carregarCardapio() {
        modelCardapio.setRowCount(0);
        List<Produto> lista = estoqueService.listarTodos();
        for (Produto p : lista) {
            if (p.getQuantidade() > 0) {
                modelCardapio.addRow(new Object[]{
                        p.getId(),
                        p.getNome(),
                        String.format("R$ %.2f", p.getPreco()),
                        p.getQuantidade(),
                        p.getTipo()
                });
            }
        }
    }

    private void adicionarItem() {
        int linha = tabelaCardapio.getSelectedRow();
        if (linha == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um produto no cardápio.");
            return;
        }

        int id = (int) modelCardapio.getValueAt(linha, 0);
        String nome = (String) modelCardapio.getValueAt(linha, 1);
        int qtd = (int) spinQuantidade.getValue();

        Produto produto = estoqueService.buscarPorId(id);
        if (produto == null) return;

        boolean ok = pedidoService.adicionarItem(pedidoAtual, produto, qtd);
        if (!ok) {
            JOptionPane.showMessageDialog(this, "Não foi possível adicionar o item.");
            return;
        }

        double subtotal = produto.getPreco() * qtd;
        modelItens.addRow(new Object[]{nome, qtd, String.format("R$ %.2f", subtotal)});

        double total = pedidoAtual.calcularTotal();
        String aviso = total > 100 ? " (10% OFF aplicado!)" : "";
        lblTotal.setText("Total: R$ " + String.format("%.2f", total > 100 ? total * 0.9 : total) + aviso);
    }

    private void confirmarPedido() {
        if (pedidoAtual.getItens().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Adicione pelo menos um item.");
            return;
        }

        boolean ok = pedidoService.confirmarPedido(pedidoAtual);
        if (ok) {
            fichaService.abrirFicha(pedidoAtual.getId());
            JOptionPane.showMessageDialog(this, "Pedido confirmado e ficha gerada!");
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Erro ao confirmar pedido.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}