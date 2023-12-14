package add;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;

/**
 * Hilfsklasse für das Standardicon, sofern kein eigenes Bild für einen Kontakt hinterlegt wurde
 *
 * Entwickler: Jan Schwenger
 */
public class MyIcon implements Icon {

   // private String imgPath = "";
    private ImageIcon img = null;
    public MyIcon() {
    }
    public MyIcon(String imgPath){
        //this.imgPath = imgPath;
        try {
            MyIcon icon_object = new MyIcon();
            // Read the selected image file
            BufferedImage image = ImageIO.read(new File(imgPath));
            // Resize the image to fit the label
            Image scaledImage = image.getScaledInstance(icon_object.getIconWidth(), icon_object.getIconHeight(), Image.SCALE_SMOOTH);
            // Update the image label with the scaled image
            this.img = new ImageIcon(scaledImage);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public int getIconHeight() {
        return 54;
    }

    public int getIconWidth() {
        return 54;
    }

    public void paintIcon(Component c, Graphics g, int x, int y) {
        g.setColor(Color.DARK_GRAY);
        g.fillOval(42 / 2, 64 / 3, 30, 30);
        g.setColor(Color.DARK_GRAY);
        g.fillRect(15, 44, 44, 18);
    }

    public ImageIcon getImg() {
        return img;
    }

    public void setImg(ImageIcon img) {
        this.img = img;
    }
}