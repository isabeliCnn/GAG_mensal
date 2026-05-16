package org.example.ui;

import javax.swing.*;
import java.awt.*;

public class EstoqueFrame extends JFrame {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(EstoqueFrame.class.getName());

    private JTextField txtNome;
    private JTextField txtQuantidade;
    private JTextField txtPreco;
    private JComboBox<String> cbTipo;

    public EstoqueFrame() {
        initComponents();
    }

    private void initComponents() {
        setTitle("Forja Bar — Cadastro de Estoque");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setMinimumSize(new Dimension(420, 400));
        setLocationRelativeTo(null);

        JPanel painelPrincipal = new JPanel(new BorderLayout());
        painelPrincipal.setBackground(new Color(30, 30, 40));
        setContentPane(painelPrincipal);

        // Cabeçalho
        JPanel cabecalho = new JPanel();
        cabecalho.setBackground(new Color(60, 160, 100));
        cabecalho.setBorder(BorderFactory.createEmptyBorder(16, 20, 16, 20));
        JLabel lblTitulo = new JLabel("📦 Cadastro de Estoque");
        lblTitulo.setFont(new Font("Segoe UI Black", Font.BOLD, 18));
        lblTitulo.setForeground(Color.WHITE);
        cabecalho.add(lblTitulo);

        // Formulário
        JPanel painelForm = new JPanel();
        painelForm.setLayout(new BoxLayout(painelForm, BoxLayout.Y_AXIS));
        painelForm.setBackground(new Color(45, 45, 60));
        painelForm.setBorder(BorderFactory.createEmptyBorder(24, 32, 24, 32));

        painelForm.add(criarLabel("Nome do Produto"));
        painelForm.add(Box.createVerticalStrut(4));
        txtNome = criarCampo();
        painelForm.add(txtNome);
        painelForm.add(Box.createVerticalStrut(14));

        painelForm.add(criarLabel("Quantidade em Estoque"));
        painelForm.add(Box.createVerticalStrut(4));
        txtQuantidade = criarCampo();
        painelForm.add(txtQuantidade);
        painelForm.add(Box.createVerticalStrut(14));

        painelForm.add(criarLabel("Preço (R$)"));
        painelForm.add(Box.createVerticalStrut(4));
        txtPreco = criarCampo();
        painelForm.add(txtPreco);
        painelForm.add(Box.createVerticalStrut(14));

        painelForm.add(criarLabel("Tipo do Produto"));
        painelForm.add(Box.createVerticalStrut(4));
        cbTipo = new JComboBox<>(new String[]{"BEBIDA", "COMIDAS"});
        cbTipo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cbTipo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        cbTipo.setBackground(new Color(60, 60, 80));
        cbTipo.setForeground(Color.WHITE);
        painelForm.add(cbTipo);
        painelForm.add(Box.createVerticalStrut(24));

        // Botões
        JPanel painelBotoes = new JPanel(new GridLayout(1, 2, 10, 0));
        painelBotoes.setBackground(new Color(45, 45, 60));
        painelBotoes.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        JButton btnVoltar = new JButton("Voltar");
        btnVoltar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnVoltar.setBackground(new Color(80, 80, 100));
        btnVoltar.setForeground(Color.WHITE);
        btnVoltar.setFocusPainted(false);
        btnVoltar.setBorderPainted(false);
        btnVoltar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnVoltar.addActionListener(e -> dispose());

        JButton btnSalvar = new JButton("Salvar Produto");
        btnSalvar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnSalvar.setBackground(new Color(60, 160, 100));
        btnSalvar.setForeground(Color.WHITE);
        btnSalvar.setFocusPainted(false);
        btnSalvar.setBorderPainted(false);
        btnSalvar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSalvar.addActionListener(e -> salvar());

        painelBotoes.add(btnVoltar);
        painelBotoes.add(btnSalvar);
        painelForm.add(painelBotoes);

        painelPrincipal.add(cabecalho, BorderLayout.NORTH);
        painelPrincipal.add(painelForm, BorderLayout.CENTER);
        pack();
    }

    private JLabel criarLabel(String texto) {
        JLabel label = new JLabel(texto);
        label.setFont(new Font("Segoe UI", Font.BOLD, 12));
        label.setForeground(new Color(200, 200, 220));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    private JTextField criarCampo() {
        JTextField campo = new JTextField();
        campo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        campo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        campo.setBackground(new Color(60, 60, 80));
        campo.setForeground(Color.WHITE);
        campo.setCaretColor(Color.WHITE);
        campo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(100, 100, 140)),
                BorderFactory.createEmptyBorder(4, 8, 4, 8)));
        return campo;
    }

    private void salvar() {
        String nome = txtNome.getText();
        String quantidadeStr = txtQuantidade.getText();
        String precoStr = txtPreco.getText();

        if (nome.isEmpty() || quantidadeStr.isEmpty() || precoStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Preencha todos os campos!");
            return;
        }

        try {
            int quantidade = Integer.parseInt(quantidadeStr);
            double preco = Double.parseDouble(precoStr.replace(",", "."));

            if (quantidade <= 5) {
                JOptionPane.showMessageDialog(this,
                        "Atenção: estoque crítico (" + quantidade + " unidades)!",
                        "Alerta de Estoque", JOptionPane.WARNING_MESSAGE);
            }

            org.example.model.TipoProduto tipo = cbTipo.getSelectedItem().toString().equals("BEBIDA")
                    ? org.example.model.TipoProduto.BEBIDA
                    : org.example.model.TipoProduto.COMIDAS;

            org.example.model.Produto produto = new org.example.model.Produto(nome, preco, quantidade, tipo);
            org.example.service.EstoqueService service = new org.example.service.EstoqueService(new org.example.repository.ProdutoRepo());
            service.adicionarProduto(produto);

            JOptionPane.showMessageDialog(this, "Produto '" + nome + "' salvo com sucesso!");
            txtNome.setText("");
            txtQuantidade.setText("");
            txtPreco.setText("");
        } catch (NumberFormatException erro) {
            JOptionPane.showMessageDialog(this, "Digite apenas números válidos!", "Atenção", JOptionPane.WARNING_MESSAGE);
        }
    }
}
