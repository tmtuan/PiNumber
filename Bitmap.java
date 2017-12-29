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
import java.util.BitSet;

class TMTBitmapFile {

   private byte[] image_array; 
   private byte[] file_type = new byte[2];
   private byte[] file_size = new byte[4];
   private byte[] reserved_01 = new byte[2]; // Reserved; actual value depends on the application that creates the image
   private byte[] reserved_02 = new byte[2]; // Reserved; actual value depends on the application that creates the image
   private byte[] offset = new byte[4];  

   /*
     * Construct Bitmap File Header from bitmap file
     */
    public TMTBitmapFile(String filename) {
        try {
            image_array =  Files.readAllBytes(new File(filename).toPath());
            // file type
            file_type = new byte[] { image_array[0], image_array[1] };
            
            // file size
            file_size = new byte[] { image_array[5], image_array[4], image_array[3], image_array[2] };

            // reserved 1
            reserved_01 = new byte[] { image_array[6], image_array[7] };

             // reserved 2
             reserved_01 = new byte[] { image_array[8], image_array[9] };

             // offset
             offset = new byte[] { image_array[13], image_array[12], image_array[11], image_array[10] };

        }  catch (IOException e) {
            System.out.println(e.toString());
        }
    }

    public Integer getFileSize() {
        BigInteger bi_size = new BigInteger(this.file_size);
        Integer size = new Integer(bi_size.toString(10));
        return size;
    }

    public Integer getOffset() {
        BigInteger bi_offset = new BigInteger(this.offset);
        Integer offset = new Integer(bi_offset.toString(10));
        return offset;
    }

    public void printFileType() {
        System.out.printf("File Type = %c%c\n", (char)file_type[0], (char)file_type[1]);
    }
    public void printOffsetInHex() {
        System.out.printf("Offset = 0x%02X%02X%02X%02X\n", offset[0], offset[1], offset[2], offset[3]);
    }

    // get string representing Image data in decimal digits
    public String getImageData() {
        BigInteger imageDecimal = new BigInteger("0");
        BigInteger base = new BigInteger("10");

        Integer offset = getOffset();
        Integer size = getFileSize();
        for(int i = offset; i < size; i++) {
            imageDecimal = imageDecimal.multiply(base);
            imageDecimal = imageDecimal.add(BigInteger.valueOf(image_array[i]));
        }
        return imageDecimal.toString();
    }
}

class MyImageViewer {
    public static void main(String[] args) {
       
        TMTBitmapFile bmp = new TMTBitmapFile(args[0]);

        if (bmp != null) {
            bmp.printFileType();
            System.out.print("file size: " + bmp.getFileSize().toString() + "\n");
            System.out.print("Bitmap data in decimal number: " + bmp.getImageData() + "\n");
        }
        System.out.println("End");
    }    
}