import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MainPage extends JFrame {
    public MainPage() {
        super("Online Exam Registration");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Maximize the window
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        // Optional: Uncomment the following line to hide the window's title bar and border
        // setUndecorated(true);

        // Main panel with background image
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon imageIcon = new ImageIcon("images\\main_bg.jpg");
                Image image = imageIcon.getImage();
                g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
            }
        };
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        // Heading label with custom font
        JLabel lblHeading = new JLabel("Online Exam Registration", SwingConstants.CENTER);
        lblHeading.setFont(new Font("Serif", Font.BOLD | Font.ITALIC, 24));
        lblHeading.setForeground(Color.red);
        lblHeading.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Customize and add buttons, action listeners, etc., as previously shown
        
        JButton btnRegister = new JButton("Register");
        JButton btnLogin = new JButton("Login");
        btnRegister.setFont(new Font("Arial", Font.PLAIN, 18));
        btnLogin.setFont(new Font("Arial", Font.PLAIN, 18));
        btnRegister.setMaximumSize(new Dimension(200, 50));
        btnLogin.setMaximumSize(new Dimension(200, 50));
        btnRegister.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnLogin.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Action listeners for buttons
        btnRegister.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("Open the registration form here.");
                new RegistrationForm().setVisible(true);
            }
        });

        btnLogin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("Open the login form here.");
                new LoginForm().setVisible(true);
            }
        });

        // Add components to the main panel
        mainPanel.add(Box.createVerticalGlue());
        mainPanel.add(lblHeading);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20))); // Spacer
        mainPanel.add(btnRegister);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Spacer
        mainPanel.add(btnLogin);
        mainPanel.add(Box.createVerticalGlue());

        // Add main panel to the frame
        this.add(mainPanel);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new MainPage().setVisible(true);
            }
        });
    }
}
