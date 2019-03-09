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
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import static spaceinvaders.Commons.ALIEN_HEIGHT;
import static spaceinvaders.Commons.ALIEN_WIDTH;
import static spaceinvaders.Commons.BOARD_WIDTH;
import static spaceinvaders.Commons.BOMB_HEIGHT;
import static spaceinvaders.Commons.BORDER_LEFT;
import static spaceinvaders.Commons.BORDER_RIGHT;
import static spaceinvaders.Commons.GROUND;
import static spaceinvaders.Commons.PLAYER_HEIGHT;
import static spaceinvaders.Commons.PLAYER_WIDTH;

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
    private final String explImg = "src/images/explosion.png";

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
    private LinkedList<Heart> hearts;

    private int direction = -1;

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
        this.hearts = new LinkedList<Heart>();
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
        this.score += 100;
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

        initAliens();
        hearts.add(new Heart(0, 20, 25, 25));
        hearts.add(new Heart(15, 20, 25, 25));
        hearts.add(new Heart(30, 20, 25, 25));

        player = new Player(getWidth() / 2 - 35, getHeight() - 50, 50, 50, this, 3);
        bullet = new Bullet();
        display.getJframe().addKeyListener(keyManager);
    }

    public void drawShot(Graphics g) {

        if (bullet.isVisible()) {
            g.drawImage(bullet.getImage(), bullet.getX(), bullet.getY(), this);
        }
    }

    public void drawBombs(Graphics g) {
        for (Alien a : aliens) {
            Alien.Bomb b = a.getbomb();

            if (!b.isDestroyed()) {
                g.drawImage(b.getImage(), b.getX(), b.getY(), this);
            }
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
        if (!isGameOver()) {
            player.tick();
            //Gamestart
            if (getKeyManager().space) {
                if (!bullet.isVisible()) {
                    bullet = new Bullet(player.getX(), player.getY());
                }
            }
            if (bullet.isVisible()) {

                int bulletX = bullet.getX();
                int bulletY = bullet.getY();

                for (int i = 0; i < aliens.size(); i++) {

                    int alienX = aliens.get(i).getX();
                    int alienY = aliens.get(i).getY();

                    if (bullet.isVisible()) {
                        if (bulletX >= (alienX)
                                && bulletX <= (alienX + aliens.get(i).getWidth())
                                && bulletY >= (alienY)
                                && bulletY <= (alienY + aliens.get(i).getHeight())) {
                            ImageIcon ii
                                    = new ImageIcon(Assets.explosion);
                            aliens.get(i).isDead();
                            increaseScore();
                            bullet.die();
                            aliens.remove(i);
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

            for (int i = 0; i < aliens.size(); i++) {

                int x = aliens.get(i).getX();
                aliens.get(i).tick();
                if (x >= BOARD_WIDTH - BORDER_RIGHT) {
                    for (int j = 0; j < aliens.size(); j++) {
                        aliens.get(j).setY(aliens.get(j).getY() + 3);
                        aliens.get(j).setDirection(-1);
                    }

                }

                if (x <= BORDER_LEFT) {
                    for (int j = 0; j < aliens.size(); j++) {
                        aliens.get(j).setY(aliens.get(j).getY() + 3);
                        aliens.get(j).setDirection(1);
                    }
                }
                if (aliens.get(i).isVisible()) {

                    int y = aliens.get(i).getY();
                    //System.out.println(y);
                    if (y > GROUND - ALIEN_HEIGHT - 45) {
                        System.out.println("end");
                        setGameOver(true);

                    }
                }
            }
               // bombs
        Random generator = new Random();

        for (Alien alien: aliens) {

            int shot = generator.nextInt(15);
            Alien.Bomb b = alien.getbomb();


            if (shot == CHANCE && alien.isVisible() && b.isDestroyed()) {

                b.setDestroyed(false);
                b.setX(alien.getX());
                b.setY(alien.getY());
            }

            int bombX = b.getX();
            int bombY = b.getY();
            int playerX = player.getX();
            int playerY = player.getY();

            if (player.isVisible() && !b.isDestroyed()) {

                if (bombX >= (playerX)
                        && bombX <= (playerX + PLAYER_WIDTH)
                        && bombY >= (playerY)
                        && bombY <= (playerY + PLAYER_HEIGHT)) {
                    b.setDestroyed(true);
                }
            }

            if (!b.isDestroyed()) {
                
                b.setY(b.getY() + 1);
                
                if (b.getY() >= GROUND - BOMB_HEIGHT) {
                    b.setDestroyed(true);
                }
            }
        }

            if (player.getLives() <= 0) {
                setGameOver(true);
            }
            
        } else if (getKeyManager().enter) {
            //init everything
            setGameOver(false);

            //Resets lives and score
            player.setLives(3);
            setScore(0);
            //RESETS PLAYER, BULLET AND ALIENS

            initAliens();
            player = new Player(getWidth() / 2 - 35, getHeight() - 50, 50, 50, this, 3);
            bullet = new Bullet();
        }
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

            //Renders lives each tick
            for (int i = 0; i < player.getLives(); i++) {
                Heart heart = hearts.get(i);
                heart.render(g);
            }
            drawBombs(g);
            for (int i = 0; i < aliens.size(); i++) {
                aliens.get(i).render(g);
            }

            if (isGameOver()) {
                g.drawImage(Assets.gameover, 125, getHeight() / 2 - 150, 250, 250, null);
                aliens.remove(g);
            }
            g.setColor(Color.WHITE);
            g.drawString("Score: " + getScore(), 5, getHeight() - 475);
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

    private void initAliens() {
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                aliens.add(new Alien(ALIEN_INIT_X + 25 * j, ALIEN_INIT_Y + 25 * i, ALIEN_HEIGHT, ALIEN_WIDTH, this));
                
            }
        }
    }
}
