package com.promising.jarvis.core.parser.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.promising.jarvis.core.parser.NLParser;
import com.promising.jarvis.llm.client.impl.DeepSeekLLMClient;
import com.promising.jarvis.llm.deepseek.ContentResponseBody;
import com.promising.jarvis.llm.deepseek.DeepSeekResponseBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

import java.io.IOException;
import java.util.Objects;

/**
 * 通过大模型解析用户需求
 */
public class DeepSeekParser implements NLParser {

    public static DeepSeekLLMClient deepSeekClient = new DeepSeekLLMClient();
    public static Gson gson = new GsonBuilder().create();
    public static DeepSeekResponseBody deepSeekResponseBody;

    @Override
    public ContentResponseBody parse(String context, String currentPlayerInfo) {
        Response response = null;
        try {
            response = deepSeekClient.getResponse(context, currentPlayerInfo);
            ResponseBody responseBody = response.body();
            if (responseBody == null) {
                throw new IOException("响应体为空");
            }
            String jsonResp = responseBody.string();
            deepSeekResponseBody = gson.fromJson(jsonResp, DeepSeekResponseBody.class);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            if (response != null) {
                response.close(); // 确保在完成使用后关闭响应
            }
        }
        // 检查 deepSeekResponseBody 并返回相应内容
        if (Objects.isNull(deepSeekResponseBody)) {
            return null;
        }
        return deepSeekResponseBody.getChoices().getFirst().getMessage().getContent();
    }

    @Override
    public int getPriority() {
        return 0;
    }
}
