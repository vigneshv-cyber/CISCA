import java.util.Scanner;
import java.util.*;
public class playfaircipher {
    private char[][] keyTable;
    private String key;
    
    public playfaircipher(String key) {
        this.key = key;
        keyTable = generateKeyTable(key);
    }
    
    private char[][] generateKeyTable(String key) {
        key = key.toUpperCase().replaceAll("J", "I");
        Set<Character> usedChars = new LinkedHashSet<>();
        for (char c : key.toCharArray()) {
            if (Character.isLetter(c)) {
                usedChars.add(c);
            }
        }
        for (char c = 'A'; c <= 'Z'; c++) {
            if (c != 'J') {
                usedChars.add(c);
            }
        }
        char[][] table = new char[5][5];
        Iterator<Character> it = usedChars.iterator();
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                table[i][j] = it.next();
            }
        }
        return table;
    }
    
    private String prepareText(String text) {
        text = text.toUpperCase().replaceAll("[^A-Z]", "").replaceAll("J", "I");
        StringBuilder sb = new StringBuilder(text);
        for (int i = 0; i < sb.length() - 1; i += 2) {
            if (sb.charAt(i) == sb.charAt(i + 1)) {
                sb.insert(i + 1, 'X');
            }
        }
        if (sb.length() % 2 != 0) {
            sb.append('X');
        }
        return sb.toString();
    }
    
    private int[] findPosition(char c) {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (keyTable[i][j] == c) {
                    return new int[]{i, j};
                }
            }
        }
        return null;
    }
    
    private String processPairs(String text, boolean encrypt) {
        StringBuilder result = new StringBuilder();
        text = prepareText(text);
        for (int i = 0; i < text.length(); i += 2) {
            char a = text.charAt(i);
            char b = text.charAt(i + 1);
            int[] posA = findPosition(a);
            int[] posB = findPosition(b);
            if (posA[0] == posB[0]) {
                result.append(keyTable[posA[0]][(posA[1] + (encrypt ? 1 : 4)) % 5]);
                result.append(keyTable[posB[0]][(posB[1] + (encrypt ? 1 : 4)) % 5]);
            } else if (posA[1] == posB[1]) {
                result.append(keyTable[(posA[0] + (encrypt ? 1 : 4)) % 5][posA[1]]);
                result.append(keyTable[(posB[0] + (encrypt ? 1 : 4)) % 5][posB[1]]);
            } else {
                result.append(keyTable[posA[0]][posB[1]]);
                result.append(keyTable[posB[0]][posA[1]]);
            }
        }
        return result.toString();
    }
    
    public String encrypt(String text) {
        return processPairs(text, true);
    }
    
    public String decrypt(String text) {
        return processPairs(text, false);
    }
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter key: ");
        String key = scanner.nextLine();
        playfaircipher cipher = new playfaircipher(key);
        System.out.print("Enter text to encrypt: ");
        String text = scanner.nextLine();
        String encrypted = cipher.encrypt(text);
        System.out.println("Encrypted text: " + encrypted);
        System.out.println("Decrypted text: " + cipher.decrypt(encrypted));
        scanner.close();
    }
}
