/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nz.ac.aut.main;

import java.util.ArrayList;

import nz.ac.aut.model.ChessBoard;
import nz.ac.aut.model.ChessPoint;
import nz.ac.aut.model.FileIO;
import nz.ac.aut.model.Game;
import nz.ac.aut.model.Judge;

/**
 * Welcome to the GOMOKU world
 * 
 * Rules:
 * 
 * 1. One player plays the BLACK, the other player plays the WHITE;
 * 
 * 2. BLACK plays first;
 * 
 * 3. The player who first places five points in a line win the game.
 * 
 * 4. The player can terminate the game whenever the player inputs 'quit'.
 * 
 * 5. If the game is terminated in the middle, the game data will be stored.
 * 
 * 6. At the beginning of the game, the player can choose to start a new game or
 *    resume a history game by reading from a history file.
 *
 * 
 * This is the Entry point class of the game. A whole gomoku game consists of
 * the following elements:
 * 
 * 1. A chess point collection 2. A chess board 3. A judge 4. A file io
 * controller
 * 
 * It instantiates the chessPointCollection, chessBoard, judge and fileIO and
 * pass these parameters to the GomokuGameController from which the game can be
 * launched.
 *
 * @author Dong Huang
 */
public class Gomoku {

	public static void main(String[] args) {
		// Instantiate the objects that are parts of the game
		ArrayList<ChessPoint> chessPointCollection = new ArrayList<ChessPoint>();
		ChessBoard chessBoard = new ChessBoard(chessPointCollection);
		Judge judge = new Judge(chessPointCollection);
		FileIO fileIO = new FileIO(chessPointCollection);

		Game gomoku = new Game(chessBoard, chessPointCollection, judge, fileIO);

		gomoku.startGame();
	}
}