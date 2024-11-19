package miniproject;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *
 * @author ISHAN
 */

import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.*;
import java.util.Random;
import javax.swing.table.DefaultTableModel;

public class GamePanel extends JPanel implements ActionListener{

	static final int SCREEN_WIDTH = 1300;
	static final int SCREEN_HEIGHT = 750;
	static final int UNIT_SIZE = 50;
	static final int GAME_UNITS = (SCREEN_WIDTH*SCREEN_HEIGHT)/(UNIT_SIZE*UNIT_SIZE);
	static final int DELAY = 100;
	final int x[] = new int[GAME_UNITS];
	final int y[] = new int[GAME_UNITS];
	private boolean whiteBlockActive = false; // Indicates if the white block is active
	private int whiteBlockX, whiteBlockY; // Position of the white block
	private Timer whiteBlockTimer; // Timer to handle disappearance of the white block

	private int yellowAppleX, yellowAppleY; // Coordinates for the yellow apple
	private boolean yellowAppleActive = false; // Is the yellow apple active?
	private Timer yellowAppleTimer; // Time
	int bodyParts = 3;
	int applesEaten;
	int appleX;
	int appleY;
	int countY=0;
	int countW=0;
	
	char direction = 'R';
	boolean running = false;
    JButton jbutton1 = new JButton("Close");
	Timer timer;
	Random random;
        String name;
        String color = "Red";
        int n = 0;

