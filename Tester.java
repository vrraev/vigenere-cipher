import java.util.*;
import edu.duke.*;
/**
 * Write a description of Tester here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Tester {
    public void testSliceString(){
        VigenereBreaker vb = new VigenereBreaker();
        System.out.println(vb.sliceString("abcdefghijklm", 3, 5));
    }
    
    public void testTryKeyLength(){
        VigenereBreaker vb = new VigenereBreaker();
        FileResource fr = new FileResource("athens_keyflute.txt");
        String encryped = fr.asString();
        System.out.println(Arrays.toString(vb.tryKeyLength(encryped, 5, 'e')));
    }
    
    public void testCountMostCommonChar(){
        VigenereBreaker vb = new VigenereBreaker();
        FileResource fr = new FileResource();
        System.out.println(vb.mostCommonCharIn(vb.readDictionary(fr)));
    }
}
