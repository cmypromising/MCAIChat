import com.promising.jarvis.llm.client.impl.DeepSeekLLMClient;
import com.promising.jarvis.llm.deepseek.ContentResponseBody;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

public class DeepSeekLLMClientTest {
    private DeepSeekLLMClient deepSeekLlmClient;

    @Before
    public void setUp() {
        // 初始化 DeepSeekLLMClient
        deepSeekLlmClient = new DeepSeekLLMClient();
    }
}