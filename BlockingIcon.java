/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Sudoku;

import java.util.concurrent.ArrayBlockingQueue;
import javax.swing.Icon;
import javax.swing.ImageIcon;

/**
 *
 * @author Oreoluwa
 */

public class BlockingIcon implements IconInterface{
    private ArrayBlockingQueue<Icon> buffer;
    
    public BlockingIcon(){
        buffer = new ArrayBlockingQueue<Icon>(1);
    }

    @Override
    public void set(Icon img) {
        //sets img into buffer
        try{
            buffer.put(img);
        }catch(InterruptedException ex){
            ex.printStackTrace();
        }
    }

    @Override
    public Icon get() {
        Icon logo = new ImageIcon();
        try{
            logo = buffer.take();//takes image from buffer
        }catch(InterruptedException ex){
            ex.printStackTrace();
        }
        return logo;
    }
    
}
