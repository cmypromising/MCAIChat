package com.promising.jarvis.core.executor.impl;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.promising.jarvis.Jarvis;
import com.promising.jarvis.core.parser.NLParser;
import com.promising.jarvis.core.parser.impl.DeepSeekParser;
import com.promising.jarvis.llm.deepseek.ContentResponseBody;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

/**
 *
 */
public class CommandExecutor {
    // 语言模式映射
    private static final NLParser parser = new DeepSeekParser();

    /**
     * @param dispatcher: 命令调度器
     */
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        // 使用新的获取参数方法
        dispatcher.register(
                CommandManager.literal("nl")
                        .requires(source -> source.hasPermissionLevel(2))
                        .then(CommandManager.argument("text", StringArgumentType.greedyString())
                                .executes(CommandExecutor::action)
                        )
        );
    }

    /**
     *
     * @param context: 待执行命令的文本，例如 /nl ...... 中的 ......
     * @return : 执行结果的响应
     */
    private static int action(CommandContext<ServerCommandSource> context) {
        String input = StringArgumentType.getString(context, "text");
        Jarvis.LOGGER.info("text: {}", input);
        ServerCommandSource source = context.getSource();

        try {
            ContentResponseBody resultBody = parser.parse(input);
            if (resultBody.isSuccess()) {
                source.getServer().getCommandManager().executeWithPrefix(
                        source,
                        resultBody.getCommand()
                );
            }

            source.sendMessage(Text.of(resultBody.getAdditionalInfo()));
            return 1;
        } catch (Exception e) {
            source.sendError(Text.of("命令执行失败：" + e.getMessage()));
            return 0;
        }
    }
}
