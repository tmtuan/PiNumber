/*
 * Bitmap File System
 */
import java.awt.BorderLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import javax.imageio.*;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

class BitmapFileHeader {

   byte[] file_type = new byte[2];
   byte[] file_size = new byte[4];
   byte[] reserved_01 = new byte[2]; // Reserved; actual value depends on the application that creates the image
   byte[] reserved_02 = new byte[2]; // Reserved; actual value depends on the application that creates the image
   byte[] offset = new byte[4];  
}

class MyImageViewer {
    public static void main(String[] args) {
        BufferedImage img = null;
        
        try {
            img = ImageIO.read(new File(args[0]));
        } catch (IOException e) {
            System.out.println("IOError: " + e.toString());
        }
        
        ImageIcon imgIcon = new ImageIcon(img);
        JLabel lbl = new JLabel("My Image Viewer");
        lbl.setIcon(imgIcon);
        
        JFrame frame = new JFrame("My Image Viewer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        frame.getContentPane().add(lbl, BorderLayout.CENTER);
        frame.pack();
        frame.setLocationRelativeTo(null);  // centering frame
        frame.setVisible(true);

        try {
            byte[] array = Files.readAllBytes(new File(args[0]).toPath());

            System.out.println("Bitmap file header: ");

            // file type
            System.out.printf("Byte 0, 1 =Ox%02X%02X - %c%c\n", array[0], array[1], (char)array[0], (char)array[1]);

            //file size
            byte[] file_size_array = new byte[] { array[5], array[4], array[3], array[2] }; 
            BigInteger bi_file_size = new BigInteger(file_size_array);

            System.out.printf("Byte 2,3,4,5 = 0x%02X%02X%02X%02X\n", array[5], array[4], array[3], array[2]);
            System.out.println("File size = " + bi_file_size.toString(10) + " bytes");    
            // Reserved 1
            System.out.printf("Byte 6,7 = 0x%02X%02X\n", array[6], array[7]);

            // Reserved 2
            System.out.printf("Byte 8,9 = 0x%02X%02X\n", array[8], array[9]);

            // offset
            System.out.printf("Byte 10,11,12,13 = 0x%02X%02X%02X%02X\n", array[10], array[11], array[12], array[13]);

        } catch (IOException e) {
            System.out.println("IOError: " + e.toString());
        }

        System.out.println("End");
    }
}