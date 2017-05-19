/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nz.ac.aut.model;

import java.util.ArrayList;

/**
 * The Game class serves as the controller of the whole game. It defines the
 * process of the game and validate the user input.
 *
 * @author Dong Huang
 */
public class Game {

    private ChessBoard chessBoard;
    private Judge judge;

    private GameEventListener gameEventListener;
    private boolean currentChessPointValid;

    /**
     * A Game consists of 2 elements: 1. ChessBoard; 2. A judge;
     */
    public Game() {
        super();

        initializeGame();
    }

    /**
     * After the game has been initialized, the game starts and the playGame
     * method controls the process of the game. Each color plays alternatively.
     * After each chess point, the judge checks whether it is game over. If it
     * is game over, the judge can tell which color win the game.
     *
     * @param scanner
     */
    private void playGame() {
    }

    /**
     * Initialize the game by prompting the user whether the user want to start
     * a new game or load history progress information. If the user want to load
     * history progress information, then the desired text file will be loaded
     * and be used to populate the chess board. Also, it validates the user
     * input.
     */
    private void initializeGame() {
        // Create a new empty chess point collection
        ArrayList<ChessPoint> chessPointCollection = new ArrayList<ChessPoint>();

        // Create a new chess board based on the empty chess point collection
        chessBoard = new ChessBoard(chessPointCollection);

        // Make the judge to return to the original status
        judge = new Judge(chessBoard);

        // At the beginning of a new game, there is no chess point on the board.
        setCurrentChessPointValid(true);

        // Notify the game event listener to update the GUI
        notifyGameEventListener();
    }

    /**
     * Read a valid chess point by prompting the user with the proper message.
     *
     * @param currentChessPoint
     */
    public void placeChessPoint(ChessPoint currentChessPoint) {
        if (judge.isChessPointValid(currentChessPoint)) {
            // Reset the flag to true once the current chess point is valid
            setCurrentChessPointValid(true);
            // Set the current chess point
            chessBoard.setCurrentChessPoint(currentChessPoint);
            // Add the current chess point to the chess point collection
            chessBoard.getChessPointCollection().add(currentChessPoint);
            // Check who should play the next chess point after playing the current chess point
            judge.setBlackTurn((currentChessPoint.getChessColor() != ChessColor.BLACK));
        } else {
            // If the current chess point is not valid, then just set the flag to false and do nothing else.
            setCurrentChessPointValid(false);
        }

        notifyGameEventListener();
    }

    public ChessBoard getChessBoard() {
        return chessBoard;
    }

    public Judge getJudge() {
        return judge;
    }

    public void setChessBoard(ChessBoard chessBoard) {
        this.chessBoard = chessBoard;
    }

    public void setJudge(Judge judge) {
        this.judge = judge;
    }

    public void createNewGame() {
        // Clear the existing chess board and reset the current chess point to null.
        chessBoard.getChessPointCollection().clear();
        chessBoard.setCurrentChessPoint(null);

        // Make use of the existing judge, and make the judge to return to the original status
        judge.setChessBoard(chessBoard);
        judge.setBlackTurn(true);
        judge.setBlackWin(false);

        // Reset the flag to true
        setCurrentChessPointValid(true);

        // Notify the game event listener to update the GUI
        notifyGameEventListener();
    }
    
    public void saveGame() {
    }
    
    public boolean isChessBoardEmpty() {
        return judge.isChessBoardEmpty();
    }
    
    private void notifyGameEventListener() {
        if (gameEventListener != null) {
            getGameEventListener().gameStateChanged();
        }
    }

    /**
     * @return the gameEventListener
     */
    public GameEventListener getGameEventListener() {
        return gameEventListener;
    }

    /**
     * @param gameEventListener the gameEventListener to set
     */
    public void setGameEventListener(GameEventListener gameEventListener) {
        this.gameEventListener = gameEventListener;
    }

    /**
     * @return the isCurrentChessPointValid
     */
    public boolean isCurrentChessPointValid() {
        return currentChessPointValid;
    }

    /**
     * @param isCurrentChessPointValid the isCurrentChessPointValid to set
     */
    public void setCurrentChessPointValid(boolean isCurrentChessPointValid) {
        this.currentChessPointValid = isCurrentChessPointValid;
    }
}
