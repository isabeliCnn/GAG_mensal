package org.example.ui;

import javax.swing.*;
import java.awt.*;

public class CadastroUsuarioFrame extends JFrame {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(CadastroUsuarioFrame.class.getName());

    private JTextField txtLogin;
    private JPasswordField txtSenha;
    private JComboBox<String> cbPerfil;

    public CadastroUsuarioFrame() {
        initComponents();
    }

    private void initComponents() {
        setTitle("Forja Bar — Cadastro de Usuário");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setMinimumSize(new Dimension(420, 360));
        setLocationRelativeTo(null);

        JPanel painelPrincipal = new JPanel(new BorderLayout());
        painelPrincipal.setBackground(new Color(30, 30, 40));
        setContentPane(painelPrincipal);

        // Cabeçalho
        JPanel cabecalho = new JPanel();
        cabecalho.setBackground(new Color(80, 120, 200));
        cabecalho.setBorder(BorderFactory.createEmptyBorder(16, 20, 16, 20));
        JLabel lblTitulo = new JLabel("👤 Cadastro de Usuário");
        lblTitulo.setFont(new Font("Segoe UI Black", Font.BOLD, 18));
        lblTitulo.setForeground(Color.WHITE);
        cabecalho.add(lblTitulo);

        // Formulário
        JPanel painelForm = new JPanel();
        painelForm.setLayout(new BoxLayout(painelForm, BoxLayout.Y_AXIS));
        painelForm.setBackground(new Color(45, 45, 60));
        painelForm.setBorder(BorderFactory.createEmptyBorder(24, 32, 24, 32));

        painelForm.add(criarLabel("Login"));
        painelForm.add(Box.createVerticalStrut(4));
        txtLogin = criarCampo();
        painelForm.add(txtLogin);
        painelForm.add(Box.createVerticalStrut(14));

        painelForm.add(criarLabel("Senha"));
        painelForm.add(Box.createVerticalStrut(4));
        txtSenha = new JPasswordField();
        estilizarCampo(txtSenha);
        painelForm.add(txtSenha);
        painelForm.add(Box.createVerticalStrut(14));

        painelForm.add(criarLabel("Perfil"));
        painelForm.add(Box.createVerticalStrut(4));
        cbPerfil = new JComboBox<>(new String[]{"ADMIN", "FUNCIONARIO"});
        cbPerfil.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cbPerfil.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        cbPerfil.setBackground(new Color(60, 60, 80));
        cbPerfil.setForeground(Color.WHITE);
        painelForm.add(cbPerfil);
        painelForm.add(Box.createVerticalStrut(24));

        JButton btnCadastrar = new JButton("Cadastrar");
        btnCadastrar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnCadastrar.setBackground(new Color(80, 120, 200));
        btnCadastrar.setForeground(Color.WHITE);
        btnCadastrar.setFocusPainted(false);
        btnCadastrar.setBorderPainted(false);
        btnCadastrar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCadastrar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        btnCadastrar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnCadastrar.addActionListener(e -> cadastrar());
        painelForm.add(btnCadastrar);

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
        estilizarCampo(campo);
        return campo;
    }

    private void estilizarCampo(JTextField campo) {
        campo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        campo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        campo.setBackground(new Color(60, 60, 80));
        campo.setForeground(Color.WHITE);
        campo.setCaretColor(Color.WHITE);
        campo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(100, 100, 140)),
                BorderFactory.createEmptyBorder(4, 8, 4, 8)));
    }

    private void cadastrar() {
        String login = txtLogin.getText();
        String senha = new String(txtSenha.getPassword());
        String perfil = cbPerfil.getSelectedItem().toString();

        if (login.isEmpty() || senha.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Preencha todos os campos!", "Atenção", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            org.example.repository.UsuarioRepository repo = new org.example.repository.UsuarioRepository();
            if (repo.buscarPorLogin(login) != null) {
                JOptionPane.showMessageDialog(this, "Login já existe!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
            org.example.model.Usuario usuario = new org.example.model.Usuario(login, senha, perfil);
            repo.salvar(usuario);
            JOptionPane.showMessageDialog(this, "Usuário '" + login + "' cadastrado com sucesso!");
            txtLogin.setText("");
            txtSenha.setText("");
        } catch (Exception erro) {
            JOptionPane.showMessageDialog(this, "Erro ao cadastrar: " + erro.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}
