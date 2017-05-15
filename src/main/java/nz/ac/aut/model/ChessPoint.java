/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nz.ac.aut.model;

/**
 * The ChessPoint consists of the chess point attributes - coordinate and the
 * chess color.
 *
 * @author xfn
 */
public class ChessPoint {

    private int x;
    private int y;
    private ChessColor chessColor;

    public ChessPoint(int x, int y, ChessColor chessColor) {
        super();
        
        this.x = x;
        this.y = y;
        this.chessColor = chessColor;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public ChessColor getChessColor() {
        return chessColor;
    }

    public void setChessColor(ChessColor chessColor) {
        this.chessColor = chessColor;
    }
}
