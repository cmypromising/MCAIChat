package com.promising.jarvis.llm.deepseek;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class DeepSeekResponseBody {
    @SerializedName("id")
    private String id;

    @SerializedName("object")
    private String object;

    @SerializedName("created")
    private long created;

    @SerializedName("model")
    private String model;

    @SerializedName("choices")
    private List<Choice> choices;

    @SerializedName("usage")
    private Usage usage;

    @SerializedName("system_fingerprint")
    private String systemFingerprint;

    // Getter 和 Setter
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getObject() { return object; }
    public void setObject(String object) { this.object = object; }

    public long getCreated() { return created; }
    public void setCreated(long created) { this.created = created; }

    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }

    public List<Choice> getChoices() { return choices; }
    public void setChoices(List<Choice> choices) { this.choices = choices; }

    public Usage getUsage() { return usage; }
    public void setUsage(Usage usage) { this.usage = usage; }

    public String getSystemFingerprint() { return systemFingerprint; }
    public void setSystemFingerprint(String systemFingerprint) { this.systemFingerprint = systemFingerprint; }

    // 内部静态类 Choice
    public static class Choice {
        @SerializedName("index")
        private int index;

        @SerializedName("message")
        private Message message;

        @SerializedName("logprobs")
        private Object logprobs;

        @SerializedName("finish_reason")
        private String finishReason;

        // Getter 和 Setter
        public int getIndex() { return index; }
        public void setIndex(int index) { this.index = index; }

        public Message getMessage() { return message; }
        public void setMessage(Message message) { this.message = message; }

        public Object getLogprobs() { return logprobs; }
        public void setLogprobs(Object logprobs) { this.logprobs = logprobs; }

        public String getFinishReason() { return finishReason; }
        public void setFinishReason(String finishReason) { this.finishReason = finishReason; }
    }

    // 内部静态类 Message
    public static class Message {
        @SerializedName("role")
        private String role;

        @SerializedName("content")
        private String content;  // 将 content 修改为 String 类型

        // Getter 和 Setter
        public String getRole() { return role; }
        public void setRole(String role) { this.role = role; }

        public ContentResponseBody getContent() {
            // 将交互的信息解析为 ContentResponseBody 对象
            Gson gson = new Gson();
            // 解析 content 字符串为 ContentResponseBody 对象
            return gson.fromJson(content, ContentResponseBody.class);
        }

        public void setContent(String content) { this.content = content; }
    }

    // 内部静态类 Usage
    public static class Usage {
        @SerializedName("prompt_tokens")
        private int promptTokens;

        @SerializedName("completion_tokens")
        private int completionTokens;

        @SerializedName("total_tokens")
        private int totalTokens;

        @SerializedName("prompt_tokens_details")
        private PromptTokensDetails promptTokensDetails;

        @SerializedName("prompt_cache_hit_tokens")
        private int promptCacheHitTokens;

        @SerializedName("prompt_cache_miss_tokens")
        private int promptCacheMissTokens;

        // Getter 和 Setter
        public int getPromptTokens() { return promptTokens; }
        public void setPromptTokens(int promptTokens) { this.promptTokens = promptTokens; }

        public int getCompletionTokens() { return completionTokens; }
        public void setCompletionTokens(int completionTokens) { this.completionTokens = completionTokens; }

        public int getTotalTokens() { return totalTokens; }
        public void setTotalTokens(int totalTokens) { this.totalTokens = totalTokens; }

        public PromptTokensDetails getPromptTokensDetails() { return promptTokensDetails; }
        public void setPromptTokensDetails(PromptTokensDetails promptTokensDetails) { this.promptTokensDetails = promptTokensDetails; }

        public int getPromptCacheHitTokens() { return promptCacheHitTokens; }
        public void setPromptCacheHitTokens(int promptCacheHitTokens) { this.promptCacheHitTokens = promptCacheHitTokens; }

        public int getPromptCacheMissTokens() { return promptCacheMissTokens; }
        public void setPromptCacheMissTokens(int promptCacheMissTokens) { this.promptCacheMissTokens = promptCacheMissTokens; }
    }

    // 内部静态类 PromptTokensDetails
    public static class PromptTokensDetails {
        @SerializedName("cached_tokens")
        private int cachedTokens;

        // Getter 和 Setter
        public int getCachedTokens() { return cachedTokens; }
        public void setCachedTokens(int cachedTokens) { this.cachedTokens = cachedTokens; }
    }
}
