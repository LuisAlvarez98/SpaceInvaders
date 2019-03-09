package spaceinvaders;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;

/**
 * Player Class
 *
 * @author Luis Felipe Alvarez Sanchez A01194173 12 Feb 2019
 */
public class Player extends Item {

    //Instance variables
    private int direction;
    private int width;
    private int height;
    private Game game;
    private Bullet bullet;
    private int lives;
    private boolean visible;

    /**
     * Player constructor
     *
     * @param x
     * @param y
     * @param direction
     * @param width
     * @param height
     * @param game
     * @param bullet
     */
    public Player(int x, int y, int width, int height, Game game,int lives) {
        super(x, y);
        this.width = width;
        this.height = height;
        this.game = game;
        this.lives = lives;
        this.visible = true;
    }
    /**
     * decreases the player lives by one
     */
    public void decreasePlayerLive() {
        this.lives--;
    }
    /**
     * setLives method
     * @param lives 
     */
    public void setLives(int lives) {
        this.lives = lives;
    }

    /**
     * getLives method
     *
     * @return lives
     */
    public int getLives() {
        return this.lives;
    }
    
    /**
     * getDirection method
     *
     * @return direction
     */
    public int getDirection() {
        return direction;
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
     * getHeight method
     *
     * @return height
     */
    public int getHeight() {
        return height;
    }

    /**
     * setDirection method
     *
     * @param direction
     */
    public void setDirection(int direction) {
        this.direction = direction;
    }

    /**
     * setWidth method
     *
     * @param width
     */
    public void setWidth(int width) {
        this.width = width;
    }

    /**
     * setHeight method
     *
     * @param height
     */
    public void setHeight(int height) {
        this.height = height;
    }

    /**
     * tick method overall movement of the player
     */
    @Override
    public void tick() {
        // vertical left up
        if (game.getKeyManager().left) {
            setX(getX() - 10);
        }
        // vertical left down
        if (game.getKeyManager().right) {
            setX(getX() + 10);
        }
       
        // reset x position and y position if colision
        if (getX() + 40 >= game.getWidth()) {
            setX(game.getWidth() - 40);
        } else if (getX() <= -10) {
            setX(-10);
        }
        
    }
    /**
     * Creates rectangle for the player
     * @return 
     */
   public Rectangle getPerimetro() {
        return new Rectangle(getX(), getY(), 40, 40);
    }
    /**
     * render method
     *
     * @param g
     */
    @Override
    public void render(Graphics g) {
        //draws the player
        g.drawImage(Assets.player, getX(), getY(), getWidth(), getHeight(), null);
    }

    boolean isVisible() {
        return visible;
    }
}