	GamePanel(String name){
                this.name = name;
		random = new Random();
		this.setPreferredSize(new Dimension(SCREEN_WIDTH,SCREEN_HEIGHT));
		this.setBackground(Color.black);
		this.setFocusable(true);
		this.addKeyListener(new MyKeyAdapter());
		startGame();
                
                jbutton1.setBackground(Color.red);
                jbutton1.setForeground(Color.white);
                this.add(jbutton1);
                jbutton1.setVisible(false);
                jbutton1.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jButton1ActionPerformed(evt);
                }
            });

	}
        
	public void startGame() {
		newApple();
		running = true;
		timer = new Timer(DELAY,this);
		timer.start();
	}
	public void paintComponent(Graphics g) {
		
		super.paintComponent(g);
		draw(g);
	}
	public void draw(Graphics g) {

		if(running) {
			/*
			for(int i=0;i<SCREEN_HEIGHT/UNIT_SIZE;i++) {
				g.drawLine(i*UNIT_SIZE, 0, i*UNIT_SIZE, SCREEN_HEIGHT);
				g.drawLine(0, i*UNIT_SIZE, SCREEN_WIDTH, i*UNIT_SIZE);
			}
			*/
            if(color.equals("Red")){
				g.setColor(Color.red);
            }
            else{
                g.setColor(Color.blue);
            }

			g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);


			if (yellowAppleActive) {
				g.setColor(Color.yellow);
				g.fillOval(yellowAppleX, yellowAppleY, UNIT_SIZE, UNIT_SIZE);
			}
			if (whiteBlockActive) {
				g.setColor(Color.white);
				g.fillRect(whiteBlockX, whiteBlockY, UNIT_SIZE, UNIT_SIZE);
			}

		

			for(int i = 0; i< bodyParts;i++) {
				if(i == 0) {
					g.setColor(Color.green);
					g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
				}
				else {
					g.setColor(new Color(45,180,0));
					//g.setColor(new Color(random.nextInt(255),random.nextInt(255),random.nextInt(255)));
					g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
				}
			}
			g.setColor(Color.red);
			g.setFont( new Font("Ink Free",Font.BOLD, 40));
			FontMetrics metrics = getFontMetrics(g.getFont());
			g.drawString("Score: "+applesEaten, (SCREEN_WIDTH - metrics.stringWidth("Score: "+applesEaten))/2, g.getFont().getSize());
		}
		else {
			gameOver(g);
		}

	}
	/*private void activateWhiteBlock() {
		whiteBlockX = random.nextInt((int)(SCREEN_WIDTH / UNIT_SIZE)) * UNIT_SIZE;
		whiteBlockY = random.nextInt((int)(SCREEN_HEIGHT / UNIT_SIZE)) * UNIT_SIZE;
		whiteBlockActive = true;
	
		// Start a timer to deactivate the white block after 5 seconds
		whiteBlockTimer = new Timer(5000, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				whiteBlockActive = false; // Deactivate the block
				whiteBlockTimer.stop(); // Stop the timer
			}
		});
		whiteBlockTimer.start();
	}*/
	
	public void newApple(){

		countY++;
		countW++;

		appleX = random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE))*UNIT_SIZE;
		appleY = random.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE))*UNIT_SIZE;


		if (countW == 5 && !whiteBlockActive) {
			whiteBlockX = random.nextInt((int)(SCREEN_WIDTH / UNIT_SIZE)) * UNIT_SIZE;
			whiteBlockY = random.nextInt((int)(SCREEN_HEIGHT / UNIT_SIZE)) * UNIT_SIZE;
			whiteBlockActive = true;
	
		// Start a timer to deactivate the white block after 5 seconds
			whiteBlockTimer = new Timer(5000, e ->whiteBlockActive = false);
			whiteBlockTimer.setRepeats(false);
			whiteBlockTimer.start();

			//reseting the counter to 0;
			countW=0;
        }

		if (countY == 10 && !yellowAppleActive) {
			yellowAppleX = random.nextInt((int) (SCREEN_WIDTH / UNIT_SIZE)) * UNIT_SIZE;
			yellowAppleY = random.nextInt((int) (SCREEN_HEIGHT / UNIT_SIZE)) * UNIT_SIZE;
			yellowAppleActive = true;
	
			// Start the 10-second timer for the yellow apple
			if (yellowAppleTimer != null) {
				yellowAppleTimer.stop();
			}
			yellowAppleTimer = new Timer(10000, e -> yellowAppleActive = false); // Deactivate after 10 seconds
			yellowAppleTimer.setRepeats(false);
			yellowAppleTimer.start();
	
			// Reset the counter after spawning the yellow apple
			countY = 0;
		}
	}
	public void move(){
		for(int i = bodyParts;i>0;i--) {
			x[i] = x[i-1];
			y[i] = y[i-1];
		}

		switch(direction) {
		case 'U':
			y[0] = y[0] - UNIT_SIZE;
			break;
		case 'D':
			y[0] = y[0] + UNIT_SIZE;
			break;
		case 'L':
			x[0] = x[0] - UNIT_SIZE;
			break;
		case 'R':
			x[0] = x[0] + UNIT_SIZE;
			break;
		}

	}
	public void checkApple() {
                
		if((x[0] == appleX) && (y[0] == appleY)) {
            if (color.equals("Red") && countY!=10){
				bodyParts++;
				applesEaten++;
    	        color="Blue";
				
            }
        	else if(color.equals("Blue") && countY!=10){
         	    bodyParts+=1;
            	applesEaten+=1;
            	color="Red";
				
            }
			
			newApple();
		}
		if (yellowAppleActive && (x[0] == yellowAppleX) && (y[0] == yellowAppleY)) {
			applesEaten += 5; // Increase score by 5
			yellowAppleActive = false; // Deactivate yellow apple
			yellowAppleTimer.stop(); // Stop the timer
		}
	}
	public void checkCollisions() {
		// Wrap around logic for screen borders
		if (x[0] < 0) {
			x[0] = SCREEN_WIDTH; // Wrap around to the right edge
		}
		if (x[0] > SCREEN_WIDTH) {
			x[0] = 0; // Wrap around to the left edge
		}
		if (y[0] < 0) {
			y[0] = SCREEN_HEIGHT; // Wrap around to the bottom edge
		}
		if (y[0] > SCREEN_HEIGHT) {
			y[0] = 0; // Wrap around to the top edge
		}
	
		// checks if head collides with body
		for (int i = bodyParts; i > 0; i--) {
			if ((x[0] == x[i]) && (y[0] == y[i])) {
				running = false;
			}
		}
		if (whiteBlockActive && (x[0] == whiteBlockX) && (y[0] == whiteBlockY)) {
			running = false; // End the game
		}
	
		if (!running) {
			timer.stop();
		}
	}
	public void gameOver(Graphics g) {
		
		//Score
                try {
            System.out.println("Hello");	
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/snakegame","root","");
            ResultSet score = ((PreparedStatement) con.prepareStatement("SELECT MAX(score) FROM scoreboard ")).executeQuery();

            score.next();
            n = Integer.parseInt(score.getString(1));
            
        }
        catch(Exception e){
       System.out.println(e);
       }
		g.setColor(Color.red);
		g.setFont( new Font("Ink Free",Font.BOLD, 40));
		FontMetrics metrics1 = getFontMetrics(g.getFont());
                
                if(applesEaten>n){
                    g.drawString("New High Score! "+applesEaten, (SCREEN_WIDTH - metrics1.stringWidth("New High Score! "+applesEaten))/2,SCREEN_HEIGHT/3 );
                }
                else{
                    g.drawString("Score: "+applesEaten, (SCREEN_WIDTH - metrics1.stringWidth("Score: "+applesEaten))/2, SCREEN_HEIGHT/3);

                }
		//Game Over text
		g.setColor(Color.red);
		g.setFont( new Font("Ink Free",Font.BOLD, 75));
		FontMetrics metrics2 = getFontMetrics(g.getFont());
		g.drawString("Game Over", (SCREEN_WIDTH - metrics2.stringWidth("Game Over"))/2, SCREEN_HEIGHT/2);
                jbutton1.setVisible(true);
                
	}
	@Override
	public void actionPerformed(ActionEvent e) {

		if(running) {
			move();
			checkApple();
			checkCollisions();
		}
		repaint();
	}
        
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {                                         
        // TODO add your handling code here:
        try {
            System.out.println("Hello");	
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/snakegame","root","");
            PreparedStatement ps = (PreparedStatement) con.prepareStatement("INSERT INTO scoreboard VALUES(?,?)");
            ps.setString(1,name);
            ps.setInt(2,applesEaten);
            int row = ps.executeUpdate();
            if(row==0){
                System.out.println("Failed");
                
            }
            else{
                System.out.println("Success");
            }
            System.out.println("Hello");
            
        }
        catch(Exception e){
       }
        ((Window) getRootPane().getParent()).dispose();
        new Home().setVisible(true);
        
    } 

	public class MyKeyAdapter extends KeyAdapter{
		@Override
		public void keyPressed(KeyEvent e) {
			switch(e.getKeyCode()) {
			case KeyEvent.VK_LEFT:
				if(direction != 'R') {
					direction = 'L';
				}
				break;
			case KeyEvent.VK_RIGHT:
				if(direction != 'L') {
					direction = 'R';
				}
				break;
			case KeyEvent.VK_UP:
				if(direction != 'D') {
					direction = 'U';
				}
				break;
			case KeyEvent.VK_DOWN:
				if(direction != 'U') {
					direction = 'D';
				}
				break;
			}
		}
	}
}
