import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class Counter {
    BufferedImage image;
    List images;
    public Counter(String fileName) {
        try {
            //BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
            image = ImageIO.read(new File(fileName));
            int[][] data = new int[image.getWidth()][image.getHeight()];
            for (int y = 0; y < image.getHeight(); y++) {
                for (int x = 0; x < image.getWidth(); x++) {
                    data[x][y] = image.getRGB(x, y);
                }
            }
            //Pipeline goes here

            output(image);
            output(negative(image));
        }
        catch (IOException e) {
            String workingDir = System.getProperty("user.dir");
            System.out.println("Current working directory : " + workingDir);
            //e.printStackTrace();
        }
    }

    private BufferedImage negative(BufferedImage img) {
        int p,a,r,b,g;
        for (int i = 0; i < image.getHeight(); i++) {
            for (int j = 0; j < image.getWidth(); j++) {
                p = image.getRGB(j,i); 
                a = (p>>24)&0xff; 
                r = (p>>16)&0xff; 
                g = (p>>8)&0xff; 
                b = p&0xff; 
                
                //subtract RGB from 255 
                r = 255 - r; 
                g = 255 - g; 
                b = 255 - b;

                //set new RGB value 
                p = (a<<24) | (r<<16) | (g<<8) | b; 
                image.setRGB(j, i, p); 
            }
        }
        return img;
    }
    public void output(BufferedImage image) {
        JFrame frame = new JFrame("Cell Image");
        JPanel panel = new JPanel();
        JLabel label = new JLabel();
        ImageIcon img = new ImageIcon(image);
        label.setIcon(img);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));
        panel.setLayout(new GridLayout(0,1));
        panel.add(label);
        frame.add(panel, BorderLayout.CENTER);

        frame.pack();
        frame.setTitle("Counter");
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    public static void main(String [] args) {
        if (args.length == 0) {
            System.out.println("Usage: java Counter <imagefile.jpg>");
            args = new String[]{"45762.jpg"};
        }
        File file = new File(args[0]);
        if (file.exists()) {new Counter(args[0]);}
    }
}
