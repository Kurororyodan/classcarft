/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.utl.dsm.rest;

/**
 *
 * @author betillo
 */

import com.google.gson.Gson;
import okhttp3.*;

import java.io.IOException;

public class ChatGPTClient {
    private static final String API_KEY = "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";
    private static final String API_URL = "https://api.openai.com/v1/chat/completions";

    private final OkHttpClient client;
    private final Gson gson;

    public ChatGPTClient() {
        this.client = new OkHttpClient();
        this.gson = new Gson();
    }

    public String generateText(String prompt) throws IOException {
        RequestBody body = RequestBody.create(
                gson.toJson(new RequestData(prompt)),
                MediaType.get("application/json; charset=utf-8")
        );

        Request request = new Request.Builder()
                .url(API_URL)
                .header("Authorization", "Bearer " + API_KEY)
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }

            String responseBody = response.body().string();
            ChatGPTResponse chatGPTResponse = gson.fromJson(responseBody, ChatGPTResponse.class);
            return chatGPTResponse.getChoices()[0].getMessage().getContent();
        }
    }

    private static class RequestData {
        private final String model = "gpt-4o-mini";
        private final Message[] messages;

        public RequestData(String prompt) {
            this.messages = new Message[]{new Message("user", prompt)};
        }

        public String getModel() {
            return model;
        }

        public Message[] getMessages() {
            return messages;
        }
    }

    private static class Message {
        private final String role;
        private final String content;

        public Message(String role, String content) {
            this.role = role;
            this.content = content;
        }

        public String getRole() {
            return role;
        }

        public String getContent() {
            return content;
        }
    }

    private static class ChatGPTResponse {
        private final Choice[] choices;

        public ChatGPTResponse(Choice[] choices) {
            this.choices = choices;
        }

        public Choice[] getChoices() {
            return choices;
        }
    }

    private static class Choice {
        private final Message message;

        public Choice(Message message) {
            this.message = message;
        }

        public Message getMessage() {
            return message;
        }
    }

    public static void main(String[] args) {
        ChatGPTClient client = new ChatGPTClient();
        try {
            String response = client.generateText("puede generar json?");
            System.out.println(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
