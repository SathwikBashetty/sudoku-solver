public class SudokuSolver {
    private static final int SIZE = 9;

    public static boolean solveSudoku(char[][] board) {
        return solve(board); // Now returns true if solved, false otherwise
    }

    private static boolean solve(char[][] board) {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (board[i][j] == '.') { // Empty cell
                    for (char c = '1'; c <= '9'; c++) {
                        if (isValid(board, i, j, c)) {
                            board[i][j] = c;

                            if (solve(board)) {
                                return true; // Found a solution
                            } else {
                                board[i][j] = '.'; // Backtrack
                            }
                        }
                    }
                    return false;
                }
            }
        }
        return true;
    }

    private static boolean isValid(char[][] board, int row, int col, char c) {
        for (int i = 0; i < SIZE; i++) {
            if (board[i][col] == c || board[row][i] == c) {
                return false; // Check row & column
            }

            int boxRow = 3 * (row / 3) + i / 3;
            int boxCol = 3 * (col / 3) + i % 3;
            if (board[boxRow][boxCol] == c) {
                return false; // Check 3x3 box
            }
        }
        return true;
    }
}
