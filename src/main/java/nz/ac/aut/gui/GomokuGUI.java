/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nz.ac.aut.gui;

import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JOptionPane;
import nz.ac.aut.model.GameEventListener;
import nz.ac.aut.model.Game;
import nz.ac.aut.model.ChessBoard;
import nz.ac.aut.model.ChessColor;
import nz.ac.aut.model.ChessPoint;

/**
 *
 * @author xfn
 */
public class GomokuGUI extends javax.swing.JFrame implements GameEventListener {

    /**
     * Creates new form GomokuGUI
     */
    public GomokuGUI(Game game) {
        this.game = game;

        initComponents();
        initializeChessBoard();

        game.setGameEventListener(this);

        update();
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The
     * content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelContent = new javax.swing.JPanel();
        panelControl = new javax.swing.JPanel();
        scoreBlack = new javax.swing.JLabel();
        scoreWhite = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        panelChessBoard = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Gomoku");
        setPreferredSize(new java.awt.Dimension(620, 660));
        setResizable(false);
        setSize(new java.awt.Dimension(620, 660));

        panelContent.setLayout(new java.awt.BorderLayout());

        scoreBlack.setText("jLabel1");

        scoreWhite.setText("jLabel2");

        jLabel3.setText(":");

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("jLabel1");

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("jLabel2");

        javax.swing.GroupLayout panelControlLayout = new javax.swing.GroupLayout(panelControl);
        panelControl.setLayout(panelControlLayout);
        panelControlLayout.setHorizontalGroup(
            panelControlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelControlLayout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(scoreBlack)
                .addGap(141, 141, 141)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 145, Short.MAX_VALUE)
                .addComponent(scoreWhite)
                .addGap(18, 18, 18)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        panelControlLayout.setVerticalGroup(
            panelControlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelControlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(scoreBlack))
            .addGroup(panelControlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(scoreWhite)
                .addComponent(jLabel3))
        );

        panelContent.add(panelControl, java.awt.BorderLayout.PAGE_END);

        panelChessBoard.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        panelChessBoard.setPreferredSize(new java.awt.Dimension(528, 528));

        javax.swing.GroupLayout panelChessBoardLayout = new javax.swing.GroupLayout(panelChessBoard);
        panelChessBoard.setLayout(panelChessBoardLayout);
        panelChessBoardLayout.setHorizontalGroup(
            panelChessBoardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 526, Short.MAX_VALUE)
        );
        panelChessBoardLayout.setVerticalGroup(
            panelChessBoardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 658, Short.MAX_VALUE)
        );

        panelContent.add(panelChessBoard, java.awt.BorderLayout.CENTER);

        getContentPane().add(panelContent, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    @Override
    public void gameStateChanged() {
        update();

        if (!game.isCurrentChessPointValid()) {
            JOptionPane.showMessageDialog(
                    this,
                    "The chess point you just played is not valid.", "Warning",
                    JOptionPane.INFORMATION_MESSAGE);
        } else {
            // Pop up a dialog to show who wins
            if (game.getJudge().isGameOver(game.getChessBoard().getCurrentChessPoint())) {
                if (game.getJudge().isBlackWin()) {
                    JOptionPane.showMessageDialog(
                            this,
                            "Black wins!", "Game over",
                            JOptionPane.INFORMATION_MESSAGE);
                    game.createNewGame();
                } else if (game.getJudge().isWhiteWin()) {
                    JOptionPane.showMessageDialog(
                            this,
                            "White wins!", "Game over",
                            JOptionPane.INFORMATION_MESSAGE);
                    game.createNewGame();
                }
            }
        }
    }

    private void update() {
        // update each chess point panel
        Component[] components = panelChessBoard.getComponents();

        for (Component comp : components) {
            ChessPointPanel cpp = (ChessPointPanel) comp;
            cpp.update();
        }

        // update the panelControl info
    }

    private void initializeChessBoard() {
        panelChessBoard.setLayout(new GridLayout(ChessBoard.NUM_OF_ROWS, ChessBoard.NUM_OF_COLS));

        for (int row = 0; row < ChessBoard.NUM_OF_ROWS; row++) {
            for (int column = 0; column < ChessBoard.NUM_OF_COLS; column++) {
                ChessPointPanel cpp = new ChessPointPanel(game, row, column);

                // Add a mouse listener for all the chess point panel
                cpp.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent evt) {
                        mouseClickedActionPerformed(evt);
                    }
                });

                // Addd all the chess point panel to the chess board
                panelChessBoard.add(cpp);
            }
        }
    }

    private void mouseClickedActionPerformed(MouseEvent evt) {
        // Create a new chess point panel after the mouse click
        ChessPointPanel cpp = (ChessPointPanel) evt.getSource();

        int row = cpp.getRow();
        int column = cpp.getColumn();

        ChessColor cc = game.getJudge().isBlackTurn() ? ChessColor.BLACK : ChessColor.WHITE;
        ChessPoint currentChessPoint = new ChessPoint(row, column, cc);

        System.out.println(currentChessPoint);

        game.placeChessPoint(currentChessPoint);
    }

    private Game game;

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel panelChessBoard;
    private javax.swing.JPanel panelContent;
    private javax.swing.JPanel panelControl;
    private javax.swing.JLabel scoreBlack;
    private javax.swing.JLabel scoreWhite;
    // End of variables declaration//GEN-END:variables
}
