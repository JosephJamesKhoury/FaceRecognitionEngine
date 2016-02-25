package everteam;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

class DrawPanel extends JPanel {
    private BufferedImage image;
    
    public void setImage (File source) throws MalformedURLException
    {
        try {
            image = ImageIO.read(source);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 3, 4, this);
    }
}