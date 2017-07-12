/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
//
package Sudoku;



import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.KeyEventPostProcessor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.event.MenuEvent;
import sun.awt.AWTAccessor;

/**
 *
 * @author Oreoluwa
 */
public class Sudoku_GUI extends JFrame{
    private final ExecutorService app = Executors.newFixedThreadPool(2);
    private final BlockingIcon iconBuffer = new BlockingIcon();
    private final ExecutorService timeApp = Executors.newFixedThreadPool(2);
    private final ExecutorService iconApp = Executors.newFixedThreadPool(2);
    private final Timing_BlockinQueue timeBuffer = new Timing_BlockinQueue();
    private IconProducer iconProducer;
    private IconConsumer iconConsumer;
    private Timing_Producer timeProducer;
    private Timing_Consumer timeConsumer;
    private final int MODIFIABLE = 0;//if the square is modifiable
    private final int UN_MODIFIABLE = 1;//if the square is unmodifiable
    private final int Game_Started = 1,Game_Paused = 0, Game_Ended = -1;
    private String time = "00:00:00";
    private String username;
    private int gameCurrentStatus = Game_Ended;
    private int mouseLocation = 35;//mouse location on the board set to the last tile
    private int noOfMarkedTiles = 0;
    private Random generator = new Random();
    private Square[][] board;
    public final int LEVEL_1 = 0, LEVEL_2= 1, LEVEL_3 =2;;
    public int level = LEVEL_1;
    private JButton startButton;
    private JTextField timeField;
    private JMenuBar menuBar;
    private JMenu controlMenu;
    private JMenuItem newGameMenu;
    private JMenuItem aboutAuthor;
    private JMenuItem aboutGame;
    private JMenu selectControlMenu;
    private JMenu levelMenu;
    private JMenu aboutMenu;
    private JMenu viewMenu;
    private JMenu highScoreJMenu;
    private JRadioButtonMenuItem keypadMenuItem;
    private JRadioButtonMenuItem mouseMenuItem;
    private JRadioButtonMenuItem[] levelMenuItems = new JRadioButtonMenuItem[3];
    private ButtonGroup buttonGroup1, buttonGroup2;
    private JMenuItem[] highScoresMenuItem = new JMenuItem[3];
    private JPanel boardPanel;
    private JPanel northPanel;
    private JPanel southPanel;
    private JLabel timeLabel;
    private JLabel headingImg;
    private JButton checkButton;
    private JButton start_pauseButton = new JButton();
    private JPanel panel2;
    private Square currentSquare;
    private MouseListener mouseListener;
    private KeyListener keyListener;
    private int[][] model;
    private int[] locationToExpose;
    private Icon icon1 = new ImageIcon(getClass().getResource("icon1.jpg"));
    private Icon icon2 = new ImageIcon(getClass().getResource("icon2.jpg"));
    private Icon icon3 = new ImageIcon(getClass().getResource("icon3.jpg"));
    private Icon icon4 = new ImageIcon(getClass().getResource("icon4.jpg"));
    private Icon icon5 = new ImageIcon(getClass().getResource("icon5.jpg"));
    private Icon icon6 = new ImageIcon(getClass().getResource("icon6.jpg"));
    private Icon icon7 = new ImageIcon(getClass().getResource("icon7.jpg"));
    private Icon heading = new ImageIcon(getClass().getResource("SudokuLogo.jpg"));
    private Icon checkIcon = new ImageIcon(getClass().getResource("Cross_Check.jpg"));
    private Icon checkIconRollOver = new ImageIcon(getClass().getResource("Cross_CheckRollOver.jpg"));
    private Icon startGameLogo = new ImageIcon(getClass().getResource("SudokuLOGO1.jpg"));
    private Icon startGameLogoRollOver = new ImageIcon(getClass().getResource("SudokuLOGO1_OVER.jpg"));
    private Icon pauseLogo = new ImageIcon(getClass().getResource("pause.jpg"));
    private Icon pauseLogoRollover = new ImageIcon(getClass().getResource("pauseRollOver.jpg"));
    private Icon continueLogo = new ImageIcon(getClass().getResource("continueLogo.jpg"));
    private Icon continueLogoRollOver = new ImageIcon(getClass().getResource("continueLogoRollOver.jpg"));
    private Icon[] icons = {heading, icon1,icon2, icon3, icon4, icon5,icon6,icon7,heading};
    public Sudoku_GUI(){
        setTitle("Sudoku!");
        
        menuBar = new JMenuBar();
        controlMenu = new JMenu("Control");
        newGameMenu = new JMenuItem("New Game");
        newGameMenu.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N,InputEvent.CTRL_DOWN_MASK));
        
        selectControlMenu = new JMenu("Select Controller");
        keypadMenuItem = new JRadioButtonMenuItem("Keyboard");
        mouseMenuItem = new JRadioButtonMenuItem("Mouse");
        buttonGroup1 = new ButtonGroup();
        buttonGroup1.add(keypadMenuItem);
        buttonGroup1.add(mouseMenuItem);
        keypadMenuItem.setSelected(true);
        selectControlMenu.add(keypadMenuItem);
        selectControlMenu.add(mouseMenuItem);
        selectControlMenu.setVisible(false);
        
       
        levelMenu = new JMenu("Level");
        buttonGroup2 = new ButtonGroup();
        ItemHandler handler = new ItemHandler();//handler for the levelMenuItems
        for(int i=0; i<levelMenuItems.length; i++){
            levelMenuItems[i] = new JRadioButtonMenuItem("Level "+(i+1));
            levelMenuItems[i].addItemListener(handler);
            levelMenu.add(levelMenuItems[i]);
            buttonGroup2.add(levelMenuItems[i]);
        }
        levelMenuItems[level].setSelected(true);
        controlMenu.add(newGameMenu);
        controlMenu.addSeparator();
        controlMenu.add(levelMenu);
        controlMenu.add(selectControlMenu);
        
        viewMenu = new JMenu("View");
        highScoreJMenu = new JMenu("High Score");
        
        buttonGroup2 = new ButtonGroup();
        for(int i=0; i<highScoresMenuItem.length; i++){
            highScoresMenuItem[i] = new JMenuItem("Level "+i);
            highScoreJMenu.add(highScoresMenuItem[i]);
            buttonGroup2.add(highScoresMenuItem[i]);
        }
        viewMenu.add(highScoreJMenu);
        
        aboutMenu = new JMenu("About");
        aboutAuthor = new JMenuItem("Author");
        aboutGame = new JMenuItem("Game");
        aboutMenu.add(aboutGame);
        aboutMenu.addSeparator();
        aboutMenu.add(aboutAuthor);
        menuBar.add(controlMenu);
        menuBar.add(viewMenu);
        menuBar.add(aboutMenu);
        setJMenuBar(menuBar);
        
        
        northPanel = new JPanel();
        headingImg = new JLabel(heading);
        headingImg.setHorizontalTextPosition(SwingConstants.CENTER);
        headingImg.setFont(new Font("Cooper Black", Font.BOLD, 72));
        headingImg.setForeground(Color.red);
        headingImg.setBackground(Color.BLACK);
        headingImg.setHorizontalTextPosition(SwingConstants.CENTER);
