#!/bin/bash
set -u -e
javac Game.java View.java Controller.java Model.java Json.java Brick.java Mario.java
java Game
