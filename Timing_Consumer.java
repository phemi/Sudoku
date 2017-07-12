/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Sudoku;

import javax.swing.JLabel;

/**
 *
 * @author Oreoluwa
 */
public class Timing_Consumer implements Runnable{
    private Timing_BlockinQueue sharedLocation;
    private JLabel label;
    public boolean isRunning = true;
    
    public Timing_Consumer(Timing_BlockinQueue shared, JLabel jLabel){
        sharedLocation = shared;
        label=jLabel;
    }

    @Override
    public void run() {
        isRunning = true;
        while(isRunning){
            try{
                Thread.sleep(1000);
                String time =sharedLocation.get();
                label.setText(time);
            
            }catch(InterruptedException ex){
                ex.printStackTrace();
            }
        }
    }
    
}
