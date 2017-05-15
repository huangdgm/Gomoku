/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nz.ac.aut.model;

import java.util.ArrayList;

/**
 * The ChessBoard class represents the actual 15*15 chess board, and there is a collection of chess points on the board.
 *
 * A chess board has the following attributes: 1. The number of rows on the board. 2. The number of columns on the
 * board. 3. A collection of chess points on the board. These points can include a number of white points as well as
 * black points.
 *
 * @author Dong Huang
 */
public class ChessBoard {

    public static final int NUM_OF_ROWS = 15;
    public static final int NUM_OF_COLS = 15;

    private ArrayList<ChessPoint> chessPointCollection;

    public ChessBoard(ArrayList<ChessPoint> chessPointCollection) {
        this.chessPointCollection = chessPointCollection;
    }

    /**
     * Draw the ASCII representation of the chess board based on the distribution of the current chess point collection.
     */
    public void drawChessBoard() {
        // The string builder serves as the string builder to print the whole chess board.
        StringBuilder sb = new StringBuilder();
        // The collection of chess points in a specific line.
        ArrayList<ChessPoint> chessPointsInARow = new ArrayList<ChessPoint>();

        sb.append("\n     0   1   2   3   4   5   6   7   8   9   10  11  12  13  14  15\n\n");

        // Construct the string builder line by line.
        for (int lineNumber = 0; lineNumber <= NUM_OF_ROWS; lineNumber++) {
            if (lineNumber < 10) {
                sb.append(lineNumber + "    ");
                drawLine(sb, chessPointsInARow, lineNumber);
            } else if (lineNumber >= 10) {
                sb.append(lineNumber + "   ");
                drawLine(sb, chessPointsInARow, lineNumber);
            }
        }

        System.out.println(sb);
    }

    /**
     * Construct a chess board line and append to the string builder.
     *
     * @param sb The string builder to which the line should be appended.
     * @param chessPointInARow The collection of chess points in the lineNumber.
     * @param lineNumber The line number that will be constructed.
     */
    private void drawLine(StringBuilder sb, ArrayList<ChessPoint> chessPointsInARow, int lineNumber) {
        // Construct the chess point collection in this line number.
        for (ChessPoint chessPoint : chessPointCollection) {
            if (chessPoint.getY() == lineNumber) {
                chessPointsInARow.add(chessPoint);
            }
        }

        if (chessPointsInARow.isEmpty()) {
            sb.append("-------------------------------------------------------------");
        } else {
            // Whether there is a chess point in this line number.
            boolean isExist = false;
            ChessColor chessColor = ChessColor.BLACK;

            for (int columnNumber = 0; columnNumber <= NUM_OF_COLS; columnNumber++) {
                for (ChessPoint chessPoint : chessPointsInARow) {
                    if (chessPoint.getX() == columnNumber) {
                        isExist = true;
                        chessColor = chessPoint.getChessColor();
                    }
                }

                if (isExist) {
                    switch (chessColor) {
                        case BLACK:
                            if (columnNumber == 15) {
                                sb.append("B");
                            } else {
                                sb.append("B---");
                            }

                            break;
                        case WHITE:
                            if (columnNumber == 15) {
                                sb.append("W");
                            } else {
                                sb.append("W---");
                            }

                            break;
                    }
                } else {
                    if (columnNumber != 15) {
                        sb.append("----");
                    } else {
                        sb.append("-");
                    }
                }

                isExist = false;
                chessColor = ChessColor.BLACK;
            }

            chessPointsInARow.clear();
        }

        if (lineNumber != 15) {
            sb.append("\n     |   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |\n");
        }
    }

    public ArrayList<ChessPoint> getChessPointCollection() {
        return chessPointCollection;
    }

    public void setChessPointCollection(ArrayList<ChessPoint> chessPointCollection) {
        this.chessPointCollection = chessPointCollection;
    }
}
