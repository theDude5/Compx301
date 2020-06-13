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
import java.net.Inet4Address;
import java.util.ArrayList;
import java.util.Stack;

public class Counter {
    BufferedImage image;
    ArrayList<BufferedImage> images;
    int[][] data;
    int a_max=0, a_min=0;

    private final int[][] gauss_3 = {{3,5,3}, {5,8,5}, {3,5,3}};
    private final int[][] gauss_5 = {{0,1,2,1,0}, {1,3,5,3,1}, {2,5,9,5,2}, {1,3,5,3,1}, {0,1,2,1,0}};
    private final int[][] laplace_3 = {{-1,-1,-1}, {-1,10,-1}, {-1,-1,-1}};
    private final int[][] laplace_5 = {{0,0,-1,0,0}, {0,-1,-2,-1,0}, {-1,-2,16,-2,-1}, {0,-1,-2,-1,0}, {0,0,-1,0,0}};
    private final int[][] blur = {{1,1,1}, {1,1,1}, {1,1,1}};
    private final int[][] sobel_y = {{1,2,1}, {0,0,0}, {-1,-2,-1}};
    private final int[][] sobel_x = {{1,0,-1}, {2,0,-2}, {1,0,-1}};
    public Counter(String fileName) {
        images = new ArrayList<BufferedImage>();
        try {
            image = ImageIO.read(new File(fileName));
            data = new int[image.getWidth()][image.getHeight()];
            for (int y = 0; y < image.getHeight(); y++) {
                for (int x = 0; x < image.getWidth(); x++) {
                    data[x][y] = image.getRGB(x, y);
                    //a_max = Math.max((data[x][y]>>24)&0xff, a_max);
                    //a_min = Math.max((data[x][y]>>24)&0xff, a_min);
                }
            }
            //Pipeline goes here
            images.add(image);
            process(image,blur);
            process(image,sobel_x);
            process(image,sobel_y);
            //gauss(image);
            //gauss(image);
            output();
        }
        catch (IOException e) { System.err.println(e); }
    }

    private void process(BufferedImage img, int[][] mask) {
        int s = (int) Math.floor(mask.length/2);
        for (int y = s; y < image.getHeight()-s; y++) {
            for (int x = s; x < image.getWidth()-s; x++) {
                apply_filter(x, y, mask);
            }
        } images.add(apply());
    }

    private void apply_filter(int x, int y, int[][] mask){
        int p=0, a=0, r=0, b=0, g=0,n=0, m;
        for (int j = 0; j < mask.length; j++) {
            for (int i = 0; i < mask.length; i++) {
                m = mask[i][j];
                if (m == 0) { continue; }
                p = data[x+i-(int) Math.floor(mask.length/2)][y+j-(int) Math.floor(mask.length/2)];
                a += m*(p>>24)&0xff;
                r += m*(p>>16)&0xff;
                g += m*(p>>8)&0xff;
                b += m*p&0xff;
                n++;
                //n+=Math.abs(m);
            }
        }
        //a=Math.abs(a)/n; r=Math.abs(r)/n; g=Math.abs(g)/n; b=Math.abs(b)/n;
        a =Math.min(255, Math.max(0,a/n)); r =Math.min(255, Math.max(0,r/n)); g=Math.min(255, Math.max(0,g/n)); b=Math.min(255, Math.max(0,b/n));
        data[x][y] = ((a<<24) | (r<<16) | (g<<8) | b);
    }

    private void negative(BufferedImage img) {
        int p,a,r,b,g;
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                p = data[x][y];
                a = (p>>24)&0xff;
                r = (p>>16)&0xff;
                g = (p>>8)&0xff;
                b = p&0xff;

                r = 255 - r;
                g = 255 - g;
                b = 255 - b;
                data[x][y] = (a<<24) | (r<<16) | (g<<8) | b;
            }
        } images.add(apply());
    }

    private BufferedImage apply() {
        BufferedImage img = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
        for (int y = 0; y < img.getHeight(); y++) {
            for (int x = 0; x < img.getWidth(); x++) { img.setRGB(x, y, data[x][y]); }
        } return img;
    }

    private void RegionLabeling(BufferedImage img) {
        BufferedImage binImg = new BufferedImage(img.getWidth(), img.getHeight(),BufferedImage.TYPE_BYTE_GRAY);      //Converts img to Binary img, hopefully
        int label = 2;
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                if(binImg.getRGB(x, y) == 1) {
                    FloodFill(img, x, y, label);
                    label++;
                }
            }
        }
        //Should output a new altered image somehow
    }

    private void FloodFill(BufferedImage img, int x, int y, int label) {
        Point n = new Point(x, y);
        Stack<Point> s = new Stack<>();
        s.push(n);
        while(s.empty() != true) {
            Point p = s.pop();
            if(p.x >= 0 && p.y >= 0 && p.x < img.getWidth() && p.y < img.getHeight() && img.getRGB(x, y) == 1) {
                //p.label = label;        //Not sure how we are supposed to apply the label to the point/coord
                Point p1 = new Point();
                p1.x = x + 1;
                p1.y = y;
                s.push(p1);

                Point p2 = new Point();
                p2.x = x;
                p2.y = y + 1;
                s.push(p2);

                Point p3 = new Point();
                p3.x = x;
                p3.y = y - 1;
                s.push(p3);

                Point p4 = new Point();
                p4.x = x - 1;
                p4.y = y;
                s.push(p4);
            }
        }
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
            args = new String[]{"test.jpg"};
        }
        File file = new File(args[0]);
        if (file.exists()) {new Counter(args[0]);}
    }
}