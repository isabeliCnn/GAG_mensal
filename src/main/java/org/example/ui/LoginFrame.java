package org.example.ui;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(LoginFrame.class.getName());

    private JTextField txtLogin;
    private JPasswordField txtSenha;
    private JButton btnEntrar;

    public LoginFrame() {
        initComponents();
    }

    private void initComponents() {
        setTitle("Forja Bar — Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(400, 320));
        setLocationRelativeTo(null);

        // Painel principal com cor de fundo
        JPanel painelPrincipal = new JPanel(new GridBagLayout());
        painelPrincipal.setBackground(new Color(30, 30, 40));
        setContentPane(painelPrincipal);

        // Painel do formulário
        JPanel painelForm = new JPanel();
        painelForm.setLayout(new BoxLayout(painelForm, BoxLayout.Y_AXIS));
        painelForm.setBackground(new Color(45, 45, 60));
        painelForm.setBorder(BorderFactory.createEmptyBorder(32, 40, 32, 40));

        // Título
        JLabel lblTitulo = new JLabel("🍶 Forja Bar");
        lblTitulo.setFont(new Font("Segoe UI Black", Font.BOLD, 26));
        lblTitulo.setForeground(new Color(255, 200, 80));
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblSubtitulo = new JLabel("Acesse o sistema");
        lblSubtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblSubtitulo.setForeground(new Color(180, 180, 200));
        lblSubtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Campo Login
        JLabel lblLogin = new JLabel("Login");
        lblLogin.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblLogin.setForeground(new Color(200, 200, 220));
        lblLogin.setAlignmentX(Component.LEFT_ALIGNMENT);

        txtLogin = new JTextField();
        txtLogin.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtLogin.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        txtLogin.setBackground(new Color(60, 60, 80));
        txtLogin.setForeground(Color.WHITE);
        txtLogin.setCaretColor(Color.WHITE);
        txtLogin.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(100, 100, 140)),
                BorderFactory.createEmptyBorder(4, 8, 4, 8)));

        // Campo Senha
        JLabel lblSenha = new JLabel("Senha");
        lblSenha.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblSenha.setForeground(new Color(200, 200, 220));
        lblSenha.setAlignmentX(Component.LEFT_ALIGNMENT);

        txtSenha = new JPasswordField();
        txtSenha.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtSenha.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        txtSenha.setBackground(new Color(60, 60, 80));
        txtSenha.setForeground(Color.WHITE);
        txtSenha.setCaretColor(Color.WHITE);
        txtSenha.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(100, 100, 140)),
                BorderFactory.createEmptyBorder(4, 8, 4, 8)));

        // Botão
        btnEntrar = new JButton("Entrar");
        btnEntrar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnEntrar.setBackground(new Color(255, 200, 80));
        btnEntrar.setForeground(new Color(30, 30, 40));
        btnEntrar.setFocusPainted(false);
        btnEntrar.setBorderPainted(false);
        btnEntrar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnEntrar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        btnEntrar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnEntrar.addActionListener(e -> fazerLogin());
        txtSenha.addActionListener(e -> fazerLogin());

        // Montar painel
        painelForm.add(lblTitulo);
        painelForm.add(Box.createVerticalStrut(4));
        painelForm.add(lblSubtitulo);
        painelForm.add(Box.createVerticalStrut(24));
        painelForm.add(lblLogin);
        painelForm.add(Box.createVerticalStrut(4));
        painelForm.add(txtLogin);
        painelForm.add(Box.createVerticalStrut(14));
        painelForm.add(lblSenha);
        painelForm.add(Box.createVerticalStrut(4));
        painelForm.add(txtSenha);
        painelForm.add(Box.createVerticalStrut(24));
        painelForm.add(btnEntrar);

        painelPrincipal.add(painelForm);
        pack();
        setMinimumSize(new Dimension(380, 340));
    }

    private void fazerLogin() {
        String loginDigitado = txtLogin.getText();
        String senhaDigitada = new String(txtSenha.getPassword());

        if (loginDigitado.isEmpty() || senhaDigitada.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Preencha o login e a senha.", "Atenção", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            org.example.repository.UsuarioRepository repository = new org.example.repository.UsuarioRepository();
            org.example.model.Usuario usuarioEncontrado = repository.buscarPorLogin(loginDigitado);

            if (usuarioEncontrado != null && usuarioEncontrado.getSenha().equals(senhaDigitada)) {
                JOptionPane.showMessageDialog(this, "Bem-vindo, " + loginDigitado + "!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                MainFrame menu = new MainFrame();
                menu.setVisible(true);
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Login ou senha inválidos.", "Erro de Autenticação", JOptionPane.ERROR_MESSAGE);
                txtSenha.setText("");
            }
        } catch (Exception erro) {
            JOptionPane.showMessageDialog(this, "Erro ao conectar com o banco: " + erro.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String args[]) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) { }
        java.awt.EventQueue.invokeLater(() -> new LoginFrame().setVisible(true));
    }
}
