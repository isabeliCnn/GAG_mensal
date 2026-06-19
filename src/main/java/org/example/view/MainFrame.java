package org.example.view;

import org.example.model.Usuario;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    private final Usuario usuarioLogado;

    public MainFrame(Usuario usuarioLogado) {
        this.usuarioLogado = usuarioLogado;
        montarTela();
    }

    private void montarTela() {
        setTitle("Forja Bar — Menu Principal");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(480, 420));
        setLocationRelativeTo(null);

        JPanel painelPrincipal = new JPanel(new BorderLayout());
        painelPrincipal.setBackground(ComponentesUi.COR_FUNDO);
        setContentPane(painelPrincipal);

        JPanel cabecalho = new JPanel(new GridLayout(2, 1));
        cabecalho.setBackground(ComponentesUi.COR_FUNDO);
        cabecalho.setBorder(BorderFactory.createEmptyBorder(28, 20, 12, 20));
        JLabel rotuloTitulo = new JLabel("Forja Bar");
        rotuloTitulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        rotuloTitulo.setForeground(ComponentesUi.COR_DESTAQUE);
        JLabel rotuloUsuario = new JLabel("Logado como: " + usuarioLogado.getLogin() + " (" + usuarioLogado.getPerfil() + ")");
        rotuloUsuario.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        rotuloUsuario.setForeground(new Color(180, 180, 200));
        cabecalho.add(rotuloTitulo);
        cabecalho.add(rotuloUsuario);

        JPanel painelBotoes = new JPanel(new GridLayout(3, 2, 12, 12));
        painelBotoes.setBackground(ComponentesUi.COR_FUNDO);
        painelBotoes.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));

        painelBotoes.add(criarBotaoMenu("Cadastrar Usuário", new Color(80, 120, 200), e -> new CadastroUsuarioFrame().setVisible(true)));
        painelBotoes.add(criarBotaoMenu("Gerenciar Estoque", new Color(60, 160, 100), e -> new EstoqueFrame().setVisible(true)));
        painelBotoes.add(criarBotaoMenu("Novo Pedido", new Color(180, 100, 60), e -> new PedidoFrame().setVisible(true)));
        painelBotoes.add(criarBotaoMenu("Fechar Ficha", new Color(140, 80, 180), e -> new FichaFrame().setVisible(true)));
        painelBotoes.add(criarBotaoMenu("Lista de Produtos", new Color(60, 150, 160), e -> new ProdutoFrame().setVisible(true)));
        painelBotoes.add(criarBotaoMenu("Relatório do Dia", new Color(180, 140, 40), e -> new RelatorioFrame().setVisible(true)));

        JPanel rodape = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rodape.setBackground(ComponentesUi.COR_FUNDO);
        rodape.setBorder(BorderFactory.createEmptyBorder(8, 20, 16, 20));
        JButton botaoSair = new JButton("Sair do Sistema");
        botaoSair.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        botaoSair.setBackground(new Color(80, 40, 40));
        botaoSair.setForeground(new Color(220, 150, 150));
        botaoSair.setFocusPainted(false);
        botaoSair.setBorderPainted(false);
        botaoSair.setCursor(new Cursor(Cursor.HAND_CURSOR));
        botaoSair.addActionListener(e -> System.exit(0));
        rodape.add(botaoSair);

        painelPrincipal.add(cabecalho, BorderLayout.NORTH);
        painelPrincipal.add(painelBotoes, BorderLayout.CENTER);
        painelPrincipal.add(rodape, BorderLayout.SOUTH);

        pack();
        setMinimumSize(new Dimension(480, 420));
    }

    private JButton criarBotaoMenu(String texto, Color cor, java.awt.event.ActionListener acao) {
        JButton botao = new JButton(texto);
        botao.setFont(new Font("Segoe UI", Font.BOLD, 13));
        botao.setBackground(cor);
        botao.setForeground(Color.WHITE);
        botao.setFocusPainted(false);
        botao.setBorderPainted(false);
        botao.setCursor(new Cursor(Cursor.HAND_CURSOR));
        botao.setPreferredSize(new Dimension(180, 60));
        botao.addActionListener(acao);
        return botao;
    }
}
