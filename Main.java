/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Sudoku;

/**
 *
 * @author Oreoluwa
 */
public class Main {
    public static void main(String[] args){
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run(){
                new Sudoku_GUI().setVisible(true);
            }
        });
    }
}
