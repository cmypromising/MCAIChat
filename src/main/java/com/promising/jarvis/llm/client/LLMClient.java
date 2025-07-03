package com.promising.jarvis.llm.client;

import java.io.IOException;

/**
 *
 */
public interface LLMClient {

    /**
     *
     * @param userMessage: 玩家需求
     * @param currentPlayerInfo: 当前玩家背景信息
     * @return : LLM响应结果
     * @throws IOException: 响应异常的报错
     */
    public String getResponse(String userMessage, String currentPlayerInfo) throws IOException, InterruptedException;
}
