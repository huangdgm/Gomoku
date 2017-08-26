package nz.ac.aut.model;

import java.util.ArrayList;

/**
 * The Game class serves as the controller of the whole game. It defines the
 * process of the game and validate the user input.
 *
 * A Game consists of 2 elements: 1. ChessBoard; 2. A judge; 3. A DB manager.
 *
 * @author Dong Huang
 */
public class Game {

    public final String URL = "jdbc:derby:gomokuDB;create=true";
    public final String USERNAME = "pdc";
    public final String PASSWORD = "123";

    private ChessBoard chessBoard;
    private Judge judge;
    private DBManager databaseManager;

    private GameEventListener gameEventListener;

    public Game() {
        super();

        initializeGame();
    }

    /**
     * Initialize the game at the beginning of the start of the whole program,
     * by creating the following elements:
     *
     *
     * 1. An empty chess point collection. 2. A database manager for handling
     * operations related to database. 3. An empty chess board 4. A judge who
     * controls the game.
     */
    private void initializeGame() {
        // Create a DB manager for storing persisting data.
        // The db connection is established when the db manager is instantiated.
        setDatabaseManager(new DBManager(URL, USERNAME, PASSWORD));

        // Create a new chess board based on the empty chess point collection
        setChessBoard(new ChessBoard(new ArrayList<ChessPoint>()));

        // Make the judge to return to the original status
        setJudge(new Judge(chessBoard));

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
            // Reset the flag to true once the current chess point is valid.
            setCurrentChessPointValid(true);

            // The chess board is changed when a new chess point is place on the board.
            setCurrentChessBoardChanged(true);

            // Set the current chess point.
            setCurrentChessPoint(currentChessPoint);

            // Add the current chess point to the chess board.
            addChessPointToChessBoard(currentChessPoint);

            // Check who should play the next chess point after playing the current chess point.
            setBlackTurn(currentChessPoint);
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

    public void saveGame(DBManager databaseManager, String tableName) {
        judge.saveGame(databaseManager, tableName);
        
        notifyGameEventListener();
    }
    
    public boolean isChessBoardEmpty() {
        return judge.isChessBoardEmpty();
    }

    public boolean isCurrentChessBoardChanged() {
        return judge.isCurrentChessBoardChanged();
    }

    public void setCurrentChessBoardChanged(boolean currentChessBoardChanged) {
        judge.setCurrentChessBoardChanged(currentChessBoardChanged);
    }

    public String getCurrentGameName() {
        return judge.getCurrentGameName();
    }

    public void setCurrentGameName(String currentGameName) {
        judge.setCurrentGameName(currentGameName);
        
        notifyGameEventListener();
    }

    public ArrayList<String> getAllGameNames(DBManager databaseManager) {
        return judge.getAllGameNames(databaseManager);
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
        return judge.isCurrentChessPointValid();
    }
    
    public boolean isBlackTurn() {
        return judge.isBlackTurn();
    }

    /**
     * @param isCurrentChessPointValid the isCurrentChessPointValid to set
     */
    public void setCurrentChessPointValid(boolean isCurrentChessPointValid) {
        judge.setCurrentChessPointValid(isCurrentChessPointValid);
    }

    /**
     * @return the databaseManager
     */
    public DBManager getDatabaseManager() {
        return databaseManager;
    }

    /**
     * @param databaseManager the databaseManager to set
     */
    public void setDatabaseManager(DBManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    /**
     * Create a new game.
     */
    public void createNewGame() {
        // Clear the existing chess board and reset the current chess point to null.
        resetChessBoardToNewGame();

        // Make use of the existing judge, and make the judge to return to the original status
        resetJudgeToNewGame(chessBoard);

        // Notify the game event listener to update the GUI
        notifyGameEventListener();
    }

    /**
     * Recover an existing game from the database.
     *
     * @param tableName
     */
    public void creatExistingGameFromTable(String tableName) {
        resetChessBoardFromExistingGame(databaseManager, tableName);
        resetJudgeFromExistingGame(tableName);
        
        notifyGameEventListener();
    }

    /**
     * Reset the chess board to the state of a new game.
     */
    private void resetChessBoardToNewGame() {
        chessBoard.resetChessBoardToNewGame();
        
        notifyGameEventListener();
    }

    private void resetChessBoardFromExistingGame(DBManager databaseManager, String tableName) {
        chessBoard.resetChessBoardFromExistingGame(databaseManager, tableName);
        
        notifyGameEventListener();
    }

    private void resetJudgeToNewGame(ChessBoard chessBoard) {
        judge.resetJudgeToNewGame(chessBoard);
        
        notifyGameEventListener();
    }

    private void resetJudgeFromExistingGame(String currentGameName) {
        judge.resetJudgeFromExistingGame(currentGameName);
        
        notifyGameEventListener();
    }

    private void setCurrentChessPoint(ChessPoint currentChessPoint) {
        chessBoard.setCurrentChessPoint(currentChessPoint);
    }

    private void addChessPointToChessBoard(ChessPoint currentChessPoint) {
        chessBoard.getChessPointCollection().add(currentChessPoint);
    }
    
    private void setBlackTurn(ChessPoint currentChessPoint) {
        judge.setBlackTurn((currentChessPoint.getChessColor() != ChessColor.BLACK));
    }
}
