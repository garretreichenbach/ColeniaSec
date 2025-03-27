package thederpgamer.coleniaSec;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

/**
 * [Description]
 *
 * @author Garret Reichenbach
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
			exception.printStackTrace();
		}
	}

	private static void saveDefaultConfig() {
		config.options().copyDefaults(true);
		config.addDefault("staffmode-enabled", true);
		config.addDefault("staffmode-persist-on-relog", true);
		config.addDefault("staffmode-no-action-timeout", 900000);
		config.addDefault("staffmode-exempted-roles", new String[] {"Owner"});
		config.addDefault("staffmode-exempted-permissions", new String[] {"coleniaSec.staffmode"});
		config.addDefault("staffmode-auto-reset-gamemode", true);
		config.addDefault("use-discord-webhook", true);
		config.addDefault("discord-webhook-url", "[Discord Webhook URL]");
	}

	private static void saveDefaultMessages() {
		messagesConfig.options().copyDefaults(true);
		messagesConfig.addDefault("staff-mode-required", "&cYou must be in staff mode to use this command.");
		messagesConfig.addDefault("staff-mode-enabled", "&aStaff mode enabled.");
		messagesConfig.addDefault("staff-mode-disabled", "&cStaff mode disabled.");
	}
}
