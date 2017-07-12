/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Sudoku;

import java.util.Random;
import javax.swing.Icon;
import javax.swing.JLabel;

/**
 *
 * @author Oreoluwa
 */
public class IconConsumer implements Runnable{
    private Random generator = new Random();
    private BlockingIcon sharedLocation;
    private JLabel label;
    public boolean  isDone = false;
    
    public IconConsumer(BlockingIcon shared, JLabel labelToBeUsed){
        sharedLocation = shared;
        label = labelToBeUsed;
    }

    @Override
    public void run() {
        while(!isDone){
            try{
                int sleepTime  = generator.nextInt(3000);
                Thread.sleep(sleepTime);
                Icon logo = sharedLocation.get();
                label.setIcon(logo);
            }catch(InterruptedException ex){
                ex.printStackTrace();
            }
        }
    }
    
    
}
