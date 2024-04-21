import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LoginForm extends JFrame {
    private JTextField txtEmail = new JTextField(20);
    private JPasswordField txtPassword = new JPasswordField(20);

    public LoginForm() {
        super("User Login");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null); // Center the window
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon imageIcon = new ImageIcon("images\\reg_login.jpg");
                Image image = imageIcon.getImage();
                g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
            }
        };
                
        mainPanel.setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                setOpaque(false); // Make the panel transparent
            }
        };

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 10, 10); // Padding
        formPanel.add(new JLabel("Email:"), gbc);

        gbc.gridx = 1;
        formPanel.add(txtEmail, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Password:"), gbc);

        gbc.gridx = 1;
        formPanel.add(txtPassword, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2; // Full width for the button
        JButton btnLogin = new JButton("Login");
        btnLogin.addActionListener(e -> authenticateUser());
        formPanel.add(btnLogin, gbc);

        mainPanel.add(formPanel, BorderLayout.CENTER);
        add(mainPanel);

        pack(); // Adjusts window to fit components
    }

    private void authenticateUser() {
        String email = txtEmail.getText();
        String password = new String(txtPassword.getPassword()); // Consider hashing in a real app

        String sql = "SELECT * FROM users WHERE email = ? AND password = ?"; // Adjust your query to check hashed password
        try (Connection con = DbConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, email);
            pstmt.setString(2, password); // Hash the password if you're hashing them in the DB
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                boolean isAdmin = rs.getBoolean("is_admin");
                if (isAdmin) {
                    JOptionPane.showMessageDialog(this, "Admin Login Successful");
                    new AdminDashboard().setVisible(true); // Implement AdminDashboard similar to QuizPage for admin functionality
                    this.dispose(); // Close login window
                } else {
                    JOptionPane.showMessageDialog(this, "Login Successful");
                    new QuizPage().setVisible(true); // Make sure QuizPage exists and is designed to handle quiz logic
                    this.dispose(); // Close login window
                }
            } else {
                JOptionPane.showMessageDialog(this, "Invalid email or password", "Login Failed", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Login failed: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginForm().setVisible(true));
    }
}
