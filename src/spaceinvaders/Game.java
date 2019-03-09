package spaceinvaders;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.ImageObserver;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

/**
 * Game Class Crystal Braker Game
 *
 * @author Luis Felipe Alvarez Sanchez and Genaro
 */
public class Game extends JPanel implements Runnable, Commons {

    private BufferStrategy bs; // BufferStrategy var
    private Graphics g; // for the graphics
    private Display display; // for the display of the game
    String title; // the title of the game
    private int width; // the width of the game
    private int height; //the height of the game
    private Thread thread; //the thread of the game
    private boolean running; //boolean saying if it is running
    private boolean paused; // paused boolean

    private ArrayList<Alien> bars; //blocks array list
    private Player player; //player instance
    private Bullet bullet;
    private KeyManager keyManager; //key manager

    private boolean gameOver; //gameover boolean
    private int score; //score of the game
    private boolean gameStart; //gamestart boolean

    private boolean won; // did you win?
    private int enemies; // number of enemies

    private final int ALIEN_INIT_X = 165;
    private final int ALIEN_INIT_Y = 15;
       private ArrayList<Alien> aliens;
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
        aliens = new ArrayList<>();
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
        

        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                 aliens.add(new Alien(ALIEN_INIT_X + 25 * j, ALIEN_INIT_Y + 25 * i, 25,25,this));
            }
        }
        
        player = new Player(getWidth()/2 - 35,getHeight()-50, 50, 50,this,3);
        bullet = new Bullet();
        display.getJframe().addKeyListener(keyManager);
    }
        public void drawShot(Graphics g) {

        if (bullet.isVisible()) {
            
            g.drawImage(bullet.getImage(), bullet.getX(), bullet.getY(), this);
        }
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
        if(getKeyManager().space) {
            if (!bullet.isVisible()) {
                        bullet = new Bullet(player.getX(), player.getY());
                    }
        }
        if (bullet.isVisible()) {

            int shotX = bullet.getX();
            int shotY = bullet.getY();

            for (Alien alien: aliens) {

                int alienX = alien.getX();
                int alienY = alien.getY();

                if (bullet.isVisible()) {
                    if (shotX >= (alienX)
                            && shotX <= (alienX + alien.getWidth())
                            && shotY >= (alienY)
                            && shotY <= (alienY + alien.getHeight())) {
                        ImageIcon ii
                                = new ImageIcon(Assets.explosion);
                        bullet.die();
                    }
                }
            }

            int y = bullet.getY();
            y -= 4;

            if (y < 0) {
                bullet.die();
            } else {
                bullet.setY(y);
            }
        }
        /*@Override
        public void keyPressed(KeyEvent e) {

            player.keyPressed(e);

            int x = player.getX();
            int y = player.getY();

            int key = e.getKeyCode();

            if (key == KeyEvent.VK_SPACE) {
                
                if (ingame) {
                    if (!shot.isVisible()) {
                        shot = new Shot(x, y);
                    }
                }*/
        
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
            drawShot(g);
            for(int i = 0; i < aliens.size(); i++){
                  aliens.get(i).render(g);
            }
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
