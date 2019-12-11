package com.company;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

class wordCount {
    private int occurences;
    private String text;

    public wordCount(String t) {
        text = t;
        occurences = 1;
    }

    public void add() {
        occurences++;
    }

    public int getOccurences() {
        return occurences;
    }

    public String getText() {
        return text;
    }
}

public class Main {
    private Scanner s = new Scanner(System.in);
    private ArrayList<ArrayList<wordCount>> hashTable = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        new Main().run();
    }

    private String makeLength(String str, int len)
    {
        return String.format("%-" + len + "s", str);
    }

    private String repeat(int n)
    {
        return new String(new char[n]).replace("\0", "─");
    }

    private wordCount findWord(ArrayList<wordCount> search, String text) {
        for (wordCount wc: search) {
            if (wc.getText().equals(text)) return wc;
        }
        return null;
    }

    private void run() throws IOException {
        int tableSize = 1009;
        for (int i = 0; i < tableSize; i++) hashTable.add(new ArrayList<>());

        System.out.println("load from text.txt (f) or enter words manually (w)");
        char in = s.nextLine().trim().toLowerCase().charAt(0);
        String[] words;
        if (in == 'w') words = getInput();
        else words = getFile();

        int longestWord = 5;
        //System.out.println(Arrays.toString(words));
        for (String word: words) {
            word = word.toLowerCase();
            longestWord = Math.max(word.length(), longestWord);
            int h = hash(word);
            int tablePos = h % tableSize;
            ArrayList<wordCount> column =  hashTable.get(tablePos);
            wordCount w = findWord(column, word);
            if (w != null) w.add();
            else column.add(new wordCount(word));
        }
        //System.out.println(Arrays.toString(hashTable.toArray()));

        StringBuilder table = new StringBuilder(" ");

        for (int i = 0; i < tableSize; i++) {
            table.append(makeLength(String.valueOf(i), longestWord+1));
        }
        table.append("\n┌");
        for (int i = 0; i < tableSize; i++) {
            table.append(repeat(longestWord));
            if (i + 1 != tableSize) table.append("┬");
        }
        table.append("┐\n");
        boolean wordAdded = true;
        int depth = 0;
        while (wordAdded) {
            wordAdded = false;
            table.append("│");
            for (int i = 0; i < tableSize; i++) {
                String word;
                try {
                    word = hashTable.get(i).get(depth).getText();
                    wordAdded = true;
                } catch (Exception IndexOutOfBoundsException) {
                    word = "";
                }
                table.append(makeLength(word, longestWord));
                table.append("│");
            }
            table.append("\n");
            depth++;
        }
        table.append("└");
        for (int i = 0; i < tableSize; i++) {
            table.append(repeat(longestWord));
            if (i + 1 != tableSize) table.append("┴");
        }
        table.append("┘");
        System.out.println(table);

        while (true) {
            System.out.println("enter a word to find");
            String word = s.nextLine().trim().toLowerCase();
            if (word.equals("break")) break;
            int h = hash(word);
            int tablePos = h % tableSize;
            wordCount w = findWord(hashTable.get(tablePos), word);
            int freq = w == null ? 0 : w.getOccurences();
            System.out.println("word exists " + freq + " time" + (freq != 1 ? "s" : "") + " (column " + tablePos + ")");
        }
    }

    private String[] getInput()
    {
        System.out.println("enter words separated by spaces");
        return s.nextLine().trim().split("([ ,:;\\-.\\n&?—])+");
    }

    private String[] getFile() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader("text.txt"));
        String line;
        StringBuilder result = new StringBuilder();
        while ((line = reader.readLine()) != null) {
            result.append(line);
            result.append("\n");
        }
        reader.close();
        return result.toString().split("([ ,:;\\-.\\n&?—])+");
    }

    private static int hash(String word)
    {
        int num = 0;
        for (char c: word.toCharArray()) num += c;
        return num * word.length();
    }
}
