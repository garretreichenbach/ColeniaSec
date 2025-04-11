package thederpgamer.coleniaSec;

import java.util.Date;
import java.util.Objects;

/**
 * [Description]
 *
 * @author TheDerpGamer
 */
public class WebHookManager {

	public static void sendStaffModeWebHookEnable(String playerName, String reason) {
		if(!ConfigManager.config.getBoolean("use-discord-webhook")) return;
		if(Objects.equals(ConfigManager.config.getString("discord-webhook-url"), "[Discord Webhook URL]")) {
			ColeniaSec.plugin.logWarning("Invalid Discord Webhook URL. Please set a valid URL in the config.yml file.");
			return;
		}
		DiscordWebhook webhook = new DiscordWebhook(getWebHookUrl());
		webhook.setUsername("ColeniaSec");
		DiscordWebhook.EmbedObject embed = new DiscordWebhook.EmbedObject();
		embed.setTitle("Staff Mode Enabled");
		embed.addField("Time Stamp", String.valueOf(new Date()), true);
		embed.addField("Player", playerName, true);
		embed.addField("Reason", reason, false);
		webhook.addEmbed(embed);
		try {
			webhook.execute();
		} catch(Exception exception) {
			ColeniaSec.plugin.logError("Failed to send Discord Webhook message: " + exception.getMessage(), exception);
		}
	}

	public static void sendStaffModeWebHookDisable(String playerName) {
		if(!ConfigManager.config.getBoolean("use-discord-webhook")) return;
		if(Objects.equals(ConfigManager.config.getString("discord-webhook-url"), "[Discord Webhook URL]")) {
			ColeniaSec.plugin.logWarning("Invalid Discord Webhook URL. Please set a valid URL in the config.yml file.");
			return;
		}
		DiscordWebhook webhook = new DiscordWebhook(getWebHookUrl());
		webhook.setUsername("ColeniaSec");
		DiscordWebhook.EmbedObject embed = new DiscordWebhook.EmbedObject();
		embed.setTitle("Staff Mode Disabled");
		embed.addField("Time Stamp", String.valueOf(new Date()), true);
		embed.addField("Player", playerName, true);
		webhook.addEmbed(embed);
		try {
			webhook.execute();
		} catch(Exception exception) {
			ColeniaSec.plugin.logError("Failed to send Discord Webhook message: " + exception.getMessage(), exception);
		}
	}

	public static void sendStaffModeCommandUsageWebHook(String playerName, String command, String[] args) {
		if(!ConfigManager.config.getBoolean("use-discord-webhook")) return;
		if(Objects.equals(ConfigManager.config.getString("discord-webhook-url"), "[Discord Webhook URL]")) {
			ColeniaSec.plugin.logWarning("Invalid Discord Webhook URL. Please set a valid URL in the config.yml file.");
			return;
		}
		DiscordWebhook webhook = new DiscordWebhook(getWebHookUrl());
		webhook.setUsername("ColeaniaSec");
		DiscordWebhook.EmbedObject embed = new DiscordWebhook.EmbedObject();
		embed.setTitle("Staff Command Usage");
		embed.addField("Time Stamp", String.valueOf(new Date()), true);
		embed.addField("Player", playerName, true);
		embed.addField("Command", command, true);
		embed.addField("Arguments", String.join(" ", args), false);
		webhook.addEmbed(embed);
		try {
			webhook.execute();
		} catch(Exception exception) {
			ColeniaSec.plugin.logError("Failed to send Discord Webhook message: " + exception.getMessage(), exception);
		}
	}

	private static String getWebHookUrl() {
		return ConfigManager.config.getString("discord-webhook-url");
	}
}
