package DictionaryCommandLine.data;

import DictionaryCommandLine.Word;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class SQLDatabase {
    //Khởi tạo cac thong so ket noi toi file csdl
    private static final String URL = "jdbc:mysql://localhost:3306/ktldictionary";
    private static final String username = "root";
    private static final String password = "123456";

    //Thiet lap ket noi toi csdl
    public List<Word> insertWordFromDatabase() {
        List<Word> wordList = new ArrayList<>();
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(URL, username, password);
// Câu lệnh truy vaans trong SQL
            String sql_query = "SELECT DISTINCT word, detail FROM tbl_edict order by word";
// Chuẩn bij trạng thái truy vấn
            PreparedStatement preparedStatement = connection.prepareStatement(sql_query);
            // Thực thi truy vấn
            ResultSet resultSet = preparedStatement.executeQuery();
            // Duyệt kết quả theo cột word và detail
            while (resultSet.next()) {
                String word_target = resultSet.getString("word");
                String word_explain = resultSet.getString("detail");
                word_explain = word_explain.replaceAll("<C><F><I><N><Q>@", "");
                word_explain = word_explain.replaceAll("<br />", "\n");
                word_explain = word_explain.replaceAll("</Q></N></I></F></C>", "");
                Word word = new Word(word_target, word_explain);
                wordList.add(word);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return wordList;
    }
    public void addWordFromDatabase(String target, String explain) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        explain = explain.replaceAll("\n", "<br />");
        explain = "<C><F><I><N><Q>@" + explain + "</Q></N></I></F></C>";

        try {
            connection = DriverManager.getConnection(URL, username, password);
            String sql_query = "INSERT INTO tbl_edict (word, detail) VALUES (?, ?)";
            preparedStatement = connection.prepareStatement(sql_query);
            preparedStatement.setString(1, target);
            preparedStatement.setString(2, explain);

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    public ArrayList<Word> searchByFirstLetter(String prefix) {
        ArrayList<Word> results = new ArrayList<>();
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(URL, username, password);

            String sql_query = "SELECT word, detail FROM tbl_edict WHERE word LIKE ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql_query);
            preparedStatement.setString(1, prefix + "%");

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String word_target = resultSet.getString("word");
                String word_explain = resultSet.getString("detail");
                Word word = new Word(word_target, word_explain);
                results.add(word);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return results;
    }

    public void replaceWordFromDatabase(String target, String explain) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        explain = explain.replaceAll("\n", "<br />");
        explain = "<C><F><I><N><Q>@" + explain + "</Q></N></I></F></C>";
        try {
            connection = DriverManager.getConnection(URL, username, password);
            String query = "UPDATE tbl_edict SET detail = ? WHERE word = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, explain);
            preparedStatement.setString(2, target);

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    public void removeWordFromDatabase(String target) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = DriverManager.getConnection(URL, username, password);
            String query = "DELETE FROM tbl_edict WHERE word = ?";
            preparedStatement = connection.prepareStatement(query);

            preparedStatement.setString(1, target);
            preparedStatement.executeUpdate();


        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
