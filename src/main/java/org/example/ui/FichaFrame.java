package org.example.ui;

import javax.swing.*;
import java.awt.*;

public class FichaFrame extends JFrame {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(FichaFrame.class.getName());

    private JTextField txtIdentificador;
    private JTextField txtProduto;
    private JComboBox<String> cbTipo;
    private JTextField txtQuantidade;
    private JTextField txtValor;
    private JTextArea txtResumo;
    private JLabel lblTotal;
    private double totalFicha = 0.0;

    public FichaFrame() {
        initComponents();
    }

    private void initComponents() {
        setTitle("Forja Bar — Ficha de Consumo");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setMinimumSize(new Dimension(700, 480));
        setLocationRelativeTo(null);

        JPanel painelPrincipal = new JPanel(new BorderLayout());
        painelPrincipal.setBackground(new Color(30, 30, 40));
        setContentPane(painelPrincipal);

        // Cabeçalho
        JPanel cabecalho = new JPanel();
        cabecalho.setBackground(new Color(140, 80, 180));
        cabecalho.setBorder(BorderFactory.createEmptyBorder(14, 20, 14, 20));
        JLabel lblTitulo = new JLabel("🗒️ Ficha de Consumo");
        lblTitulo.setFont(new Font("Segoe UI Black", Font.BOLD, 18));
        lblTitulo.setForeground(Color.WHITE);
        cabecalho.add(lblTitulo);

        // Corpo principal em duas colunas
        JPanel corpo = new JPanel(new GridLayout(1, 2, 12, 0));
        corpo.setBackground(new Color(45, 45, 60));
        corpo.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Coluna esquerda — formulário
        JPanel colEsq = new JPanel();
        colEsq.setLayout(new BoxLayout(colEsq, BoxLayout.Y_AXIS));
        colEsq.setBackground(new Color(45, 45, 60));

        colEsq.add(criarLabel("🪑 Mesa ou Nome do Cliente"));
        colEsq.add(Box.createVerticalStrut(4));
        txtIdentificador = criarCampo();
        colEsq.add(txtIdentificador);
        colEsq.add(Box.createVerticalStrut(16));

        JSeparator sep = new JSeparator();
        sep.setForeground(new Color(100, 100, 130));
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        colEsq.add(sep);
        colEsq.add(Box.createVerticalStrut(12));

        colEsq.add(criarLabel("Produto (Ex: Sushi, Chopp)"));
        colEsq.add(Box.createVerticalStrut(4));
        txtProduto = criarCampo();
        colEsq.add(txtProduto);
        colEsq.add(Box.createVerticalStrut(10));

        colEsq.add(criarLabel("Tipo"));
        colEsq.add(Box.createVerticalStrut(4));
        cbTipo = new JComboBox<>(new String[]{"Sushis e Temakis", "Drinks e Bebidas"});
        cbTipo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cbTipo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));
        cbTipo.setBackground(new Color(60, 60, 80));
        cbTipo.setForeground(Color.WHITE);
        colEsq.add(cbTipo);
        colEsq.add(Box.createVerticalStrut(10));

        colEsq.add(criarLabel("Quantidade"));
        colEsq.add(Box.createVerticalStrut(4));
        txtQuantidade = criarCampo();
        colEsq.add(txtQuantidade);
        colEsq.add(Box.createVerticalStrut(10));

        colEsq.add(criarLabel("Valor Unitário (R$)"));
        colEsq.add(Box.createVerticalStrut(4));
        txtValor = criarCampo();
        colEsq.add(txtValor);

        // Coluna direita — resumo
        JPanel colDir = new JPanel(new BorderLayout(0, 8));
        colDir.setBackground(new Color(45, 45, 60));

        JLabel lblResumo = criarLabel("📋 Resumo do Consumo");
        colDir.add(lblResumo, BorderLayout.NORTH);

        txtResumo = new JTextArea();
        txtResumo.setEditable(false);
        txtResumo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtResumo.setBackground(new Color(35, 35, 50));
        txtResumo.setForeground(new Color(200, 220, 200));
        txtResumo.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        JScrollPane scroll = new JScrollPane(txtResumo);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(100, 100, 140)));
        colDir.add(scroll, BorderLayout.CENTER);

        lblTotal = new JLabel("Total: R$ 0,00");
        lblTotal.setFont(new Font("Segoe UI Black", Font.BOLD, 16));
        lblTotal.setForeground(new Color(255, 200, 80));
        colDir.add(lblTotal, BorderLayout.SOUTH);

        corpo.add(colEsq);
        corpo.add(colDir);

        // Rodapé com botões
        JPanel rodape = new JPanel(new GridLayout(1, 2, 10, 0));
        rodape.setBackground(new Color(30, 30, 40));
        rodape.setBorder(BorderFactory.createEmptyBorder(10, 20, 16, 20));

        JButton btnVoltar = new JButton("Voltar ao Menu");
        btnVoltar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnVoltar.setBackground(new Color(80, 80, 100));
        btnVoltar.setForeground(Color.WHITE);
        btnVoltar.setFocusPainted(false);
        btnVoltar.setBorderPainted(false);
        btnVoltar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnVoltar.addActionListener(e -> dispose());

        JButton btnAdicionar = new JButton("➕ Adicionar Item");
        btnAdicionar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnAdicionar.setBackground(new Color(140, 80, 180));
        btnAdicionar.setForeground(Color.WHITE);
        btnAdicionar.setFocusPainted(false);
        btnAdicionar.setBorderPainted(false);
        btnAdicionar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAdicionar.addActionListener(e -> adicionarItem());

        rodape.add(btnVoltar);
        rodape.add(btnAdicionar);

        painelPrincipal.add(cabecalho, BorderLayout.NORTH);
        painelPrincipal.add(corpo, BorderLayout.CENTER);
        painelPrincipal.add(rodape, BorderLayout.SOUTH);
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
        campo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        campo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));
        campo.setBackground(new Color(60, 60, 80));
        campo.setForeground(Color.WHITE);
        campo.setCaretColor(Color.WHITE);
        campo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(100, 100, 140)),
                BorderFactory.createEmptyBorder(4, 8, 4, 8)));
        return campo;
    }

    private void adicionarItem() {
        String identificador = txtIdentificador.getText();
        String produto = txtProduto.getText();
        String tipo = cbTipo.getSelectedItem().toString();
        String quantidadeStr = txtQuantidade.getText();
        String valorStr = txtValor.getText();

        if (identificador.isEmpty() || produto.isEmpty() || quantidadeStr.isEmpty() || valorStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Preencha todos os campos do pedido!");
            return;
        }

        try {
            int quantidade = Integer.parseInt(quantidadeStr);
            double valorUnitario = Double.parseDouble(valorStr.replace(",", "."));
            double subtotalItem = quantidade * valorUnitario;
            totalFicha += subtotalItem;

            double valorACobrar = totalFicha;
            String avisoDesconto = "";
            if (totalFicha > 100.0) {
                valorACobrar = totalFicha * 0.90;
                avisoDesconto = " ✅ 10% OFF aplicado!";
            }

            txtResumo.append(quantidade + "x " + produto + " (" + tipo + ") — R$ " + String.format("%.2f", subtotalItem) + "\n");
            lblTotal.setText("Total: R$ " + String.format("%.2f", valorACobrar) + avisoDesconto);

            txtProduto.setText("");
            txtQuantidade.setText("");
            txtValor.setText("");
            txtProduto.requestFocus();

        } catch (NumberFormatException erro) {
            JOptionPane.showMessageDialog(this, "Digite apenas números válidos na quantidade e no valor!");
        }
    }
}
