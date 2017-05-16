/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nz.ac.aut.model;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * The Game class serves as the controller of the whole game. It defines the process of the game and validate the user
 * input.
 *
 * @author Dong Huang
 */
public class Game {

    private ChessBoard chessBoard = null;
    private Judge judge;
    private ArrayList<ChessPoint> chessPointCollection;
    private GameEventListener gameEventListener;

    /**
     * A Game consists of 3 elements: 1. ChessBoard; 2. A collection of ChessPoint; 3. A judge;
     */
    public Game() {
        super();
        createNewGame();
    }

    /**
     * The main process the game should follow.
     */
    public void startGame() {
        Scanner scanner = new Scanner(System.in);

        initializeGame();
        playGame();

        scanner.close();
    }

    /**
     * After the game has been initialized, the game starts and the playGame method controls the process of the game.
     * Each color plays alternatively. After each chess point, the judge checks whether it is game over. If it is game
     * over, the judge can tell which color win the game.
     *
     * @param scanner
     */
    private void playGame() {
    }

    /**
     * Initialize the game by prompting the user whether the user want to start a new game or load history progress
     * information. If the user want to load history progress information, then the desired text file will be loaded and
     * be used to populate the chess board. Also, it validates the user input.
     */
    private void initializeGame() {
    }

    /**
     * Read a valid chess point by prompting the user with the proper message.
     *
     * @param scanner
     * @param chessColor
     * @return The chess point which the player just plays.
     */
    private ChessPoint readChessPoint() {
        return null;
    }

    public ChessBoard getChessBoard() {
        return chessBoard;
    }

    public ArrayList<ChessPoint> getChessPointCollection() {
        return chessPointCollection;
    }

    public Judge getJudge() {
        return judge;
    }

    public void setChessBoard(ChessBoard chessBoard) {
        this.chessBoard = chessBoard;
    }

    public void setChessPointCollection(ArrayList<ChessPoint> chessPointCollection) {
        this.chessPointCollection = chessPointCollection;
    }

    public void setJudge(Judge judge) {
        this.judge = judge;
    }

    public void createNewGame() {
        // Create a new empty chess point collection
        chessPointCollection = new ArrayList<ChessPoint>();

        // Create a new chess board based on the empty chess point collection
        chessBoard = new ChessBoard(chessPointCollection);

        // Create a new judge based on the 
        judge = new Judge(chessBoard);

        // Notify the game event listener to update the GUI
        notifyGameEventListener();
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
}
