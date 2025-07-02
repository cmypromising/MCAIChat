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
import net.minecraft.util.math.BlockPos;

import java.util.Objects;

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
        String currentPlayerInfo = getCurrentPlayerInformation(source);

        try {
            ContentResponseBody resultBody = parser.parse(input, currentPlayerInfo);
            if (resultBody.getType() == 1) {
                source.getServer().getCommandManager().executeWithPrefix(
                        source,
                        resultBody.getCommand()
                );
            }
            source.sendMessage(Text.of("Jarvis: " + resultBody.getAdditionalInfo()));
            return 1;
        } catch (Exception e) {
            source.sendError(Text.of("命令执行失败：" + e.getMessage()));
            return 0;
        }
    }

    private static String getCurrentPlayerInformation(ServerCommandSource source) {
        // 玩家名称
        String playerName = source.getName();

        // 获取玩家的位置
        BlockPos playerPosition = Objects.requireNonNull(source.getPlayer()).getBlockPos(); // 玩家位置
        double posX = playerPosition.getX();
        double posY = playerPosition.getY();
        double posZ = playerPosition.getZ();

        // 获取玩家的健康、饥饿和经验
        float health = source.getPlayer().getHealth(); // 健康值
        int foodLevel = source.getPlayer().getHungerManager().getFoodLevel(); // 饥饿值
        int experience = source.getPlayer().experienceLevel; // 经验值

        // 拼接信息为字符串
        return String.format("\n姓名: %s \n坐标: (%.2f, %.2f, %.2f)\n健康值: %.1f\n饥饿值: %d\n经验值: %d",
                playerName, posX, posY, posZ, health, foodLevel, experience);
    }
}
