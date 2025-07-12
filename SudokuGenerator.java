import java.util.Random;

public class SudokuGenerator {
    private static final int SIZE = 9;
    private static final Random random = new Random();

    public static int[][] generatePuzzle() {
        int[][] board = new int[SIZE][SIZE];
        fillDiagonalBoxes(board);
        solve(board); // Solve the board to make sure it's valid
        removeNumbers(board, 40); // Remove numbers to create a puzzle
        return board;
    }

    private static void fillDiagonalBoxes(int[][] board) {
        for (int i = 0; i < SIZE; i += 3) {
            fillBox(board, i, i);
        }
    }

    private static void fillBox(int[][] board, int row, int col) {
        boolean[] used = new boolean[SIZE + 1];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                int num;
                do {
                    num = random.nextInt(9) + 1;
                } while (used[num]);
                used[num] = true;
                board[row + i][col + j] = num;
            }
        }
    }

    private static boolean solve(int[][] board) {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if (board[row][col] == 0) {
                    for (int num = 1; num <= 9; num++) {
                        if (isValid(board, row, col, num)) {
                            board[row][col] = num;
                            if (solve(board)) {
                                return true;
                            }
                            board[row][col] = 0;
                        }
                    }
                    return false;
                }
            }
        }
        return true;
    }

    private static boolean isValid(int[][] board, int row, int col, int num) {
        for (int i = 0; i < SIZE; i++) {
            if (board[row][i] == num || board[i][col] == num) {
                return false;
            }
            int boxRow = 3 * (row / 3) + i / 3;
            int boxCol = 3 * (col / 3) + i % 3;
            if (board[boxRow][boxCol] == num) {
                return false;
            }
        }
        return true;
    }

    private static void removeNumbers(int[][] board, int count) {
        for (int i = 0; i < count; i++) {
            int row, col;
            do {
                row = random.nextInt(9);
                col = random.nextInt(9);
            } while (board[row][col] == 0);
            board[row][col] = 0;
        }
    }
}