//        headingImg = new JLabel(heading);
        northPanel.add(headingImg);
        northPanel.setBackground(Color.BLACK);
        //northPanel.setPreferredSize(new Dimension(200, 200));
        //add(northPanel, BorderLayout.NORTH);
        
        
        panel2 = new JPanel();//set up panel to ocontain board panel
        panel2.setBackground(Color.BLACK);
        panel2.setBorder(BorderFactory.createEtchedBorder());
        //panel2.setMinimumSize(new Dimension(400, 400));
        //panel2.setPreferredSize(new Dimension(400, 400));
        //panel2.add(headingImg, BorderLayout.NORTH);
        
        startButton = new JButton(startGameLogo);
        startButton.setRolloverEnabled(true);
        startButton.setRolloverIcon(startGameLogoRollOver);
        startButton.setToolTipText("Click to start game");
        startButton.setBackground(Color.BLACK);
        startButton.setBorderPainted(true);
        startButton.setBorder(BorderFactory.createLineBorder(new java.awt.Color(255, 0, 255), 0));
        startButton.setContentAreaFilled(false);
        startButton.setOpaque(true);
        
        start_pauseButton.setBackground(Color.BLACK);
        start_pauseButton.setBorderPainted(true);
        start_pauseButton.setBorder(BorderFactory.createLineBorder(new java.awt.Color(255, 0, 255), 0));
        start_pauseButton.setContentAreaFilled(false);
        start_pauseButton.setRolloverEnabled(true);
        start_pauseButton.setIcon(pauseLogo);//sets it to pause
        start_pauseButton.setRolloverIcon(pauseLogoRollover);
        start_pauseButton.setOpaque(true);
