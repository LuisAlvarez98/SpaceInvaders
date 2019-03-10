package spaceinvaders;

import java.awt.Graphics;
import java.awt.Rectangle;
import javax.swing.ImageIcon;
import static spaceinvaders.Commons.ALIEN_HEIGHT;
import static spaceinvaders.Commons.ALIEN_WIDTH;

/**
 * Player class
 *
 * @author Luis Felipe Alvarez Sanchez A01194173 4 Feb 2019
 */
public class Alien extends Item {

    private int width;
    private int height;
    private int health;
    private Game game;
    private Bomb bomb;
    private boolean dead;
    private int direction;
    private boolean visible;

    /**
     * Player constructor
     *
     * @param x
     * @param y
     * @param width
     * @param height
     * @param game
     */
    public Alien(int x, int y, int width, int height, Game game) {
        super(x, y);
        this.width = width;
        this.height = height;
        this.game = game;
        this.dead = false;
        this.direction = 1;
        this.visible = true;
        bomb = new Bomb(x, y);
        this.visible = true;
        this.dead = false;
    }
    /**
     * getBomb method
     * @return bomb
     */
    public Bomb getBomb() {
        return bomb;
    }
    /**
     * isVisible method
     * @return 
     */
    public boolean isVisible() {
        return visible;
    }
    /**
     * setVisible method
     * @param visible 
     */
    public void setVisible(boolean visible) {
        this.visible = visible;
    }
    /**
     * getDirection method
     * @return direction
     */
    public int getDirection() {
        return direction;
    }
    /**
     * setDirection method
     * @param direction 
     */
    public void setDirection(int direction) {
        this.direction = direction;
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
     * setDead method
     *
     * @param dead
     */
    public void setDead(boolean dead) {
        this.dead = dead;
    }

    /**
     * isDead method
     *
     * @return dead
     */
    public boolean isDead() {
        return dead;
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
     * getHealth method
     *
     * @return health
     */
    public int getHealth() {
        return health;
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
     * setWidth method
     *
     * @param width
     */
    public void setWidth(int width) {
        this.width = width;
    }

    /**
     * setHealth method
     *
     * @param health
     */
    public void setHealth(int health) {
        this.health = health;
    }

    /**
     * tick method The overall movement of the object
     */
    @Override
    public void tick() {
        if (getDirection() >= 1) {
            this.x += +1;
        } else {
            this.x += -1;
        }
    }
    //Collisions

    /**
     * getPerimetro method
     *
     * @return rectangle
     */
    public Rectangle getPerimetro() {

        return new Rectangle(getX(), getY(), getWidth(), getHeight() - 50);
    }
    /**
     * setPerimetro creates a new rectangle
     * @return 
     */
    public Rectangle setPerimetro() {
        return new Rectangle(0, 0, 0, 0);
    }
    /**
     * getBomb method
     * @return bomb
     */
    public Bomb getbomb() {
        return bomb;
    }

    /**
     * Renders the player
     *
     * @param g
     */
    @Override
    public void render(Graphics g) {
        g.drawImage(Assets.alien, getX(), getY(), getWidth(), getHeight(), null);
        
    }
    /**
     * Bomb clas
     */
    public class Bomb extends Sprite {

        private boolean destroyed;
        private int x;
        private int y;
        public Bomb(int x, int y) {
            int randNum = (int) (Math.random() * 100 + 1);
            this.x = x;
            this.y = y;
            if (randNum % 8 == 0) {
                initBomb(x, y);
            }
        }
        /**
         * getX
         * @return x
         */
        public int getX() {
            return x;
        }
        /**
         * getY
         * @return y 
         */
        public int getY() {
            return y;
        }
        /**
         * setX
         * @param x 
         */
        public void setX(int x) {
            this.x = x;
        }
        /**
         * setY 
         * @param y 
         */
        public void setY(int y) {
            this.y = y;
        }
           /**
            * initBomb
            * @param x
            * @param y 
            */
        private void initBomb(int x, int y) {
            setDestroyed(true);
            this.x = x;
            this.y = y;
            setImage(Assets.bomb);
        }
        /**
         * setDestroyed method
         * @param destroyed 
         */
        public void setDestroyed(boolean destroyed) {

            this.destroyed = destroyed;
        }
        /**
         * isDestroyed method
         * @return destroyed
         */
        public boolean isDestroyed() {

            return destroyed;
        }

    }
}
