/*
 * PiNumber file.txt: Contains series of digits after the decimal point
 */
import java.io.*;
import java.lang.String;
import java.math.BigDecimal;
import java.math.BigInteger;

class PiNumber {
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

    public static void main(String[] args) throws IOException {
        FileInputStream in = null;
        
        try {
            in = new FileInputStream("pi-thousand.txt");
            BigInteger bd = new BigInteger("0");
            BigInteger base = new BigInteger("10");
            int c;

            System.out.print("Reading pi number from file....\n");
            
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

        if (args.length == 2) {
            int i = PiNumber.digitAt(Integer.parseInt(String.valueOf(args[0])), args[1]);
            System.out.println("\nDigit at " + args[0] + "-th of Pi is " + Integer.toString(i, 10));
            System.out.println("\nDigit (in binary) at " + args[0] + "-th of Pi is " + Integer.toString(i, 2));
        }
        else if (args.length == 3){
            BigInteger digit_range = PiNumber.digitRange(Integer.parseInt(String.valueOf(args[0])), Integer.parseInt(String.valueOf(args[1])), args[2]);
            System.out.println("\nDigits from " + args[0] + "-th to " + args[1] + "-th is: " + digit_range.toString());
            System.out.println("\nDigits (in binary) from " + args[0] + "-th to " + args[1] + "-th is: " + digit_range.toString(2));
        }
        else {
            System.out.println("\n Please rerun with syntax : java PiNumber nth [mth] Pi-filename.txt");
        }


    }
}