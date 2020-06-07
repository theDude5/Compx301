import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Counter {

    public Counter(String fileName) {
        try
        {
            BufferedImage picture = ImageIO.read(new File(fileName));
            //Pipeline goes here
            output(picture);
        }
        catch (IOException e)
        {
            String workingDir = System.getProperty("user.dir");
            System.out.println("Current working directory : " + workingDir);
            e.printStackTrace();
        }
    }

    public void output(BufferedImage picture) {
        JFrame editorFrame = new JFrame("Cell Image");
        editorFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        ImageIcon imageIcon = new ImageIcon(picture);
        JLabel jLabel = new JLabel();
        jLabel.setIcon(imageIcon);
        editorFrame.getContentPane().add(jLabel, BorderLayout.CENTER);

        editorFrame.pack();
        editorFrame.setLocationRelativeTo(null);
        editorFrame.setVisible(true);
    }

    public static void main(String [] args) {
        try {
            if (args.length == 0) {
                System.out.println("Usage: java Counter <imagefile.jpg>");
            }
            else {
                String fileName = args[0];
                new Counter(fileName);
            }
        }
        catch (Error e) {
            System.err.println(e);
        }
    }
}
