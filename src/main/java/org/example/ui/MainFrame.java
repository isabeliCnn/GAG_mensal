package org.example.ui;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(MainFrame.class.getName());

    public MainFrame() {
        initComponents();
    }

    private void initComponents() {
        setTitle("Forja Bar — Menu Principal");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(480, 400));
        setLocationRelativeTo(null);

        JPanel painelPrincipal = new JPanel(new BorderLayout());
        painelPrincipal.setBackground(new Color(30, 30, 40));
        setContentPane(painelPrincipal);

        // Cabeçalho
        JPanel cabecalho = new JPanel();
        cabecalho.setBackground(new Color(30, 30, 40));
        cabecalho.setBorder(BorderFactory.createEmptyBorder(28, 20, 12, 20));
        JLabel lblTitulo = new JLabel("🍶 Forja Bar");
        lblTitulo.setFont(new Font("Segoe UI Black", Font.BOLD, 28));
        lblTitulo.setForeground(new Color(255, 200, 80));
        JLabel lblSub = new JLabel("   Painel de Controle");
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblSub.setForeground(new Color(180, 180, 200));
        cabecalho.add(lblTitulo);
        cabecalho.add(lblSub);

        // Grid de botões
        JPanel painelBotoes = new JPanel(new GridLayout(3, 2, 12, 12));
        painelBotoes.setBackground(new Color(30, 30, 40));
        painelBotoes.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));

        painelBotoes.add(criarBotao("👤  Cadastrar Usuário", new Color(80, 120, 200), e -> {
            new CadastroUsuarioFrame().setVisible(true);
        }));
        painelBotoes.add(criarBotao("📦  Gerenciar Estoque", new Color(60, 160, 100), e -> {
            new EstoqueFrame().setVisible(true);
        }));
        painelBotoes.add(criarBotao("🛒  Novo Pedido", new Color(180, 100, 60), e -> {
            new PedidoFrame().setVisible(true);
        }));
        painelBotoes.add(criarBotao("🗒️  Ver Fichas", new Color(140, 80, 180), e -> {
            new FichaFrame().setVisible(true);
        }));
        painelBotoes.add(criarBotao("📋  Produtos", new Color(60, 150, 160), e -> {
            new ProdutoFrame().setVisible(true);
        }));
        painelBotoes.add(criarBotao("📊  Relatório do Dia", new Color(180, 140, 40), e -> {
            new RelatorioFrame().setVisible(true);
        }));

        // Rodapé
        JPanel rodape = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rodape.setBackground(new Color(30, 30, 40));
        rodape.setBorder(BorderFactory.createEmptyBorder(8, 20, 16, 20));
        JButton btnSair = new JButton("Sair do Sistema");
        btnSair.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        btnSair.setBackground(new Color(80, 40, 40));
        btnSair.setForeground(new Color(220, 150, 150));
        btnSair.setFocusPainted(false);
        btnSair.setBorderPainted(false);
        btnSair.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSair.addActionListener(e -> System.exit(0));
        rodape.add(btnSair);

        painelPrincipal.add(cabecalho, BorderLayout.NORTH);
        painelPrincipal.add(painelBotoes, BorderLayout.CENTER);
        painelPrincipal.add(rodape, BorderLayout.SOUTH);

        pack();
        setMinimumSize(new Dimension(480, 400));
    }

    private JButton criarBotao(String texto, Color cor, java.awt.event.ActionListener acao) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBackground(cor);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(180, 60));
        btn.addActionListener(acao);
        return btn;
    }

    public static void main(String args[]) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) { }
        java.awt.EventQueue.invokeLater(() -> new MainFrame().setVisible(true));
    }
}
