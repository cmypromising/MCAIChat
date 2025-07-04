package com.promising.jarvis.llm.client.impl;

import com.promising.jarvis.Jarvis;
import com.promising.jarvis.llm.client.LLMClient;
import com.promising.jarvis.llm.deepseek.DeepSeekRequestBody;
import joptsimple.internal.Strings;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Properties;
import java.util.Scanner;

public class DeepSeekLLMClient implements LLMClient {
    private static final String BASE_URL = "https://api.deepseek.com/chat/completions";
    private static final String AUTH_TOKEN = loadTokenFromEnv();

    private final HttpClient client;
    private static final String JSON_MEDIA_TYPE = "application/json";

    public DeepSeekLLMClient() {
        this.client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(30))
                .version(HttpClient.Version.HTTP_2)
                .build();
    }

    public String getResponse(String userMessage, String currentPlayerInfo) throws IOException, InterruptedException {
        if (!Strings.isNullOrEmpty(currentPlayerInfo)) {
            userMessage = "当前时刻主人基本信息{" + currentPlayerInfo + "},主人需求{\n" + userMessage + "\n}";
        }

        Jarvis.LOGGER.info("userMessage: {}", userMessage);

        DeepSeekRequestBody requestBody = DeepSeekRequestBody.builder()
                .addSystemPrompt("minecraft_assistant")
                .addUserMessage(userMessage)
                .setTemperature(0.7)
                .setMaxTokens(1024)
                .build();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL))
                .header("Content-Type", JSON_MEDIA_TYPE)
                .header("Accept", JSON_MEDIA_TYPE)
                .header("Authorization", "Bearer " + AUTH_TOKEN)
                .POST(HttpRequest.BodyPublishers.ofString(requestBody.toJson()))
                .timeout(Duration.ofSeconds(60))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Jarvis.LOGGER.debug("Response Code: {}", response.statusCode());

        if (response.statusCode() >= 200 && response.statusCode() < 300) {
            return response.body();
        } else {
            String errorDetails = response.body();
            Jarvis.LOGGER.error("API调用失败. 状态码: {}, 错误信息: {}",
                    response.statusCode(),
                    errorDetails != null ? errorDetails : "无错误详情");
            throw new IOException("API调用失败. 状态码: " + response.statusCode());
        }
    }

    private static String loadTokenFromEnv() {
        try (InputStream input = DeepSeekLLMClient.class.getClassLoader().getResourceAsStream(".env")) {
            if (input == null) {
                Jarvis.LOGGER.warn(".env文件未找到，尝试从环境变量获取");
                return getTokenFromEnvVar();
            }

            // 简单解析 .env 文件
            Properties envProps = new Properties();
            try (Scanner scanner = new Scanner(input, StandardCharsets.UTF_8)) {
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine().trim();
                    if (!line.startsWith("#") && line.contains("=")) {
                        int idx = line.indexOf('=');
                        String key = line.substring(0, idx).trim();
                        String value = line.substring(idx + 1).trim();
                        envProps.setProperty(key, value);
                    }
                }
            }

            String token = envProps.getProperty("DEEPSEEK_API_TOKEN");
            if (token == null || token.isEmpty()) {
                throw new SecurityException(".env文件中未找到DEEPSEEK_API_TOKEN配置");
            }

            Jarvis.LOGGER.info("成功从.env文件加载API令牌");
            return token;
        } catch (IOException e) {
            Jarvis.LOGGER.error("读取.env文件失败", e);
            return getTokenFromEnvVar();
        }
    }

    /**
     * 从系统环境变量获取令牌（部署备用方案）
     */
    private static String getTokenFromEnvVar() {
        String token = System.getenv("DEEPSEEK_API_TOKEN");
        if (token == null || token.isEmpty()) {
            throw new SecurityException("未配置DeepSeek API令牌! "
                    + "请在.env文件或系统环境变量中设置DEEPSEEK_API_TOKEN");
        }
        Jarvis.LOGGER.info("从系统环境变量加载API令牌");
        return token;
    }
}