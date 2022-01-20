/* 
 * date: 10/14/2021
 * assignment description:
 * 
 * polymorphism
 */

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class Coin extends Sprite {
    static BufferedImage image = null;
    double horizontal;
    double vertical = 0;

    Coin(int x, int y) {
        super.x = x;
        super.y = y;
        super.type = "coin";

        if (image == null) {
            try {
                image = ImageIO.read(new File("coin.png"));
            }
            catch(Exception e) {
                e.printStackTrace(System.err);
                System.exit(1);
            }
        }
        super.w = image.getWidth();
        super.h = image.getHeight();

        // generate random direction
        int randDirection = (int) (Math.random() * -2);
        if (randDirection == 0) {
            randDirection = 1;
        }
        this.horizontal = randDirection * (Math.random() * 12 + 1);
    }

    void update() {
        super.x += this.horizontal;

        // gravity
        super.y += this.vertical;
        this.vertical += 1.2;
    }

    void draw(Graphics g, int offest) {
        g.drawImage(Coin.image, super.x - offest, super.y, super.w, super.h, null);
    }
}
