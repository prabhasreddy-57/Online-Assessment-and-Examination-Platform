import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Vector;

public class QuestionManagementPanel extends JPanel {
    private JTable questionTable = new JTable();
    private JTextField txtId = new JTextField(20);
    private JTextField txtQuestion = new JTextField(20);
    private JTextField txtOptionA = new JTextField(20);
    private JTextField txtOptionB = new JTextField(20);
    private JTextField txtOptionC = new JTextField(20);
    private JTextField txtOptionD = new JTextField(20);
    private JTextField txtCorrectAnswer = new JTextField(20);
    private JButton btnSave = new JButton("Save Changes");
    private JButton btnNewQuestion = new JButton("Add New Question"); // Add New Question button
    private JLabel backgroundLabel = new JLabel();

    public QuestionManagementPanel() {
        setLayout(new BorderLayout());

        initializeUI();
        loadQuestions();
        setOpaque(false); // Make the panel transparent
        add(backgroundLabel); // Add the background label to the panel
    }

    private void initializeUI() {
        backgroundLabel.setLayout(new BorderLayout());
        backgroundLabel.setIcon(new ImageIcon("\\images\\reg_login.jpg")); // Set your image path here
        
        JPanel contentPanel = new JPanel(new BorderLayout());
        backgroundLabel.add(contentPanel); // Add content panel to the background label

        questionTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        questionTable.setOpaque(false); // Make the table transparent
        questionTable.setBackground(new Color(0,0,0,0)); // Set background color to transparent
        questionTable.getSelectionModel().addListSelectionListener(this::tableRowSelected);
        JScrollPane scrollPane = new JScrollPane(questionTable);
        scrollPane.setOpaque(false); // Make the scroll pane transparent
        scrollPane.getViewport().setOpaque(false); // Make the viewport transparent
        contentPanel.add(scrollPane, BorderLayout.CENTER);
        
        JPanel editPanel = new JPanel(new GridLayout(0, 2, 5, 5));
        editPanel.setOpaque(false); // Make the edit panel transparent
        editPanel.add(new JLabel("ID:"));
        txtId.setEnabled(false); // ID is not editable
        editPanel.add(txtId);
        editPanel.add(new JLabel("Question:"));
        editPanel.add(txtQuestion);
        editPanel.add(new JLabel("Option A:"));
        editPanel.add(txtOptionA);
        editPanel.add(new JLabel("Option B:"));
        editPanel.add(txtOptionB);
        editPanel.add(new JLabel("Option C:"));
        editPanel.add(txtOptionC);
        editPanel.add(new JLabel("Option D:"));
        editPanel.add(txtOptionD);
        editPanel.add(new JLabel("Correct Answer:"));
        editPanel.add(txtCorrectAnswer);
        btnSave.addActionListener(e -> saveChanges());
        editPanel.add(btnSave);

        contentPanel.add(editPanel, BorderLayout.SOUTH);

        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> loadQuestions());
        contentPanel.add(refreshButton, BorderLayout.NORTH);
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(btnNewQuestion);
        contentPanel.add(topPanel, BorderLayout.NORTH);

        btnNewQuestion.addActionListener(e -> openQuestionInputForm());
    }

    private void openQuestionInputForm() {
        QuestionInputForm inputForm = new QuestionInputForm(); // Assuming this is your form class
        inputForm.setVisible(true);
        inputForm.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Ensure it only closes the form, not the entire application
    }

    private void tableRowSelected(ListSelectionEvent e) {
        int selectedRow = questionTable.getSelectedRow();
        if (!e.getValueIsAdjusting() && selectedRow != -1) {
            txtId.setText(questionTable.getValueAt(selectedRow, 0).toString());
            txtQuestion.setText(questionTable.getValueAt(selectedRow, 1).toString());
            txtOptionA.setText(questionTable.getValueAt(selectedRow, 2).toString());
            txtOptionB.setText(questionTable.getValueAt(selectedRow, 3).toString());
            txtOptionC.setText(questionTable.getValueAt(selectedRow, 4).toString());
            txtOptionD.setText(questionTable.getValueAt(selectedRow, 5).toString());
            txtCorrectAnswer.setText(questionTable.getValueAt(selectedRow, 6).toString());
        }
    }

    private void loadQuestions() {
        DefaultTableModel model = new DefaultTableModel(new String[]{"ID", "Question", "Option A", "Option B", "Option C", "Option D", "Correct Answer"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Prevent direct editing in the table
            }
        };
        questionTable.setModel(model);

        try (Connection connection = DbConnection.getConnection()) {
            PreparedStatement ps = connection.prepareStatement("SELECT id, question, option_a, option_b, option_c, option_d, correct_answer FROM quiz_questions");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                for (int i = 1; i <= 7; i++) {
                    row.add(rs.getObject(i));
                }
                model.addRow(row);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading questions: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void saveChanges() {
        if (txtId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select a question to edit");
            return;
        }

        String sql = "UPDATE quiz_questions SET question = ?, option_a = ?, option_b = ?, option_c = ?, option_d = ?, correct_answer = ? WHERE id = ?";
        try (Connection connection = DbConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, txtQuestion.getText());
            ps.setString(2, txtOptionA.getText());
            ps.setString(3, txtOptionB.getText());
            ps.setString(4, txtOptionC.getText());
            ps.setString(5, txtOptionD.getText());
            ps.setString(6, txtCorrectAnswer.getText());
            ps.setInt(7, Integer.parseInt(txtId.getText()));

            int updatedRows = ps.executeUpdate();
            if (updatedRows > 0) {
                JOptionPane.showMessageDialog(this, "Question updated successfully.");
                loadQuestions(); // Refresh the question list
            } else {
                JOptionPane.showMessageDialog(this, "Question could not be updated.", "Update Failed", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error updating question: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
