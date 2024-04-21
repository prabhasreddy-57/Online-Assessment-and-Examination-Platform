import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class QuestionInputForm extends JFrame {
    private JTextField txtQuestion = new JTextField(20);
    private JTextField txtOptionA = new JTextField(20);
    private JTextField txtOptionB = new JTextField(20);
    private JTextField txtOptionC = new JTextField(20);
    private JTextField txtOptionD = new JTextField(20);
    private JTextField txtCorrectAnswer = new JTextField(20);
    private JButton btnSave = new JButton("Save Question");

    public QuestionInputForm() {
        super("Add New Question");
        setLayout(new GridLayout(0, 2));

        add(new JLabel("Question:"));
        add(txtQuestion);
        add(new JLabel("Option A:"));
        add(txtOptionA);
        add(new JLabel("Option B:"));
        add(txtOptionB);
        add(new JLabel("Option C:"));
        add(txtOptionC);
        add(new JLabel("Option D:"));
        add(txtOptionD);
        add(new JLabel("Correct Answer (A/B/C/D):"));
        add(txtCorrectAnswer);
        
        btnSave.addActionListener(e -> saveQuestionToDatabase());
        add(btnSave);

        pack();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void saveQuestionToDatabase() {
        String sql = "INSERT INTO quiz_questions (question, option_a, option_b, option_c, option_d, correct_answer) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection con = DbConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
             
            pstmt.setString(1, txtQuestion.getText());
            pstmt.setString(2, txtOptionA.getText());
            pstmt.setString(3, txtOptionB.getText());
            pstmt.setString(4, txtOptionC.getText());
            pstmt.setString(5, txtOptionD.getText());
            pstmt.setString(6, txtCorrectAnswer.getText().toUpperCase()); // Ensure correct answer is in uppercase
            
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Question saved successfully!");
            clearForm();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error saving question: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearForm() {
        txtQuestion.setText("");
        txtOptionA.setText("");
        txtOptionB.setText("");
        txtOptionC.setText("");
        txtOptionD.setText("");
        txtCorrectAnswer.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new QuestionInputForm().setVisible(true));
    }
}
