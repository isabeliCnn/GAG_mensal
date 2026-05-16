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
import java.awt.*;
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
    private JLabel lblTotal;
    private JSpinner spinQuantidade;

    public PedidoFrame() {
        estoqueService = new EstoqueService(new ProdutoRepo());
        pedidoService = new PedidoService(estoqueService);
        fichaService = new FichaService(new FichaRepository(), pedidoService);
        pedidoAtual = pedidoService.criarPedido();
        initComponents();
        carregarCardapio();
    }

    private void initComponents() {
        setTitle("Forja Bar — Novo Pedido");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setMinimumSize(new Dimension(860, 540));
        setLocationRelativeTo(null);

        JPanel painelPrincipal = new JPanel(new BorderLayout());
        painelPrincipal.setBackground(new Color(30, 30, 40));
        setContentPane(painelPrincipal);

        // Cabeçalho
        JPanel cabecalho = new JPanel();
        cabecalho.setBackground(new Color(180, 100, 60));
        cabecalho.setBorder(BorderFactory.createEmptyBorder(14, 20, 14, 20));
        JLabel lblTitulo = new JLabel("🛒 Novo Pedido");
        lblTitulo.setFont(new Font("Segoe UI Black", Font.BOLD, 18));
        lblTitulo.setForeground(Color.WHITE);
        cabecalho.add(lblTitulo);

        // Corpo em duas colunas
        JPanel corpo = new JPanel(new GridLayout(1, 2, 12, 0));
        corpo.setBackground(new Color(45, 45, 60));
        corpo.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        // Coluna esquerda — Cardápio
        JPanel colEsq = new JPanel(new BorderLayout(0, 8));
        colEsq.setBackground(new Color(45, 45, 60));

        JLabel lblCard = new JLabel("Cardápio disponível");
        lblCard.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblCard.setForeground(new Color(200, 200, 220));

        String[] colsCardapio = {"ID", "Nome", "Preço", "Estoque", "Tipo"};
        modelCardapio = new DefaultTableModel(colsCardapio, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tabelaCardapio = criarTabela(modelCardapio);
        JScrollPane scrollCard = new JScrollPane(tabelaCardapio);
        scrollCard.setBorder(BorderFactory.createLineBorder(new Color(80, 80, 110)));
        scrollCard.getViewport().setBackground(new Color(45, 45, 60));

        JPanel painelQtd = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        painelQtd.setBackground(new Color(45, 45, 60));
        JLabel lblQtd = new JLabel("Quantidade:");
        lblQtd.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblQtd.setForeground(new Color(200, 200, 220));
        spinQuantidade = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
        spinQuantidade.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        spinQuantidade.setPreferredSize(new Dimension(70, 30));
        JButton btnAdicionar = criarBotao("➕ Adicionar ao Pedido", new Color(180, 100, 60));
        btnAdicionar.addActionListener(e -> adicionarItem());
        painelQtd.add(lblQtd);
        painelQtd.add(spinQuantidade);
        painelQtd.add(btnAdicionar);

        colEsq.add(lblCard, BorderLayout.NORTH);
        colEsq.add(scrollCard, BorderLayout.CENTER);
        colEsq.add(painelQtd, BorderLayout.SOUTH);

        // Coluna direita — Itens do pedido
        JPanel colDir = new JPanel(new BorderLayout(0, 8));
        colDir.setBackground(new Color(45, 45, 60));

        JLabel lblItens = new JLabel("Itens do Pedido");
        lblItens.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblItens.setForeground(new Color(200, 200, 220));

        String[] colsItens = {"Produto", "Qtd", "Subtotal"};
        modelItens = new DefaultTableModel(colsItens, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tabelaItens = criarTabela(modelItens);
        JScrollPane scrollItens = new JScrollPane(tabelaItens);
        scrollItens.setBorder(BorderFactory.createLineBorder(new Color(80, 80, 110)));
        scrollItens.getViewport().setBackground(new Color(45, 45, 60));

        lblTotal = new JLabel("Total: R$ 0,00");
        lblTotal.setFont(new Font("Segoe UI Black", Font.BOLD, 16));
        lblTotal.setForeground(new Color(255, 200, 80));

        colDir.add(lblItens, BorderLayout.NORTH);
        colDir.add(scrollItens, BorderLayout.CENTER);
        colDir.add(lblTotal, BorderLayout.SOUTH);

        corpo.add(colEsq);
        corpo.add(colDir);

        // Botões rodapé
        JPanel rodape = new JPanel(new GridLayout(1, 2, 10, 0));
        rodape.setBackground(new Color(30, 30, 40));
        rodape.setBorder(BorderFactory.createEmptyBorder(10, 20, 16, 20));

        JButton btnVoltar = criarBotao("Voltar", new Color(80, 80, 100));
        btnVoltar.addActionListener(e -> dispose());

        JButton btnConfirmar = criarBotao("✅ Confirmar Pedido", new Color(60, 160, 100));
        btnConfirmar.addActionListener(e -> confirmarPedido());

        rodape.add(btnVoltar);
        rodape.add(btnConfirmar);

        painelPrincipal.add(cabecalho, BorderLayout.NORTH);
        painelPrincipal.add(corpo, BorderLayout.CENTER);
        painelPrincipal.add(rodape, BorderLayout.SOUTH);
        pack();
    }

    private JTable criarTabela(DefaultTableModel model) {
        JTable tabela = new JTable(model);
        tabela.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tabela.setRowHeight(26);
        tabela.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tabela.setBackground(new Color(45, 45, 60));
        tabela.setForeground(new Color(220, 220, 220));
        tabela.getTableHeader().setBackground(new Color(35, 35, 50));
        tabela.getTableHeader().setForeground(new Color(200, 200, 220));
        tabela.setSelectionBackground(new Color(180, 100, 60));
        tabela.setSelectionForeground(Color.WHITE);
        tabela.setGridColor(new Color(60, 60, 80));
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        return tabela;
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

    private void carregarCardapio() {
        modelCardapio.setRowCount(0);
        List<Produto> lista = estoqueService.listarTodos();
        for (Produto p : lista) {
            if (p.getQuantidade() > 0) {
                modelCardapio.addRow(new Object[]{
                        p.getId(), p.getNome(),
                        String.format("R$ %.2f", p.getPreco()),
                        p.getQuantidade(), p.getTipo()
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
        String aviso = total > 100 ? " ✅ 10% OFF!" : "";
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
