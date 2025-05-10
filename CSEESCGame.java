import java.util.*;

public class CSEESCGame {
    private static final int WIN_SCORE = 10;
    private static final int LOSE_SCORE = -10;
    private static final int DRAW_SCORE = 0;

    private static char[][] board = {
        {' ', ' ', ' '},
        {'S', ' ', ' '},
        {' ', ' ', ' '}
    };

    private static final char PLAYER = 'C';
    private static final char COMPUTER = 'E';
    private static final char EMPTY = ' ';

    private static final char[] LETTERS = {'C', 'S', 'E'};

    public static void main(String[] args) {
        while (true) {
            printBoard();
            playerMove();

            if (isGameFinished(PLAYER)) {
                System.out.println("You win!");
                break;
            }

            if (isBoardFull()) {
                System.out.println("It's a draw!");
                break;
            }

            computerMove();
            printBoard();

            if (isGameFinished(COMPUTER)) {
                System.out.println("Computer wins!");
                break;
            }

            if (isBoardFull()) {
                System.out.println("It's a draw!");
                break;
            }
        }
    }

    private static void printBoard() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                System.out.print(board[i][j]);
                if (j < 2) {
                    System.out.print("|");
                }
            }
            System.out.println();
            if (i < 2) {
                System.out.println("-----");
            }
        }
        System.out.println();
    }

    private static void playerMove() {
        Scanner scanner = new Scanner(System.in);
        int row, col;
        char letter;
        do {
            System.out.print("Enter row (0-2): ");
            row = scanner.nextInt();
            System.out.print("Enter column (0-2): ");
            col = scanner.nextInt();
            System.out.print("Enter letter (C, S, or E): ");
            letter = scanner.next().charAt(0);
        } while (!isValidMove(row, col) || !isValidLetter(letter));
        board[row][col] = letter;
    }

    private static boolean isValidMove(int row, int col) {
        return row >= 0 && row < 3 && col >= 0 && col < 3 && board[row][col] == EMPTY;
    }

    private static boolean isValidLetter(char letter) {
        for (char l : LETTERS) {
            if (letter == l) {
                return true;
            }
        }
        return false;
    }

    private static boolean isBoardFull() {
        for (char[] row : board) {
            for (char cell : row) {
                if (cell == EMPTY) {
                    return false;
                }
            }
        }
        return true;
    }

    private static boolean isGameFinished(char player) {
        return checkRows(player) || checkColumns(player) || checkDiagonals(player);
    }

    private static boolean checkRows(char player) {
        for (int i = 0; i < 3; i++) {
            if ((board[i][0] == 'C' && board[i][1] == 'S' && board[i][2] == 'E') ||
                (board[i][2] == 'C' && board[i][1] == 'S' && board[i][0] == 'E')) {
                return true;
            }
        }
        return false;
    }

    private static boolean checkColumns(char player) {
        for (int i = 0; i < 3; i++) {
            if ((board[0][i] == 'C' && board[1][i] == 'S' && board[2][i] == 'E') ||
                (board[2][i] == 'C' && board[1][i] == 'S' && board[0][i] == 'E')) {
                return true;
            }
        }
        return false;
    }

    private static boolean checkDiagonals(char player) {
        if (((board[0][0] == 'C' && board[1][1] == 'S' && board[2][2] == 'E') ||
             (board[0][0] == 'E' && board[1][1] == 'S' && board[2][2] == 'C')) ||
            ((board[0][2] == 'C' && board[1][1] == 'S' && board[2][0] == 'E') ||
             (board[0][2] == 'E' && board[1][1] == 'S' && board[2][0] == 'C'))) {
            return true;
        }
        return false;
    }

    private static void computerMove() {
        int[] bestMove = findBestMove(COMPUTER);
        char bestLetter = findBestLetterForMove(bestMove);
        board[bestMove[0]][bestMove[1]] = bestLetter;
        System.out.println("Computer's move: Row = " + bestMove[0] + ", Column = " + bestMove[1] + ", Letter = " + bestLetter);
    }

    private static char findBestLetterForMove(int[] move) {
        char bestLetter = ' ';
        int bestScore = Integer.MIN_VALUE;

        for (char letter : LETTERS) {
            board[move[0]][move[1]] = letter;
            int score = minimax(board, 0, false);
            System.out.println("For letter " + letter + " at (" + move[0] + ", " + move[1] + ") score is: " + score); // Debugging print
            board[move[0]][move[1]] = EMPTY;

            if (score > bestScore) {
                bestScore = score;
                bestLetter = letter;
            }
        }
        return bestLetter;
    }

    private static int[] findBestMove(char player) {
        int[] bestMove = new int[]{-1, -1};
        int bestScore = (player == COMPUTER) ? Integer.MIN_VALUE : Integer.MAX_VALUE;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == EMPTY) {
                    for (char letter : LETTERS) {
                        board[i][j] = letter;
                        int score = minimax(board, 0, false);
                        System.out.println("For move (" + i + ", " + j + ") with letter " + letter + " score is: " + score); // Debugging print
                        board[i][j] = EMPTY;

                        if ((player == COMPUTER && score > bestScore) || (player == PLAYER && score < bestScore)) {
                            bestScore = score;
                            bestMove[0] = i;
                            bestMove[1] = j;
                        }
                    }
                }
            }
        }

        return bestMove;
    }

    private static int minimax(char[][] board, int depth, boolean isMaximizing) {
        if (isGameFinished(PLAYER)) {
            return LOSE_SCORE - depth;
        }
        if (isGameFinished(COMPUTER)) {
            return WIN_SCORE + depth;
        }
        if (isBoardFull()) {
            return DRAW_SCORE;
        }

        if (isMaximizing) {
            int bestScore = Integer.MIN_VALUE;
            for (char letter : LETTERS) {
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        if (board[i][j] == EMPTY) {
                            board[i][j] = letter;
                            int score = minimax(board, depth + 1, false);
                            board[i][j] = EMPTY;
                            bestScore = Math.max(bestScore, score);
                            if (score >= WIN_SCORE) {
                                return score; // Pruning
                            }
                        }
                    }
                }
            }
            return bestScore;
        } else {
            int bestScore = Integer.MAX_VALUE;
            for (char letter : LETTERS) {
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        if (board[i][j] == EMPTY) {
                            board[i][j] = letter;
                            int score = minimax(board, depth + 1, true);
                            board[i][j] = EMPTY;
                            bestScore = Math.min(bestScore, score);
                            if (score <= LOSE_SCORE) {
                                return score; // Pruning
                            }
                        }
                    }
                }
            }
            return bestScore;
        }
    }
}
