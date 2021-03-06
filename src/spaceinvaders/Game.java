package spaceinvaders;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
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

    private final int ALIEN_INIT_X = 165;
    private final int ALIEN_INIT_Y = 15;
    private ArrayList<Alien> aliens;
    private LinkedList<Heart> hearts;
    private boolean gameover, goodgame;
    private int direction = -1;

    private int alienSize;

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
        this.hearts = new LinkedList<Heart>();
        this.alienSize = 36;
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

    public void setAlienSize(int alienSize) {
        this.alienSize = alienSize;
    }

    public int getAlienSize() {
        return alienSize;
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
    public boolean isGoodGame() {
        return goodgame;
    }

    /**
     * setScore method
     *
     * @param score
     */
    public void setGoodGame(boolean goodgame) {
        this.goodgame = goodgame;
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
        Assets.main.setLooping(true);
        Assets.main.play();
        

        initAliens();
        hearts.add(new Heart(0, 20, 25, 25));
        hearts.add(new Heart(15, 20, 25, 25));
        hearts.add(new Heart(30, 20, 25, 25));

        player = new Player(getWidth() / 2 - 35, getHeight() - 50, 50, 50, this, 3);
        bullet = new Bullet();
        display.getJframe().addKeyListener(keyManager);
    }
    /**
     * Draw shoot method
     * @param g 
     */
    public void drawShot(Graphics g) {

        if (bullet.isVisible()) {
            g.drawImage(bullet.getImage(), bullet.getX(), bullet.getY(), this);
        }
    }
    /**
     * Draw bombs method
     * @param g 
     */
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

        if (getKeyManager().enter) {
            setGameStart(true);
        }
        if (getKeyManager().load && !isGameStart()) {
            //setGameStart(true);
            //load game
            // The name of the file to open.
            String fileName = "gamesave.txt";

            // This will reference one line at a time
            String line = null;

            try {
                // FileReader reads text files in the default encoding.
                FileReader fileReader
                        = new FileReader(fileName);

                // Always wrap FileReader in BufferedReader.
                BufferedReader bufferedReader
                        = new BufferedReader(fileReader);
                //reads everything from the file
                setScore(Integer.parseInt(bufferedReader.readLine()));
                player.setLives(Integer.parseInt(bufferedReader.readLine()));
                player.setX(Integer.parseInt(bufferedReader.readLine()));
                player.setY(Integer.parseInt(bufferedReader.readLine()));
                bullet.setX(Integer.parseInt(bufferedReader.readLine()));
                bullet.setY(Integer.parseInt(bufferedReader.readLine()));

                for (int i = 0; i < 36; i++) {
                    Alien alien = aliens.get(i);
                    alien.setX(Integer.parseInt(bufferedReader.readLine()));
                    alien.setY(Integer.parseInt(bufferedReader.readLine()));
                    int dead = Integer.parseInt(bufferedReader.readLine());
                    //if dead is true
                    alien.setDead(dead == 1);
                }

                bufferedReader.close();
            } catch (FileNotFoundException ex) {
                System.out.println(
                        "Unable to open file '"
                        + fileName + "'");
            } catch (IOException ex) {
                System.out.println(
                        "Error reading file '"
                        + fileName + "'");
            }
        }
        if (getKeyManager().save && !isGameOver() && isGameStart()) {
            //counter used to avoid multiple clicks
            int count = 0;
            if (count == 0) {
                count++;
                // The name of the file to open.
                String fileName = "gamesave.txt";

                try {
                    // Assume default encoding.
                    FileWriter fileWriter
                            = new FileWriter(fileName);

                    // Always wrap FileWriter in BufferedWriter.
                    BufferedWriter bufferedWriter
                            = new BufferedWriter(fileWriter);

                    // saves everything on the textfile
                    bufferedWriter.write(Integer.toString(getScore()) + '\n');
                    bufferedWriter.write(Integer.toString(player.getLives()) + '\n');
                    bufferedWriter.write(Integer.toString(player.getX()) + '\n');
                    bufferedWriter.write(Integer.toString(player.getY()) + '\n');
                    bufferedWriter.write(Integer.toString(bullet.getX()) + '\n');
                    bufferedWriter.write(Integer.toString(bullet.getY()) + '\n');

                    for (int i = 0; i < 36; i++) {
                        Alien alien = aliens.get(i);
                        bufferedWriter.write(Integer.toString(alien.getX()) + '\n');
                        bufferedWriter.write(Integer.toString(alien.getY()) + '\n');
                        int dead = (alien.isDead() ? 1 : 0);
                        bufferedWriter.write(Integer.toString(dead) + '\n');
                    }

                    // Always close files.
                    bufferedWriter.close();
                } catch (IOException ex) {
                    System.out.println(
                            "Error writing to file '"
                            + fileName + "'");
                }
            }
        }
        //if aint !gameover, gamestart and !isGoodGame
        if (!isGameOver() && isGameStart() && !isGoodGame()) {
            //Pause logic
            if (getKeyManager().pause) {
                getKeyManager().setKeyDown();
                paused = !paused;

            }
            if (!paused) {
                player.tick();
                //Gamestart
                if (getKeyManager().space) {
                    if (!bullet.isVisible()) {
                        bullet = new Bullet(player.getX(), player.getY());
                    }
                }
                if (getKeyManager().restart) {
                    //Resets lives and score
                    player.setLives(3);
                    setScore(0);
                    //RESETS PLAYER, BULLET AND ALIENS
                    aliens = new ArrayList<Alien>();
                    initAliens();
                    player = new Player(getWidth() / 2 - 35, getHeight() - 60, 50, 50, this, 3);
                    bullet = new Bullet();
                }
                if (bullet.isVisible()) {

                    int bulletX = bullet.getX();
                    int bulletY = bullet.getY();

                    for (int i = 0; i < aliens.size(); i++) {

                        int alienX = aliens.get(i).getX();
                        int alienY = aliens.get(i).getY();

                        if (bullet.isVisible() && !aliens.get(i).isDead()) {
                            if (bulletX >= (alienX)
                                    && bulletX <= (alienX + aliens.get(i).getWidth())
                                    && bulletY >= (alienY)
                                    && bulletY <= (alienY + aliens.get(i).getHeight())) {
                                ImageIcon ii
                                        = new ImageIcon(Assets.explosion);
                                increaseScore();
                                Assets.bombExp.play();
                                
                                bullet.die();
                                aliens.get(i).setDead(true);
                                setAlienSize(getAlienSize() - 1);
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
                   //checks alien position
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
                        if (y > GROUND - ALIEN_HEIGHT - 20) {
                            System.out.println("end");
                            setGameOver(true);

                        }
                    }
                }

                // bombs
                Random generator = new Random();

                for (Alien alien : aliens) {

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
                    if (!b.isDestroyed()) {

                        b.setY(b.getY() + 5);

                        if (b.getY() >= GROUND - 35) {
                            //b.setY(b.getY() - 50);
                            b.setDestroyed(true);
                        }
                    }
                    if (player.isVisible() && !b.isDestroyed()) {

                        if (bombX >= (playerX)
                        && bombX <= (playerX + 7)
                        && bombY >= (playerY)
                        && bombY <= (playerY + 7) && !b.isDestroyed()) {
                            b.setDestroyed(true);
                            if(b.isDestroyed()) {
                               player.setLives(player.getLives() - 1); 
                            }
                            Assets.bombExp.play();
                        }
                    }
                    /*if (player.intersects(b)) {
                        int counter = 0;
                        if (counter == 0) {
                            player.setLives(player.getLives() - 1);
                            b.setDestroyed(true);
                            counter++;
                        }

                    }*/
                    
                }

                if (player.getLives() <= 0) {
                    setGameOver(true);
                }
            }

        } else if (getKeyManager().enter) {
            //init everything
            setGameOver(false);
            setGoodGame(false);

            //Resets lives and score
            player.setLives(3);
            setScore(0);
            //RESETS PLAYER, BULLET AND ALIENS
            aliens = new ArrayList<Alien>();
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
                if (!aliens.get(i).isDead()) {
                    aliens.get(i).render(g);
                }
            }
            //Logic of game
            if (paused) {
                g.drawImage(Assets.paused, 0, 0, width, height, null);
            }
            if (!isGameStart()) {
                g.drawImage(Assets.start, 0, 0, width, height, null);
            }
            if (isGameOver()) {
                g.drawImage(Assets.gameover, 0, 0, width, height, null);
                aliens.remove(g);
            }
            if (isGoodGame()) {
                g.drawImage(Assets.gg, 0, 0, width, height, null);
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
    /**
     * Init the aliens on the game
     */
    private void initAliens() {
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                aliens.add(new Alien(ALIEN_INIT_X + 25 * j, ALIEN_INIT_Y + 25 * i, ALIEN_HEIGHT, ALIEN_WIDTH, this));

            }
        }
    }
}
