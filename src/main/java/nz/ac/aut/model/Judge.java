/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nz.ac.aut.model;

import java.util.ArrayList;

/**
 * This class serves as the 'judge' class, which can check the following conditions:
 *
 * 1. Who win the game? 2. Is it black's turn for the current chess point? 3. Game over or not?
 *
 * @author Dong Huang
 */
public class Judge {

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

    private boolean blackTurn;
    private boolean blackWin;
    private boolean whiteWin;
    private ChessBoard chessBoard;

    // To control the game, all the information the judge should know is the current chess board.
    public Judge(ChessBoard chessBoard) {
        // According to Gomoku's rule, black player comes first
        blackTurn = true;

        // At the beginning of the game, no player wins the game.
        blackWin = false;

        this.chessBoard = chessBoard;
    }

    /**
     * To check whether the current chess point meets the following criteria:
     *
     * 1. The chess point falls into the scope of the chess board; 2. There is no chess point in the same position.
     *
     * @param currentChessPoint The chess point to be checked.
     * @return True - if the chess point is valid or False - if the chess point is invalid.
     */
    public boolean isChessPointValid(ChessPoint currentChessPoint) {
        boolean result = true;

        int x = currentChessPoint.getX();
        int y = currentChessPoint.getY();

        if (isChessExist(x, y) || x < 0 || x > 15 || y < 0 || y > 15) {
            result = false;
        }

        //System.out.println(x + " : " + y + " : " + currentChessPoint.getChessColor());
        //System.out.println(isChessExist(x, y) || x < 0 || x > 15 || y < 0 || y > 15);

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
            setBlackWin(currentChessPoint.getChessColor() == ChessColor.BLACK ? true : false);
            setWhiteWin(currentChessPoint.getChessColor() == ChessColor.WHITE ? true : false);
        }

        return result;
    }

    /**
     * Count the number of continuous chess points to a specific direction for a specific color.
     *
     * @param currentChessPoint The current chess point.
     * @param direction The direction to which the number of continuous chess points will be counted.
     * @return The number of continuous chess points to that direction for the chess points with that color.
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
     * @return TURE - if there is a chess point in that position. FALSE - if there is no chess point in that position.
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
     * To check whether there is a chess point with a specific color existing in a position.
     *
     * @param x The x position of the chess point
     * @param y The y position of the chess point
     * @param chessColor The color of the chess point
     * @return TURE - if there is a chess point with that color. FALSE - if there is no chess point with that color
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
}
