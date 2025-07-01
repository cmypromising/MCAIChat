package com.promising.jarvis.llm.client.impl;

import com.promising.jarvis.llm.client.LLMClient;
import com.promising.jarvis.llm.deepseek.DeepSeekRequestBody;
import io.github.cdimascio.dotenv.Dotenv;
import okhttp3.*;
import okhttp3.logging.HttpLoggingInterceptor;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class DeepSeekLLMClient implements LLMClient {
    private static final Dotenv dotenv = Dotenv.load();
    private static final String BASE_URL = "https://api.deepseek.com/chat/completions";
    private static final String AUTH_TOKEN = dotenv.get("DEEPSEEK_API_TOKEN");

    private final OkHttpClient client;
    private final MediaType mediaType;

    public DeepSeekLLMClient() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        this.client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .addNetworkInterceptor(
                        new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
                )
                .build();
        this.mediaType = MediaType.parse("application/json");
    }

    public Response getResponse(String userMessage) throws IOException {
        DeepSeekRequestBody requestBody = DeepSeekRequestBody.builder()
                .addSystemPrompt("minecraft_assistant")
                .addUserMessage(userMessage)
                .setTemperature(0.7)
                .setMaxTokens(1024)
                .build();

        RequestBody body = RequestBody.create(
                mediaType,
                requestBody.toJson()
        );

        Request request = new Request.Builder()
                .url(BASE_URL)
                .post(body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")
                .addHeader("Authorization", "Bearer " + AUTH_TOKEN)
                .build();

        Response response = client.newCall(request).execute();
        System.out.println("Response Code: " + response.code());

        if (!response.isSuccessful()) {
            String errorBody = response.body() != null ? response.body().string() : "No error details";
            System.err.println("Error Response Body: " + errorBody);
            throw new IOException("API调用失败. 状态码: " + response.code() +
                    ", 错误信息: " + errorBody);
        }

        return response;
    }
}