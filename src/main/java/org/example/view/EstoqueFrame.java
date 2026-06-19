package org.example.view;

import org.example.controller.EstoqueController;
import org.example.util.AppContext;
import org.example.util.ResultadoOperacao;

import javax.swing.*;
import java.awt.*;

public class EstoqueFrame extends JFrame {

    private final EstoqueController estoqueController;

    private JTextField campoNome;
    private JTextField campoQuantidade;
    private JTextField campoPreco;
    private JComboBox<String> comboTipo;

    public EstoqueFrame() {
        this.estoqueController = AppContext.estoqueController();
        montarTela();
    }

    private void montarTela() {
        setTitle("Forja Bar — Cadastro de Produto");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setMinimumSize(new Dimension(420, 420));
        setLocationRelativeTo(null);

        JPanel painelPrincipal = new JPanel(new BorderLayout());
        painelPrincipal.setBackground(ComponentesUi.COR_FUNDO);
        setContentPane(painelPrincipal);

        JPanel cabecalho = ComponentesUi.criarCabecalho("Cadastro de Produto", new Color(60, 160, 100));

        JPanel painelFormulario = new JPanel();
        painelFormulario.setLayout(new BoxLayout(painelFormulario, BoxLayout.Y_AXIS));
        painelFormulario.setBackground(ComponentesUi.COR_PAINEL);
        painelFormulario.setBorder(BorderFactory.createEmptyBorder(24, 32, 24, 32));

        campoNome = ComponentesUi.criarCampoTexto();
        campoQuantidade = ComponentesUi.criarCampoTexto();
        campoPreco = ComponentesUi.criarCampoTexto();
        comboTipo = ComponentesUi.criarComboBox("BEBIDA", "COMIDAS");

        painelFormulario.add(ComponentesUi.criarRotuloCampo("Nome do Produto"));
        painelFormulario.add(Box.createVerticalStrut(4));
        painelFormulario.add(campoNome);
        painelFormulario.add(Box.createVerticalStrut(14));

        painelFormulario.add(ComponentesUi.criarRotuloCampo("Quantidade em Estoque"));
        painelFormulario.add(Box.createVerticalStrut(4));
        painelFormulario.add(campoQuantidade);
        painelFormulario.add(Box.createVerticalStrut(14));

        painelFormulario.add(ComponentesUi.criarRotuloCampo("Preço (R$)"));
        painelFormulario.add(Box.createVerticalStrut(4));
        painelFormulario.add(campoPreco);
        painelFormulario.add(Box.createVerticalStrut(14));

        painelFormulario.add(ComponentesUi.criarRotuloCampo("Tipo do Produto"));
        painelFormulario.add(Box.createVerticalStrut(4));
        painelFormulario.add(comboTipo);
        painelFormulario.add(Box.createVerticalStrut(24));

        JPanel painelBotoes = new JPanel(new GridLayout(1, 2, 10, 0));
        painelBotoes.setBackground(ComponentesUi.COR_PAINEL);
        painelBotoes.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        JButton botaoVoltar = ComponentesUi.criarBotaoSecundario("Voltar ao Menu");
        botaoVoltar.addActionListener(e -> dispose());

        JButton botaoSalvar = ComponentesUi.criarBotaoPrimario("Salvar Produto", new Color(60, 160, 100), Color.WHITE);
        botaoSalvar.addActionListener(e -> salvarProduto());

        painelBotoes.add(botaoVoltar);
        painelBotoes.add(botaoSalvar);
        painelFormulario.add(painelBotoes);

        painelPrincipal.add(cabecalho, BorderLayout.NORTH);
        painelPrincipal.add(painelFormulario, BorderLayout.CENTER);
        pack();
    }

    private void salvarProduto() {
        String nome = campoNome.getText();
        String quantidadeTexto = campoQuantidade.getText();
        String precoTexto = campoPreco.getText();
        String tipo = (String) comboTipo.getSelectedItem();

        ResultadoOperacao resultado = estoqueController.cadastrarProduto(nome, precoTexto, quantidadeTexto, tipo);

        if (!resultado.isSucesso()) {
            JOptionPane.showMessageDialog(this, resultado.getMensagem(), "Não foi possível salvar", JOptionPane.WARNING_MESSAGE);
            return;
        }

        JOptionPane.showMessageDialog(this, resultado.getMensagem());
        campoNome.setText("");
        campoQuantidade.setText("");
        campoPreco.setText("");
    }
}
