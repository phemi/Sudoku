/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Sudoku;

import java.util.Random;
import javax.swing.Icon;

/**
 *
 * @author Oreoluwa
 */
public class IconProducer implements Runnable{
    private Random generator = new Random();
    private BlockingIcon sharedLocation;
    private Icon[] logos;
    public boolean isDone = false;
    
    public IconProducer(BlockingIcon shared, Icon[] images){
        sharedLocation =  shared;
        logos = images;
    }

    @Override
    public void run() {
        try{
            for(int i =0; i<=logos.length && !isDone ; i++){
                int sleepTime = generator.nextInt(3000);//sleeps for 3secs
                Thread.sleep(sleepTime);
                
                sharedLocation.set(logos[i]);
                if(i==(logos.length-1)){
                    i=0;//so that i doesnt reach the end. i.e a continuous loop
                }
            }
        }catch(InterruptedException ex){
            ex.printStackTrace();
        }
    }
    
    
}
