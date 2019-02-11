import com.sun.deploy.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TokenGame {

    //length and height of the board
    public static final int BOARD_LENGTH = 4;

    //board game for 4*4 grid
    private int[][] board;

    //player number (1- player one,2- player two)
    private static int player;

    //list of valid put operations
    private List<Integer> validput;

    //switch between two players.
    public static boolean playerturns;

    public TokenGame() {
        this.board = new int[BOARD_LENGTH][BOARD_LENGTH];
        this.player = 1;
        this.validput = new ArrayList<Integer>();
        this.playerturns = true;
    }

    public static void main(String[] args) {

        prologue();
        TokenGame tk = new TokenGame();
        Scanner sc = new Scanner(System.in);

        String line = null;

        do {
            System.out.println("\nEnter a command: (PUT|GET|BOARD|EXIT)");
            System.out.print("> ");
            line = sc.nextLine().trim().toLowerCase();

            //check for a valid PUT command or return error message.
            if (line.substring(0, 3).equals("put")) {
                if (line.length() <= 4 || line.charAt(3) != ' ')
                    System.out.println("Please enter PUT command in this format --> PUT <column>");

                int colnum = Integer.parseInt(line.split("\\s+")[1]);

                if (colnum < 1 || colnum > BOARD_LENGTH)
                    System.out.println("Invalid Column Number!! Column Number should lie between 1 and 4. ");
                else
                    tk.put(colnum, playerturns ? player : player + 1);

            } else if (line.matches("get")) {
                tk.get();
            } else if (line.matches("board")) {
                tk.printBoard();
            } else if (line.matches("exit")) {
                System.exit(0);
            } else {
                System.out.println("Incorrect input !! Please try again.");
            }
        } while (!line.equals("exit"));
    }

    /**
     * A player wins in the Board Game if there are 4 tokens next to each other
     * either in one of them:
     * 1.in a row or
     * 2.in a column or
     * 3.in a left-diagonal or.
     * 4.in a right-diagonal.
     */
    public boolean playerWins(int player, int rowNumber, int columnNumber) {

        //check for horizontal row
        boolean cols = true;

        for (int i = 0; i < BOARD_LENGTH; i++) {
            if (board[i][columnNumber - 1] != player) {
                cols = false;
                break;
            }
        }

        //check for vertical column
        boolean rows = true;
        for (int j = 0; j < BOARD_LENGTH; j++) {
            if (board[rowNumber][j] != player) {
                rows = false;
                break;
            }
        }

        //check for left and right diagonals
        //4 cells can be counted only when it passes through middle
        boolean leftDiagonal = false;
        if (rowNumber == columnNumber - 1) {
            leftDiagonal = true;
            for (int mid = 0; mid < BOARD_LENGTH; mid++) {
                if (board[mid][mid] != player) {
                    leftDiagonal = false;
                    break;
                }
            }
        }

        boolean rightDiagonal = false;
        if (rowNumber + columnNumber == BOARD_LENGTH) {
            rightDiagonal = true;
            for (int row = 0; row < BOARD_LENGTH; row++) {
                if (board[row][BOARD_LENGTH - row - 1] != player) {
                    rightDiagonal = false;
                    break;
                }
            }
        }

        return cols || rows || leftDiagonal || rightDiagonal;
    }

    /**
     * Print's the 4*4 board with values as 0,1,2
     * where 0 is unfilled,1 is player one,2 is player two.
     */
    public void printBoard() {
        for (int i = 0; i < BOARD_LENGTH; i++) {
            for (int j = 0; j < BOARD_LENGTH; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println("--------");
        System.out.println("1 2 3 4");
    }

    /**
     * List maintains the order in which players has dropped tokens on the board.
     * It maintains history of all the moves made by both players.
     */
    public void get() {
        this.validput.forEach(value -> System.out.println(value));
    }

    /**
     * PUT method does multiple tasks here :
     * 1.Put or drops the token to the correct position in the board.
     * 2.Checks whether move made by current player results into a WIN or a DRAW.
     * 3.Checks whether the current move can be possible or not i.e OK(possible) or ERROR(impossible).
     *
     * @param columnNumber
     * @param player       : player one as 1 ,player 2 as 2 which is signified by variable playerturns.
     * @return
     */
    public boolean put(int columnNumber, int player) {

        for (int rowNumber = BOARD_LENGTH - 1; rowNumber >= 0; rowNumber--) {
            if (this.board[rowNumber][columnNumber - 1] == 0) {
                this.board[rowNumber][columnNumber - 1] = player;
                this.validput.add(columnNumber);

                if (playerWins(player, rowNumber, columnNumber)) {
                    System.out.println("WIN");
                } else if (checkForDraw()) {
                    System.out.println("DRAW");
                } else {
                    System.out.println("OK");//: Successfully put Player " + player + " in the Column " + columnNumber);
                }
                playerturns = !playerturns; //flip the turn for next player;
                return true;
            }
        }

        System.out.println("ERROR");//: Unsuccessful attempt to put Player " + player + " in the Column " + columnNumber);
        return false;
    }

    /**
     * Game is Draw when neither of players wins as entire-board is full with 16 members but
     * there is no row,column or diagonal with 4 adjacent same cells.
     *
     * @return
     */
    private boolean checkForDraw() {
        return this.validput.size() == Math.pow(BOARD_LENGTH, 2);
    }


    /**
     * Introduction of the Game - Drop Tokens.
     */
    public static void prologue() {
        System.out.println("Welcome to the DropTokens Game!!");
        System.out.println("Rules of the Game:\n\n1.DropToken is played on 4*4 Grid and played by two players.\n" + "2.A player wins when they have 4 tokens next to each\n" +
                "other either along a row, in a column, or on a diagonal.\n.If the board is filled, and nobody has won then the game is a draw.");
        System.out.println("-------------------------------");
        System.out.println("4 COMMANDS KEY:");
        System.out.println("1.PUT <column>: OK|ERROR|WIN|DRAW");
        System.out.println("2.GET: List of columns that have been successfully put to");
        System.out.println("3.BOARD:a 4x4 matrix shows state of the board \nWhere 0 is unfilled, 1 is player 1, and 2 is player 2.");
        System.out.println("4.EXIT: ends the game.");
        System.out.println("-------------------------------\n\n");
    }
}
