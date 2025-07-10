import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameWindow extends JFrame {
    private JTextPane gameLog;
    private GameLogic gameLogic;
    private StyledDocument doc;

    public GameWindow() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        setTitle("Probability Cricket Game");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        Font font = new Font("Arial", Font.PLAIN, 16);

        JLabel title = new JLabel("🏏 Probability Cricket Game 🏏", SwingConstants.CENTER);
        title.setFont(new Font("Serif", Font.BOLD, 26));
        title.setOpaque(true);
        title.setBackground(new Color(70, 130, 180));
        title.setForeground(Color.WHITE);
        title.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel setupPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        setupPanel.setBackground(new Color(230, 240, 255));
        setupPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String[] difficulties = {"Easy", "Medium", "Hard", "Custom"};
        JComboBox<String> difficultyBox = new JComboBox<>(difficulties);
        difficultyBox.setFont(font);

        JButton startButton = new JButton("Start Game");
        startButton.setFont(font);
        startButton.setBackground(new Color(144, 238, 144));
        startButton.setFocusPainted(false);

        setupPanel.add(new JLabel("Select Difficulty:"));
        setupPanel.add(difficultyBox);
        setupPanel.add(startButton);

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(230, 240, 255));
        headerPanel.add(title, BorderLayout.NORTH);
        headerPanel.add(setupPanel, BorderLayout.SOUTH);

        add(headerPanel, BorderLayout.NORTH);

        gameLog = new JTextPane();
        gameLog.setEditable(false);
        gameLog.setFont(font);
        doc = gameLog.getStyledDocument();
        JScrollPane scrollPane = new JScrollPane(gameLog);
        add(scrollPane, BorderLayout.CENTER);

        Style defaultStyle = StyleContext.getDefaultStyleContext().getStyle(StyleContext.DEFAULT_STYLE);

        Style boldStyle = doc.addStyle("bold", defaultStyle);
        StyleConstants.setBold(boldStyle, true);

        Style redBold = doc.addStyle("out", defaultStyle);
        StyleConstants.setForeground(redBold, Color.RED);
        StyleConstants.setBold(redBold, true);

        Style greenBold = doc.addStyle("score", defaultStyle);
        StyleConstants.setForeground(greenBold, new Color(0, 150, 0));
        StyleConstants.setBold(greenBold, true);

        Style yellowBold = doc.addStyle("miss", defaultStyle);
        StyleConstants.setForeground(yellowBold, new Color(255, 165, 0)); // orange-ish
        StyleConstants.setBold(yellowBold, true);

        JPanel controlPanel = new JPanel(new GridLayout(1, 5, 10, 10));
        controlPanel.setBackground(new Color(240, 250, 255));
        controlPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        int[] choices = {0, 1, 2, 4, 6};
        for (int c : choices) {
            JButton button = new JButton(String.valueOf(c));
            button.setFont(font);
            button.setBackground(new Color(255, 255, 204));
            button.setToolTipText("Play " + c + " run(s)");
            button.setFocusPainted(false);

            button.addActionListener(e -> {
                if (gameLogic != null) {
                    appendStyled("👉 Choice: " + c + "\n", "bold");

                    String result = gameLogic.playBall(c);

                    if (result.toLowerCase().contains("out")) {
                        appendStyled(result + "\n", "out");
                    } else if (result.toLowerCase().contains("miss")) {
                        appendStyled(result + "\n", "miss");
                    } else {
                        appendStyled(result + "\n", "score");
                    }

                    appendStyled(gameLogic.getStatus(), "bold");
                    appendStyled(gameLogic.getProbs(), "default");
                }
            });
            controlPanel.add(button);
        }

        add(controlPanel, BorderLayout.SOUTH);

        startButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String selected = difficultyBox.getSelectedItem().toString().toLowerCase();
                Target target = new Target();
                int[] score;

                if (selected.equals("custom")) {
                    String targetInput = JOptionPane.showInputDialog("Enter Your Target (Runs):");
                    String ballsInput = JOptionPane.showInputDialog("Enter Number of Balls:");
                    String wicketsInput = JOptionPane.showInputDialog("Enter Number of Wickets:");

                    if (targetInput != null && ballsInput != null && wicketsInput != null) {
                        int targetScore = Integer.parseInt(targetInput);
                        int balls = Integer.parseInt(ballsInput);
                        int wickets = Integer.parseInt(wicketsInput);

                        score = new int[] {targetScore, balls, wickets};
                    } else {
                        appendStyled("❌ Invalid custom difficulty input.\n", "out");
                        return;
                    }
                } else {
                    score = target.setTarget(selected);
                }

                if (score[0] == -1) {
                    appendStyled("❌ Invalid difficulty selected.\n", "out");
                    return;
                }

                gameLogic = new GameLogic(score[0], score[1], score[2]);

                gameLog.setText("");
                appendStyled("🎯 Target: " + score[0] + " runs in " + score[1] + " balls (Total Wickets: " + score[2] + ")\n", "boldWithMargin");
                appendStyled("✅ Game Started!\n\n", "boldWithMargin");
                appendStyled(gameLogic.getStatus(), "bold");
                appendStyled(gameLogic.getProbs(), "default");
            }
        });

        setVisible(true);
    }

    private void appendStyled(String text, String style) {
        try {

            if (style.equals("boldWithMargin")) {
                SimpleAttributeSet marginSet = new SimpleAttributeSet();
                StyleConstants.setLeftIndent(marginSet, 10);
                doc.setParagraphAttributes(doc.getLength(), text.length(), marginSet, false);
            } else if (!style.equals("bold")) {
                SimpleAttributeSet marginSet = new SimpleAttributeSet();
                StyleConstants.setLeftIndent(marginSet, 20);
                doc.setParagraphAttributes(doc.getLength(), text.length(), marginSet, false);
            }
            doc.insertString(doc.getLength(), text, doc.getStyle(style));
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }
}
