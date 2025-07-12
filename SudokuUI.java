import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.*;

public class SudokuUI {
    private JFrame frame;
    private JTextField[][] grid = new JTextField[9][9];
    private JButton generateButton, solveButton;

    public SudokuUI() {
        frame = new JFrame("Sudoku Solver");
        frame.setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(9, 9));

        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                JTextField cell = new JTextField();
                cell.setHorizontalAlignment(JTextField.CENTER);
                cell.setFont(new Font("Arial", Font.BOLD, 18));
                grid[row][col] = cell;

                // Add real-time validation for conflicts
                cell.addKeyListener(new KeyAdapter() {
                    public void keyReleased(KeyEvent e) {
                        validateBoard();
                    }
                });

                // Set thick borders for 3x3 box visibility
                int top = (row % 3 == 0) ? 2 : 1;
                int left = (col % 3 == 0) ? 2 : 1;
                int bottom = (row == 8) ? 2 : 1;
                int right = (col == 8) ? 2 : 1;
                cell.setBorder(new MatteBorder(top, left, bottom, right, Color.BLACK));

                panel.add(cell);
            }
        }

        JPanel buttonPanel = new JPanel();
        generateButton = new JButton("Generate Puzzle");
        solveButton = new JButton("Solve");

        generateButton.addActionListener(e -> {
            int[][] puzzle = SudokuGenerator.generatePuzzle();
            for (int row = 0; row < 9; row++) {
                for (int col = 0; col < 9; col++) {
                    if (puzzle[row][col] != 0) {
                        grid[row][col].setText(String.valueOf(puzzle[row][col]));
                        grid[row][col].setEditable(false);
                        grid[row][col].setBackground(Color.LIGHT_GRAY);
                    } else {
                        grid[row][col].setText("");
                        grid[row][col].setEditable(true);
                        grid[row][col].setBackground(Color.WHITE);
                    }
                }
            }
        });

        solveButton.addActionListener(e -> {
            char[][] board = new char[9][9];

            for (int row = 0; row < 9; row++) {
                for (int col = 0; col < 9; col++) {
                    String text = grid[row][col].getText();
                    board[row][col] = text.isEmpty() ? '.' : text.charAt(0);
                }
            }

            if (SudokuSolver.solveSudoku(board)) {
                for (int row = 0; row < 9; row++) {
                    for (int col = 0; col < 9; col++) {
                        grid[row][col].setText(String.valueOf(board[row][col]));
                        grid[row][col].setBackground(Color.WHITE);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(frame, "No solution exists!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        buttonPanel.add(generateButton);
        buttonPanel.add(solveButton);

        frame.add(panel, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);
        frame.setSize(500, 500);
        frame.setVisible(true);
    }

    // Re-validates the board for user-entered values
    private void validateBoard() {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                JTextField cell = grid[row][col];
                String text = cell.getText();

                if (text.isEmpty() || !text.matches("[1-9]")) {
                    cell.setBackground(Color.WHITE);
                    continue;
                }

                int value = Integer.parseInt(text);
                boolean valid = isValid(row, col, value);
                cell.setBackground(valid ? Color.WHITE : Color.PINK);
            }
        }
    }

    // Check for duplicates in row, column, and 3x3 box
    private boolean isValid(int row, int col, int value) {
        for (int i = 0; i < 9; i++) {
            if (i != col && grid[row][i].getText().equals(String.valueOf(value)))
                return false;
            if (i != row && grid[i][col].getText().equals(String.valueOf(value)))
                return false;
        }

        int boxRowStart = (row / 3) * 3;
        int boxColStart = (col / 3) * 3;

        for (int r = boxRowStart; r < boxRowStart + 3; r++) {
            for (int c = boxColStart; c < boxColStart + 3; c++) {
                if ((r != row || c != col) && grid[r][c].getText().equals(String.valueOf(value)))
                    return false;
            }
        }

        return true;
    }

    public static void main(String[] args) {
        new SudokuUI();
    }
}
