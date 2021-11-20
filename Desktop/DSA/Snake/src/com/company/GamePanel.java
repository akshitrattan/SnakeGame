package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;


public class GamePanel extends JPanel implements ActionListener {

    final static int SCREEN_WIDTH = 600;
    final static int SCREEN_HEIGHT = 600;
    static final int UNIT_SIZE = 25; // how big do we want the items in the screen
    static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT)/UNIT_SIZE;
    static final int DELAY = 80;
    final int[] X = new int[GAME_UNITS]; // coordinates of the snake body parts
    final int[] Y = new int[GAME_UNITS]; // coordinates of the snake body parts
    int bodyParts = 6;
    int applesEaten;
    int appleX;
    int appleY;
    char direction = 'R'; // keeps a check in which direction is the snake moving;
    boolean running  = false;
    Timer timer;
    Random random;

    @Override
    public void actionPerformed(ActionEvent e) {
        if( running ) {
            move();
            CheckApple();
            CheckCollisions();

        }
        repaint( );

    }

    GamePanel() {
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.WHITE);
        this.setFocusable(true);
        this.addKeyListener(new myKeyAdapter());
        startGame();
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
    public void draw( Graphics g ) {
        if ( running ) {
//            for (int i = 0; i < SCREEN_HEIGHT / UNIT_SIZE; i++) {// build a grid for better visualization;
//                g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, SCREEN_HEIGHT);
//                g.drawLine(0, i * UNIT_SIZE, SCREEN_WIDTH, i * UNIT_SIZE);
//            }
            g.setColor(Color.red);
            g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);

            for (int i = 0; i < bodyParts; i++) {
                if (i == 0) {
                    g.setColor(Color.green);
                    g.fillRect(X[i], Y[i], UNIT_SIZE, UNIT_SIZE);
                } else {
                    g.setColor(new Color(45, 180, 0));
                    g.fillRect(X[i], Y[i], UNIT_SIZE, UNIT_SIZE);
                }

            }
            g.setColor(Color.red);
            g.setFont(new Font("Ink Free",Font.BOLD, 40));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("Score: " + applesEaten, (SCREEN_WIDTH - metrics.stringWidth("Score: " + applesEaten))/2, g.getFont().getSize() );
        }
        else {
            GameOver(g);
        }
    }
    public void newApple() {
        appleX = random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE)) * UNIT_SIZE;
        appleY = random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE)) * UNIT_SIZE;

    }

    public void move() {
        for (int i = bodyParts; i > 0 ; i--) {
            X[i] = X[i - 1];
            Y[i] = Y[i - 1];
        }

        switch (direction) {
            case 'U':
                Y[0] = Y[0] - UNIT_SIZE;
                break;
            case 'R':
                X[0] = X[0] + UNIT_SIZE;
                break;
            case 'D':
                Y[0] = Y[0] + UNIT_SIZE;
                break;
            case 'L':
                X[0] = X[0] - UNIT_SIZE;
                break;
        }
    }
    public void CheckApple() {
        if ( appleX == X[0] && appleY == Y[0] ) {
            bodyParts++;
            applesEaten++;
            newApple();
        }

    }
    public void CheckCollisions() {
        // checks if head collides with body
        for (int i = bodyParts; i > 0 ; i--) {
            if ( (X[0] == X[i]) && (Y[0] == Y[i]) ) {
                running = false;
            }

        }
        // checks if head touches the left border;
        if ( X[0] < 0 ) {
            running = false;
        }
        // checks if head touches the right border;
        if ( X[0] > SCREEN_WIDTH ) {
            running = false;
        }
        // checks if head touches the bottom border;
        if ( Y[0] > SCREEN_HEIGHT ) {
            running = false;
        }
        // checks if head touches the top border;
        if ( Y[0] < 0 ) {
            running = true;
        }

        if ( !running ) {
            timer.stop();
        }

    }
    public void GameOver(Graphics g ) {
        //Game Over Text
        g.setColor(Color.red);
        g.setFont(new Font("Ink Free",Font.BOLD, 75));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("GAME OVER", (SCREEN_WIDTH - metrics.stringWidth("GAME OVER"))/2, SCREEN_HEIGHT/2);

    }

    public class myKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed( KeyEvent e ) {
            switch ( e.getKeyCode() ) {
                case KeyEvent.VK_LEFT:
                    if ( direction != 'R' ) {
                        direction = 'L';
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if ( direction != 'L') {
                        direction = 'R';
                    }
                    break;

                case KeyEvent.VK_UP:
                    if ( direction != 'D') {
                        direction = 'U';
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if ( direction != 'U' ) {
                        direction = 'D';
                    }
                    break;

            }
        }
    }
}
