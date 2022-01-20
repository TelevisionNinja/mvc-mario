'''

date: 12/9/2021
assignment description:

recreate the java and javascript Mario in python with pygame

'''

import pygame
from abc import ABC, abstractmethod
import random
import sys

class Sprite(ABC):
    def __init__(self, x = 0, y = 0, w = 0, h = 0):
        self.x = x
        self.y = y
        self.w = w
        self.h = h
        self.type = ""

    @abstractmethod
    def update(self):
        pass

    @abstractmethod
    def draw(self, g, offset):
        pass

class Brick(Sprite):
    def __init__(self, x = 0, y = 0, w = 0, h = 0):
        super().__init__(x, y, w, h)
        self.type = "brick"

        self.image = None
        if self.image == None:
            self.loadImage()

    def loadImage(self):
        # load images
        try:
            self.image = pygame.image.load("brick.png")
            self.image = pygame.transform.scale(self.image, (self.w, self.h))
        except Exception:
            print(Exception, "brick loading error")

    # Marshals this object into a JSON DOM
    # marshal(self):
    # 	obj =:
    # 		x:self.x,
    # 		y:self.y,
    # 		w:self.w,
    # 		h:self.h
    # 	return obj

    def draw(self, g, offset):
        g.blit(self.image, (self.x - offset, self.y))

    def update(self):
        pass

class Mario(Sprite):
    def __init__(self):
        super().__init__()
        self.type = "mario"
        self.previousX = 0
        self.previousY = 0
        self.direction = 1
        self.currentMarioFrameIndex = 0
        self.screenLocation = 0
        self.vert_vel = 0
        self.images = []
        self.isWalking = False
        self.isJumping = False
        self.jumpFrame = 0
        self.jumpDuration = 20
        self.jumpAmount = 20

        # load mario images
        if len(self.images) == 0:
            for i in range(1, 6):
                self.images.append(self._loadImage("mario" + str(i) + ".png"))

        self.w = self.images[0].get_rect().width
        self.h = self.images[0].get_rect().height

    def _loadImage(self, fileName):
        try:
            return pygame.image.load(fileName)
        except Exception:
            print(Exception, "mario loading error")

        return None

    def setWalking(self, b):
        self.isWalking = b

    def setPos(self, x, y):
        self.previousX = self.x
        self.previousY = self.y

        self.x = x
        self.y = y

    def update(self):
        # update mario image
        if self.isWalking:
            self.currentMarioFrameIndex = (self.currentMarioFrameIndex + 1) % len(self.images)

        # gravity
        self.vert_vel += 1.5
        self.setPos(self.x, int(self.y + self.vert_vel)) # convert to int

        # snap back to the ground
        if self.y > 500:
            self.y = 500
            self.previousY = 500
            self.vert_vel = 0

        # jump
        if self.isJumping:
            self.jumpFrame += 1

            if self.jumpFrame < self.jumpDuration:
                self.vert_vel = 0
                self.setPos(self.x, self.previousY - self.jumpAmount)
            elif self.vert_vel == 0:
                self.jumpFrame = 0
        else:
            if self.vert_vel == 0:
                self.jumpFrame = 0
            else:
                self.jumpFrame = self.jumpDuration

    def draw(self, g, offset):
        if self.direction < 0:
            frame = self.images[self.currentMarioFrameIndex]
            flipped = pygame.transform.flip(frame, True, False)
            g.blit(flipped, (self.screenLocation, self.y))

        else:
            g.blit(self.images[self.currentMarioFrameIndex], (self.screenLocation, self.y))

class Coin(Sprite):
    def __init__(self, x, y):
        super().__init__(x,y)
        self.type = "coin"

        self.image = None
        self.vertical = 0

        if self.image == None:
            try:
                self.image = pygame.image.load("coin.png")
                self.w = self.image.get_rect().width
                self.h = self.image.get_rect().height
                self.y -= self.h

            except Exception:
                print(Exception, "coin loading error")

        # generate random direction
        randDirection = random.randint(0,1)

        if randDirection == 0:
            randDirection = -1

        self.horizontal = randDirection * random.uniform(1, 13)

    def update(self):
        self.x += self.horizontal

        # gravity
        self.y += self.vertical
        self.vertical += 1.2

    def draw(self, g, offset):
        g.blit(self.image, (self.x - offset, self.y))

