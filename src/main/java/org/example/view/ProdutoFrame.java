package org.example.view;

import org.example.controller.EstoqueController;
import org.example.model.Produto;
import org.example.util.AppContext;
import org.example.util.ResultadoOperacao;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ProdutoFrame extends JFrame {

    private final EstoqueController estoqueController;
    private DefaultTableModel modeloTabela;
    private JTable tabelaProdutos;

    public ProdutoFrame() {
        this.estoqueController = AppContext.estoqueController();
        montarTela();
        carregarProdutos();
    }

    private void montarTela() {
        setTitle("Forja Bar — Lista de Produtos");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setMinimumSize(new Dimension(680, 450));
        setLocationRelativeTo(null);

        JPanel painelPrincipal = new JPanel(new BorderLayout());
        painelPrincipal.setBackground(ComponentesUi.COR_FUNDO);
        setContentPane(painelPrincipal);

        JPanel cabecalho = ComponentesUi.criarCabecalho("Lista de Produtos", new Color(60, 150, 160));

        String[] colunas = {"ID", "Nome", "Preço (R$)", "Estoque", "Tipo"};
        modeloTabela = new DefaultTableModel(colunas, 0) {
            public boolean isCellEditable(int linha, int coluna) { return false; }
        };
        tabelaProdutos = ComponentesUi.criarTabelaEstilizada(modeloTabela, new Color(60, 150, 160));

        JScrollPane scroll = new JScrollPane(tabelaProdutos);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(ComponentesUi.COR_PAINEL);

        JPanel rodape = new JPanel(new GridLayout(1, 3, 10, 0));
        rodape.setBackground(ComponentesUi.COR_FUNDO);
        rodape.setBorder(BorderFactory.createEmptyBorder(10, 20, 16, 20));

        JButton botaoAtualizar = ComponentesUi.criarBotaoPrimario("Atualizar Lista", new Color(60, 150, 160), Color.WHITE);
        botaoAtualizar.addActionListener(e -> carregarProdutos());

        JButton botaoRemover = ComponentesUi.criarBotaoPrimario("Remover Produto", new Color(160, 60, 60), Color.WHITE);
        botaoRemover.addActionListener(e -> removerProdutoSelecionado());

        JButton botaoVoltar = ComponentesUi.criarBotaoSecundario("Voltar ao Menu");
        botaoVoltar.addActionListener(e -> dispose());

        rodape.add(botaoAtualizar);
        rodape.add(botaoRemover);
        rodape.add(botaoVoltar);

        painelPrincipal.add(cabecalho, BorderLayout.NORTH);
        painelPrincipal.add(scroll, BorderLayout.CENTER);
        painelPrincipal.add(rodape, BorderLayout.SOUTH);
        pack();
    }

    private void carregarProdutos() {
        modeloTabela.setRowCount(0);
        List<Produto> produtos = estoqueController.listarTodos();
        for (Produto produto : produtos) {
            modeloTabela.addRow(new Object[]{
                    produto.getId(),
                    produto.getNome(),
                    String.format("%.2f", produto.getPreco()),
                    produto.getQuantidade(),
                    produto.getTipo()
            });
        }
    }

    private void removerProdutoSelecionado() {
        int linhaSelecionada = tabelaProdutos.getSelectedRow();
        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um produto na tabela.");
            return;
        }
        int id = (int) modeloTabela.getValueAt(linhaSelecionada, 0);
        String nome = (String) modeloTabela.getValueAt(linhaSelecionada, 1);

        int confirmacao = JOptionPane.showConfirmDialog(this,
                "Remover o produto '" + nome + "'?", "Confirmar remoção", JOptionPane.YES_NO_OPTION);
        if (confirmacao != JOptionPane.YES_OPTION) {
            return;
        }

        ResultadoOperacao resultado = estoqueController.removerProduto(id);
        JOptionPane.showMessageDialog(this, resultado.getMensagem());
        if (resultado.isSucesso()) {
            carregarProdutos();
        }
    }
}
