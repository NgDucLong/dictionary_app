package DictionaryCommandLine;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class Dictionary {
    private TrieNode root = new TrieNode();
    private ArrayList<Word> words = new ArrayList<>();


    public Dictionary() {

    }
    public ArrayList<Word> getWords() {
        return words;
    }

    public void setWords(ArrayList<Word> words) {
        this.words = words;
    }

    public void addWord(Word word) {
        root.insert(word.getWord_target(),word);
        words.add(word);
    }

    // Phương thức tìm một từ dựa trên mã băm của từ
    public Word searchWord(String word) {
        return root.searchWord(word);
    }
    public ArrayList<Word> searchAllWords(String preWord) {
        return root.searchAllWords(preWord);
    }

    public void removeWord(String word) {
        root.remove(word);
        words.removeIf(w -> w.getWord_target().equalsIgnoreCase(word));
    }

    public void updateWord(String oldWordTarget, Word newWord) {
        removeWord(oldWordTarget);
        addWord(newWord);
    }

    public void exportToFile(String fileName) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
            root.export(writer);
            System.out.println("Export successfully!");
        } catch (IOException e) {
            System.out.println("Export failed: " + e.getMessage());
        }
    }
}


