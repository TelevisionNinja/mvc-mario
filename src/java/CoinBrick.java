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

public class CoinBrick extends Brick {
    static BufferedImage[] images = null;
    boolean hit = false;
    int coinsLeft = 5,
        imageIndex = 0;

    CoinBrick(int x, int y) {
        super.x = x;
        super.y = y;
        super.type = "coinBrick";

        loadImage();

        super.w = images[0].getWidth();
        super.h = images[0].getHeight();
    }

    // Unmarshaling constructor
    CoinBrick(Json ob) {
        super.x = (int) ob.getLong("x");
        super.y = (int) ob.getLong("y");
        loadImage();
        super.w = images[0].getWidth();
        super.h = images[0].getHeight();
        super.type = "coinBrick";
    }

    private void loadImage() {
        if (images == null) {
            images = new BufferedImage[2];

            for (int i = 0; i < 2; i++) {
                images[i] = loadImage("block" + (i + 1) + ".png");
            }
        }
    }

    private BufferedImage loadImage(String fileName) {
        try {
            return ImageIO.read(new File(fileName));
        }
        catch(Exception e) {
            e.printStackTrace(System.err);
            System.exit(1);
        }

        return null;
    }

    Coin spawnCoin() {
        if (coinsLeft > 0) {
            coinsLeft--;

            if (coinsLeft == 0) {
                imageIndex = 1;
            }

            return new Coin(super.x, super.y);
        }

        return null;
    }

    void update() {
    }

    void draw(Graphics g, int offest) {
        g.drawImage(CoinBrick.images[imageIndex], super.x - offest, super.y, super.w, super.h, null);
    }

    // Marshals this object into a JSON DOM
    Json marshal() {
        Json ob = Json.newObject();
        ob.add("x", x);
        ob.add("y", y);

        return ob;
    }
}
