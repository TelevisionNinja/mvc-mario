import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

/* 
 * date: 10/14/2021
 * assignment description:
 * 
 * polymorphism
 */

public class Brick extends Sprite {
    static BufferedImage image = null;

    Brick() {

    }

    Brick(int x, int y, int w, int h) {
        super(x, y, w, h);
        super.type = "brick";

        loadImage();
    }

    // Unmarshaling constructor
    Brick(Json ob) {
        super((int) ob.getLong("x"),
            (int) ob.getLong("y"),
            (int) ob.getLong("w"),
            (int) ob.getLong("h"));
        super.type = "brick";

        loadImage();
    }

    private void loadImage() {
        // load images
        if (image == null) {
            try {
                image = ImageIO.read(new File("brick.png"));
            }
            catch(Exception e) {
                e.printStackTrace(System.err);
                System.exit(1);
            }
        }
    }

    // Marshals this object into a JSON DOM
    Json marshal() {
        Json ob = Json.newObject();
        ob.add("x", x);
        ob.add("y", y);
        ob.add("w", w);
        ob.add("h", h);

        return ob;
    }

    void draw(Graphics g, int offest) {
        g.drawImage(Brick.image, super.x - offest, super.y, super.w, super.h, null);
    }

    void update() {
    }
}
