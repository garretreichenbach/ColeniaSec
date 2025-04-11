package thederpgamer.coleniaSec;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

/**
 * [Description]
 *
 * @author TheDerpGamer
 */
public class ConfigManager {

	public static FileConfiguration config;
	public static FileConfiguration messagesConfig;

	public static void initialize(ColeniaSec plugin) {
		try {
			File configFile = new File(plugin.getDataFolder(), "config.yml");
			if(!configFile.exists()) {
				configFile.getParentFile().mkdirs();
				plugin.saveResource("config.yml", false);
			}
			config = new YamlConfiguration();
			saveDefaultConfig();
			config.load(configFile);

			File messagesFile = new File(plugin.getDataFolder(), "messages.yml");
			if(!messagesFile.exists()) {
				messagesFile.getParentFile().mkdirs();
				plugin.saveResource("messages.yml", false);
			}
			messagesConfig = new YamlConfiguration();
			saveDefaultMessages();
			messagesConfig.load(messagesFile);
		} catch(Exception exception) {
			plugin.logError("An error occurred while initializing the configuration files.", exception);
		}
	}

	private static void saveDefaultConfig() {
		config.options().copyDefaults(true);
		config.addDefault("use-discord-webhook", false);
		config.addDefault("discord-webhook-url", "[Discord Webhook URL]");
		config.addDefault("building-world-name", "Creative_World");
	}

	private static void saveDefaultMessages() {
		messagesConfig.options().copyDefaults(true);
	}
}