//        timeField = new JTextField(5);
//        panel2.add(timeField, BorderLayout.SOUTH);
        setBackground(Color.BLACK);
        add(startButton, BorderLayout.CENTER);
        
        
        checkButton = new JButton(checkIcon);
        checkButton.setRolloverEnabled(true);
        checkButton.setRolloverIcon(checkIconRollOver);
        checkButton.setBorderPainted(true);
        checkButton.setBorder(BorderFactory.createLineBorder(new java.awt.Color(255, 0, 255), 0));
        checkButton.setContentAreaFilled(false);
        checkButton.setOpaque(true);
        
        timeLabel = new JLabel(time);//label for the time
        timeLabel.setForeground(Color.WHITE);
        timeLabel.setFont(new Font("Forte", Font.BOLD, 45));
        
        southPanel = new JPanel();
        southPanel.setBackground(Color.BLACK);
        southPanel.add(checkButton);
        southPanel.add(timeLabel);
        southPanel.add(start_pauseButton);
        
        //on click startButton
        startButton.addMouseListener(
                new MouseAdapter() {

            @Override
            public void mouseReleased(MouseEvent e) {
                startNewGame();//starts new game
                //starts the threading for the logo
                
                try{
                    iconProducer = new IconProducer(iconBuffer, icons);
                    iconConsumer = new IconConsumer(iconBuffer, headingImg);
                    iconApp.execute(iconProducer);
                    iconApp.execute(iconConsumer);
                }catch(Exception ex){
                    ex.printStackTrace();
                }
                
                
            }
        });
        
        newGameMenu.addActionListener(
                new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                //if game has not ended
                if(gameCurrentStatus != Game_Ended){
                    int response  = JOptionPane.showConfirmDialog(getContentPane(), "Do you wanna terminate your current game?", "Sudoku",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if(response == JOptionPane.YES_OPTION){
                        endGame();//first ends game
                        startNewGame();//starts new game
                    }
                }
            } 
        });
        
