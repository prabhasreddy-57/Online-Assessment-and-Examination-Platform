import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class QuizPage extends JFrame {
    private List<Question> questions = new ArrayList<>();
    private int currentQuestionIndex = 0;
    private int score = 0;
    private JLabel questionLabel = new JLabel("", SwingConstants.CENTER);
    private JRadioButton[] optionsButtons = new JRadioButton[4];
    private ButtonGroup optionsGroup = new ButtonGroup();
    private Timer questionTimer;
    private JLabel timerLabel = new JLabel("Time left: 60", SwingConstants.CENTER);
    private final int QUESTION_TIME_LIMIT = 60; // seconds
    private JButton nextButton = new JButton("Next Question");

    public QuizPage() {
        super("Quiz Application");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        initializeUI();
        initializeQuestions();
        if (!questions.isEmpty()) {
            loadQuestion(currentQuestionIndex);
        }

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void initializeUI() {
        // Set Background Image
        setContentPane(new JLabel(new ImageIcon("images/quiz_bg.jpg"))); // Set your image path here
        setLayout(new BorderLayout());

        questionLabel.setFont(new Font("Serif", Font.BOLD, 22));
        questionLabel.setForeground(Color.WHITE); // Set text color to white
        add(questionLabel, BorderLayout.NORTH);

        JPanel optionsPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(5, 0, 5, 0); // Padding
        ActionListener optionListener = e -> { /* Listener will be invoked by the Next button */ };
        for (int i = 0; i < optionsButtons.length; i++) {
            optionsButtons[i] = new JRadioButton();
            optionsButtons[i].setFont(new Font("Arial", Font.PLAIN, 18));
            optionsButtons[i].setForeground(Color.WHITE); // Set text color to white
            optionsButtons[i].setOpaque(false); // Make the button transparent
            optionsButtons[i].addActionListener(optionListener);
            optionsGroup.add(optionsButtons[i]);
            optionsPanel.add(optionsButtons[i], gbc);
        }
        optionsPanel.setOpaque(false); // Make the panel transparent
        add(optionsPanel, BorderLayout.CENTER);

        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        timerLabel.setFont(new Font("Arial", Font.BOLD, 18));
        timerLabel.setForeground(Color.WHITE); // Set text color to white
        footerPanel.add(timerLabel);

        nextButton.addActionListener(e -> loadNextQuestion());
        footerPanel.add(nextButton);
        footerPanel.setOpaque(false); // Make the panel transparent
        add(footerPanel, BorderLayout.SOUTH);
    }

    private void initializeQuestions() {
        String sql = "SELECT question, option_a, option_b, option_c, option_d, correct_answer FROM quiz_questions";
        try (Connection con = DbConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                String questionText = rs.getString("question");
                String[] options = {
                    rs.getString("option_a"),
                    rs.getString("option_b"),
                    rs.getString("option_c"),
                    rs.getString("option_d")
                };
                String correctAnswer = options["ABCD".indexOf(rs.getString("correct_answer").toUpperCase().charAt(0))];
                questions.add(new Question(questionText, options, correctAnswer));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading questions: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadQuestion(int index) {
        if (index < questions.size()) {
            Question currentQuestion = questions.get(index);
            questionLabel.setText("<html><body style='text-align: center;'>" + currentQuestion.question + "</body></html>");
            for (int i = 0; i < currentQuestion.options.length; i++) {
                optionsButtons[i].setText(currentQuestion.options[i]);
            }
            optionsGroup.clearSelection();
            startTimer();
        } else {
            endQuiz();
        }
    }

    private void loadNextQuestion() {
        String selectedOption = null;
        for (int i = 0; i < optionsButtons.length; i++) {
            if (optionsButtons[i].isSelected()) {
                selectedOption = optionsButtons[i].getText();
                break;
            }
        }

        if (selectedOption != null && questions.get(currentQuestionIndex).checkAnswer(selectedOption)) {
            score++;
        }
        currentQuestionIndex++;
        if (currentQuestionIndex < questions.size()) {
            loadQuestion(currentQuestionIndex);
        } else {
            endQuiz();
        }
    }

    private void startTimer() {
        if (questionTimer != null) questionTimer.stop();
        timerLabel.setText("Time left: " + QUESTION_TIME_LIMIT);
        int[] timeLeft = {QUESTION_TIME_LIMIT};
        questionTimer = new Timer(1000, e -> {
            timeLeft[0]--;
            timerLabel.setText("Time left: " + timeLeft[0]);
            if (timeLeft[0] <= 0) {
                questionTimer.stop();
                loadNextQuestion();
            }
        });
        questionTimer.start();
    }

    private void endQuiz() {
        questionTimer.stop();
        JOptionPane.showMessageDialog(this, "Quiz completed! Your score: " + score );
        dispose(); // Close the quiz window
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new QuizPage());
    }
}
