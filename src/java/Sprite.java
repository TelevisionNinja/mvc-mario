/* 
 * date: 10/14/2021
 * assignment description:
 * 
 * polymorphism
 */

import java.awt.Graphics;

public abstract class Sprite {
    int x,
        y,
        w,
        h;
    String type;

    Sprite() {}

    Sprite(int x, int y, int w, int h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

    abstract void update();
    abstract void draw(Graphics g, int offest);
}
