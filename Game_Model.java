/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Sudoku;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 *
 * @author Oreoluwa
 */
public class Game_Model {
     
    private ArrayList<String> spaceList;
    private String[] modelSpace = {"00","01","02","03","04","05","10","11","12","13","14","15","20","21","22","23","24","25","31","32","33","34","35","40","41","42","43","44","45","50","51","52","53","54","55"};
    // {00,01,02,03,04,05,10,11,12,13,14,15,20,21,22,23,24,25,31,32,33,34,35,40,41,42,43,44,45,50,51,52,53,54,55};
    private Random generator =new Random();
    public void model(){
        spaceList =new ArrayList<String>();
        
        for(String val: modelSpace){
            spaceList.add(val);
        }
    }
    public void setValue(int val, int[][] model){
        int pos = generator.nextInt((spaceList.size()));
        String string = spaceList.get(pos);
        //System.out.println("String: "+string);
        char[] rowAndCol = string.toCharArray();
        //System.out.println("String: "+rowAndCol);
        int row = Integer.parseInt(""+rowAndCol[0]);
        int col = Integer.parseInt(""+rowAndCol[1]);
        
        boolean checkRow = searchRow(col, row,val, model);
        boolean checkCol = searchCol(col, row,val, model);
        //System.out.println("check Row: "+checkRow+" Check col: "+checkCol);
        //if it isn't found any where else in that row or col then it sets the value and then removes the position from spacelist
        if(!checkCol && !checkRow){
            model[row][col] = val;
            System.out.println("Val: "+val + "Where: "+string);
            spaceList.remove(pos);
            
        }else{
            setValue(val, model);
        }
    }
    
    //method to search thru rows of a given column, returns true if found
    public boolean searchRow(int colPos, int rowPos, int searchVal,int[][] model){
        boolean isFound = false;
        
        //loops thru each row
        for(int i = 0; i<model.length; i++){
            
            //if it gets to the row position of that no it skips it
            if(i== rowPos){
                continue; 
            }
            //if it finds it stops search
            if(model[i][colPos]== searchVal){
                //System.out.println("Found at row: "+rowPos+" col: "+colPos+" vlaue: ");
                isFound = true;
                break;
            }
        }
        return isFound;
            
    }
    
    //method to search thru cols of a given row, returns true if found
    public boolean searchCol(int colPos, int rowPos,int searchVal, int[][] model){
        boolean isFound = false;
        
        //loops thru each col of given row
        for(int i = 0; i<model[rowPos].length; i++){
            
            //if it gets to the col position of that no it skips it
            if(i== colPos){
                continue; 
            }
            //if it finds it stops search
            if(model[rowPos][i]== searchVal){
                isFound = true;
                break;
            }
        }
        return isFound;
            
    }
    private void locationToExpose(){
        int[] region0 = {0,1,2,6,7,8};
        int[] region1 = {3,4,5,9,10,11};
        int[] region2 = {12,13,14,18,19,20};
        int[] region3 = {15,16,17,21,22,23};
        int[] region4 = {24,25,26,30,31,32};
        int[] region5 = {27,28,29,33,34,35};
        int[] location = new int[12];
        for(int i=0; i<6;i++){
            int pos = generator.nextInt(3);
        }
    }
    
    public static void main(String[] args){
        Game_Model game_Model = new Game_Model();
        int[] play = {5,3,1,5,6,4,3,5,6,1,4,2,1,6,5,2,4,3,1,4,6,3,1,2,5,4,2,6,1,2,5,3,2,4,6,3};
        int[][] model = new int[6][6];
        game_Model.model();
        for(int i=0; i<34; i++){
            game_Model.setValue(play[i], model);
        }
        for(int i=0; i<model.length; i++){
            System.out.println();
            for(int j= 0; j<model[i].length;j++){
                System.out.print(model[i][j] + "\t");
            }
        }
    }
        
}
