import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Vector;

public class UserManagementPanel extends JPanel {
    private JTable userTable = new JTable();
    private JTextField txtId = new JTextField(20);
    private JTextField txtEmail = new JTextField(20);
    private JTextField txtFirstName = new JTextField(20);
    private JTextField txtLastName = new JTextField(20);
    private JTextField txtQualification = new JTextField(20);
    private JTextField txtDOB = new JTextField(20);
    private JTextField txtCollegeName = new JTextField(20);
    private JTextField txtAddress = new JTextField(20);
    private JTextField txtPhone = new JTextField(20);
    private JCheckBox chkIsAdmin = new JCheckBox("Is Admin");
    private JButton btnSave = new JButton("Save Changes");

    public UserManagementPanel() {
        setLayout(new BorderLayout());
        initializeUI();
        loadUsers();
    }

    private void initializeUI() {
        userTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        userTable.getSelectionModel().addListSelectionListener(this::tableRowSelected);
        JScrollPane scrollPane = new JScrollPane(userTable);
        add(scrollPane, BorderLayout.CENTER);

        JPanel editPanel = new JPanel(new GridLayout(11, 2, 5, 5));
        editPanel.add(new JLabel("ID:"));
        txtId.setEnabled(false); // ID is not editable
        editPanel.add(txtId);
        editPanel.add(new JLabel("Email:"));
        editPanel.add(txtEmail);
        editPanel.add(new JLabel("First Name:"));
        editPanel.add(txtFirstName);
        editPanel.add(new JLabel("Last Name:"));
        editPanel.add(txtLastName);
        editPanel.add(new JLabel("Qualification:"));
        editPanel.add(txtQualification);
        editPanel.add(new JLabel("DOB (YYYY-MM-DD):"));
        editPanel.add(txtDOB);
        editPanel.add(new JLabel("College Name:"));
        editPanel.add(txtCollegeName);
        editPanel.add(new JLabel("Address:"));
        editPanel.add(txtAddress);
        editPanel.add(new JLabel("Phone:"));
        editPanel.add(txtPhone);
        editPanel.add(new JLabel("Is Admin:"));
        editPanel.add(chkIsAdmin);
        btnSave.addActionListener(e -> saveChanges());
        editPanel.add(btnSave);

        add(editPanel, BorderLayout.SOUTH);
    }

    private void tableRowSelected(ListSelectionEvent e) {
        int selectedRow = userTable.getSelectedRow();
        if (!e.getValueIsAdjusting() && selectedRow != -1) {
            // Assuming columns match the database structure
            txtId.setText(userTable.getValueAt(selectedRow, 0).toString());
            txtEmail.setText(userTable.getValueAt(selectedRow, 1).toString());
            txtFirstName.setText(userTable.getValueAt(selectedRow, 2).toString());
            txtLastName.setText(userTable.getValueAt(selectedRow, 3).toString());
            txtQualification.setText(userTable.getValueAt(selectedRow, 4).toString());
            txtDOB.setText(userTable.getValueAt(selectedRow, 5).toString());
            txtCollegeName.setText(userTable.getValueAt(selectedRow, 6).toString());
            txtAddress.setText(userTable.getValueAt(selectedRow, 7).toString());
            txtPhone.setText(userTable.getValueAt(selectedRow, 8).toString());
            chkIsAdmin.setSelected((Boolean) userTable.getValueAt(selectedRow, 9));
        }
    }

    private void loadUsers() {
        DefaultTableModel model = new DefaultTableModel(new String[]{"ID", "Email", "First Name", "Last Name", "Qualification", "DOB", "College Name", "Address", "Phone", "Is Admin"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Prevent direct editing in the table
            }
        };
        userTable.setModel(model);

        try (Connection connection = DbConnection.getConnection()) {
            PreparedStatement ps = connection.prepareStatement("SELECT id, email, first_name, last_name, qualification, dob, college_name, address, phone, is_admin FROM users");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                for (int i = 1; i <= 10; i++) { // Assuming 10 columns
                    row.add(rs.getObject(i));
                }
                model.addRow(row);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading users: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void saveChanges() {
        // Validate fields here if necessary
        String sql = "UPDATE users SET email = ?, first_name = ?, last_name = ?, qualification = ?, dob = ?, college_name = ?, address = ?, phone = ?, is_admin = ? WHERE id = ?";
        try (Connection connection = DbConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, txtEmail.getText());
            ps.setString(2, txtFirstName.getText());
            ps.setString(3, txtLastName.getText());
            ps.setString(4, txtQualification.getText());
            ps.setString(5, txtDOB.getText());
            ps.setString(6, txtCollegeName.getText());
            ps.setString(7, txtAddress.getText());
            ps.setString(8, txtPhone.getText());
            ps.setBoolean(9, chkIsAdmin.isSelected());
            ps.setInt(10, Integer.parseInt(txtId.getText()));

            int updatedRows = ps.executeUpdate();
            if (updatedRows > 0) {
                JOptionPane.showMessageDialog(this, "User updated successfully.");
                loadUsers(); // Refresh the user list
            } else {
                JOptionPane.showMessageDialog(this, "User could not be updated.", "Update Failed", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error updating user: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
