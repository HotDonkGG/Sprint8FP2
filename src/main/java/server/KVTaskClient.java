package main.java.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class KVTaskClient {
    private final String apiUrl;
    private final String apiToken;

    public KVTaskClient(String apiUrl) throws IOException {
        this.apiUrl = apiUrl;
      this.apiToken = register();
    }

    private String register() throws IOException {
        URL url = new URL(apiUrl + "/register");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
        String token = reader.readLine();
        reader.close();

        return token;
    }

    public void save(String key, String json) throws IOException { //json - объект(value), который кладу в мапу на сервере по ключу key
        URL url = new URL(apiUrl + "/save/" + key + "?API_TOKEN=" + apiToken);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);

        byte[] postDataBytes = json.getBytes(StandardCharsets.UTF_8);
        connection.getOutputStream().write(postDataBytes);

        connection.getResponseCode();
    }

    public String delete(String key) throws IOException{
        URL url = new URL(apiUrl + "/delete/" + key + "?API_TOKEN=" + apiToken);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("DELETE");
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();

        return response.toString();
    }

    public String load(String key) throws IOException {
        URL url = new URL(apiUrl + "/load/" + key + "?API_TOKEN=" + apiToken);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();

        return response.toString();
    }
}