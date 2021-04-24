import java.util.*;
import edu.duke.*;
import java.io.File;

public class VigenereBreaker {
    public String sliceString(String message, int whichSlice, int totalSlices) {
        StringBuilder sliced = new StringBuilder();
        for (int i = whichSlice; i < message.length(); i += totalSlices){
            sliced.append(message.charAt(i));
        }
        return sliced.toString();
    }

    public int[] tryKeyLength(String encrypted, int klength, char mostCommon) {
        int[] key = new int[klength];
        CaesarCracker cc = new CaesarCracker(mostCommon);
        for (int i = 0; i < klength; i++){
            String sliced = sliceString(encrypted, i, klength);
            key[i] = cc.getKey(sliced);
        }
        return key;
    }

    public HashSet<String> readDictionary(FileResource fr){
        HashSet<String> hsEnglishWords = new HashSet<String>();
        for (String s : fr.lines()){
            s = s.toLowerCase();
            hsEnglishWords.add(s);
        }
        return hsEnglishWords;
    }
    
    public int countWords(String message, HashSet<String> dictionary){
        int counter = 0;
        String[] words = message.split("\\W+");
        for (String s : words){
            s = s.toLowerCase();
            if (dictionary.contains(s)){
                counter++;
            }
        }
        return counter;
    }
    
    public String breakForLanguage(String encrypted, HashSet<String> dictionary){
        String decrypted = "";
        int realWords = 0;
        int[] realKey = new int[100];
        for (int i = 1; i <=100; i++){
            int[] key = tryKeyLength(encrypted, i, mostCommonCharIn(dictionary));
            VigenereCipher vc = new VigenereCipher(key);
            String currDecrypted = vc.decrypt(encrypted);
            int currWords = countWords(currDecrypted, dictionary);
            if (currWords > realWords){
                decrypted = currDecrypted;
                realWords = currWords;
                realKey = key;
            }
        }
        System.out.println(realWords);
        System.out.println(realKey.length);
        return decrypted;
    }
    
    public char mostCommonCharIn(HashSet<String> dictionary){
        HashMap<Character, Integer> hmChars = new HashMap<Character, Integer>();
        char mostCommonChar = 0;
        int biggestAmount = 0;
        for (String s : dictionary){
            char[] charArr = s.toCharArray();
            for (char c : charArr){
                if (hmChars.containsKey(c)){
                    hmChars.put(c, hmChars.get(c) + 1);
                } else {
                    hmChars.put(c, 1);
                }
            }
        }   
        for (char c1 : hmChars.keySet()){
            int currAmount = hmChars.get(c1);
            if (currAmount > biggestAmount){
                mostCommonChar = c1;
                biggestAmount = currAmount;
            }
        }
        return mostCommonChar;
    }
    
    public void breakForAllLangs(String encrypted, HashMap<String, HashSet<String>> languages){
        int mostRealWords = 0;
        String realDecrypted = "";
        String langDecryptedFrom = "";
        for (String lang : languages.keySet()){
            String currDecrypted = breakForLanguage(encrypted,languages.get(lang));
            int currRealWords = countWords(currDecrypted, languages.get(lang));
            if (currRealWords > mostRealWords){
                realDecrypted = currDecrypted;
                mostRealWords = currRealWords;
                langDecryptedFrom = lang;
            }
        }
        System.out.println(realDecrypted);
        System.out.println("Decrypted from: " + langDecryptedFrom);
    }
    
    
    public void breakVigenere () {
        FileResource fr = new FileResource();
        String encrypted = fr.asString();
        DirectoryResource dirResDicts = new DirectoryResource();
        HashMap<String, HashSet<String>> languages = new HashMap<String, HashSet<String>>();
        for (File f : dirResDicts.selectedFiles()){
            FileResource fr1 = new FileResource(f);
            HashSet<String> dictWords = readDictionary(fr1);
            languages.put(f.getName(), dictWords);
        }
        breakForAllLangs(encrypted, languages);
    }
}
