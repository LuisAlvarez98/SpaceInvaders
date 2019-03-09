/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceinvaders;

import javax.swing.ImageIcon;

/**
 *
 * @author genar
 */
public class Bullet extends Sprite{
    
    private final String shotImg = "src/images/bullet.png";
    private final int H_SPACE = 6;
    private final int V_SPACE = 1;

    public Bullet() {
    }

    public Bullet(int x, int y) {
        initBullet(x, y);
    }

    private void initBullet(int x, int y) {

        ImageIcon ii = new ImageIcon(shotImg);
        setImage(ii.getImage());
        
        setX(x + H_SPACE);
        setY(y - V_SPACE);
    }
}
