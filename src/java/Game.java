/* 
 * date: 10/14/2021
 * assignment description:
 * 
 * polymorphism
 */

import javax.swing.JFrame;
import java.awt.Toolkit;

public class Game extends JFrame {
    Model model;
    Controller controller;
    View view;
    final int width = 1280,
        height = 720;

    // initialize game object
    public Game() {
        model = new Model();
        model.mario.screenLocation = (width - model.mario.w)/2;
        controller = new Controller(model);
        view = new View(controller, model);
        view.addMouseListener(controller);
        this.addKeyListener(controller);
        this.setTitle("Assignment5");
        this.setSize(width, height);
        this.setFocusable(true);
        this.getContentPane().add(view);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }

    public void run() {
        while(true) {
            controller.update();
            model.update(); // update model every frame
            view.repaint(); // Indirectly calls View.paintComponent
            Toolkit.getDefaultToolkit().sync(); // Updates screen

            // Go to sleep for 40 milliseconds
            try {
                Thread.sleep(40); // 25 fps
            }
            catch(Exception e) {
                e.printStackTrace();
                System.exit(1);
            }
        }
    }

    // run game object
    public static void main(String[] args) {
        Game g = new Game();
        g.run();
    }
}
