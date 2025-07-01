package com.promising.jarvis.llm.client;

import com.promising.jarvis.llm.deepseek.ContentResponseBody;
import okhttp3.Response;

import java.io.IOException;

/**
 *
 */
public interface LLMClient {

    /**
     *
     * @param userMessage: 用户询问LLM的需求及问题
     * @return : LLM响应结果
     * @throws IOException: 响应异常的报错
     */
    public Response getResponse(String userMessage) throws IOException;
}
