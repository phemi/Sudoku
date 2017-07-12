/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Sudoku;

import java.util.concurrent.ArrayBlockingQueue;

/**
 *
 * @author Oreoluwa
 */
public class Timing_BlockinQueue implements Timing_Buffer{
    ArrayBlockingQueue<String> buffer;
    
    public Timing_BlockinQueue(){
        buffer = new ArrayBlockingQueue<String>(1);
    }

    @Override
    public void set(String time) {
        try{
            buffer.put(time);
        }catch(InterruptedException ex){
            ex.printStackTrace();
        }
    }

    @Override
    public String get() {
        String time ="" ;
        try{
            time =buffer.take();
        }catch(InterruptedException ex){
            ex.printStackTrace();
        }
        return time;
    }
    
}