class CoinBrick(Brick):
    def __init__(self, x, y):
        super().__init__(x, y)
        self.type = "coinBrick"

        self.hit = False
        self.coinsLeft = 5
        self.imageIndex = 0
        self.images = []

        self._loadImages()

        self.w = self.images[0].get_rect().width
        self.h = self.images[0].get_rect().height

    def _loadImage(self, fileName):
        try:
            return pygame.image.load(fileName)
        except Exception:
            print(Exception, "coin brick laoding error")

        return None

    def _loadImages(self):
        if not len(self.images):
            for i in range(1,3):
                self.images.append(self._loadImage("block" + str(i) + ".png"))

    def spawnCoin(self):
        if self.coinsLeft > 0:
            self.coinsLeft -= 1

            if self.coinsLeft == 0:
                self.imageIndex = 1

            return Coin(self.x, self.y)

        return None

    def update(self):
        pass

    def draw(self, g, offset):
        g.blit(self.images[self.imageIndex], (self.x - offset, self.y))

    # Marshals this object into a JSON DOM
    # marshal(self):
    # 	obj =:
    # 		x: self.x,
    # 		y: self.y
    #

    # 	return obj
    #

class Model:
    def __init__(self):
        self.sprites = []
        self.mario = Mario()

        # load map on startup
        try:
            self.unmarshall()
        except Exception:
            print(Exception, "map loading error")

    # marshall(self):
    # 	saveFile = Json.newObject()

    # 	tmpListBricks = Json.newList()
    # 	tmpListCoinBLocks = Json.newList()

    # 	for (i = 0 i < self.sprites.size() i += 1):
    # 		b = self.sprites.get(i)
    # 		if b.type == "brick":
    # 			tmpListBricks.add(b).marshal()
    #
    # 		elif b.type == "coinBrick":
    # 			tmpListCoinBLocks.add(b).marshal()

    # 	saveFile.add("bricks", tmpListBricks)
    # 	saveFile.add("coinBricks", tmpListCoinBLocks)
    # 	saveFile.save("map.json")
    #

    def unmarshall(self):
        # self.sprites = []
        # self.sprites.append(self.mario)
        # saveFile = Json.load("map.json")

        # tmpList = saveFile.get("bricks")
        # for (i = 0 i < tmpList.size() i += 1):
        # 	self.sprites.append(Brick(tmpList.get(i)))
        #

        # tmpList = saveFile.get("coinBricks")
        # for (i = 0 i < tmpList.size() i += 1):
        # 	self.sprites.append(CoinBrick(tmpList.get(i)))
        #

        # hardcoded bricks

        self.sprites = []
        self.sprites.append(self.mario)

        self.sprites.append(Brick(42,337,425,46))
        self.sprites.append(Brick(126,414,148,234))
        self.sprites.append(Brick(737,480,99,130))
        self.sprites.append(Brick(860,270,124,110))
        self.sprites.append(Brick(105,243,146,141))

        self.sprites.append(CoinBrick(280,35))
        self.sprites.append(CoinBrick(1100,250))
        self.sprites.append(CoinBrick(1000,500))

    def _isCollision(self, m, b):
        deltaLeft = (b.x - m.x) - (m.w + m.screenLocation)
        deltaRight = m.screenLocation - (b.w + b.x - m.x)
        deltaTop = b.y - (m.h + m.y)
        deltaBottom = m.y - (b.h + b.y)

        checkLeft = deltaLeft >= 0
        checkRight = deltaRight >= 0
        checkTop = deltaTop >= 0
        checkBottom = deltaBottom >= 0

        return not (checkLeft or checkRight or checkTop or checkBottom)

    def _correctPos(self, b):
        if self.mario.previousY < self.mario.y:
            self.mario.y = b.y - self.mario.h

            self.mario.vert_vel = 0.0
            self.mario.jumpFrame = 0

        elif self.mario.previousY > self.mario.y:
            self.mario.y = b.y + b.h

            self.mario.vert_vel = 0.0
            self.mario.jumpFrame = self.mario.jumpAmount

            # coinblock collision
            if b.type == "coinBrick":
                newCoin = b.spawnCoin()
                if newCoin != None:
                    self.sprites.append(newCoin)

        if self.mario.previousX < self.mario.x:
            self.mario.x = b.x - (self.mario.w + self.mario.screenLocation)
        elif self.mario.previousX > self.mario.x:
            self.mario.x = b.w + b.x - self.mario.screenLocation

    def checkCollision(self):
        i = 0
        while i < len(self.sprites):
            b = self.sprites[i]

            # remove coins
            if b.type == "coin":
                if b.y > 720:
                    del self.sprites[i]
                    i -= 1
            # collision
            elif self._isCollision(self.mario, b):
                if b.type == "brick" or b.type == "coinBrick":
                    self._correctPos(b)

            i += 1

    def update(self):
        for s in self.sprites:
            s.update()

        self.checkCollision()

