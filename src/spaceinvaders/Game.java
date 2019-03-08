package spaceinvaders;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Game Class Crystal Braker Game
 *
 * @author Luis Felipe Alvarez Sanchez and Genaro
 */
public class Game implements Runnable {

    private BufferStrategy bs; // BufferStrategy var
    private Graphics g; // for the graphics
    private Display display; // for the display of the game
    String title; // the title of the game
    private int width; // the width of the game
    private int height; //the height of the game
    private Thread thread; //the thread of the game
    private boolean running; //boolean saying if it is running
    private boolean paused; // paused boolean

    private ArrayList<Bar> bars; //blocks array list
    private Player player; //player instance
    private KeyManager keyManager; //key manager

    private boolean gameOver; //gameover boolean
    private int score; //score of the game
    private boolean gameStart; //gamestart boolean

    private boolean won; // did you win?
    private int enemies; // number of enemies

    private ArrayList<PowerUp> powerups;

    /**
     * Game Constructor
     *
     * @param title
     * @param width
     * @param height
     */
    public Game(String title, int width, int height) {
        this.title = title;
        this.width = width;
        this.height = height;
        running = false;
        paused = false;
        keyManager = new KeyManager();
        bars = new ArrayList<Bar>();
        powerups = new ArrayList<PowerUp>();
        this.score = 0;
        this.gameStart = false;
        this.won = false;
        this.enemies = 0;
    }

    /**
     * setGameStart method
     *
     * @param gameStart
     */
    public void setGameStart(boolean gameStart) {
        this.gameStart = gameStart;
    }

    /**
     * isPaused method
     *
     * @return paused
     */
    public boolean isPaused() {
        return paused;
    }

    /**
     * setPaused method
     *
     * @param paused
     */
    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    /**
     * isWon
     *
     * @return won
     */
    public boolean isWon() {
        return won;
    }

    /**
     * setWon method
     *
     * @param won
     */
    public void setWon(boolean won) {
        this.won = won;
    }

    /**
     * isGameStart method
     *
     * @return gameStart
     */
    public boolean isGameStart() {
        return gameStart;
    }

    /**
     * setGameOver method
     *
     * @param gameOver
     */
    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    /**
     * isGameOver method
     *
     * @return gameOver
     */
    public boolean isGameOver() {
        return gameOver;
    }

    /**
     * IncreaseScore method increases score by 10
     */
    public void increaseScore() {
        this.score += 10;
    }

    /**
     * setScore method
     *
     * @param score
     */
    public void setScore(int score) {
        this.score = score;
    }

    /**
     * getScore method
     *
     * @return score
     */
    public int getScore() {
        return score;
    }

    /**
     * getHeight method
     *
     * @return height
     */
    public int getHeight() {
        return height;
    }

    /**
     * getWidth method
     *
     * @return width
     */
    public int getWidth() {
        return width;
    }
    /**
     * inits the game with the display and player
     */
    public void init() {
        display = new Display(title, getWidth(), getHeight());
        Assets.init();
        player = new Player(getWidth()/2 - 35,getHeight()-50, 50, 50,this,3);
        display.getJframe().addKeyListener(keyManager);
    }

    /**
     * run method
     */
    @Override
    public void run() {
        init();
        int fps = 50;
        double timeTick = 1000000000 / fps;
        double delta = 0;
        long now;
        long lastTime = System.nanoTime();
        while (running) {
            now = System.nanoTime();
            delta += (now - lastTime) / timeTick;
            lastTime = now;

            if (delta >= 1) {
                tick();
                render();
                delta--;
            }
        }
        stop();
    }

    /**
     * getKeyManager method
     *
     * @return keyManager
     */
    public KeyManager getKeyManager() {
        return keyManager;
    }

    /**
     * tick method
     */
    private void tick() {
        keyManager.tick();
        player.tick();
        //Gamestart
    }

    /**
     * render method where all the magic happens
     */
    private void render() {
        bs = display.getCanvas().getBufferStrategy();
        if (bs == null) {
            display.getCanvas().createBufferStrategy(3);
        } else {
            g = bs.getDrawGraphics();
            g.drawImage(Assets.background, 0, 0, width, height, null);
            player.render(g);
            g.setColor(Color.WHITE);
            g.drawString("Score: " + getScore(), 10, getHeight() - 485);
            g.setColor(Color.WHITE);
            bs.show();
            g.dispose();
        }
    }

    /**
     * start method
     */
    public synchronized void start() {
        if (!running) {
            running = true;
            thread = new Thread(this);
            thread.start();
        }
    }

    /**
     * stop method
     */
    public synchronized void stop() {
        if (running) {
            running = false;
            try {
                thread.join();
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }
        }
    }
}
