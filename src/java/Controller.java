/* 
 * date: 10/14/2021
 * assignment description:
 * 
 * polymorphism
 */

import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;

class Controller implements MouseListener, KeyListener {
    View view;
    Model model;
    boolean keyLeft = false,
        keyRight = false,
        keySave = false,
        keyLoad = false,
        editEnabled = false;

    Controller(Model m) {
        model = m;
    }

    void setView(View v) {
        view = v;
    }

    //-------------------------------------------
    // mouse functionality

    public void mousePressed(MouseEvent e) {
        if (!editEnabled) {
            return;
        }

        model.sprites.add(new Brick(e.getX(), e.getY(), 0, 0));
    }

    public void mouseReleased(MouseEvent e) {
        if (!editEnabled) {
            return;
        }

        Sprite b = model.sprites.get(model.sprites.size() - 1);
        int mouseX = e.getX();
        int mouseY = e.getY();

        if (mouseX > b.x) {
            b.w = mouseX - b.x;
        }
        else {
            b.w = b.x - mouseX;
            b.x = mouseX;
        }

        b.x += model.mario.x;

        if (mouseY > b.y) {
            b.h = mouseY - b.y;
        }
        else {
            b.h = b.y - mouseY;
            b.y = mouseY;
        }
    }

    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}

    public void mouseClicked(MouseEvent e) {
        if(e.getY() < 100) {
            System.out.println("break here");
        }
    }

    //-------------------------------------------
    // keyboard functionality

    public void keyPressed(KeyEvent e) {
        switch(e.getKeyCode()) {
            case KeyEvent.VK_RIGHT:
                keyRight = true;
                break;
            case KeyEvent.VK_LEFT:
                keyLeft = true;
                break;
            case KeyEvent.VK_SPACE:
                model.mario.isJumping = true;
                break;
            case KeyEvent.VK_S:
                keySave = true;
                break;
            case KeyEvent.VK_L:
                keyLoad = true;
                break;
            default:
                break;
        }
    }

    public void keyReleased(KeyEvent e) {
        switch(e.getKeyCode()) {
            case KeyEvent.VK_RIGHT:
                keyRight = false;
                break;
            case KeyEvent.VK_LEFT:
                keyLeft = false;
                break;
            case KeyEvent.VK_SPACE:
                model.mario.isJumping = false;
                break;
            case KeyEvent.VK_S:
                keySave = false;
                break;
            case KeyEvent.VK_L:
                keyLoad = false;
                break;
            case KeyEvent.VK_E:
                editEnabled = !editEnabled;
                System.out.println("edit mode: " + editEnabled);
                break;
            default:
                break;
        }
    }

    public void keyTyped(KeyEvent e) {}

    void update() {
        if (keyRight) {
            model.mario.setPos(model.mario.x + 10, model.mario.y);
            model.mario.direction = 1;
            model.checkCollision();
        }

        if (keyLeft) {
            model.mario.setPos(model.mario.x - 10, model.mario.y);
            model.mario.direction = -1;
            model.checkCollision();
        }

        model.mario.setWalking(keyRight || keyLeft);

        if (keySave) {
            model.marshall();
            System.out.println("saved map");
        }

        if (keyLoad) {
            try {
                model.unmarshall();
                System.out.println("loaded map");
            }
            catch (Exception e) {
                e.printStackTrace(System.err);
                System.exit(1);
            }
        }
    }
}
