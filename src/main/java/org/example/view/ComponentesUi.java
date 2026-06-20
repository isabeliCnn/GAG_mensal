package org.example.view;

import javax.swing.*;
import java.awt.*;

/**
 * Componentes visuais reaproveitáveis entre as telas, evitando que cada
 * Frame repita o mesmo código de estilização (cores, fontes, bordas).
 */
final class ComponentesUi {

    static final Color COR_FUNDO = new Color(30, 30, 40);
    static final Color COR_PAINEL = new Color(45, 45, 60);
    static final Color COR_CAMPO = new Color(60, 60, 80);
    static final Color COR_BORDA_CAMPO = new Color(100, 100, 140);
    static final Color COR_TEXTO_CLARO = new Color(220, 220, 220);
    static final Color COR_TEXTO_ROTULO = new Color(200, 200, 220);
    static final Color COR_DESTAQUE = new Color(255, 200, 80);

    private ComponentesUi() {
    }

    static JTextField criarCampoTexto() {
        JTextField campo = new JTextField();
        estilizarCampo(campo);
        return campo;
    }

    static void estilizarCampo(JTextField campo) {
        campo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        campo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        campo.setBackground(COR_CAMPO);
        campo.setForeground(Color.WHITE);
        campo.setCaretColor(Color.WHITE);
        campo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COR_BORDA_CAMPO),
                BorderFactory.createEmptyBorder(6, 8, 6, 8)));
    }

    static JLabel criarRotuloCampo(String texto) {
        JLabel rotulo = new JLabel(texto);
        rotulo.setFont(new Font("Segoe UI", Font.BOLD, 12));
        rotulo.setForeground(COR_TEXTO_ROTULO);
        rotulo.setAlignmentX(Component.LEFT_ALIGNMENT);
        return rotulo;
    }

    static JComboBox<String> criarComboBox(String... opcoes) {
        JComboBox<String> combo = new JComboBox<>(opcoes);
        combo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        combo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        combo.setBackground(COR_CAMPO);
        combo.setForeground(Color.WHITE);
        return combo;
    }

    static JButton criarBotaoPrimario(String texto, Color cor, Color corTexto) {
        JButton botao = new JButton(texto);
        botao.setFont(new Font("Segoe UI", Font.BOLD, 14));
        botao.setBackground(cor);
        botao.setForeground(corTexto);
        botao.setFocusPainted(false);
        botao.setBorderPainted(false);
        botao.setCursor(new Cursor(Cursor.HAND_CURSOR));
        botao.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        return botao;
    }

    static JButton criarBotaoSecundario(String texto) {
        JButton botao = new JButton(texto);
        botao.setFont(new Font("Segoe UI", Font.BOLD, 13));
        botao.setBackground(new Color(80, 80, 100));
        botao.setForeground(Color.WHITE);
        botao.setFocusPainted(false);
        botao.setBorderPainted(false);
        botao.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return botao;
    }

    static JTable criarTabelaEstilizada(javax.swing.table.TableModel modelo, Color corSelecao) {
        JTable tabela = new JTable(modelo);
        tabela.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tabela.setRowHeight(28);
        tabela.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tabela.setBackground(COR_PAINEL);
        tabela.setForeground(COR_TEXTO_CLARO);
        tabela.getTableHeader().setBackground(new Color(35, 35, 50));
        tabela.getTableHeader().setForeground(COR_TEXTO_ROTULO);
        tabela.setSelectionBackground(corSelecao);
        tabela.setSelectionForeground(Color.WHITE);
        tabela.setGridColor(new Color(60, 60, 80));
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        return tabela;
    }

    static JPanel criarCabecalho(String titulo, Color corFundo) {
        JPanel cabecalho = new JPanel();
        cabecalho.setBackground(corFundo);
        cabecalho.setBorder(BorderFactory.createEmptyBorder(14, 20, 14, 20));
        JLabel rotulo = new JLabel(titulo);
        rotulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        rotulo.setForeground(Color.WHITE);
        cabecalho.add(rotulo);
        return cabecalho;
    }
}
