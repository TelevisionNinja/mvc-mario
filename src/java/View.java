/* 
 * date: 10/14/2021
 * assignment description:
 * 
 * polymorphism
 */

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.awt.Color;

class View extends JPanel {	
	Model model;
	static BufferedImage background = null,
			ground = null;
	int groundLevel = 596;

	View(Controller c, Model m) {
		model = m;
		c.setView(this);
		
		if (background != null && ground != null) {
			return;
		}

		try {
			background = ImageIO.read(new File("sky.jpg"));
			ground = ImageIO.read(new File("ground.png"));
		}
		catch(Exception e) {
			e.printStackTrace(System.err);
			System.exit(1);
		}
	}
	
	public void paintComponent(Graphics g) {
		// draw background color
		g.setColor(new Color(128, 255, 255));
		g.fillRect(0, 0, this.getWidth(), this.getHeight());

		// draw background image
		g.drawImage(View.background, -model.mario.x/2, 0, View.background.getWidth(), groundLevel, null);

		// draw ground line
		g.setColor(Color.gray);
		g.drawLine(0, groundLevel, 2000, groundLevel);
		
		// draw ground image
		g.drawImage(View.ground, -model.mario.x, groundLevel, View.background.getWidth(), View.ground.getHeight(), null);
		
		// draw sprites
		drawSprites(g);
	}
	
	private void drawSprites(Graphics g) {
		for(int i = 0; i < model.sprites.size(); i++) {
			Sprite b = model.sprites.get(i);
			b.draw(g, model.mario.x);
		}
	}
}
