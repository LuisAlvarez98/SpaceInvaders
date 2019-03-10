package spaceinvaders;

import java.awt.image.BufferedImage;

/**
 * Assets Class
 *
 * @author Luis Felipe Alvarez Sanchez and Genaro 4 Feb 2019
 */
public class Assets {

    public static BufferedImage player, alien, background, bullet, explosion, gameover, bomb, start, paused, gg;
    public static SoundClip main, bombExp;

    /**
     * loads the assets
     */
    public static void init() {
        background = ImageLoader.loadImage("/images/bg.png");
        player = ImageLoader.loadImage("/images/player.png");
        alien = ImageLoader.loadImage("/images/alien.png");
        bullet = ImageLoader.loadImage("/images/bullet.png");
        explosion = ImageLoader.loadImage("/images/explosion.png");
        gameover = ImageLoader.loadImage("/images/gameover.png");
        start = ImageLoader.loadImage("/images/start.png");
        bomb = ImageLoader.loadImage("/images/bomb.png");
        paused = ImageLoader.loadImage("/images/paused.png");
        gg = ImageLoader.loadImage("/images/goodgame.png");
        main = new SoundClip("/sounds/space.wav");
        bombExp = new SoundClip("/sounds/explosion.wav");
    }

}
