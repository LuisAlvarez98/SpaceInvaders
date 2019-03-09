package spaceinvaders;

import java.awt.Graphics;
/**
 *  Heart class
 * @author Luis Felipe Alvarez Sanchez A01194173
 *  10 Feb 2019
 */
public class Heart extends Item{
    //Instance variables
    private int width;
    private int height;
    /**
     * 
     * @param x
     * @param y
     * @param width
     * @param height
     */
    public Heart(int x, int y, int width, int height) {
        super(x, y);
        this.width = width;
        this.height = height;
    }
    /**
     * getHeight method
     * @return height
     */
    public int getHeight() {
        return height;
    }
    /**
     * getWidth method
     * @return width
     */
    public int getWidth() {
        return width;
    }
    /**
     * setHeight method
     * @param height 
     */
    public void setHeight(int height) {
        this.height = height;
    }
    /**
     * setWidth method
     * @param width 
     */
    public void setWidth(int width) {
        this.width = width;
    }
     /**
      * tick method
     */
    @Override
    public void tick() {  
        
    }
    /**
     * Renders the heart
     * @param g 
     */
    @Override
    public void render(Graphics g) {
         g.drawImage(Assets.player,getX(), getY(), getWidth(), getHeight(), null);
    }
}
