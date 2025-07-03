package com.promising.jarvis;

import com.promising.jarvis.core.register.NLRegister;
import com.promising.jarvis.core.register.NLRegisterFactory;
import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class Jarvis implements ModInitializer {
	public static final String MOD_ID = "jarvis";
	private static Properties configProperties;

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		for(NLRegister register : NLRegisterFactory.createRegisters()){
			register.register();
		}
		LOGGER.info("Hello Fabric world!");
	}

	public static Properties getConfigProperties() {
		return configProperties;
	}

	private static void loadConfig() {
		configProperties = new Properties();
		Path configPath = Paths.get("config", "jarvis.properties");

		try {
			if (Files.exists(configPath)) {
				configProperties.load(Files.newInputStream(configPath));
			} else {
				// 创建默认配置
				configProperties.setProperty("deepseek_api_token", "YOUR_DEFAULT_TOKEN");
				Files.createDirectories(configPath.getParent());
				Files.write(configPath, () -> configProperties.entrySet().stream()
						.<CharSequence>map(e -> e.getKey() + "=" + e.getValue())
						.iterator());
			}
		} catch (IOException e) {
			LOGGER.error("Failed to load configuration", e);
		}
	}
}