class View:
    def __init__(self, c, m, w, h):
        self.model = m
        self.background = None
        self.ground = None
        self.groundLevel = 596
        self.width = w
        self.height = h
        self.screen = pygame.display.set_mode((self.width, self.height))

        c.setView(self)

        if self.background != None and self.ground != None:
            return

        try:
            self.background = pygame.image.load("sky.jpg")
            self.ground = pygame.image.load("ground.png")

            self.background = pygame.transform.scale(self.background, (self.background.get_rect().width, self.groundLevel))
            self.ground = pygame.transform.scale(self.ground, (self.background.get_rect().width, self.ground.get_rect().height))
        except Exception:
            print(Exception, "backgroun error")

    def update(self):
        # draw background color
        self.screen.fill([0,255,255])

        # draw background image
        self.screen.blit(self.background, (-self.model.mario.x//2, 0))

        # draw ground line
        pygame.draw.line(self.screen, (0,0,0), (0, self.groundLevel), (2000, self.groundLevel))

        # draw ground image
        self.screen.blit(self.ground, (-self.model.mario.x, self.groundLevel))

        # draw sprites
        self.drawSprites(self.screen)

        pygame.display.flip()

    def drawSprites(self, g):
        for b in self.model.sprites:
            b.draw(g, self.model.mario.x)

class Controller:
    def __init__(self, m):
        self.model = m
        self.view = None
        self.keyLeft = False
        self.keyRight = False
        self.keySave = False
        self.keyLoad = False
        self.editEnabled = False

    def setView(self, v):
        self.view = v

    #-------------------------------------------
    # mouse functionality

    def _mousePressed(self):
        if not self.editEnabled:
            return

        mouseX, mouseY = pygame.mouse.get_pos()

        self.model.sprites.append(Brick(mouseX + self.model.mario.x, mouseY, 0, 0))

    def _mouseReleased(self):
        if not self.editEnabled:
            return

        b = self.model.sprites[-1]

        mouseX, mouseY = pygame.mouse.get_pos()

        b.w = mouseX - b.x + self.model.mario.x
        b.h = mouseY - b.y
        b.loadImage()

    #-------------------------------------------
    # keyboard functionality

    def _keyPressed(self):
        # match k:
        #     case pygame.K_RIGHT:
        #         self.keyRight = True
        #     case pygame.K_LEFT:
        #         self.keyLeft = True
        #     case pygame.K_SPACE:
        #         self.model.mario.isJumping = True
        #     # case pygame.K_s:
        #     # 	self.keySave = True
        #     case pygame.K_l:
        #         self.keyLoad = True

        keys = pygame.key.get_pressed()

        if keys[pygame.K_RIGHT]:
            self.keyRight = True
        else:
            self.keyRight = False
        if keys[pygame.K_LEFT]:
            self.keyLeft = True
        else:
            self.keyLeft = False
        if keys[pygame.K_SPACE]:
            self.model.mario.isJumping = True
        else:
            self.model.mario.isJumping = False
        # if keys[pygame.K_s]:
        # 	self.keySave = True
        if keys[pygame.K_l]:
            self.keyLoad = True
        else:
            self.keyLoad = False
        if keys[pygame.K_e]:
            self.editEnabled = not self.editEnabled
            print("edit mode: " + str(self.editEnabled))

    def _keyReleased(self):
        # match k:
        #     case pygame.K_RIGHT:
        #         self.keyRight = False
        #     case pygame.K_LEFT:
        #         self.keyLeft = False
        #     case pygame.K_SPACE:
        #         self.model.mario.isJumping = False
        #     # case pygame.K_s:
        #     # 	self.keySave = False
        #     case pygame.K_l:
        #         self.keyLoad = False
        #     case pygame.K_e:
        #         self.editEnabled = not self.editEnabled
        #         print("edit mode: " + str(self.editEnabled))

        self._keyPressed()

    def update(self):
        for event in pygame.event.get():
            match event.type:
                case pygame.QUIT:
                    sys.exit()
                case pygame.KEYDOWN:
                    self._keyPressed()
                case pygame.KEYUP:
                    self._keyReleased()
                case pygame.MOUSEBUTTONUP:
                    self._mouseReleased()
                case pygame.MOUSEBUTTONDOWN:
                    self._mousePressed()

        if self.keyRight:
            self.model.mario.setPos(self.model.mario.x + 10, self.model.mario.y)
            self.model.mario.direction = 1
            self.model.checkCollision()

        if self.keyLeft:
            self.model.mario.setPos(self.model.mario.x - 10, self.model.mario.y)
            self.model.mario.direction = -1
            self.model.checkCollision()

        self.model.mario.setWalking(self.keyRight or self.keyLeft)

        # if self.keySave:
        # 	self.model.marshall()
        # 	print("saved map")

        if self.keyLoad:
            try:
                self.model.unmarshall()
                print("loaded map")
            except Exception:
                print(Exception, "manual map loading error")

class Game:
    # initialize game object
    def __init__(self):
        width = 1280
        height = 720

        self.model = Model()
        self.model.mario.screenLocation = (width - self.model.mario.w)//2
        self.controller = Controller(self.model)
        self.view = View(self.controller, self.model, width, height)

    def update(self):
        self.controller.update()
        self.model.update() # update model every frame
        self.view.update()

def main():
    game = Game()
    clock = pygame.time.Clock()

    # 25 fps
    while True:
        game.update()
        clock.tick(25)

if __name__ == "__main__":
    main()
