package nz.ac.aut.model;

import java.sql.ResultSet;
import java.util.ArrayList;

/**
 * This class serves as the 'judge' class, which can check the following
 * conditions:
 *
 * 1. Who win the game? 2. Is it the black's turn for the current chess point? 3. Game
 * over or not?
 *
 * @author Dong Huang
 */
public class Judge {

    private ChessBoard chessBoard;

    private String currentGameName;
    private boolean currentChessPointValid;
    private boolean currentChessBoardChanged;

    private boolean blackTurn;
    private boolean blackWin;
    private boolean whiteWin;
    private int scoreBlack;
    private int scoreWhite;

    // To control the game, all the information the judge should know is the current chess board.
    public Judge(ChessBoard chessBoard) {
        // When the game starts, there's no name for the name.
        currentGameName = "Untitled";

        // After the game starts, there is no current chess point on the board,
        // however, since this status is valid, so we still set this flag to true.
        currentChessPointValid = true;

        // After the game starts, the chess board is empty and thus not changed.
        currentChessBoardChanged = false;

        // According to Gomoku's rule, black player plays first.
        blackTurn = true;

        // At the beginning of the game, no player wins the game.
        blackWin = false;
        whiteWin = false;

        scoreBlack = 0;
        scoreWhite = 0;

        this.chessBoard = chessBoard;
    }

    public void saveGame(DBManager databaseManager, String tableName) {
        ResultSet rs = databaseManager.getAllTableNames();
        ArrayList<String> tableNamesCollection = databaseManager.convertResultSetToTableNameCollection(rs);

        // If the table name already exists in the database, then just update that table,
        // otherwise, create a new table and insert value into that table.
        if (tableNamesCollection.contains(tableName)) {
            databaseManager.updateTableFromChessBoard(tableName, chessBoard);
        } else {
            databaseManager.createAndInsertIntoTableFromChessBoard(tableName, chessBoard);
        }

        // After saving game, the chess board is not changed unless a new chess point is placed on the board.
        setCurrentChessBoardChanged(false);
        setCurrentChessPointValid(true);

        // If the current game has no user specified name, then update it.
        if (getCurrentGameName().equals("Untitled")) {
            setCurrentGameName(tableName);
        }
    }

    /**
     * To check whether the current chess point meets the following criteria:
     *
     * 1. The chess point falls into the scope of the chess board; 2. There is
     * no chess point in the same position.
     *
     * @param currentChessPoint The chess point to be checked.
     * @return True - if the chess point is valid or False - if the chess point
     * is invalid.
     */
    public boolean isChessPointValid(ChessPoint currentChessPoint) {
        boolean result = true;

        int x = currentChessPoint.getX();
        int y = currentChessPoint.getY();

        if (isChessExist(x, y) || x < 0 || x > ChessBoard.NUM_OF_COLS || y < 0 || y > ChessBoard.NUM_OF_ROWS) {
            result = false;
        }

        return result;
    }

    /**
     * To check whether it is game over after the current chess point.
     *
     * @param currentChessPoint The current chess point to be checked.
     * @return True - if game over or False - if not game over.
     */
    public boolean isGameOver(ChessPoint currentChessPoint) {
        boolean result = false;

        // Only if the current chess point is not null can the check winner method be invoked.
        if (currentChessPoint != null && isWinnerAfterTheCurrentChessPoint(currentChessPoint)) {
            result = true;
        }

        return result;
    }

