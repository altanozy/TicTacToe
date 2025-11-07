import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class TicTacToe extends JFrame {

    // --- Components ---
    private Container container;
    private JButton[][] buttons = new JButton[3][3];
    private String currentPlayer = "X";
    private boolean gameEnded = false;
    private JMenuBar menuBar;
    private JMenu gameMenu;
    private JMenuItem restartItem;
    private JMenuItem quitItem;


    // --- Constructor ---
    public TicTacToe() {
        // Basic window setup
        setTitle("Tic Tac Toe");
        setSize(400, 400);
        setLocationRelativeTo(null); // Center the window
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(200, 200));

        // Set up the main container
        container = getContentPane();
        container.setLayout(new GridLayout(3, 3));

        // Create the menu
        setupMenu();

        // Create and add buttons to the grid
        initializeButtons();

        // Add the component listener to handle resizing and startup correctly.
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                SwingUtilities.invokeLater(() -> {
                    updateFontSizes();
                });
            }

            @Override
            public void componentShown(ComponentEvent e) {
                SwingUtilities.invokeLater(() -> {
                    updateFontSizes();
                });
            }
        });
    }

    private void setupMenu() {
        menuBar = new JMenuBar();
        gameMenu = new JMenu("Game");
        restartItem = new JMenuItem("Restart");
        restartItem.addActionListener(e -> resetGame());
        quitItem = new JMenuItem("Quit");
        quitItem.addActionListener(e -> System.exit(0));
        gameMenu.add(restartItem);
        gameMenu.add(quitItem);
        menuBar.add(gameMenu);
        setJMenuBar(menuBar);
    }

    private void initializeButtons() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j] = new JButton();
                buttons[i][j].setText("");
                buttons[i][j].setFocusable(false);
                buttons[i][j].addActionListener(new ButtonClickListener());
                container.add(buttons[i][j]);
            }
        }
    }
    
    private void updateFontSizes() {
        if (buttons[0][0] == null) return;
        int buttonWidth = buttons[0][0].getWidth();
        int buttonHeight = buttons[0][0].getHeight();
        if (buttonWidth == 0 || buttonHeight == 0) {
            return;
        }
        int size = Math.min(buttonWidth, buttonHeight);
        int fontSize = (int) (size * 0.8);
        Font newFont = new Font("Arial", Font.BOLD, fontSize);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setFont(newFont);
            }
        }
    }


    // --- Game Logic ---

    private class ButtonClickListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton clickedButton = (JButton) e.getSource();
            if (gameEnded || !clickedButton.getText().equals("")) {
                return;
            }
            clickedButton.setText(currentPlayer);
            if (checkForWin()) {
                gameEnded = true;
                // MODIFIED: Call our new game over dialog instead of the simple message.
                showGameOverDialog("Player " + currentPlayer + " wins!");
            } else if (isBoardFull()) {
                gameEnded = true;
                // MODIFIED: Call our new game over dialog instead of the simple message.
                showGameOverDialog("It's a tie!");
            } else {
                currentPlayer = (currentPlayer.equals("X")) ? "O" : "X";
            }
        }
    }

    // NEW: A dedicated method to handle the end-of-game dialog.
    private void showGameOverDialog(String message) {
        Object[] options = {"Play Again", "Quit"};
        int choice = JOptionPane.showOptionDialog(this,
                message + "\nWhat would you like to do?",
                "Game Over",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                options,
                options[0]); // Default button is "Play Again"
                setLocationRelativeTo(container);

        if (choice == 0) { // User clicked "Play Again"
            resetGame();
        } else { // User clicked "Quit" or closed the dialog
            System.exit(0);
        }
    }

    private boolean checkForWin() {
        for (int i = 0; i < 3; i++) {
            if (checkThree(buttons[i][0], buttons[i][1], buttons[i][2])) return true;
        }
        for (int i = 0; i < 3; i++) {
            if (checkThree(buttons[0][i], buttons[1][i], buttons[2][i])) return true;
        }
        if (checkThree(buttons[0][0], buttons[1][1], buttons[2][2]) || checkThree(buttons[0][2], buttons[1][1], buttons[2][0])) {
            return true;
        }
        return false;
    }

    private boolean checkThree(JButton b1, JButton b2, JButton b3) {
        return !b1.getText().equals("") && b1.getText().equals(b2.getText()) && b2.getText().equals(b3.getText());
    }

    private boolean isBoardFull() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (buttons[i][j].getText().equals("")) {
                    return false;
                }
            }
        }
        return true;
    }

    private void resetGame() {
        currentPlayer = "X";
        gameEnded = false; // This is important to allow clicks again
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setText("");
            }
        }
    }

    // --- Main Method (Entry Point) ---
    // NEW, CORRECTED WAY
public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
        TicTacToe game = new TicTacToe(); // Create an instance of the game
        game.setVisible(true);             // 1. Make the window appear first (at the default corner)
        game.setLocationRelativeTo(null);
         // 2. Immediately tell the visible window to move to the center
    });
}
}