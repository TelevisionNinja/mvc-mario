::@echo off
javac Game.java View.java Controller.java Model.java Json.java Brick.java Mario.java Coin.java CoinBrick.java Sprite.java
if %errorlevel% neq 0 (
	echo There was an error. Exiting now.	
) else (
	echo Compiled correctly! Running Game...
	java Game	
)