    /**
     * To check whether it is game over after the current chess point.
     *
     * @param currentChessPoint The current chess point to be checked.
     * @return True - if game over or False - if not game over.
     */
    public boolean isWinnerAfterTheCurrentChessPoint(ChessPoint currentChessPoint) {
        boolean result = false;

        // The initial value for the total number of chess points to be counted in a line should be one, since there is 
        // at least one chess point to be counted.
        int numberOfChessPointsInARow = 1;
        int numberOfChessPointsInAColumn = 1;
        int numberOfChessPointsInENWS = 1;
        int numberOfChessPointsInESWN = 1;

        // The initial value for the total number of chess points to be counted to a specific direction should be zero, 
        // since there is zero or more chess points to be counted in that direction.
        int numberOfChessPointsFoundHorizontally = 0;
        int numberOfChessPointsFoundVertically = 0;
        int numberOfChessPointsFoundENWS = 0;
        int numberOfChessPointsFoundESWN = 0;

        // Count the number of chess points in a line based on the current chess point color.
        for (Direction direction : Direction.values()) {
            // Count the number of chess points in a row.
            if (direction == Direction.EAST || direction == Direction.WEST) {
                numberOfChessPointsFoundHorizontally = findChessPointToADirection(currentChessPoint, direction);
                numberOfChessPointsInARow += numberOfChessPointsFoundHorizontally;
            }

            // Count the number of chess points in a column.
            if (direction == Direction.SOUTH || direction == Direction.NORTH) {
                numberOfChessPointsFoundVertically = findChessPointToADirection(currentChessPoint, direction);
                numberOfChessPointsInAColumn += numberOfChessPointsFoundVertically;
            }

            // Count the number of chess points in the northeast and the southwest direction.
            if (direction == Direction.EAST_NORTH || direction == Direction.WEST_SOUTH) {
                numberOfChessPointsFoundENWS = findChessPointToADirection(currentChessPoint, direction);
                numberOfChessPointsInENWS += numberOfChessPointsFoundENWS;
            }

            // Count the number of chess points in the southeast and the northwest direction.
            if (direction == Direction.EAST_SOUTH || direction == Direction.WEST_NORTH) {
                numberOfChessPointsFoundESWN = findChessPointToADirection(currentChessPoint, direction);
                numberOfChessPointsInESWN += numberOfChessPointsFoundESWN;
            }
        }

        // If any of the direction has 5 or more continuous points with the same color, then there's a winner.
        if (numberOfChessPointsInARow >= 5 || numberOfChessPointsInAColumn >= 5 || numberOfChessPointsInENWS >= 5 || numberOfChessPointsInESWN >= 5) {
            result = true;

            // Set the flag whether the black player wins or the white player wins.
            if (currentChessPoint.getChessColor() == ChessColor.BLACK) {
                setBlackWin(true);
                setScoreBlack(++scoreBlack);
            } else if (currentChessPoint.getChessColor() == ChessColor.WHITE) {
                setWhiteWin(true);
                setScoreWhite(++scoreWhite);
            }
        }

        return result;
    }

