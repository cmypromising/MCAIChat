package com.promising.jarvis.core.parser;

import com.promising.jarvis.llm.deepseek.ContentResponseBody;

import java.io.IOException;

/**
 * 解析用户的需求
 */
public interface NLParser {
    /**
     * 解析自然语言
     * @param context 用户的自然语言需求
     * @return 解析结果
     */
    ContentResponseBody parse(String context) throws IOException;

    /**
     * 解析器优先级
     * @return 优先级
     */
    int getPriority();
}

