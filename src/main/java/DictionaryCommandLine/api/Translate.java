package DictionaryCommandLine.api;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import static com.sun.speech.freetts.InputMode.URL;

public class Translate {
    private static final String ApiUrl = "https://api.mymemory.translated.net/get";

    public static String translateText(String text, String source, String target) {
        try {
            //Ma hoa van ban can dich
            String encodedText = URLEncoder.encode(text, StandardCharsets.UTF_8);
            //Dinh dang chuoi
            String apiFormat = String.format("%s?q=%s&langpair=%s|%s", ApiUrl, encodedText, source, target);
            URL url = new URL(apiFormat);
            //Mo connection Http va nhan luong doc from API
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }

            bufferedReader.close();
            httpURLConnection.disconnect();
            //Sử dụng JSONParser để phân tích dữ liệu JSON từ phản hồi và tạo một đối tượng JSONObject.
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(stringBuilder.toString());
            //Truy cập vào đối tượng JSON để trích xuất văn bản đã dịch từ phản hồi của API.
            JSONObject responseData = (JSONObject) jsonObject.get("responseData");
            if (responseData != null) {
                String translatedText = (String) responseData.get("translatedText");
                return translatedText;
            } else {
                return "Invalid!";
            }
        } catch (ParseException | IOException e) {
            e.printStackTrace();
            return "Error";
        }
    }
}