//        keypadMenuItem.addItemListener(
//                new ItemListener() {
//
//            @Override
//            public void itemStateChanged(ItemEvent e) {
//                if(e.getStateChange() == ItemEvent.SELECTED){
//                    //removes all mouse listener
//                    for(int i=0; i<36; i++){
//                        board[i/6][i%6].removeMouseListener(mouseListener);
//                        board[i/6][i%6].addKeyListener(keyListener);
//                    }
//                }
//            }
//        });
//        mouseMenuItem.addItemListener(
//                new ItemListener() {
//
//            @Override
//            public void itemStateChanged(ItemEvent e) {
//                if(e.getStateChange() == ItemEvent.SELECTED){
//                    //removes all mouse listener
//                    for(int i=0; i<36; i++){
//                        board[i/6][i%6].removeKeyListener(keyListener);
//                        mouseListener = new MouseListener(board[i/6][i%6].getSquareLocation());
//                        board[i/6][i%6].addMouseListener(mouseListener);
//                        
//                    }
//                }
//            }
//        });
        
        start_pauseButton.addActionListener(
                new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                switch(gameCurrentStatus){
                    case Game_Started:
                        //if game is currently been played, pauses it
                        pauseGame();
                        
                        break;
                    case Game_Paused:
                        //if game is paused.
                        //contiues the game
                        
                        continueGame();
                        break;
                }
            }
        });
        //check's button's listener
        checkButton.addMouseListener(
                new MouseAdapter() {
                public void mousePressed(MouseEvent evt){
                    verifyAnswer();
                }
                
                public void mouseReleased(MouseEvent evt){
                    for(int i=0; i<36; i++){
                        board[i/6][i%6].setBackground(board[i/6][i%6].getColor());
                        board[i/6][i%6].requestFocus();
                    }
                }});
        
        aboutGame.addActionListener(
                new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                //if game is on ...pauses the game
                if(gameCurrentStatus == Game_Started){
                    pauseGame();
                }
                String msg = "Press the number keys\n"
                        + "to insert numbers into the board.\n"
                        + "No number can be repeated in \n"
                        + "any row, column and region.";
                
                JOptionPane.showMessageDialog(getContentPane(), msg, "Sudoku Team", JOptionPane.INFORMATION_MESSAGE);
                
            }
        });
        setMinimumSize(new Dimension(550, 700));
        setSize(550, 700);
        //setResizable(false);
        setVisible(true);
    }
        
        
    private class Square extends JPanel{
        private String mark; //mark to be drawn in this square
        private int location;
        private Color color;
        private int squareStatus;
        
        
        public Square(String squareMark, int squareLocation, int status){
            mark = squareMark;//set mark for this square
            location = squareLocation; //sets locaton for this square
            squareStatus = status;
            switch(status){
                case MODIFIABLE:
                    color = Color.WHITE;//sets it's color to white
                    break;
                case UN_MODIFIABLE:
                    color = Color.GREEN;//sets it's color to yellow
                    break;
            }
            setBackground(color);
            setFont(new Font("Forte", Font.BOLD, 50));
            mouseListener = new MouseListener(location);
            keyListener =new KeyListener();
            addKeyListener(keyListener); 
            addMouseListener(mouseListener);
        }
        
        

        @Override
        public boolean isFocusTraversable() {
            return true;
        }
        
        
        //return preffered size of square
        public Dimension getPrefferedSize(){
            return new Dimension(60, 60);//return preffered size
        }
        
        public Dimension getMinimumSize(){
            return getPrefferedSize();//return preffered size
        }
        
        public void setMark(String newMark){
            mark = newMark;//set mark for square
            repaint();//repaint square
            //as it set sets mark as soon as it sets mark for all the tiles
            if(getNoOfMarkedTiles() == 36){
                //checks if it is correct?
                int noOfWrongTiles = verifyAnswer();
                if(noOfWrongTiles == 0){
                    String msg ="Oga you Try O! U get everytin! Kudos!";
                    JOptionPane.showMessageDialog(getContentPane(), msg, "Sudoku Team", JOptionPane.INFORMATION_MESSAGE);
                    
                }else{
                    String msg = "Oga you don mark everytin u no get am\n"+noOfWrongTiles+" wrong Tiles!";
                    JOptionPane.showMessageDialog(getContentPane(), msg, "Sudoku Team", JOptionPane.INFORMATION_MESSAGE);
                    //sets board back to original setting
                    for(int i=0; i<36; i++){
                        board[i/6][i%6].setBackground(board[i/6][i%6].getColor());
                        board[i/6][i%6].requestFocus();
                    }
                }
                
            }
        }
        public String getMark(){
            return mark;
        }
        public int getSquareLocation(){
            return location;//return loaction of square
        }
        
        public void paintComponent(Graphics g){
            super.paintComponent(g);
            
            g.drawRect(0, 0, 60, 60);
            g.drawString(mark, 20, 40);
        }
        
        public Color getColor(){
            return color;
        }
        
        
    }
    
    //mouse listener for each tiles
    private class MouseListener extends MouseAdapter{
        private int location ;
        public MouseListener(int tileLocation){
            location = tileLocation;
        }
        public void mouseReleased(MouseEvent evt){
             //setSquareIntoFocus(location);
        }
                        
        public void mouseEntered(MouseEvent evt){
            int prevLocation = mouseLocation;
            setSquareOutOfFocus(prevLocation);//sets prev[ous mouse location iout of focus for situations of key listner interference
            mouseLocation = location; //sets mouse location to this tile
            setSquareIntoFocus(location);
        }
        public void mouseExited(MouseEvent evt){
            setSquareOutOfFocus(location);
        }
    }
    
    private class KeyListener extends KeyAdapter{
         public void keyPressed(KeyEvent evt){
                            int keyCode= evt.getKeyCode();
                            int currentLocation = mouseLocation;//gets previous mouse location
                            if(evt.isActionKey()){
                                //if the user presses the action key down
                                if(evt.getKeyCode() ==40){
                                    //on pressing the down key, it moves mouse location downwards
                                    //if n only if d current location isnt on d last row i.e currentLocation/6 > 5, i mean last row
                                    if((currentLocation/6) < 5){
                                        mouseLocation += 6 ;//sets new mouse location downwards
                                        setSquareIntoFocus(mouseLocation); //sets new tile into focus
                                        setSquareOutOfFocus(currentLocation);//sets the outdated current location out of focus
                                    }else{
                                        //it's on d last row
                                        //moves it to the topmost row of that column 
                                        mouseLocation %= 6; 
                                        setSquareIntoFocus(mouseLocation); //sets new tile into focus
                                        setSquareOutOfFocus(currentLocation);//sets the outdated current location out of focus
                                    }
                                }
                                //if the user presses the action key upwards
                                if(evt.getKeyCode() == 38){
                                    //on pressing the up key, it moves mouse location upwards
                                    //if n only if d current location isnt on d first row i.e currentLocation/6 > 0, i mean first row
                                    if((currentLocation/6) > 0){
                                        mouseLocation -= 6 ;//sets new mouse location upwards
                                        setSquareIntoFocus(mouseLocation); //sets new tile into focus
                                        setSquareOutOfFocus(currentLocation);//sets the outdated current location out of focus
                                    }else{
                                        //it's on d first row
                                        //moves it to the last row of that column 
                                        mouseLocation = 6*5 + currentLocation%6; //30 + current column
                                        setSquareIntoFocus(mouseLocation); //sets new tile into focus
                                        setSquareOutOfFocus(currentLocation);//sets the outdated current location out of focus
                                    }
                                }
                                //if the user presses the action key leftwards
                                if(evt.getKeyCode() == 37){
                                    //on pressing the left key, it moves mouse location leftwards
                                    //if n only if d current location isnt on d last column leftwards i.e currentLocation%6 > 0, i mean last col leftwards
                                    if((currentLocation%6) > 0){
                                        mouseLocation -= 1 ;//sets new mouse location leftwards
                                        setSquareIntoFocus(mouseLocation); //sets new tile into focus
                                        setSquareOutOfFocus(currentLocation);//sets the outdated current location out of focus
                                    }else{
                                        //it's on d last col leftwards 
                                        //moves it to the last col rightwards
                                        mouseLocation += 5; 
                                        setSquareIntoFocus(mouseLocation); //sets new tile into focus
                                        setSquareOutOfFocus(currentLocation);//sets the outdated current location out of focus
                                    }
                                }
                                //if the user presses the action key to the right
                                if(evt.getKeyCode() == 39){
                                    //on pressing the right key, it moves mouse location rghtwards
                                    //if n only if d current location isnt on d last column rightwards i.e currentLocation%6 < 5, i mean last col leftwards
                                    if((currentLocation%6) < 5){
                                        mouseLocation += 1 ;//sets new mouse location rightwards
                                        setSquareIntoFocus(mouseLocation); //sets new tile into focus
                                        setSquareOutOfFocus(currentLocation);//sets the outdated current location out of focus
                                    }else{
                                        //it's on d last col rightwards 
                                        //moves it to the last col leftwards
                                        mouseLocation -= 5; 
                                        setSquareIntoFocus(mouseLocation); //sets new tile into focus
                                        setSquareOutOfFocus(currentLocation);//sets the outdated current location out of focus
                                    }
                                }
                            }
                                
                            if(!evt.isActionKey()){
                                char key = evt.getKeyChar();
                                boolean isNo = keyCode>=48 && keyCode<=54 ; //i.e if key is btwn 0-9
                                //if the tile is modifiable and and the key is a no
                                if(board[mouseLocation/6][mouseLocation%6].squareStatus == MODIFIABLE && isNo){
                                    String mark = ""+key;
                                    if(mark.equals("0")){
                                        mark = "" ;//sets mark to b empty
                                    }
                                    board[mouseLocation/6][mouseLocation%6].setMark(""+mark);//marks the tile the no
                                }
                            }
                        
                    
                            
                        }
                    
                    public void keyReleased(KeyEvent evt){
                        //System.out.println(evt.getKeyCode() +"location : " +getSquareLocation());
                    }
                    
                    public void keyTyped(KeyEvent evt){
                        //System.out.println(evt.getKeyCode() +"location : " +getSquareLocation());
                    }
    }
    public final int[][] generateModel(){
        int[][] model1 ={{2,4,1,6,3,5},{5,3,6,2,4,1},{1,2,3,4,5,6},{4,6,5,3,1,2},{3,5,2,1,6,4},{6,1,4,5,2,3}}; 
        int[][] model2 ={{1,4,6,2,5,3},{3,2,5,6,4,1},{2,5,1,4,3,6},{6,3,4,5,1,2},{4,6,3,1,2,5},{5,1,2,3,6,4}}; 
        int[][] model3 ={{5,1,3,6,2,4},{4,2,6,1,5,3},{3,6,4,5,1,2},{1,5,2,3,4,6},{2,3,1,4,6,5},{6,4,5,2,3,1}}; 
        int pos = generator.nextInt(3);
        switch(pos){
            case 0:
                return model1;
            case 1:
                return model2;
                case 2:
                    return model3;
                
        }
        return model1;
    }
    
    public final int[] locationToExpose(){
        int[] region0 = {0,1,2,6,7,8};
        int[] region1 = {3,4,5,9,10,11};
        int[] region2 = {12,13,14,18,19,20};
        int[] region3 = {15,16,17,21,22,23};
        int[] region4 = {24,25,26,30,31,32};
        int[] region5 = {27,28,29,33,34,35};
        int[] location = new int[12];
        for(int i=0; i<6; i++){
            int pos = generator.nextInt(3);
            int pos2 = generator.nextInt(6);
            switch(i){
                case 0:
                    location[0] = region0[pos];
                    location[1]= region0[pos2];
                    break;
                case 1:
                    location[2] = region1[pos2];
                    location[3]= region1[pos];
                    break;
                case 2:
                    location[4] = region2[pos];
                    location[5]= region2[pos2];
                    break;
                case 3:
                    location[6] = region3[pos2];
                    location[7]= region3[pos];
                    break;
                case 4:
                    location[8] = region4[pos];
                    location[9]= region4[pos2];
                    break;
                case 5:
                    location[10] = region5[pos2];
                    location[11]= region5[pos];
                    break;
            }
        }
        return location;
    }
    public void setSquareIntoFocus(int location){
        board[location/6][location%6].setBackground(Color.RED);
    }
    public void setSquareOutOfFocus(int location){
        board[location/6][location%6].setBackground(board[location/6][location%6].getColor());
    }
    
    public int[][] getBoardModel(){
        int[][] boardModel = new int[6][6];
        for(int i=0; i<boardModel.length; i++){
            for(int j =0; j<boardModel[i].length; j++){
                String mark = board[i][j].getMark();
                if(mark.equals("")){
                    mark = ""+0;
                }
                boardModel[i][j] = Integer.parseInt(mark);
            }
        }
        return boardModel;
    }
    
    //inner  class for the level menu item, in other to change levels
    private class ItemHandler implements ItemListener{
        //if it is selected, change level
        @Override
        public void itemStateChanged(ItemEvent e) {
            //if item is selected changes variable to presnt level
            if(e.getStateChange() == ItemEvent.SELECTED){
                if(e.getSource() == levelMenuItems[0]){
                   level = LEVEL_1; //changes level to level 1;
                }
                if(e.getSource() == levelMenuItems[1]){
                   level = LEVEL_2;//changes level to level 2;
                }
                if(e.getSource() == levelMenuItems[2]){
                   level = LEVEL_3;//changes level to level 3;
                }
            }
        }
        
    }
    
    //method that verifies the board and returns no of wrong ans
    public int verifyAnswer(){
        int wrong = 0;
        int[][] boardModel = getBoardModel();
        for(int i = 0; i<boardModel.length; i++){
            for(int j = 0; j<boardModel[i].length;j++){
                //if it doesnt correspond to the question model
                if(model[i][j] != boardModel[i][j]){
                    wrong++;
                    board[i][j].setBackground(Color.ORANGE);
                 }
             }
         }
        return  wrong;
    }
    
    //method that returns no marked tiles
    public int getNoOfMarkedTiles(){
        int no=0;
        for(int i=0; i<36; i++){
                String boardMark = board[i/6][i%6].getMark();
                if(!boardMark.equals("")){
                     //if mark aint null
                     no++;//increments no of marked tiles
                }
         }
        return no;
    }
    public void requestBoardFocus(){
        for(int i=0; i<36; i++){
                        board[i/6][i%6].requestFocus();
         }
    }
    
    public void startNewGame(){
        username = JOptionPane.showInputDialog(getContentPane(),"Enter username:");
//                JLabel userLabel = new JLabel("Welcome "+username);
//                userLabel.setForeground(Color.ORANGE);
//                userLabel.setFont(new Font("Forte", Font.ROMAN_BASELINE, 22));
        getContentPane().removeAll();
        panel2.removeAll();
        
        start_pauseButton.setVisible(true);//sets it visible which might hav been set invisible by endGame()
        checkButton.setVisible(true);//sets it visible which might hav been set invisible by endGame()
        
        //sets content pane
        panel2.add(newGamePanel(), BorderLayout.CENTER);
        getContentPane().add(northPanel, BorderLayout.NORTH);
        getContentPane().add(panel2, BorderLayout.CENTER);
        
        gameCurrentStatus = Game_Started;// set current status to game started
        
        getContentPane().add(southPanel, BorderLayout.SOUTH);//adds start_pause button
        getContentPane().validate();
        requestBoardFocus(); //requests board focus to enable it's key listener
        selectControlMenu.setVisible(true);
        //starts threading for the time
        time = "00:00:00";//sets time back to beginning
                try{
                    timeProducer = new Timing_Producer(timeBuffer, time);
                    timeConsumer = new Timing_Consumer(timeBuffer, timeLabel);
                    timeApp.execute(timeProducer);
                    timeApp.execute(timeConsumer);
                }catch(Exception ex){
                    ex.printStackTrace();
                }
    }
    public void pauseGame(){
        //pauses the game
        gameCurrentStatus = Game_Paused;//sets game current status to pause
        
        //pauses timing by terminatin timeApp
        timeProducer.done = true;//pauses the timing by setting done = true; i.e terminates producer
        timeConsumer.isRunning= false;//pauses the timing by setting isRunning = false; i.e terminates consumer
        
        boardPanel.requestFocus(false);//removes focus so that they wouldn't be able to mark
        start_pauseButton.setIcon(continueLogo);
        start_pauseButton.setRolloverIcon(continueLogoRollOver);
        checkButton.setVisible(false);//disables check button
    }
    
    public void continueGame(){
        //pauses the game
        gameCurrentStatus = Game_Started;//sets game current status to started
        
        try{
                //continues the timing
                time = timeLabel.getText();//gets current time from label
                //executes timeApp with current time of timeLabel
                timeProducer = new Timing_Producer(timeBuffer, time);
                timeConsumer = new Timing_Consumer(timeBuffer, timeLabel);
                timeApp.execute(timeProducer);
                timeApp.execute(timeConsumer);
        }catch(Exception ex){
                ex.printStackTrace();
        }
        
        requestBoardFocus();//set focus on board
        start_pauseButton.setIcon(pauseLogo);
        start_pauseButton.setRolloverIcon(pauseLogoRollover);
        checkButton.setVisible(true);//disables check button
    }
    
    public void endGame(){
        gameCurrentStatus = Game_Ended;
        //stop timing by terminatin timeApp
        timeProducer.done = true;//stops the timing by setting done = true; i.e terminates producer
        timeConsumer.isRunning= false;//stops the timing by setting isRunning = false; i.e terminates consumer
//        
        time = timeLabel.getText();
//        //stop threading for head label
//        iconProducer.isDone= true;
//        iconConsumer.isDone = true;
        
        boardPanel.requestFocus(false);//removes focus so that they wouldn't be able to mark
        checkButton.setVisible(false);
        start_pauseButton.setVisible(false);
    }
    public void getWinnersPage(){
        String[] winnerMsg = { username+",", "Congratulations on the successful completion of your game!", "Time: "+time, "Sudoku Team"};
        getContentPane().removeAll();
        panel2.removeAll();
        panel2.setLayout(new BoxLayout(panel2, BoxLayout.PAGE_AXIS));
        for(int i=0; i<winnerMsg.length; i++){
            JLabel msgLabel = new JLabel(winnerMsg[i]);
            msgLabel.setForeground(Color.BLUE);
            msgLabel.setFont(new Font("Tahoma", Font.ITALIC, 30));
            panel2.add(msgLabel);
        }
        //sets content pane
        getContentPane().add(northPanel, BorderLayout.NORTH);
        getContentPane().add(panel2, BorderLayout.CENTER);
        getContentPane().validate();
    }
    //method for generating a new game for level1
    public JPanel newGamePanel(){
        boardPanel = new JPanel();//set up panel for squares
        boardPanel.setLayout(new GridLayout(6, 6, 0, 0));
        boardPanel.setBorder(BorderFactory.createEtchedBorder());
        boardPanel.setBackground(Color.BLACK);
        boardPanel.setMinimumSize(new Dimension(370, 370));
        boardPanel.setPreferredSize(new Dimension(370, 370));
        board = new Square[6][6];//create board
        
        model = generateModel();
        locationToExpose = locationToExpose();
        Arrays.sort(locationToExpose);
        
        //llop over the rows in the board
        for(int row = 0; row< board.length; row++){
             
            //loop over column in the board
            for(int column =0; column < board[row].length; column++){
                //create square
                int status = MODIFIABLE; //every tile is defaylt modifiable
                int location = row*6+column;
                String mark ="";
                if(Arrays.binarySearch(locationToExpose, location)>=0){
                    mark = (""+model[location/6][location%6]);
                    status = UN_MODIFIABLE;//sets the tile un_modifiable
                }
                board[row][column] = new Square(mark, row*6+column, status);
                boardPanel.add(board[row][column]);//add square
            }//end inner for
        }//end outer for
        return boardPanel;
    }
    
    public static  void main(String[] args){
        Sudoku_GUI app = new Sudoku_GUI();
        //int[][] model ={{1,2,3,4,5,6},{1,2,3,4,5,6,},{1,2,3,4,5,6},{1,2,3,4,5,6},{1,2,3,4,5,6},{1,2,3,4,5,6}}; 
//        for(int i=0; i<model.length; i++){
//            for(int j= 0; j<model[i].length;j++){
//                app.board[i][j].setMark(""+model[i][j]);
//            }
//        }
//        Game_Timing time = new Game_Timing(app.timeField);
//        time.execute();
        app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
