package DictionaryCommandLine;


import DictionaryCommandLine.data.SQLDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class DictionaryCommandLine {
    private static DictionaryManagement dictionaryManagement;
    private static Dictionary dictionary;
    private static SQLDatabase sqlDatabase = new SQLDatabase();

    public DictionaryCommandLine() {
        dictionaryManagement = new DictionaryManagement();
        dictionary = new Dictionary();
    }

    public static void showAllWords() {
        SQLDatabase sqlDatabase = new SQLDatabase();
        List<Word> words = sqlDatabase.insertWordFromDatabase();
        if (words.isEmpty()) {
            System.out.println("Dictionary is empty.");
        } else {
            System.out.println("No | English | Vietnamese Meaning ");
            for (int i = 0; i < words.size(); i++) {
                Word word = words.get(i);
                System.out.println((i + 1) + " | " + word.getWord_target() + " | " + word.getWord_explain());
            }
        }
    }


    public static void dictionaryAdvanced() {
        Scanner sc = new Scanner(System.in);
        System.out.println("\t\t------- Learning English Dictionary -------\t\t");
        System.out.println("\t\t\t\t Menu \t\t\t\t");
        System.out.println("\t\t1. Insert word from Command Line. ");
        System.out.println("\t\t2. Show all words. ");
        System.out.println("\t\t3. Search word. ");
        System.out.println("\t\t4. Update word. ");
        System.out.println("\t\t5. Remove word. ");
        System.out.println("\t\t6. MiniGame. ");
        System.out.println("\t\t7. Insert word from File");
        System.out.println("\t\t0. Exit. ");

        while (true) {
            System.out.print("Select an option: ");
            // Kiểm tra tính hợp lệ của đầu vào
            while (!sc.hasNextInt()) {
                System.out.println("Invalid option, please enter a number.");
                sc.next(); // Tiêu thụ giá trị không hợp lệ
            }
            int option = sc.nextInt();
            sc.nextLine(); // Tiêu thụ dòng mới


            switch (option) {
                case 1:
                    dictionaryManagement.insertFromCommandLine(dictionary);
                    break;
                case 2:
                    showAllWords();
                    break;
                case 3:
                    System.out.print("Enter a word to search: ");
                    String word_target = sc.nextLine();
                    dictionaryManagement.dictionarySearcher(sqlDatabase, word_target);
                    break;
                case 4:
                    System.out.println("Enter a word that needs to Update: ");
                    String oldWord = sc.nextLine();
                    System.out.println("Enter the new English word : ");
                    String newWordTarget = sc.nextLine();
                    System.out.println("Enter the new Vietnamese meaning : ");
                    String newWordExplain = sc.nextLine();
                    Word newWord = new Word(newWordTarget, newWordExplain);
                    dictionaryManagement.dictionaryUpdate(dictionary,oldWord,newWord);
                    break;
                case 5:
                    System.out.println("Enter a word to delete: ");
                    String deleteWord = sc.nextLine();
                    dictionaryManagement.wordDelete(dictionary,deleteWord);
                case 6:
                    // Thêm trò chơi
                    break;
                case 7:
                    dictionaryManagement.insertFromFile(dictionary, "src/data/E_V.txt");
                    System.out.println("Words inserted from file successfully.");
                    break;
                case 0:
                    System.out.println("Exiting the application. ");
                    sc.close(); // Đóng Scanner trước khi thoát
                    return;
                default:
                    System.out.println("Invalid option, please try again! ");
            }
        }
    }

}

