/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nz.ac.aut.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Dong Huang
 */
public class DBManager {

    Connection conn = null;
    
    String url;
    String username;
    String password;

    public DBManager(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;

        // The connection is established when the DBManager instance is created.
        connectDB();
    }

    public void connectDB() {
        try {
            conn = DriverManager.getConnection(url, username, password);
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public ResultSet getResultSetFromTable(String tableName) {
        String querySQL = "SELECT X, Y, COLOR FROM " + tableName;

        Statement stmt = null;
        ResultSet rs = null;

        try {
            stmt = conn.createStatement();
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            rs = stmt.executeQuery(querySQL);
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }

        return rs;
    }

    public void createAndInsertIntoTableFromChessBoard(String tableName, ChessBoard chessBoard) {
        createTable(tableName);
        insertIntoTable(tableName, chessBoard);
    }

    /**
     *
     * @param rs
     * @return chessPointCollection
     */
    public ArrayList<ChessPoint> convertResultSetToChessPointCollection(ResultSet rs) {
        ArrayList<ChessPoint> chessPointCollection = new ArrayList<ChessPoint>();
        ChessPoint chessPoint;
        ChessColor chessColor;
        int x;
        int y;

        try {
            while (rs.next()) {
                x = rs.getInt("X");
                y = rs.getInt("Y");
                chessColor = rs.getString("COLOR").equals("BLACK") ? ChessColor.BLACK : ChessColor.WHITE;

                chessPoint = new ChessPoint(x, y, chessColor);
                chessPointCollection.add(chessPoint);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }

        return chessPointCollection;
    }

    public ArrayList<String> convertResultSetToTableNameCollection(ResultSet rs) {
        ArrayList<String> tableNameCollection = new ArrayList<String>();
        String tableName = null;

        try {
            while (rs.next()) {
                tableName = rs.getString("TABLENAME");
                tableNameCollection.add(tableName);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }

        return tableNameCollection;
    }

    public ResultSet getAllTableNames() {
        String sql = "select * from SYS.SYSTABLES where TABLETYPE='T'";
        ResultSet rs = null;

        Statement stmt = null;

        try {
            stmt = conn.createStatement();
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            rs = stmt.executeQuery(sql);
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }

        return rs;
    }

    private void createTable(String tableName) {
        //Create the table:
        String createTableSQL = "CREATE TABLE " + tableName + " (X INT, Y INT, COLOR VARCHAR(5))";

        Statement stmt = null;

        try {
            stmt = conn.createStatement();
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            stmt.executeUpdate(createTableSQL);
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void insertIntoTable(String tableName, ChessBoard chessBoard) {
        // Insert value into table
        PreparedStatement pstmt = null;

        try {
            pstmt = conn.prepareStatement("INSERT INTO " + tableName + " VALUES (?,?,?)");
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }

        for (ChessPoint chessPoint : chessBoard.getChessPointCollection()) {
            try {
                pstmt.setInt(1, chessPoint.getX());
                pstmt.setInt(2, chessPoint.getY());
                pstmt.setString(3, chessPoint.getChessColor().toString());

                pstmt.executeUpdate();
            } catch (SQLException ex) {
                Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

//        try {
//            rs.close();
//            conn.close();
//        } catch (SQLException ex) {
//            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }

    /**
     * Overwrite the existing game record with the new game record.
     * 
     * @param tableName
     * @param chessBoard 
     */
    void updateTableFromChessBoard(String tableName, ChessBoard chessBoard) {
        clearTable(tableName);
        insertIntoTable(tableName, chessBoard);
    }

    private void clearTable(String tableName) {
        String deleteTableSQL = "DELETE FROM " + tableName;

        Statement stmt = null;

        try {
            stmt = conn.createStatement();
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            stmt.executeUpdate(deleteTableSQL);
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
