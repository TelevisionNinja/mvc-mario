#!/bin/bash
set -u -e
javac Game.java View.java Controller.java Model.java Json.java Brick.java Mario.java Coin.java CoinBrick.java Sprite.java
java Game
