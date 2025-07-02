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

    @Test
    public void testGetResponseWithMinecraftCommand() throws IOException {
        // 测试获取 Minecraft 命令相关的响应
        String userMessage = "我死了东西全掉了，不想让他掉";

    }
}