package Game.Question;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class InputQuestion {
    public static ArrayList<Question> loadQuestionsFromFile(String filePath) {
        ArrayList<Question> questionList = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length == 6) {
                    String questionText = parts[0];
                    ArrayList<String> options = new ArrayList<>();
                    for (int i = 1; i <= 4; i++) { // Adding options
                        options.add(parts[i]);
                    }
                    String correctAnswer = parts[5];
                    questionList.add(new Question(questionText, options, correctAnswer));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return questionList;
    }
}