    /**
     * Count the number of continuous chess points to a specific direction for a
     * specific color.
     *
     * @param currentChessPoint The current chess point.
     * @param direction The direction to which the number of continuous chess
     * points will be counted.
     * @return The number of continuous chess points to that direction for the
     * chess points with that color.
     */
    public int findChessPointToADirection(ChessPoint currentChessPoint, Direction direction) {
        int numberOfChessPointsFound = 0;
        int xPositionOfChessPoint = currentChessPoint.getX();
        int yPositionOfChessPoint = currentChessPoint.getY();
        ChessColor chessColor = currentChessPoint.getChessColor();

        // Count the number of chess points to a specific direction based on the
        // current chess point color.
        switch (direction) {
            case EAST:
                for (int x = xPositionOfChessPoint + 1; x <= ChessBoard.NUM_OF_COLS; x++) {
                    if (isChessExist(x, yPositionOfChessPoint, chessColor)) {
                        numberOfChessPointsFound++;
                    } else {
                        break;
                    }
                }

                break;
            case SOUTH:
                for (int y = yPositionOfChessPoint + 1; y <= ChessBoard.NUM_OF_ROWS; y++) {
                    if (isChessExist(xPositionOfChessPoint, y, chessColor)) {
                        numberOfChessPointsFound++;
                    } else {
                        break;
                    }
                }

                break;
            case WEST:
                for (int x = xPositionOfChessPoint - 1; x >= 0; x--) {
                    if (isChessExist(x, yPositionOfChessPoint, chessColor)) {
                        numberOfChessPointsFound++;
                    } else {
                        break;
                    }
                }

                break;
            case NORTH:
                for (int y = yPositionOfChessPoint - 1; y >= 0; y--) {
                    if (isChessExist(xPositionOfChessPoint, y, chessColor)) {
                        numberOfChessPointsFound++;
                    } else {
                        break;
                    }
                }

                break;
            case EAST_NORTH:
                for (int x = xPositionOfChessPoint + 1, y = yPositionOfChessPoint - 1; x <= ChessBoard.NUM_OF_COLS && y >= 0; x++, y--) {
                    if (isChessExist(x, y, chessColor)) {
                        numberOfChessPointsFound++;
                    } else {
                        break;
                    }
                }

                break;
            case EAST_SOUTH:
                for (int x = xPositionOfChessPoint + 1, y = yPositionOfChessPoint + 1; x <= ChessBoard.NUM_OF_COLS && y <= ChessBoard.NUM_OF_ROWS; x++, y++) {
                    if (isChessExist(x, y, chessColor)) {
                        numberOfChessPointsFound++;
                    } else {
                        break;
                    }
                }

                break;
            case WEST_SOUTH:
                for (int x = xPositionOfChessPoint - 1, y = yPositionOfChessPoint + 1; x >= 0 && y <= ChessBoard.NUM_OF_ROWS; x--, y++) {
                    if (isChessExist(x, y, chessColor)) {
                        numberOfChessPointsFound++;
                    } else {
                        break;
                    }
                }

                break;
            case WEST_NORTH:
                for (int x = xPositionOfChessPoint - 1, y = yPositionOfChessPoint - 1; x >= 0 && y >= 0; x--, y--) {
                    if (isChessExist(x, y, chessColor)) {
                        numberOfChessPointsFound++;
                    } else {
                        break;
                    }
                }

                break;
        }

        return numberOfChessPointsFound;
    }

    /**
     * To check whether there is a chess point existing in a position.
     *
     * @param x The x position of the chess point.
     * @param y The y position of the chess point.
     * @return TURE - if there is a chess point in that position. FALSE - if
     * there is no chess point in that position.
     */
    public boolean isChessExist(int x, int y) {
        boolean result = false;

        int xPositionOfChessPoint;
        int yPositionOfChessPoint;

        for (ChessPoint chessPoint : getChessBoard().getChessPointCollection()) {
            if (chessPoint != null) {
                xPositionOfChessPoint = chessPoint.getX();
                yPositionOfChessPoint = chessPoint.getY();

                if (xPositionOfChessPoint == x && yPositionOfChessPoint == y) {
                    result = true;
                }
            }
        }

        return result;
    }

    /**
     * To check whether there is a chess point with a specific color existing in
     * a position.
     *
     * @param x The x position of the chess point
     * @param y The y position of the chess point
     * @param chessColor The color of the chess point
     * @return TURE - if there is a chess point with that color. FALSE - if
     * there is no chess point with that color
     */
    public boolean isChessExist(int x, int y, ChessColor chessColor) {
        boolean result = false;

        int xPositionOfChessPoint;
        int yPositionOfChessPoint;
        ChessColor colorOfChessPoint;

        for (ChessPoint chessPoint : getChessBoard().getChessPointCollection()) {
            if (chessPoint != null) {
                xPositionOfChessPoint = chessPoint.getX();
                yPositionOfChessPoint = chessPoint.getY();
                colorOfChessPoint = chessPoint.getChessColor();

                if (xPositionOfChessPoint == x && yPositionOfChessPoint == y && colorOfChessPoint == chessColor) {
                    result = true;
                }
            }
        }

        return result;
    }

    public boolean isChessBoardEmpty() {
        boolean result = false;

        if (chessBoard.getChessPointCollection().isEmpty()) {
            result = true;
        }

        return result;
    }

    /**
     * @return the scoreBlack
     */
    public int getScoreBlack() {
        return scoreBlack;
    }

    /**
     * @param scoreBlack the scoreBlack to set
     */
    public void setScoreBlack(int scoreBlack) {
        this.scoreBlack = scoreBlack;
    }

    /**
     * @return the scoreWhite
     */
    public int getScoreWhite() {
        return scoreWhite;
    }

