package thederpgamer.coleniaSec;

import org.bukkit.plugin.java.JavaPlugin;

public final class ColeniaSec extends JavaPlugin {
	
	public static ColeniaSec plugin;

	@Override
	public void onEnable() {
		plugin = this;
		logInfo("Enabling ColeniaSec...");
		APIManager.initialize(this);
		ConfigManager.initialize(this);
		EventManager.initialize(this);
		PermissionManager.initialize(this);
		CommandManager.initialize(this);
		logInfo("ColeniaSec has been enabled.");
	}

	@Override
	public void onDisable() {
		logInfo("Disabling ColeniaSec...");
		logInfo("ColeniaSec has been disabled.");
	}
	
	public void logDebug(String message) {
		if(ConfigManager.config.getBoolean("debug-mode")) getLogger().log(Level.DEBUG, message);
	}
	
	public void logInfo(String message) {
		getLogger().log(Level.INFO, message);
	}
	
	public void logWarning(String message) {
		getLogger().log(Level.WARNING, message);
	}
	
	public void logError(String message) {
		getLogger().log(Level.SEVERE, message);
	}
}
