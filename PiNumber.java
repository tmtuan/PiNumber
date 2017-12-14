import java.io.*;
import java.math.BigDecimal;
import java.math.BigInteger;

class PiNumber {
    public static void main(String[] args) throws IOException {
        FileInputStream in = null;
        
        try {
            in = new FileInputStream("pi-billion.txt");
            BigInteger bd = new BigInteger("0");
            BigInteger base = new BigInteger("10");
            int c;

            System.out.print("Reading pi number from file....\n");
            c = in.read();
            bd = bd.add(new BigInteger(String.valueOf(c-48)));
            
            while( (c = in.read()) != -1 ) {                
                bd = bd.multiply(base);
                bd = bd.add(new BigInteger(String.valueOf(c-48))); 
            }
            System.out.print("Pi number: 3." + bd.toString() + "\n");
        } finally {
            if (in != null) {
                in.close();
            }
           
        }
    }
}