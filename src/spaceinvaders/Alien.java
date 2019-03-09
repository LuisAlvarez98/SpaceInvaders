package spaceinvaders;

import java.awt.Graphics;
import java.awt.Rectangle;
import javax.swing.ImageIcon;

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
        health = 3;
        this.game = game;
        this.dead = false;
        this.direction = 1;
        this.visible =true;
        bomb = new Bomb(x, y);
        this.visible = true;
    }
    public Bomb getBomb() {
        return bomb;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    
    public int getDirection() {
        return direction;
    }

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
        if(getDirection() >= 1){
              this.x += +1;
        }else{
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
        g.drawImage(Assets.alien,getX(), getY(), getWidth(), getHeight(), null);
    }
    public class Bomb extends Sprite {
        private boolean destroyed;
        
        public Bomb(int x, int y) {
          int randNum = (int) (Math.random() * 100 + 1);
            if(randNum%5==0) {
                initBomb(x,y);
            }
        }
        private void initBomb(int x, int y) {
            setDestroyed(true);
            this.x = x;
            this.y = y;
            setImage(Assets.bomb);
        }
        public void setDestroyed(boolean destroyed) {
        
            this.destroyed = destroyed;
        }

        public boolean isDestroyed() {
        
            return destroyed;
        }
    }
}
