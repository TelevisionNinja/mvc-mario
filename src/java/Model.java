/* 
 * date: 10/14/2021
 * assignment description:
 * 
 * polymorphism
 */

import java.util.ArrayList;
import java.util.Iterator;

class Model {
    ArrayList<Sprite> sprites;
    Mario mario;

    Model() {
        sprites = new ArrayList<Sprite>();
        mario = new Mario();

        // load map on startup
        try {
            unmarshall();
        }
        catch (Exception e) {
            e.printStackTrace(System.err);
            System.exit(1);
        }
    }

    void marshall() {
        Json saveFile = Json.newObject();

        Json tmpListBricks = Json.newList();
        Json tmpListCoinBLocks = Json.newList();

        for (int i = 0; i < sprites.size(); i++) {
            Sprite b = sprites.get(i);
            if (b.type == "brick") {
                tmpListBricks.add(((Brick) b).marshal());
            }
            else if (b.type == "coinBrick") {
                tmpListCoinBLocks.add(((CoinBrick) b).marshal());
            }
        }

        saveFile.add("bricks", tmpListBricks);
        saveFile.add("coinBricks", tmpListCoinBLocks);
        saveFile.save("map.json");
    }

    void unmarshall() {
        sprites.clear();
        sprites.add(mario);
        Json saveFile = Json.load("map.json");

        Json tmpList = saveFile.get("bricks");
        for (int i = 0; i < tmpList.size(); i++) {
            sprites.add(new Brick(tmpList.get(i)));
        }

        tmpList = saveFile.get("coinBricks");
        for (int i = 0; i < tmpList.size(); i++) {
            sprites.add(new CoinBrick(tmpList.get(i)));
        }
    }

    private boolean isCollision(Mario m, Sprite b) {
        int deltaLeft = (b.x - m.x) - (m.w + m.screenLocation),
            deltaRight = m.screenLocation - (b.w + b.x - m.x),
            deltaTop = b.y - (m.h + m.y),
            deltaBottom = m.y - (b.h + b.y);

        boolean checkLeft = deltaLeft >= 0,
            checkRight = deltaRight >= 0,
            checkTop = deltaTop >= 0,
            checkBottom = deltaBottom >= 0;

        if (checkLeft || checkRight || checkTop || checkBottom) {
            return false;
        }

        return true;
    }

    private void correctPos(Sprite b) {
        if (mario.previousY < mario.y) {
            mario.y = b.y - mario.h;

            mario.vert_vel = 0.0;
            mario.jumpFrame = 0;
        }
        else if (mario.previousY > mario.y) {
            mario.y = b.y + b.h;

            mario.vert_vel = 0.0;
            mario.jumpFrame = mario.jumpAmount;

            // coinblock collision
            if (b.type == "coinBrick") {
                Coin newCoin = ((CoinBrick) b).spawnCoin();
                if (newCoin != null) {
                    sprites.add(newCoin);
                }
            }
        }

        if (mario.previousX < mario.x) {
            mario.x = b.x - (mario.w + mario.screenLocation);
        }
        else if (mario.previousX > mario.x) {
            mario.x = b.w + b.x - mario.screenLocation;
        }
    }

    void checkCollision() {
        for (int i = 0; i < sprites.size(); i++) {
            Sprite b = sprites.get(i);

            // remove coins
            if (b.type == "coin") {
                if (((Coin) b).y > 720) {
                    sprites.remove(i);
                    i--;
                }
            }

            // collision
            else if (isCollision(mario, b)) {
                if (b.type == "brick" || b.type == "coinBrick") {
                    correctPos(b);
                }
            }
        }
    }

    void update() {
        Iterator<Sprite> i = sprites.iterator();
        while (i.hasNext()) {
            i.next().update();
        }

        checkCollision();
    }
}