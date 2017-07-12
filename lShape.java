/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Sudoku;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author OLUWANIRAN
 */
public class lShape extends JFrame{
    Shape panel;
    
    public lShape(){
        panel = new Shape();
        add(panel);
        setSize(500, 500);
        setVisible(true);
    }
    
    public static void main(String [] Args){
        
        lShape app = new lShape();
    }
    
    public void createLShape(){
        panel =  new Shape();
        
    }
    
    private class Shape extends JPanel{
        
        @Override
        public void paintComponent(Graphics g){
            super.paintComponent(g);
            
            g.setColor(Color.red);
            //g.drawRect(70, 40, 50, 200);
            g.fillRect(70, 40, 50, 200);
            
            g.setColor(Color.red);
            g.fillRect(70, 200, 200, 50);
        }
    }
}