    /**
     * @param scoreWhite the scoreWhite to set
     */
    public void setScoreWhite(int scoreWhite) {
        this.scoreWhite = scoreWhite;
    }

    /**
     * @return the blackTurn
     */
    public boolean isBlackTurn() {
        return blackTurn;
    }

    /**
     * @param blackTurn the blackTurn to set
     */
    public void setBlackTurn(boolean blackTurn) {
        this.blackTurn = blackTurn;
    }

    /**
     * @return the blackWin
     */
    public boolean isBlackWin() {
        return blackWin;
    }

    /**
     * @param blackWin the blackWin to set
     */
    public void setBlackWin(boolean blackWin) {
        this.blackWin = blackWin;
    }

    /**
     * @return the whiteWin
     */
    public boolean isWhiteWin() {
        return whiteWin;
    }

    /**
     * @param whiteWin the whiteWin to set
     */
    public void setWhiteWin(boolean whiteWin) {
        this.whiteWin = whiteWin;
    }

    /**
     * @return the chessBoard
     */
    public ChessBoard getChessBoard() {
        return chessBoard;
    }

    /**
     * @param chessBoard the chessBoard to set
     */
    public void setChessBoard(ChessBoard chessBoard) {
        this.chessBoard = chessBoard;
    }

    /**
     * @return the currentGameName
     */
    public String getCurrentGameName() {
        return currentGameName;
    }

    public ArrayList<String> getAllGameNames(DBManager dataBManager) {
        ResultSet rs = dataBManager.getAllTableNames();

        return dataBManager.convertResultSetToTableNameCollection(rs);
    }

    /**
     * @param currentGameName the currentGameName to set
     */
    public void setCurrentGameName(String currentGameName) {
        this.currentGameName = currentGameName;
    }

    /**
     * @return the currentChessPointValid
     */
    public boolean isCurrentChessPointValid() {
        return currentChessPointValid;
    }

    /**
     * @param currentChessPointValid the currentChessPointValid to set
     */
    public void setCurrentChessPointValid(boolean currentChessPointValid) {
        this.currentChessPointValid = currentChessPointValid;
    }

    /**
     * Reset the status of the judge to a new game.
     *
     * @param chessBoard The chess board the judge should be reset to.
     */
    public void resetJudgeToNewGame(ChessBoard chessBoard) {
        setCurrentGameName("Untitled");
        setCurrentChessBoardChanged(false);
        setCurrentChessPointValid(true);
        setChessBoard(chessBoard);
        setBlackTurn(true);
        setBlackWin(false);
        setWhiteWin(false);
    }

    /**
     * Reset the status of the judge to a new game.
     *
     * @param chessBoard The chess board the judge should be reset to.
     */
    public void resetJudgeFromExistingGame(String currentGameName) {
        setCurrentGameName(currentGameName);
        setBlackTurn(getNumOfChess(chessBoard.getChessPointCollection(), ChessColor.BLACK) == getNumOfChess(chessBoard.getChessPointCollection(), ChessColor.WHITE));
        setBlackWin(false);
        setWhiteWin(false);
        setCurrentChessBoardChanged(false); // After the existing game first starts, the chess board is not changed unless a new chess point is placed on the board.
        setCurrentChessPointValid(true);
        setScoreBlack(0);
        setScoreWhite(0);
    }

    private int getNumOfChess(ArrayList<ChessPoint> chessPointCollection, ChessColor chessColor) {
        int numOfChess = 0;

        for (ChessPoint cp : chessPointCollection) {
            numOfChess += (cp.getChessColor() == chessColor ? 1 : 0);
        }

        return numOfChess;
    }

    /**
     * @return the currentChessBoardChanged
     */
    public boolean isCurrentChessBoardChanged() {
        return currentChessBoardChanged;
    }

    /**
     * @param currentChessBoardChanged the currentChessBoardChanged to set
     */
    public void setCurrentChessBoardChanged(boolean currentChessBoardChanged) {
        this.currentChessBoardChanged = currentChessBoardChanged;
    }
}
