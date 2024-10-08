package Game.Question;

import java.util.ArrayList;

public class QuizModel extends Question {
    ArrayList<Question> questionList;

    // Getter and Setter
    public ArrayList<Question> getQuestions() {
        return questionList;
    }

    public void setQuestions(ArrayList<Question> questionList) {
        this.questionList = questionList;
    }

    // Constructor
    public QuizModel(ArrayList<Question> questionList) {
        this.questionList = questionList;
    }

    public static void main(String[] args) {
        QuizModel quiz = new QuizModel(
                InputQuestion.loadQuestionsFromFile("src/main/java/Game/Question/questionList.txt"));
        System.out.println(quiz.getQuestions().get(0).getQuestionText());
    }
}
