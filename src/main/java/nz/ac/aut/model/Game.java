/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nz.ac.aut.model;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * The Game class serves as the controller of the whole game. It
 * defines the process of the game and validate the user input.
 * 
 * @author Dong Huang
 */
public class Game {
	ChessBoard chessBoard = null;
	Judge judge = null;
        ArrayList<ChessPoint> chessPointCollection = null;
	FileIO fileIO = null;

	/**
	 * A GomokuGameController consists of 4 elements: 1. ChessBoard; 2. A
	 * collection of ChessPoint; 3. A judge; 4. A FileIO.
	 */
	public Game(ChessBoard chessBoard, Judge judge, FileIO fileIO) {
		super();
                
		this.chessBoard = chessBoard;
                this.chessPointCollection = chessBoard.getChessPointCollection();
		this.judge = judge;
		this.fileIO = fileIO;
	}

	/**
	 * The main process the game should follow.
	 */
	public void startGame() {
		Scanner scanner = new Scanner(System.in);

		printWelcomeMessage();
		printOptionMessage();

		initializeGame(scanner);
		playGame(scanner);

		scanner.close();
	}

	private void printOptionMessage() {
		System.out.println("Choose your option?");
		System.out.println("1. Start a new game");
		System.out.println("2. Load a history game");
	}

	/**
	 * After the game has been initialized, the game starts and the playGame
	 * method controls the process of the game. Each color plays alternatively.
	 * After each chess point, the judge checks whether it is game over. If it
	 * is game over, the judge can tell which color win the game.
	 * 
	 * @param scanner
	 */
	private void playGame(Scanner scanner) {
		ChessPoint currentChessPoint = null;

		do {
			chessBoard.drawChessBoard();

			if (judge.isBlackTurn()) {
				currentChessPoint = readChessPoint(scanner, ChessColor.BLACK);
				judge.setBlackTurn(false);
			} else {
				currentChessPoint = readChessPoint(scanner, ChessColor.WHITE);
				judge.setBlackTurn(true);
			}

			chessPointCollection.add(currentChessPoint);
			chessBoard.setChessPointCollection(chessPointCollection);
			judge.setChessPointCollection(chessPointCollection);
		} while (!judge.isGameOver(currentChessPoint));

		chessBoard.drawChessBoard();

		System.out.print("\nGame Over...");

		if (judge.isBlackWin()) {
			System.out.println("BLACK wins!");
		} else {
			System.out.println("WHITE wins!");
		}
	}

	/**
	 * Initialize the game by prompting the user whether the user want to start
	 * a new game or load history progress information. If the user want to load
	 * history progress information, then the desired text file will be loaded
	 * and be used to populate the chess board. Also, it validates the user
	 * input.
	 */
	private void initializeGame(Scanner scanner) {
		String optionNumber = null;

		do {
			optionNumber = scanner.next().replaceAll("\\s+", " ").trim();

			if (optionNumber.equals("2")) {
				File[] files = fileIO.listFiles();
				int index = 0;

				do {
					System.out.print("\nChoose a history file: ");

					while (!scanner.hasNextInt()) {
						scanner.next();
						System.out.println("\nInvalid choice...Please input an integer.");
						System.out.print("\nChoose a history file: ");
					}

					index = scanner.nextInt();

					if (index <= 0 || index >= files.length + 1) {
						System.out.println("\nInvalid choice...Please Please input an integer between 1 and " + files.length);
					}
				} while (index <= 0 || index >= files.length + 1);

				fileIO.readProgress(files[index - 1].getPath());
			} else if (!optionNumber.equals("1")) {
				System.out.println("\nInvalid choice...Please select again");
			}
		} while (!optionNumber.equals("1") && !optionNumber.equals("2"));
	}

	private void printWelcomeMessage() {
		System.out.println("\t\tWelcome to the GOMOKU world\n");
		System.out.println("Rules:\n");
		System.out.println("1. One player plays the BLACK, the other player plays the WHITE;\n");
		System.out.println("2. BLACK plays first;\n");
		System.out.println("3. The player who first places five points in a line win the game.\n");
	}

	/**
	 * Read a valid chess point by prompting the user with the proper message.
	 * 
	 * @param scanner
	 * @param chessColor
	 * @return The chess point which the player just plays.
	 */
	private ChessPoint readChessPoint(Scanner scanner, ChessColor chessColor) {
		ChessPoint currentChessPoint = null;
		int x = 0;
		int y = 0;

		do {
			if (currentChessPoint != null) {
				System.out.println("Invalid position...please try again (0,0) ~ (15,15).");
			}

			if (judge.isBlackTurn()) {
				System.out.println("\nEnter the position (BLACK).");
			} else {
				System.out.println("\nEnter the position (WHITE).");
			}

			System.out.print("x: ");
			x = readNumber(scanner);

			System.out.print("y: ");
			y = readNumber(scanner);

			currentChessPoint = new ChessPoint(x, y, chessColor);
		} while (!judge.isChessPointValid(currentChessPoint));

		return currentChessPoint;
	}

	/**
	 * Read a integer number from the user input and return this integer number,
	 * or just exist the program if the user chooses to quit.
	 * 
	 * @param scanner
	 * 
	 * @return The integer number of user input or just exist the program if the
	 *         user wants to quit the game.
	 */
	private int readNumber(Scanner scanner) {
		String input = null;
		int result = 0;

		do {
			input = scanner.next().trim();

			if (input.equalsIgnoreCase("quit")) {
				System.out.println("Your progress is saved successfully. Goodbye");
				fileIO.saveProgress();
				System.exit(0);
			} else if (input.matches("[0-9]+")) {
				result = Integer.valueOf(input);
			} else {
				System.out.print("Invalid position...Please input an integer: ");
			}
		} while (!input.matches("[0-9]+"));

		return result;
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

	public FileIO getFileIO() {
		return fileIO;
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

	public void setFileIO(FileIO fileIO) {
		this.fileIO = fileIO;
	}
}