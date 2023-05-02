package io.reflectoring.startup;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class HttpMessageSender {

    private final String url;
    private final String httpMethod;

    public HttpMessageSender(String url, String httpMethod) {
        this.url = url;
        this.httpMethod = httpMethod;
    }

    public MessageResponse sendMessage(String body, String contentType) throws Exception {
        // Create URL object
        URL apiUrl = new URL(url);

        // Open connection
        HttpURLConnection connection = (HttpURLConnection) apiUrl.openConnection();

        try {

            // Set request method
            connection.setRequestMethod(httpMethod);

            // Enable output and set content type
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "text/plain");

            try (OutputStream outputStream = connection.getOutputStream()) {
                outputStream.write(body.getBytes());
                outputStream.flush();
                outputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Get response code
            int responseCode = connection.getResponseCode();
            System.out.println("Response Code: " + responseCode);
            StringBuilder response = new StringBuilder();
            String line;
            // Read response body
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Print response body
            System.out.println("Response Body: " + response.toString());

            return new MessageResponse(responseCode, response.toString());

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Close connection
            connection.disconnect();
        }
        return null;
    }
}

class MessageResponse {
    private int responseCode;
    private String responseBody;

    public MessageResponse(int responseCode, String responseBody) {
        this.responseCode = responseCode;
        this.responseBody = responseBody;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public String getResponseBody() {
        return responseBody;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public void setResponseBody(String responseBody) {
        this.responseBody = responseBody;
    }

}