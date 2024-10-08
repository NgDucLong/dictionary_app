package DictionaryCommandLine;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TrieNode {
    private final HashMap<Character, TrieNode> children = new HashMap<>();
    private Word word = null;

    public TrieNode() {

    }
    
    private ArrayList<Word> getAllWords() {
        ArrayList<Word> allWords = new ArrayList<>();
        if (word != null) {
            allWords.add(word);
        }
        for (TrieNode current : children.values()) {
            allWords.addAll(current.getAllWords());
        }
        return allWords;
    }

    private boolean optimizeRemove(TrieNode current, String word, int index) {
        if (index == word.length()) {
            if (current.word == null) {
                return false;
            }
            current.word = null;
            return current.children.isEmpty();
        }
        char c = word.charAt(index);
        TrieNode node = current.children.get(c);
        if (node == null) {
            return false;
        }
        boolean currentNodeDelete = optimizeRemove(node, word, index + 1);

        if (currentNodeDelete) {
            current.children.remove(c);
            return current.children.isEmpty();
        }
        return false;
    }
    private void optimizeExport(TrieNode current, StringBuilder stringBuilder, PrintWriter printWriter) {
        if (current.word != null) {
            printWriter.println(stringBuilder.toString() + "    " + current.word.getWord_explain());
        }
        for (Map.Entry<Character,TrieNode> entry : current.children.entrySet()) {
            char c = entry.getKey();
            TrieNode child = entry.getValue();
            optimizeExport(child, stringBuilder.append(c),printWriter);
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        }
    }

    public void insert(String word, Word wordObj) {
        TrieNode current = this;
        for (char c : word.toCharArray()) {
            current.children.putIfAbsent(c, new TrieNode());
            current = current.children.get(c);
        }
        current.word = wordObj;
    }

    public Word searchWord(String word) {
        TrieNode current = this;
        for (char c : word.toCharArray()) {
            if (!current.children.containsKey(c)) {
                return null;
            }
            current = current.children.get(c);
        }
        return current.word;
    }
    public ArrayList<Word> searchAllWords(String preWord) {
        TrieNode current = this;
        for (char c : preWord.toCharArray()) {
            if (!current.children.containsKey(c)) {
                return new ArrayList<>();
            }
            current = current.children.get(c);
        }
        return current.getAllWords();
    }

    public void remove(String word) {
        optimizeRemove(this, word,0);
    }
    public void export(PrintWriter printWriter) {
        optimizeExport(this, new StringBuilder() ,printWriter);
    }
    private boolean isEmpty() {
        return children.isEmpty();
    }
}
