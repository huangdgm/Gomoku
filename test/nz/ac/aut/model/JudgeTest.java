/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nz.ac.aut.model;

import java.util.ArrayList;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Dong Huang
 */
public class JudgeTest {

    ArrayList<ChessPoint> chessPointCollection;
    ChessBoard chessBoard;
    Judge judge;

    public JudgeTest() {
    }

    @Before
    public void setUp() {
        chessPointCollection = new ArrayList<>();
        chessBoard = new ChessBoard(chessPointCollection);
        judge = new Judge(chessBoard);
    }

    @After
    public void tearDown() {
        chessPointCollection = null;
        chessBoard = null;
        judge = null;
    }

    /**
     * Test of isChessPointValid method, of class Judge.
     *
     * Test the situation when there's already an existing chess point on the
     * board, the isChessPointValid method should correctly return false against
     * the duplicated chess point.
     */
    @Test
    public void testIsChessPointValidOnExistingChessPoint() {
        chessPointCollection.add(new ChessPoint(1, 2, ChessColor.BLACK));
        ChessPoint cp = new ChessPoint(1, 2, ChessColor.BLACK);

        boolean expResult = false;
        boolean result = judge.isChessPointValid(cp);

        assertEquals(expResult, result);
    }

    /**
     * Test of isChessPointValid method, of class Judge.
     *
     * Test the situation when the player try to place the chess point out of
     * the bound of the chess board, the isChessPointValid method should
     * correctly return false against those invalid chess point.
     */
    @Test
    public void testIsChessPointValidOnOutOfBoundChessPoint() {
        ChessPoint[] currentChessPoint = new ChessPoint[]{
            new ChessPoint(-1, 3, ChessColor.BLACK),
            new ChessPoint(1, 30, ChessColor.BLACK),
            new ChessPoint(1, -3, ChessColor.BLACK),
            new ChessPoint(-1, -3, ChessColor.BLACK),
            new ChessPoint(40, 30, ChessColor.BLACK),
            new ChessPoint(40, 3, ChessColor.BLACK)
        };

        boolean expResult = false;

        for (int i = 0; i < 5; i++) {
            boolean result = judge.isChessPointValid(currentChessPoint[i]);
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of isChessPointExist method, of class Judge.
     *
     * Test the situation when there's already an existing chess point on the
     * board, the isChessPointValid method should correctly return false against
     * the duplicated chess point.
     */
    @Test
    public void testIsChessPointExistOnExistingChessPoint() {
        chessPointCollection.add(new ChessPoint(2, 3, ChessColor.BLACK));
        ChessPoint cp = new ChessPoint(2, 3, ChessColor.BLACK);

        boolean expResult = false;
        boolean result = judge.isChessPointValid(cp);

        assertEquals(expResult, result);
    }

    /**
     * Test of isWinnerAfterTheCurrentChessPoint method, of class Judge.
     *
     * Test the isWinnerAfterTheCurrentChessPoint can successfully detect the
     * winner of the game when there are at least five chess points with the
     * same color in a line.
     */
    @Test
    public void testIsWinnerAfterTheCurrentChessPoint() {
        ChessPoint currentChessPoint = new ChessPoint(3, 5, ChessColor.BLACK);

        chessPointCollection.add(new ChessPoint(3, 6, ChessColor.BLACK));
        chessPointCollection.add(new ChessPoint(3, 7, ChessColor.BLACK));
        chessPointCollection.add(new ChessPoint(3, 8, ChessColor.BLACK));
        chessPointCollection.add(new ChessPoint(3, 9, ChessColor.BLACK));

        boolean expResult = true;
        boolean result = judge.isWinnerAfterTheCurrentChessPoint(currentChessPoint);

        assertEquals(expResult, result);
    }

    /**
     * Test of findChessPointToADirection method, of class Judge.
     *
     * Test the findChessPointToADirection can successfully count the number of
     * continuous chess points with the same color to the direction specified by
     * the parameter.
     */
    @Test
    public void testFindChessPointToEast() {
        Direction direction = Direction.EAST;
        ChessPoint currentChessPoint = new ChessPoint(8, 8, ChessColor.BLACK);

        chessPointCollection.add(new ChessPoint(9, 8, ChessColor.BLACK));
        chessPointCollection.add(new ChessPoint(10, 8, ChessColor.BLACK));
        chessPointCollection.add(new ChessPoint(11, 8, ChessColor.WHITE));

        int expResult = 2;  // Only two black chess points
        int result = judge.findChessPointToADirection(currentChessPoint, direction);

        assertEquals(expResult, result);
    }

    /**
     * Test of findChessPointToADirection method, of class Judge.
     *
     * Test the findChessPointToADirection can successfully count the number of
     * continuous chess points with the same color to the direction specified by
     * the parameter.
     */
    @Test
    public void testFindChessPointToSouth() {
        Direction direction = Direction.SOUTH;
        ChessPoint currentChessPoint = new ChessPoint(8, 8, ChessColor.BLACK);

        chessPointCollection.add(new ChessPoint(8, 9, ChessColor.BLACK));
        chessPointCollection.add(new ChessPoint(8, 10, ChessColor.BLACK));
        chessPointCollection.add(new ChessPoint(8, 11, ChessColor.WHITE));
        chessPointCollection.add(new ChessPoint(8, 12, ChessColor.BLACK));

        int expResult = 2;  // Only two continuous black chess points
        int result = judge.findChessPointToADirection(currentChessPoint, direction);

        assertEquals(expResult, result);
    }

    /**
     * Test of findChessPointToADirection method, of class Judge.
     *
     * Test the findChessPointToADirection can successfully count the number of
     * continuous chess points with the same color to the direction specified by
     * the parameter.
     */
    @Test
    public void testFindChessPointToWest() {
        Direction direction = Direction.WEST;
        ChessPoint currentChessPoint = new ChessPoint(8, 8, ChessColor.BLACK);

        chessPointCollection.add(new ChessPoint(7, 8, ChessColor.BLACK));
        chessPointCollection.add(new ChessPoint(6, 8, ChessColor.BLACK));
        chessPointCollection.add(new ChessPoint(5, 8, ChessColor.WHITE));
        chessPointCollection.add(new ChessPoint(4, 8, ChessColor.BLACK));

        int expResult = 2;  // Only two continuous black chess points
        int result = judge.findChessPointToADirection(currentChessPoint, direction);

        assertEquals(expResult, result);
    }

    /**
     * Test of findChessPointToADirection method, of class Judge.
     *
     * Test the findChessPointToADirection can successfully count the number of
     * continuous chess points with the same color to the direction specified by
     * the parameter.
     */
    @Test
    public void testFindChessPointToNorth() {
        Direction direction = Direction.NORTH;
        ChessPoint currentChessPoint = new ChessPoint(8, 8, ChessColor.BLACK);

        chessPointCollection.add(new ChessPoint(8, 7, ChessColor.BLACK));
        chessPointCollection.add(new ChessPoint(8, 6, ChessColor.BLACK));
        chessPointCollection.add(new ChessPoint(8, 5, ChessColor.WHITE));
        chessPointCollection.add(new ChessPoint(8, 4, ChessColor.BLACK));

        int expResult = 2;  // Only two continuous black chess points
        int result = judge.findChessPointToADirection(currentChessPoint, direction);

        assertEquals(expResult, result);
    }

    /**
     * Test of findChessPointToADirection method, of class Judge.
     *
     * Test the findChessPointToADirection can successfully count the number of
     * continuous chess points with the same color to the direction specified by
     * the parameter.
     */
    @Test
    public void testFindChessPointToNorthEast() {
        Direction direction = Direction.EAST_NORTH;
        ChessPoint currentChessPoint = new ChessPoint(8, 8, ChessColor.BLACK);

        chessPointCollection.add(new ChessPoint(9, 7, ChessColor.BLACK));
        chessPointCollection.add(new ChessPoint(10, 6, ChessColor.BLACK));
        chessPointCollection.add(new ChessPoint(11, 5, ChessColor.WHITE));
        chessPointCollection.add(new ChessPoint(12, 4, ChessColor.BLACK));

        int expResult = 2;  // Only two continuous black chess points
        int result = judge.findChessPointToADirection(currentChessPoint, direction);

        assertEquals(expResult, result);
    }

    /**
     * Test of findChessPointToADirection method, of class Judge.
     *
     * Test the findChessPointToADirection can successfully count the number of
     * continuous chess points with the same color to the direction specified by
     * the parameter.
     */
    @Test
    public void testFindChessPointToSouthEast() {
        Direction direction = Direction.EAST_SOUTH;
        ChessPoint currentChessPoint = new ChessPoint(8, 8, ChessColor.BLACK);

        chessPointCollection.add(new ChessPoint(9, 9, ChessColor.BLACK));
        chessPointCollection.add(new ChessPoint(10, 10, ChessColor.BLACK));
        chessPointCollection.add(new ChessPoint(11, 11, ChessColor.WHITE));
        chessPointCollection.add(new ChessPoint(12, 12, ChessColor.BLACK));

        int expResult = 2;  // Only two continuous black chess points
        int result = judge.findChessPointToADirection(currentChessPoint, direction);

        assertEquals(expResult, result);
    }

    /**
     * Test of findChessPointToADirection method, of class Judge.
     *
     * Test the findChessPointToADirection can successfully count the number of
     * continuous chess points with the same color to the direction specified by
     * the parameter.
     */
    @Test
    public void testFindChessPointToSouthWest() {
        Direction direction = Direction.WEST_SOUTH;
        ChessPoint currentChessPoint = new ChessPoint(8, 8, ChessColor.BLACK);

        chessPointCollection.add(new ChessPoint(7, 9, ChessColor.BLACK));
        chessPointCollection.add(new ChessPoint(6, 10, ChessColor.BLACK));
        chessPointCollection.add(new ChessPoint(5, 11, ChessColor.WHITE));
        chessPointCollection.add(new ChessPoint(4, 12, ChessColor.BLACK));

        int expResult = 2;  // Only two continuous black chess points
        int result = judge.findChessPointToADirection(currentChessPoint, direction);

        assertEquals(expResult, result);
    }

    /**
     * Test of findChessPointToADirection method, of class Judge.
     *
     * Test the findChessPointToADirection can successfully count the number of
     * continuous chess points with the same color to the direction specified by
     * the parameter.
     */
    @Test
    public void testFindChessPointToNorthWest() {
        Direction direction = Direction.WEST_NORTH;
        ChessPoint currentChessPoint = new ChessPoint(8, 8, ChessColor.BLACK);

        chessPointCollection.add(new ChessPoint(7, 7, ChessColor.BLACK));
        chessPointCollection.add(new ChessPoint(6, 6, ChessColor.BLACK));
        chessPointCollection.add(new ChessPoint(5, 5, ChessColor.WHITE));
        chessPointCollection.add(new ChessPoint(4, 4, ChessColor.BLACK));

        int expResult = 2;  // Only two continuous black chess points
        int result = judge.findChessPointToADirection(currentChessPoint, direction);

        assertEquals(expResult, result);
    }
}
