package DictionaryCommandLine;

import DictionaryCommandLine.Dictionary;
import DictionaryCommandLine.Word;
import DictionaryCommandLine.data.DictionaryFileIO;
import DictionaryCommandLine.data.SQLDatabase;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class DictionaryManagement {
    public void insertFromCommandLine(Dictionary dictionary) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter the number of words to insert: ");
        int numOfWords = sc.nextInt();
        sc.nextLine();

        SQLDatabase sqlDatabase = new SQLDatabase();
        for (int i = 0; i < numOfWords; i++) {
            System.out.print("Enter English word #" + (i + 1) + ": ");
            String wordTarget = sc.nextLine();

            System.out.print("Enter Vietnamese meaning #" + (i + 1) + ": ");
            String wordExplain = sc.nextLine();

            if (wordTarget.trim().isEmpty() || wordExplain.trim().isEmpty()) {
                System.out.println("Invalid input, word not added.");
                continue;
            }

            sqlDatabase.addWordFromDatabase(wordTarget, wordExplain);
            System.out.println("Word added successfully.");

        }
    }

    public void insertFromFile(Dictionary dictionary, String filePath) {
        DictionaryFileIO dictionaryFileIO = new DictionaryFileIO();
        ArrayList<Word> wordList = dictionaryFileIO.readWordsFromFile("src/data/V_E.txt");
        if (wordList.isEmpty()) {
            System.out.println("No words in the file !");
            return;
        }
        for (Word word : wordList) {
            dictionary.addWord(word);
        }
        System.out.println("Words inserted from file successfully.");
    }
    public ArrayList<Word> searchByFirstLetter(Dictionary dictionary, String prefix) {
        ArrayList<Word> results = new ArrayList<>();

        Word foundWord = dictionary.searchWord(prefix);
        if (foundWord != null) {
            results.add(foundWord);
        }

        ArrayList<Word> wordsWithPrefix = dictionary.searchAllWords(prefix);
        for (Word word : wordsWithPrefix) {
            if (!results.contains(word)) {
                results.add(word);
            }
        }
        return results;
    }

    public void dictionarySearcher(SQLDatabase sqlDatabase, String keyword) {
        String lowerKeyword = keyword.toLowerCase();
        List<Word> searchResults = sqlDatabase.searchByFirstLetter(lowerKeyword);

        if (!searchResults.isEmpty()) {
            for (Word word : searchResults) {
                System.out.println("Word: " + word.getWord_target() + " | Meaning: " + word.getWord_explain());
            }
        } else {
            System.out.println("Found Nothing!!!");
        }
    }


    public void dictionaryUpdate(Dictionary dictionary, String oldWordTarget, Word newWord) {
        SQLDatabase sqlDatabase = new SQLDatabase();
        sqlDatabase.replaceWordFromDatabase(oldWordTarget, newWord.getWord_explain());
        System.out.println("Updated word: " + oldWordTarget + " to " + newWord.getWord_target());
    }

    public void wordDelete(Dictionary dictionary, String wordTarget) {
        SQLDatabase database = new SQLDatabase();
        database.removeWordFromDatabase(wordTarget);
        System.out.println("Removed word: " + wordTarget);
    }

}
