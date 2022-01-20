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

public class Mario extends Sprite {
    int previousX,
        previousY,
        direction = 1,
        currentMarioFrameIndex = 0,
        screenLocation;
    double vert_vel;
    static BufferedImage[] images = null;
    boolean isWalking = false,
        isJumping = false;
    int jumpFrame = 0,
        jumpDuration = 20,
        jumpAmount = 20;

    Mario() {
        // load mario images
        if (images == null) {
            images = new BufferedImage[5];

            for (int i = 0; i < 5; i++) {
                images[i] = loadImage("mario" + (i + 1) + ".png");
            }
        }

        super.w = images[0].getWidth(null);
        super.h = images[0].getHeight(null);
        super.type = "mario";
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

    void setWalking(boolean b) {
        this.isWalking = b;
    }

    void setPos(int x, int y) {
        this.previousX = super.x;
        this.previousY = super.y;

        super.x = x;
        super.y = y;
    }

    void update() {
        // update mario image
        if (this.isWalking) {
            currentMarioFrameIndex = (currentMarioFrameIndex + 1) % images.length;
        }

        // gravity
        this.vert_vel += 1.5;
        setPos(super.x, (int) (super.y + this.vert_vel));

        // snap back to the ground
        if (super.y > 500) {
            super.y = 500;
            this.previousY = 500;
            this.vert_vel = 0;
        }

        // jump
        if (isJumping) {
            this.jumpFrame++;

            if (this.jumpFrame < jumpDuration) {
                this.vert_vel = 0;
                setPos(super.x, this.previousY - jumpAmount);
            }
            else if (this.vert_vel == 0) {
                this.jumpFrame = 0;
            }
        }
        else {
            if (this.vert_vel == 0) {
                this.jumpFrame = 0;
            }
            else {
                this.jumpFrame = jumpDuration;
            }
        }
    }

    void draw(Graphics g, int offest) {
        if (this.direction < 0) {
            g.drawImage(Mario.images[this.currentMarioFrameIndex], this.screenLocation + super.w, super.y, -super.w, super.h, null);
        }
        else {
            g.drawImage(Mario.images[this.currentMarioFrameIndex], this.screenLocation, super.y, super.w, super.h, null);
        }
    }
}
