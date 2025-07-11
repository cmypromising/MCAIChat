package com.promising.jarvis.llm.prompt;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Optional;

public class SystemPromptLoader {
    private static final String PROMPT_CONFIG_FILE = "assets/jarvis/llm/system-prompts.json";
    private static final JsonObject promptsConfig;

    static {
        try {
            // 加载配置文件
            Gson gson = new Gson();
            JsonReader reader = new JsonReader(
                    new InputStreamReader(
                            Objects.requireNonNull(SystemPromptLoader.class.getClassLoader().getResourceAsStream(PROMPT_CONFIG_FILE)),
                            StandardCharsets.UTF_8
                    )
            );
            promptsConfig = gson.fromJson(reader, JsonObject.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load system prompts configuration", e);
        }
    }

    /**
     * 获取指定的系统提示词
     * @param promptKey 提示词的键
     * @return 系统提示词内容，如果未找到则返回空
     */
    public static Optional<SystemPrompt> getPrompt(String promptKey) {
        if (promptsConfig == null || !promptsConfig.has(promptKey)) {
            return Optional.empty();
        }

        JsonObject promptObj = promptsConfig.getAsJsonObject(promptKey);

        // 获取 role
        String role = promptObj.get("role").getAsString();

        // 获取 content 数组
        JsonArray contentArray = promptObj.getAsJsonArray("content");

        // 拼接 content 数组中的字符串
        StringBuilder contentBuilder = new StringBuilder();
        for (int i = 0; i < contentArray.size(); i++) {
            contentBuilder.append(contentArray.get(i).getAsString());
            // 可以选择性地添加换行符或空格
            if (i < contentArray.size() - 1) {
                contentBuilder.append("\n"); // 如果需要换行
            }
        }
        return Optional.of(new SystemPrompt(role, contentBuilder.toString()));
    }

    /**
     * 系统提示词类，用于表示提示词的角色和内容
     */
    public static class SystemPrompt {
        private final String role;
        private final String content;

        public SystemPrompt(String role, String content) {
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
}