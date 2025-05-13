import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.*;

public class ImageHoverPixelInfo extends JPanel {
    private BufferedImage image;
    private int mouseX = -1, mouseY = -1;

    public ImageHoverPixelInfo(String imagePath) {
        try {
            image = ImageIO.read(new File(imagePath));
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Mouse motion listener to update mouse coordinates
        addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseMoved(MouseEvent e) {
                mouseX = e.getX();
                mouseY = e.getY();
                repaint(); // Refresh to show updated pixel info
            }
        });

        setPreferredSize(new Dimension(image.getWidth(), image.getHeight()));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, this);

        if (mouseX >= 0 && mouseX < image.getWidth() &&
            mouseY >= 0 && mouseY < image.getHeight()) {
            int pixel = image.getRGB(mouseX, mouseY);
            int r = (pixel >> 16) & 0xff;
            int gVal = (pixel >> 8) & 0xff;
            int b = pixel & 0xff;

            String pixelInfo = String.format("X: %d Y: %d - R: %d G: %d B: %d",
                                             mouseX, mouseY, r, gVal, b);

            // Draw pixel info background & text
            g.setColor(new Color(255, 255, 255, 200));
            g.fillRect(10, 10, 240, 20);
            g.setColor(Color.BLACK);
            g.drawString(pixelInfo, 15, 25);
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Hover to View Pixel Value");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new ImageHoverPixelInfo("path_to_your_image.jpg"));
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}