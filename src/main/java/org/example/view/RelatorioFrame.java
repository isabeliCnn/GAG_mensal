package org.example.view;

import org.example.controller.RelatorioController;
import org.example.model.Pedido;
import org.example.util.AppContext;
import org.example.util.RelatorioDoDia;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class RelatorioFrame extends JFrame {

    private final RelatorioController relatorioController;

    private DefaultTableModel modeloTabela;
    private JLabel rotuloTotalGeral;
    private JLabel rotuloTotalPedidos;
    private JLabel rotuloProdutoMaisVendido;

    public RelatorioFrame() {
        this.relatorioController = AppContext.relatorioController();
        montarTela();
        carregarRelatorio();
    }

    private void montarTela() {
        setTitle("Forja Bar — Relatório do Dia");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setMinimumSize(new Dimension(680, 500));
        setLocationRelativeTo(null);

        JPanel painelPrincipal = new JPanel(new BorderLayout());
        painelPrincipal.setBackground(ComponentesUi.COR_FUNDO);
        setContentPane(painelPrincipal);

        JPanel cabecalho = ComponentesUi.criarCabecalho("Relatório do Dia", new Color(180, 140, 40));

        String[] colunas = {"Pedido", "Status", "Total (R$)", "Itens"};
        modeloTabela = new DefaultTableModel(colunas, 0) {
            public boolean isCellEditable(int linha, int coluna) { return false; }
        };
        JTable tabela = ComponentesUi.criarTabelaEstilizada(modeloTabela, new Color(180, 140, 40));
        JScrollPane scroll = new JScrollPane(tabela);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(ComponentesUi.COR_PAINEL);

        JPanel painelTotais = new JPanel(new GridLayout(3, 1, 0, 4));
        painelTotais.setBackground(new Color(35, 35, 50));
        painelTotais.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));

        rotuloTotalPedidos = new JLabel("Total de pedidos: 0");
        rotuloTotalPedidos.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        rotuloTotalPedidos.setForeground(ComponentesUi.COR_TEXTO_ROTULO);

        rotuloProdutoMaisVendido = new JLabel("Produto mais vendido: —");
        rotuloProdutoMaisVendido.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        rotuloProdutoMaisVendido.setForeground(ComponentesUi.COR_TEXTO_ROTULO);

        rotuloTotalGeral = new JLabel("Total geral: R$ 0,00");
        rotuloTotalGeral.setFont(new Font("Segoe UI", Font.BOLD, 16));
        rotuloTotalGeral.setForeground(ComponentesUi.COR_DESTAQUE);

        painelTotais.add(rotuloTotalPedidos);
        painelTotais.add(rotuloProdutoMaisVendido);
        painelTotais.add(rotuloTotalGeral);

        JPanel rodape = new JPanel(new GridLayout(1, 2, 10, 0));
        rodape.setBackground(ComponentesUi.COR_FUNDO);
        rodape.setBorder(BorderFactory.createEmptyBorder(10, 20, 16, 20));

        JButton botaoAtualizar = ComponentesUi.criarBotaoPrimario("Atualizar Relatório", new Color(180, 140, 40), Color.WHITE);
        botaoAtualizar.addActionListener(e -> carregarRelatorio());

        JButton botaoVoltar = ComponentesUi.criarBotaoSecundario("Voltar ao Menu");
        botaoVoltar.addActionListener(e -> dispose());

        rodape.add(botaoAtualizar);
        rodape.add(botaoVoltar);

        JPanel sul = new JPanel(new BorderLayout());
        sul.setBackground(ComponentesUi.COR_FUNDO);
        sul.add(painelTotais, BorderLayout.NORTH);
        sul.add(rodape, BorderLayout.SOUTH);

        painelPrincipal.add(cabecalho, BorderLayout.NORTH);
        painelPrincipal.add(scroll, BorderLayout.CENTER);
        painelPrincipal.add(sul, BorderLayout.SOUTH);
        pack();
    }

    private void carregarRelatorio() {
        modeloTabela.setRowCount(0);
        RelatorioDoDia relatorio = relatorioController.gerarRelatorioDoDia();

        for (Pedido pedido : relatorio.getPedidos()) {
            modeloTabela.addRow(new Object[]{
                    "#" + pedido.getId(),
                    pedido.getStatus(),
                    String.format("%.2f", pedido.calcularTotalComDesconto()),
                    pedido.getItens().size()
            });
        }

        rotuloTotalPedidos.setText("Total de pedidos: " + relatorio.getPedidos().size());
        rotuloTotalGeral.setText(String.format("Total geral: R$ %.2f", relatorio.getTotalGeral()));
        rotuloProdutoMaisVendido.setText(
                "Produto mais vendido: " + relatorio.getProdutoMaisVendido().orElse("—"));
    }
}
