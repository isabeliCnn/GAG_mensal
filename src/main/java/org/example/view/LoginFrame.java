package org.example.view;

import org.example.controller.LoginController;
import org.example.model.Usuario;
import org.example.util.AppContext;

import javax.swing.*;
import java.awt.*;

/**
 * View de login. Não acessa AuthService nem UsuarioRepository diretamente —
 * toda a lógica passa pelo LoginController.
 */
public class LoginFrame extends JFrame {

    private final LoginController loginController;

    private JTextField campoLogin;
    private JPasswordField campoSenha;

    public LoginFrame() {
        this.loginController = AppContext.loginController();
        montarTela();
    }

    private void montarTela() {
        setTitle("Forja Bar — Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(400, 340));
        setLocationRelativeTo(null);

        JPanel painelPrincipal = new JPanel(new GridBagLayout());
        painelPrincipal.setBackground(new Color(30, 30, 40));
        setContentPane(painelPrincipal);

        JPanel painelFormulario = new JPanel();
        painelFormulario.setLayout(new BoxLayout(painelFormulario, BoxLayout.Y_AXIS));
        painelFormulario.setBackground(new Color(45, 45, 60));
        painelFormulario.setBorder(BorderFactory.createEmptyBorder(32, 40, 32, 40));

        JLabel rotuloTitulo = new JLabel("Forja Bar");
        rotuloTitulo.setFont(new Font("Segoe UI", Font.BOLD, 26));
        rotuloTitulo.setForeground(new Color(255, 200, 80));
        rotuloTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel rotuloSubtitulo = new JLabel("Acesse o sistema");
        rotuloSubtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        rotuloSubtitulo.setForeground(new Color(180, 180, 200));
        rotuloSubtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        campoLogin = ComponentesUi.criarCampoTexto();
        campoSenha = new JPasswordField();
        ComponentesUi.estilizarCampo(campoSenha);

        JButton botaoEntrar = ComponentesUi.criarBotaoPrimario("Entrar", new Color(255, 200, 80), new Color(30, 30, 40));
        botaoEntrar.setAlignmentX(Component.CENTER_ALIGNMENT);
        botaoEntrar.addActionListener(e -> tentarLogin());
        campoSenha.addActionListener(e -> tentarLogin());

        painelFormulario.add(rotuloTitulo);
        painelFormulario.add(Box.createVerticalStrut(4));
        painelFormulario.add(rotuloSubtitulo);
        painelFormulario.add(Box.createVerticalStrut(24));
        painelFormulario.add(ComponentesUi.criarRotuloCampo("Login"));
        painelFormulario.add(Box.createVerticalStrut(4));
        painelFormulario.add(campoLogin);
        painelFormulario.add(Box.createVerticalStrut(14));
        painelFormulario.add(ComponentesUi.criarRotuloCampo("Senha"));
        painelFormulario.add(Box.createVerticalStrut(4));
        painelFormulario.add(campoSenha);
        painelFormulario.add(Box.createVerticalStrut(24));
        painelFormulario.add(botaoEntrar);

        painelPrincipal.add(painelFormulario);
        pack();
        setMinimumSize(new Dimension(380, 340));
    }

    private void tentarLogin() {
        String login = campoLogin.getText();
        String senha = new String(campoSenha.getPassword());

        LoginController.ResultadoLogin resultado = loginController.autenticar(login, senha);

        if (!resultado.isSucesso()) {
            JOptionPane.showMessageDialog(this, resultado.getMensagemErro(), "Erro ao entrar", JOptionPane.ERROR_MESSAGE);
            campoSenha.setText("");
            return;
        }

        Usuario usuario = resultado.getUsuario();
        JOptionPane.showMessageDialog(this, "Bem-vindo, " + usuario.getLogin() + "!");
        new MainFrame(usuario).setVisible(true);
        dispose();
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
        }
        EventQueue.invokeLater(() -> new LoginFrame().setVisible(true));
    }
}
