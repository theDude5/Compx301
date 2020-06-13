import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Counter {
    BufferedImage image;
    ArrayList<BufferedImage> images;
    private final int[][] gauss_3 = {{3,5,3}, {5,8,5}, {3,5,3}};
    private final int[][] gauss_5 = {{0,1,2,1,0}, {1,3,5,3,1}, {2,5,9,5,2}, {1,3,5,3,1}, {0,1,2,1,0}};
    private final int[][] laplace_3 = {{-1,-1,-1}, {-1,8,-1}, {-1,-1,-1}};
    private final int[][] laplace_5 = {{0,0,-1,0,0}, {0,-1,-2,-1,0}, {-1,-2,16,-2,-1}, {0,-1,-2,-1,0}, {0,0,-1,0,0}};
    private final int[][] box = {{1,1,1}, {1,1,1}, {1,1,1}};
    private final int[][] sobel_y = {{1,2,1}, {0,0,0}, {-1,-2,-1}};
    private final int[][] sobel_x = {{1,0,-1}, {2,0,-2}, {1,0,-1}};
    private final int[][] sobely = {{1,2,1}, {2,0,-2}, {-1,-2,-1}};
    
    public Counter(String fileName) {
        images = new ArrayList<BufferedImage>();
        try {
            image = ImageIO.read(new File(fileName)); 

            //Pipeline goes here
            images.add(image);
            //grayscale(image);
            //image = negative();
            image = process(box);
            image = process(gauss_3);
            image = process(laplace_3);
            process(sobely);
            output();
        }
        catch (IOException e) { System.err.println(e); }
    }
    
    private BufferedImage process(int[][] mask) {
        int s = mask.length/2;
        BufferedImage img = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
        for (int y = s; y < image.getHeight()-s; y++) {
            for (int x = s; x < image.getWidth()-s; x++) {
                //apply_filter(x, y, mask);
                img.setRGB(x, y, apply_filter(x, y, mask));
            }
        } images.add(img);
        return img;
    }

    private int apply_filter(int x, int y, int[][] mask){
        int p=0, a=0, r=0, b=0, g=0,n=0, m; 
        for (int j = 0; j < mask.length; j++) {
           for (int i = 0; i < mask.length; i++) {
                m = mask[i][j];
                p = image.getRGB(x+i-mask.length/2, y+j-mask.length/2);
                a += m*((p>>24)&0xff);
                r += m*((p>>16)&0xff);
                g += m*((p>>8)&0xff);
                b += m*(p&0xff);
                n+=m;
            }
        }
        if (n != 0) { a/=n; r/=n; g/=n; b/=n; }
        a=Math.min(255, Math.max(a,0)); r=Math.min(255, Math.max(r,0)); g=Math.min(255, Math.max(g,0)); b=Math.min(255, Math.max(b,0));
        return ((a<<24) | (r<<16) | (g<<8) | b);
    }

    private BufferedImage negative() {
        BufferedImage img = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
        int p,a,r,b,g;
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {        
                p = image.getRGB(x, y);
                a = (p>>24)&0xff; 
                r = (p>>16)&0xff; 
                g = (p>>8)&0xff; 
                b = p&0xff;
                
                r = 255 - r; 
                g = 255 - g; 
                b = 255 - b;
                img.setRGB(x, y, (a<<24) | (r<<16) | (g<<8) | b);
            }
        } images.add(img);
        return img;
    }

    private BufferedImage grayscale() {
        BufferedImage img = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
        int p,a,r,b,g;
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {        
                p = image.getRGB(x, y);
                a = (p>>24)&0xff; 
                r = (p>>16)&0xff; 
                g = (p>>8)&0xff; 
                b = p&0xff;
                p = (a+r+b+g)/4;
                img.setRGB(x, y, (a<<24) | (r<<16) | (g<<8) | b);
            }
        } images.add(img);
        return img;
    }
    
    private BufferedImage clone(BufferedImage img) {
        BufferedImage im = new BufferedImage(img.getWidth(), img.getHeight(), img.getType());
        for (int y = 0; y < img.getHeight(); y++) {
            for (int x = 0; x < img.getWidth(); x++) { im.setRGB(x, y, img.getRGB(x, y)); }
        } return im;
    }

    public void output() {
        JFrame frame = new JFrame("Cell Image");
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        panel.setLayout(new GridLayout(images.size(),1,10,10));        
        for (BufferedImage item : images) { panel.add(new JLabel( new ImageIcon(item))); }
        frame.add(new JScrollPane(panel), BorderLayout.CENTER);
        frame.setBounds(0, 0, image.getWidth()+100, image.getHeight()+75);
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
