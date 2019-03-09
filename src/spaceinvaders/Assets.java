package spaceinvaders;

import java.awt.image.BufferedImage;

/**
 * Assets Class
 * @author Luis Felipe Alvarez Sanchez and Genaro
 * 4 Feb 2019
 */
public class Assets {
    public static BufferedImage player,alien, background, bullet, explosion;
    /**
     * loads the assets
     */
    public static void init(){
        background = ImageLoader.loadImage("/images/bg.png");
        player = ImageLoader.loadImage("/images/player.png");
        alien = ImageLoader.loadImage("/images/alien.png");
        bullet = ImageLoader.loadImage("/images/bullet.png");
        explosion = ImageLoader.loadImage("/images/explosion.png");
    }
    
}
