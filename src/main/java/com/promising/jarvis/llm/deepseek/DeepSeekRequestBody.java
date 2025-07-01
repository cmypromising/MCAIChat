package com.promising.jarvis.llm.deepseek;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.promising.jarvis.llm.prompt.SystemPromptLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DeepSeekRequestBody {
    // 内部消息类
    public static class Message {
        @SerializedName("role")
        private String role;

        @SerializedName("content")
        private String content;

        public Message(String role, String content) {
            this.role = role;
            this.content = content;
        }
    }

    // 请求体的各个字段
    @SerializedName("messages")
    private List<Message> messages;

    @SerializedName("model")
    private String model = "deepseek-chat";

    @SerializedName("frequency_penalty")
    private double frequencyPenalty = 0;

    @SerializedName("max_tokens")
    private int maxTokens = 2048;

    @SerializedName("presence_penalty")
    private double presencePenalty = 0;

    @SerializedName("response_format")
    private ResponseFormat responseFormat;

    @SerializedName("stop")
    private Object stop = null;

    @SerializedName("stream")
    private boolean stream = false;

    @SerializedName("stream_options")
    private Object streamOptions = null;

    @SerializedName("temperature")
    private double temperature = 1.0;

    @SerializedName("top_p")
    private double topP = 1.0;

    @SerializedName("tools")
    private Object tools = null;

    @SerializedName("tool_choice")
    private String toolChoice = "none";

    @SerializedName("logprobs")
    private boolean logprobs = false;

    @SerializedName("top_logprobs")
    private Object topLogprobs = null;

    // 响应格式内部类
    public static class ResponseFormat {
        @SerializedName("type")
        private String type = "json_object";
    }

    // 私有构造方法
    private DeepSeekRequestBody() {
        this.messages = new ArrayList<>();
        this.responseFormat = new ResponseFormat();
    }

    // 构建器类
    public static class Builder {
        private DeepSeekRequestBody requestBody;

        public Builder() {
            requestBody = new DeepSeekRequestBody();
        }

        // 添加系统消息
        public Builder addSystemMessage(String content) {
            requestBody.messages.add(new Message("system", content));
            return this;
        }

        // 支持直接使用 SystemPromptLoader 加载的提示词
        public Builder addSystemPrompt(String promptKey) {
            Optional<SystemPromptLoader.SystemPrompt> prompt =
                    SystemPromptLoader.getPrompt(promptKey);

            prompt.ifPresent(systemPrompt ->
                    requestBody.messages.add(
                            new Message(systemPrompt.getRole(), systemPrompt.getContent())
                    )
            );

            return this;
        }

        // 添加用户消息
        public Builder addUserMessage(String content) {
            requestBody.messages.add(new Message("user", content));
            return this;
        }

        // 设置模型
        public Builder setModel(String model) {
            requestBody.model = model;
            return this;
        }

        // 设置温度
        public Builder setTemperature(double temperature) {
            requestBody.temperature = temperature;
            return this;
        }

        // 设置最大 token 数
        public Builder setMaxTokens(int maxTokens) {
            requestBody.maxTokens = maxTokens;
            return this;
        }

        // 设置频率惩罚
        public Builder setFrequencyPenalty(double frequencyPenalty) {
            requestBody.frequencyPenalty = frequencyPenalty;
            return this;
        }

        // 设置存在惩罚
        public Builder setPresencePenalty(double presencePenalty) {
            requestBody.presencePenalty = presencePenalty;
            return this;
        }

        // 构建最终的 DeepSeekRequestBody
        public DeepSeekRequestBody build() {
            // 可以在这里添加一些验证逻辑
            if (requestBody.messages.isEmpty()) {
                throw new IllegalStateException("至少需要添加一条消息");
            }
            return requestBody;
        }
    }

    // 转换为 JSON 字符串
    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    // 静态方法用于创建构建器
    public static Builder builder() {
        return new Builder();
    }
}
