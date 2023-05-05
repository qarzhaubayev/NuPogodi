import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

public class Main {
    private static boolean running = true;
    private static Thread gameThread;

    private static JLabel wolfBody_left;
    private static JLabel wolfBody_right;
    private static JLabel wolfArms;
    private static JLabel position, armPosition;
    public static boolean left;
    private static ImageIcon imgWolf = new ImageIcon("wolf.png");
    private static ImageIcon imgArms = new ImageIcon("arms.png");
    private static ImageIcon img = new ImageIcon("background.jpg");

    public static Image flipImage(Image image) {
        BufferedImage bufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = bufferedImage.createGraphics();
        g2d.drawImage(image, 0, 0, null);
        g2d.dispose();

        AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
        tx.translate(-bufferedImage.getWidth(null), 0);
        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);

        bufferedImage = op.filter(bufferedImage, null);

        return bufferedImage;
    }
    public static void startGameThread() {
        running = true;

        gameThread = new Thread(() -> {
            while (running) {
                updateGameState();
                render();

                try {
                    Thread.sleep(1000 / 60);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        gameThread.start();
    }

    public static void stopGameThread() {
        running = false;

        try {
            gameThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void updateGameState() {
        // Update the game state here
    }

    public static void render() {
        // Get the current position of the wolf
        int x = position.getX();
        int y = position.getY();

        // Get the current image of the wolf
        ImageIcon currentImage = (ImageIcon) (left ? wolfBody_left.getIcon() : wolfBody_right.getIcon());
        Image image = currentImage.getImage();

        // Create a BufferedImage from the image
        BufferedImage bufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = bufferedImage.createGraphics();
        g2d.drawImage(image, 0, 0, null);
        g2d.dispose();

        // Create an AffineTransform to flip the image horizontally
        AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
        tx.translate(-bufferedImage.getWidth(null), 0);
        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);

        // Flip the image horizontally
        bufferedImage = op.filter(bufferedImage, null);

        // Create a new ImageIcon from the flipped image
        ImageIcon flippedImageIcon = new ImageIcon(bufferedImage);

        // Update the image of the wolf based on the direction
        if (left) {
            wolfBody_left.setIcon(flippedImageIcon);
            wolfBody_left.setBounds(x + 150, y, -150, 200);
            position = wolfBody_left;
            wolfArms_left.setIcon()

        } else {
            wolfBody_right.setIcon(flippedImageIcon);
            wolfBody_right.setBounds(x - 150, y, 150, 200);
            position = wolfBody_right;
        }
    }

    public static void turnLeft(JLabel wolfLabel) {
        // Get the current wolf image
        ImageIcon wolfIcon = (ImageIcon) wolfLabel.getIcon();
        Image wolfImage = wolfIcon.getImage();

        // Create a mirrored transformation
        AffineTransform transform = new AffineTransform();
        transform.scale(-1, 1);

        // Apply the transformation to the wolf image
        BufferedImage flippedWolfImage = new BufferedImage(
                wolfImage.getWidth(null),
                wolfImage.getHeight(null),
                BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = flippedWolfImage.createGraphics();
        g.setTransform(transform);
        g.drawImage(wolfImage, -wolfImage.getWidth(null), 0, null);
        g.dispose();

        // Update the wolf label with the flipped image
        wolfLabel.setIcon(new ImageIcon(flippedWolfImage));
    }

    public static void turnRight(JLabel wolfLabel) {
        // Get the current wolf image
        ImageIcon wolfIcon = (ImageIcon) wolfLabel.getIcon();
        Image wolfImage = wolfIcon.getImage();

        // Create a mirrored transformation
        AffineTransform transform = new AffineTransform();
        transform.translate(wolfImage.getWidth(null), 0);
        transform.scale(-1, 1);

        // Apply the transformation to the wolf image
        BufferedImage flippedWolfImage = new BufferedImage(
                wolfImage.getWidth(null),
                wolfImage.getHeight(null),
                BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = flippedWolfImage.createGraphics();
        g.setTransform(transform);
        g.drawImage(wolfImage, 0, 0, null);
        g.dispose();

        // Update the wolf label with the flipped image
        wolfLabel.setIcon(new ImageIcon(flippedWolfImage));
    }

    public static void handsUp(JLabel wolfArms) {
        // Get the current image of the wolf arms
        ImageIcon currentImage = (ImageIcon) wolfArms.getIcon();
        Image image = currentImage.getImage();

        // Create a BufferedImage from the image
        BufferedImage bufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = bufferedImage.createGraphics();
        g2d.drawImage(image, 0, 0, null);
        g2d.dispose();

        // Create an AffineTransform to rotate the image
        AffineTransform tx = AffineTransform.getRotateInstance(Math.toRadians(-90), bufferedImage.getWidth() / 2, bufferedImage.getHeight() / 2);
        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);

        // Rotate the image
        bufferedImage = op.filter(bufferedImage, null);

        // Create a new ImageIcon from the rotated image
        ImageIcon rotatedImageIcon = new ImageIcon(bufferedImage);

        // Update the image of the wolf arms
        wolfArms.setIcon(rotatedImageIcon);
    }

    public static void main(String[] args) {
        startGameThread();
        Random random = new Random(System.currentTimeMillis());
        JFrame frame = new JFrame("Nu Pogodi");
        frame.setSize(1000, 500);
        frame.setLayout(null);

        JLabel background = new JLabel(img);
        background.setBounds(0, 0, 1000, 500);

        wolfBody_left = new JLabel(imgWolf);
        wolfBody_right = new JLabel(new ImageIcon(flipImage(imgWolf.getImage())));

        wolfArms = new JLabel(imgArms);
        position = wolfBody_left;
        wolfBody_left.setBounds(420, 170, 150, 200);
        wolfBody_right.setBounds(420, 170, 150, 200);


        frame.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
                public void keyPressed(KeyEvent e) {
                    if(e.getKeyCode() == KeyEvent.VK_LEFT){
                        turnLeft(position);
                    }
                    if(e.getKeyCode() == KeyEvent.VK_RIGHT){
                        turnRight(position);
                    }
                if(e.getKeyCode() == KeyEvent.VK_UP){
                    handsUp();
                }
                if(e.getKeyCode() == KeyEvent.VK_DOWN){
                    handsDown();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });

        JLayeredPane layers = new JLayeredPane();
        layers.setBounds(0, 0, 1000, 500);
        layers.add(background, new Integer(0));
        layers.add(position, new Integer(1));

        frame.setLayeredPane(layers);


        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }
}