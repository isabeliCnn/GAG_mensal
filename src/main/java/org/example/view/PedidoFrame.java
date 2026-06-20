package org.example.view;

import org.example.controller.PedidoController;
import org.example.model.Produto;
import org.example.util.AppContext;
import org.example.util.ResultadoOperacao;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PedidoFrame extends JFrame {

    private final PedidoController pedidoController;

    private DefaultTableModel modeloCardapio;
    private DefaultTableModel modeloItensPedido;
    private JTable tabelaCardapio;
    private JLabel rotuloTotal;
    private JSpinner spinnerQuantidade;

    public PedidoFrame() {
        this.pedidoController = AppContext.novoPedidoController();
        montarTela();
        carregarCardapio();
    }

    private void montarTela() {
        setTitle("Forja Bar — Novo Pedido");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setMinimumSize(new Dimension(860, 540));
        setLocationRelativeTo(null);

        JPanel painelPrincipal = new JPanel(new BorderLayout());
        painelPrincipal.setBackground(ComponentesUi.COR_FUNDO);
        setContentPane(painelPrincipal);

        JPanel cabecalho = ComponentesUi.criarCabecalho("Novo Pedido", new Color(180, 100, 60));

        JPanel corpo = new JPanel(new GridLayout(1, 2, 12, 0));
        corpo.setBackground(ComponentesUi.COR_PAINEL);
        corpo.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        corpo.add(montarColunaCardapio());
        corpo.add(montarColunaItensPedido());

        JPanel rodape = new JPanel(new GridLayout(1, 2, 10, 0));
        rodape.setBackground(ComponentesUi.COR_FUNDO);
        rodape.setBorder(BorderFactory.createEmptyBorder(10, 20, 16, 20));

        JButton botaoVoltar = ComponentesUi.criarBotaoSecundario("Voltar ao Menu");
        botaoVoltar.addActionListener(e -> dispose());

        JButton botaoConfirmar = ComponentesUi.criarBotaoPrimario("Confirmar Pedido", new Color(60, 160, 100), Color.WHITE);
        botaoConfirmar.addActionListener(e -> confirmarPedido());

        rodape.add(botaoVoltar);
        rodape.add(botaoConfirmar);

        painelPrincipal.add(cabecalho, BorderLayout.NORTH);
        painelPrincipal.add(corpo, BorderLayout.CENTER);
        painelPrincipal.add(rodape, BorderLayout.SOUTH);
        pack();
    }

    private JPanel montarColunaCardapio() {
        JPanel coluna = new JPanel(new BorderLayout(0, 8));
        coluna.setBackground(ComponentesUi.COR_PAINEL);

        JLabel rotulo = ComponentesUi.criarRotuloCampo("Cardápio Disponível");
        String[] colunas = {"ID", "Nome", "Preço", "Estoque", "Tipo"};
        modeloCardapio = new DefaultTableModel(colunas, 0) {
            public boolean isCellEditable(int linha, int coluna) { return false; }
        };
        tabelaCardapio = ComponentesUi.criarTabelaEstilizada(modeloCardapio, new Color(180, 100, 60));
        JScrollPane scroll = new JScrollPane(tabelaCardapio);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(80, 80, 110)));
        scroll.getViewport().setBackground(ComponentesUi.COR_PAINEL);

        JPanel painelQuantidade = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        painelQuantidade.setBackground(ComponentesUi.COR_PAINEL);
        JLabel rotuloQuantidade = ComponentesUi.criarRotuloCampo("Quantidade:");
        spinnerQuantidade = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
        spinnerQuantidade.setPreferredSize(new Dimension(70, 30));

        JButton botaoAdicionar = ComponentesUi.criarBotaoPrimario("Adicionar ao Pedido", new Color(180, 100, 60), Color.WHITE);
        botaoAdicionar.addActionListener(e -> adicionarItemAoPedido());

        painelQuantidade.add(rotuloQuantidade);
        painelQuantidade.add(spinnerQuantidade);
        painelQuantidade.add(botaoAdicionar);

        coluna.add(rotulo, BorderLayout.NORTH);
        coluna.add(scroll, BorderLayout.CENTER);
        coluna.add(painelQuantidade, BorderLayout.SOUTH);
        return coluna;
    }

    private JPanel montarColunaItensPedido() {
        JPanel coluna = new JPanel(new BorderLayout(0, 8));
        coluna.setBackground(ComponentesUi.COR_PAINEL);

        JLabel rotulo = ComponentesUi.criarRotuloCampo("Itens do Pedido");
        String[] colunas = {"Produto", "Quantidade", "Subtotal"};
        modeloItensPedido = new DefaultTableModel(colunas, 0) {
            public boolean isCellEditable(int linha, int coluna) { return false; }
        };
        JTable tabelaItens = ComponentesUi.criarTabelaEstilizada(modeloItensPedido, new Color(180, 100, 60));
        JScrollPane scroll = new JScrollPane(tabelaItens);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(80, 80, 110)));
        scroll.getViewport().setBackground(ComponentesUi.COR_PAINEL);

        rotuloTotal = new JLabel("Total: R$ 0,00");
        rotuloTotal.setFont(new Font("Segoe UI", Font.BOLD, 16));
        rotuloTotal.setForeground(ComponentesUi.COR_DESTAQUE);

        coluna.add(rotulo, BorderLayout.NORTH);
        coluna.add(scroll, BorderLayout.CENTER);
        coluna.add(rotuloTotal, BorderLayout.SOUTH);
        return coluna;
    }

    private void carregarCardapio() {
        modeloCardapio.setRowCount(0);
        List<Produto> cardapio = pedidoController.listarCardapioDisponivel();
        for (Produto produto : cardapio) {
            modeloCardapio.addRow(new Object[]{
                    produto.getId(), produto.getNome(),
                    String.format("R$ %.2f", produto.getPreco()),
                    produto.getQuantidade(), produto.getTipo()
            });
        }
    }

    private void adicionarItemAoPedido() {
        int linhaSelecionada = tabelaCardapio.getSelectedRow();
        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um produto no cardápio.");
            return;
        }
        int idProduto = (int) modeloCardapio.getValueAt(linhaSelecionada, 0);
        String nomeProduto = (String) modeloCardapio.getValueAt(linhaSelecionada, 1);
        int quantidade = (int) spinnerQuantidade.getValue();

        ResultadoOperacao resultado = pedidoController.adicionarItem(idProduto, quantidade);

        if (!resultado.isSucesso()) {
            JOptionPane.showMessageDialog(this, resultado.getMensagem(), "Não foi possível adicionar", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Produto produto = pedidoController.getPedidoEmAndamento().getItens().get(
                pedidoController.getPedidoEmAndamento().getItens().size() - 1).getProduto();
        double subtotal = produto.getPreco() * quantidade;
        modeloItensPedido.addRow(new Object[]{nomeProduto, quantidade, String.format("R$ %.2f", subtotal)});

        atualizarRotuloTotal();
        carregarCardapio();
    }

    private void atualizarRotuloTotal() {
        double total = pedidoController.calcularTotalAtual();
        if (pedidoController.pedidoAtualTemDesconto()) {
            rotuloTotal.setText(String.format("Total: R$ %.2f (10%% OFF aplicado)", pedidoController.calcularTotalComDescontoAtual()));
        } else {
            rotuloTotal.setText(String.format("Total: R$ %.2f", total));
        }
    }

    private void confirmarPedido() {
        ResultadoOperacao resultado = pedidoController.confirmarPedidoEAbrirFicha();

        if (!resultado.isSucesso()) {
            JOptionPane.showMessageDialog(this, resultado.getMensagem(), "Não foi possível confirmar", JOptionPane.WARNING_MESSAGE);
            return;
        }

        JOptionPane.showMessageDialog(this, resultado.getMensagem());
        dispose();
    }
}
