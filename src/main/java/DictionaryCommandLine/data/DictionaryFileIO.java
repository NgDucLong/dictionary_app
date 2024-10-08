package DictionaryCommandLine.data;

import DictionaryCommandLine.Word;

import java.io.*;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DictionaryFileIO {
    public void writeWordToFile (Word word, String filePath) {
        try {
            FileWriter fw = new FileWriter(filePath,true);
            BufferedWriter bw = new BufferedWriter(fw);

            bw.write("<i>" + word.getWord_target() + "<i> <html>" + word.getWord_explain() + "<html>");
            bw.newLine();
            bw.close();
            fw.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public ArrayList<Word> readWordsFromFile(String filePath) {
        ArrayList<Word> wordList = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String newLine;

            while((newLine = br.readLine()) != null) {
                String regexTarget = "<i>(.*?)</i>";
                String regexExplain = "<html>(.*?)</html>";
                Pattern patternTarget = Pattern.compile(regexTarget);
                Pattern patternExplain = Pattern.compile(regexExplain);

                Matcher target = patternTarget.matcher(newLine);
                Matcher explain = patternExplain.matcher(newLine);

                if (target.find() && explain.find()) {
                    String targetFound = target.group(1);
                    String explainFound = explain.group(1);

                    Word wordFound = new Word(targetFound,explainFound);
                    wordList.add(wordFound);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return wordList;
    }
    public void deleteWordFromFile (Word deleteWord, String filePath) {
        try {
            ArrayList<Word> words = readWordsFromFile(filePath);

            try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
                for (Word word : words) {
                    if(!word.getWord_target().equalsIgnoreCase(deleteWord.getWord_target())) {
                        bw.write("<i>" + word.getWord_target() + "</i> <html>" + word.getWord_explain() + "</html>");
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }
}
