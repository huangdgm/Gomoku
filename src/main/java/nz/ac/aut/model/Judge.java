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

    private boolean isBlackTurn;
    private boolean isBlackWin;
    private boolean gameOver;
    private ChessBoard chessBoard;

    // To control the game, all the information the judge should know is the current chess board.
    public Judge(ChessBoard chessBoard) {
        // According to Gomoku's rule, black player comes first
        isBlackTurn = true;

        // At the beginning of the game, no player wins the game.
        isBlackWin = false;
        gameOver = false;

        this.chessBoard = chessBoard;
    }

    /**
     * To check whether the current chess point meets the following criteria:
     *
     * 1. The chess point falls into the scope of the chess board; 2. There is no chess point in the same position.
     *
     * @param chessPoint The chess point to be checked.
     * @return True - if the chess point is valid or False - if the chess point is invalid.
     */
    public boolean isChessPointValid(ChessPoint chessPoint) {
        boolean result = true;

        int x = chessPoint.getX();
        int y = chessPoint.getY();

        if (isChessExist(x, y) || x < 0 || x > 15 || y < 0 || y > 15) {
            result = false;
        }

        return result;
    }

    /**
     * To check whether it is game over after the current chess point.
     *
     * @param chessPoint The current chess point to be checked.
     * @return True - if game over or False - if not game over.
     */
    public boolean isGameOver(ChessPoint chessPoint) {
        boolean result = false;

        if (isWinnerAfterTheCurrentChessPoint(chessPoint)) {
            result = true;
        }

        return result;
    }

    /**
     * To check whether it is game over after the current chess point.
     *
     * @param chessPoint The current chess point to be checked.
     * @return True - if game over or False - if not game over.
     */
    private boolean isWinnerAfterTheCurrentChessPoint(ChessPoint chessPoint) {
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
                numberOfChessPointsFoundHorizontally = findChessPointToADirection(chessPoint, direction);
                numberOfChessPointsInARow += numberOfChessPointsFoundHorizontally;
            }

            // Count the number of chess points in a column.
            if (direction == Direction.SOUTH || direction == Direction.NORTH) {
                numberOfChessPointsFoundVertically = findChessPointToADirection(chessPoint, direction);
                numberOfChessPointsInAColumn += numberOfChessPointsFoundVertically;
            }

            // Count the number of chess points in the northeast and the southwest direction.
            if (direction == Direction.EAST_NORTH || direction == Direction.WEST_SOUTH) {
                numberOfChessPointsFoundENWS = findChessPointToADirection(chessPoint, direction);
                numberOfChessPointsInENWS += numberOfChessPointsFoundENWS;
            }

            // Count the number of chess points in the southeast and the northwest direction.
            if (direction == Direction.EAST_SOUTH || direction == Direction.WEST_NORTH) {
                numberOfChessPointsFoundESWN = findChessPointToADirection(chessPoint, direction);
                numberOfChessPointsInESWN += numberOfChessPointsFoundESWN;
            }
        }

        // If any of the direction has 5 or more continuous points with the same color, then there's a winner.
        if (numberOfChessPointsInARow >= 5 || numberOfChessPointsInAColumn >= 5 || numberOfChessPointsInENWS >= 5 || numberOfChessPointsInESWN >= 5) {
            result = true;
            // Set the flag whether the black player wins or the white player wins.
            setBlackWin(chessPoint.getChessColor() == ChessColor.BLACK ? true : false);
        }

        return result;
    }

    /**
     * Count the number of continuous chess points to a specific direction for a specific color.
     *
     * @param chessPoint The current chess point.
     * @param direction The direction to which the number of continuous chess points will be counted.
     * @return The number of continuous chess points to that direction for the chess points with that color.
     */
    private int findChessPointToADirection(ChessPoint chessPoint, Direction direction) {
        int numberOfChessPointsFound = 0;
        int xPositionOfChessPoint = chessPoint.getX();
        int yPositionOfChessPoint = chessPoint.getY();
        ChessColor chessColor = chessPoint.getChessColor();

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
    private boolean isChessExist(int x, int y) {
        boolean result = false;

        int xPositionOfChessPoint;
        int yPositionOfChessPoint;

        for (ChessPoint chessPoint : chessBoard.getChessPointCollection()) {
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
    private boolean isChessExist(int x, int y, ChessColor chessColor) {
        boolean result = false;

        int xPositionOfChessPoint;
        int yPositionOfChessPoint;
        ChessColor colorOfChessPoint;

        for (ChessPoint chessPoint : chessBoard.getChessPointCollection()) {
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

    public boolean isBlackTurn() {
        return isBlackTurn;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public void setBlackTurn(boolean isBlackTurn) {
        this.isBlackTurn = isBlackTurn;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    public boolean isBlackWin() {
        return isBlackWin;
    }

    public void setBlackWin(boolean isBlackWin) {
        this.isBlackWin = isBlackWin;
    }
}
