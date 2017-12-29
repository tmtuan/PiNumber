/*
 * Find Bitmap's occurence in Pi number
 */
import java.io.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

class PiNumber {

    private BigInteger decimalDigit;
    /*
     * Contruct PiNumber object from text file containing pi number
     */ 
    public PiNumber(String filename) throws IOException {
        FileInputStream in = null;

        try {
            in = new FileInputStream(filename);

            if (in != null) {
                decimalDigit = new BigInteger("0");
                BigInteger base = new BigInteger("10");
                int c;
                while( (c = in.read()) != -1 ) {                
                    decimalDigit = decimalDigit.multiply(base);
                    decimalDigit = decimalDigit.add(new BigInteger(String.valueOf(c-48))); 
                }
            }
        } catch (IOException e) {
            System.out.print("IO Error: " + e.toString());
        } catch (Exception e) {
            System.out.print("IO Error: " + e.toString());
        } finally {
            if (in != null) {
                in.close();
            }
        }
    }

    public String piToString() {
        return "3." + decimalDigit.toString();
    }
    /*
     * Find the nth number of pi
     * arguments: nth number (starting from 1), text file containing series of digits after the decimal point of pi number
     */
    public static int digitAt(int nth, String pinumber) throws IOException {
        int i = -1; // digits not found because of being out of range

        FileInputStream input = null;
        try {
            input = new FileInputStream(pinumber);
            BigInteger bd = new BigInteger("0");
            BigInteger base = new BigInteger("10");
            int c;
            int index = 1;
            while ((c = input.read()) != -1) {
                if (index == nth) {
                    i = Integer.parseInt(String.valueOf(c-48));
                    break;
                }
                index++;
            }
        }  catch (IOException e) {
            System.out.println("ERROR: " + e.toString());
        } catch (Exception e) {
            System.out.println("ERROR: " + e.toString());
        }
        finally {
           if (input != null) {
               input.close();
           }
       }
        return i;
    }

    /*
     * Find the series of digits from n-th to m-th after the decimal point of Pi
     */
    public static BigInteger digitRange(int n_th, int m_th, String pinumber ) throws IOException {
       
        FileInputStream input = null;
        BigInteger bd = new BigInteger("0");
        BigInteger base = new BigInteger("10");
        
        try {
            input = new FileInputStream(pinumber);
            
            int c;
            int index = 1;
            while((c = input.read()) != -1 ) {
                if (index == n_th) {
                    bd = bd.multiply(base);
                    bd = bd.add(new BigInteger(String.valueOf(c-48)));
                    break;
                }
                index++;
            } 
            while ((c = input.read()) != -1) {
                if(index < m_th) {
                    bd = bd.multiply(base);
                    bd = bd.add(new BigInteger(String.valueOf(c-48))); 
                    index++;
                } else {
                    break;
                }
                
            }
        }  catch (IOException e) {
            System.out.println("ERROR: " + e.toString());
        } catch (Exception e) {
            System.out.println("ERROR: " + e.toString());
        }
        finally {
           if (input != null) {
               input.close();
           }
       }
       return bd;
    }
}

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
             image_array = Files.readAllBytes(new File(filename).toPath());
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

 class BitmapOccurrencePi {
     public static void main(String[] args) throws IOException {
        PiNumber pi = new PiNumber(args[0]);
        TMTBitmapFile bmp = new TMTBitmapFile(args[1]);

        if (pi != null && bmp != null) {
            String pi_str =pi.piToString();
            System.out.println("Pi number: " + pi_str);

            String bmp_str = bmp.getImageData();
            System.out.println("Bitmap data: " + bmp_str);
            int i = pi_str.indexOf(bmp_str);
            System.out.println("Bmp occurs in pi at: " + i);
        }
     }
 }