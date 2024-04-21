import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class RegistrationForm extends JFrame {
    // Form fields
    private JTextField txtFirstName = new JTextField();
    private JTextField txtLastName = new JTextField();
    private JTextField txtQualification = new JTextField();
    private JTextField txtDob = new JTextField(); // Ensure this is in YYYY-MM-DD format
    private JTextField txtCollegeName = new JTextField();
    private JTextField txtAddress = new JTextField();
    private JTextField txtEmail = new JTextField();
    private JTextField txtPhone = new JTextField();
    private JPasswordField txtPassword = new JPasswordField();

    public RegistrationForm() {
        super("Register");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Adjust size as necessary
        setLocationRelativeTo(null); // Center the window

        // Form panel with GridLayout for the form fields and labels
        JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon imageIcon = new ImageIcon("images\\reg_login.jpg");
                Image image = imageIcon.getImage();
                g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
            }
        };

        // Set font and color for labels and fields
        Font labelFont = new Font("Arial", Font.BOLD, 14);
        Color labelColor = Color.WHITE;
        Font fieldFont = new Font("Arial", Font.PLAIN, 14);
        Color fieldColor = Color.BLACK;

        // Adding form components to the formPanel with customized font and color
        addFormField(formPanel, "First Name:", txtFirstName, labelFont, labelColor, fieldFont, fieldColor);
        addFormField(formPanel, "Last Name:", txtLastName, labelFont, labelColor, fieldFont, fieldColor);
        addFormField(formPanel, "Qualification:", txtQualification, labelFont, labelColor, fieldFont, fieldColor);
        addFormField(formPanel, "DOB (YYYY-MM-DD):", txtDob, labelFont, labelColor, fieldFont, fieldColor);
        addFormField(formPanel, "College Name:", txtCollegeName, labelFont, labelColor, fieldFont, fieldColor);
        addFormField(formPanel, "Address:", txtAddress, labelFont, labelColor, fieldFont, fieldColor);
        addFormField(formPanel, "Email:", txtEmail, labelFont, labelColor, fieldFont, fieldColor);
        addFormField(formPanel, "Phone:", txtPhone, labelFont, labelColor, fieldFont, fieldColor);
        addFormField(formPanel, "Password:", txtPassword, labelFont, labelColor, fieldFont, fieldColor);

        // Submit button
        JButton btnSubmit = new JButton("Register");
        btnSubmit.setFont(new Font("Arial", Font.BOLD, 14));
        btnSubmit.setForeground(Color.WHITE);
        btnSubmit.setBackground(Color.BLUE); // Adjust button color as needed
        btnSubmit.addActionListener(e -> registerUser());
        formPanel.add(btnSubmit);

        // Adding some padding around the formPanel
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Set formPanel as the content pane of the frame
        setContentPane(formPanel);
    }

    private void addFormField(JPanel formPanel, String label, JTextField textField, Font labelFont, Color labelColor, Font fieldFont, Color fieldColor) {
        JLabel lblField = new JLabel(label);
        lblField.setFont(labelFont);
        lblField.setForeground(labelColor);
        formPanel.add(lblField);

        textField.setFont(fieldFont);
        textField.setForeground(fieldColor);
        formPanel.add(textField);
    }

    private void registerUser() {
        // Collect data from the form fields
        String firstName = txtFirstName.getText();
        String lastName = txtLastName.getText();
        String qualification = txtQualification.getText();
        String dob = txtDob.getText(); // Ensure this is in YYYY-MM-DD format
        String collegeName = txtCollegeName.getText();
        String address = txtAddress.getText();
        String email = txtEmail.getText();
        String phone = txtPhone.getText();
        String password = new String(txtPassword.getPassword()); // Convert char[] to String

        // SQL INSERT statement
        String sql = "INSERT INTO users (first_name, last_name, qualification, dob, college_name, address, email, phone, password) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection con = DbConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            
            // Set parameters
            pstmt.setString(1, firstName);
            pstmt.setString(2, lastName);
            pstmt.setString(3, qualification);
            pstmt.setString(4, dob);
            pstmt.setString(5, collegeName);
            pstmt.setString(6, address);
            pstmt.setString(7, email);
            pstmt.setString(8, phone);
            pstmt.setString(9, password); // Consider hashing the password before storing
            
            // Execute the insert
            pstmt.executeUpdate();
            
            JOptionPane.showMessageDialog(null, "Registration successful!");
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Registration failed: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new RegistrationForm().setVisible(true));
    }
}
