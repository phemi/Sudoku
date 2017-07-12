/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Sudoku;

/**
 *
 * @author Oreoluwa
 */
public class Timing_Producer implements Runnable{
    private Timing_BlockinQueue sharedLocation;
    public boolean  done = false;
    private String time;
    private int secs= 00;
    private int mins = 00;
    private int hrs = 00;
    private int day = 00;
    
    //constructor takes in the shared queue and when to start timin from
    public Timing_Producer(Timing_BlockinQueue shared, String timeStarts){
        sharedLocation = shared;
        time = timeStarts;
        String[] timing = time.split(":");
        //i.e if it's up2 hrs
        if(timing.length == 3){
            secs = Integer.parseInt(timing[2]);
            mins = Integer.parseInt(timing[1]);
            hrs = Integer.parseInt(timing[0]);
        }else{
            secs = Integer.parseInt(timing[3]);
            mins = Integer.parseInt(timing[2]);
            hrs = Integer.parseInt(timing[1]);
            day = Integer.parseInt(timing[0]);
        }
    }

    @Override
    public void run() {
        String secsPrefix ;
        String minsPrefix ;
        String hrsPrefix ;
        try{
            while(!done){
                secsPrefix = ":";
                minsPrefix = ":";
                hrsPrefix = ":";
                Thread.sleep(1000);
                secs++;
                
                if(secs == 60){
                    mins++; //60 secs make one min
                    secs = 0;//re- initializes secs back to 0
                }
                if(mins == 60){
                    hrs++;//60 mins make 1hr
                    mins = 0;//re- initializes mins back to 0
                }
                
                 if(secs<10){
                     secsPrefix = ":0";
                 }
                 if(mins<10){
                     minsPrefix = ":0";
                 }
                 if(hrs<10){
                     hrsPrefix = ":0";
                 }
                 time = hrs+minsPrefix+mins+secsPrefix+secs;
                
                 if(hrs == 24){
                    day++;//24hrs make 1 day
                    time = day+ hrsPrefix+hrs+minsPrefix+mins+secsPrefix+secs;
                }
                
                sharedLocation.set(time);
            }
        }catch(InterruptedException ex){
        }
    }
}
