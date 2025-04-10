package thederpgamer.coleniaSec;

import org.bukkit.plugin.java.JavaPlugin;
import thederpgamer.coleniaSec.commands.CommandManager;
import thederpgamer.coleniaSec.data.permissions.PermissionManager;

import java.util.logging.Level;

public final class ColeniaSec extends JavaPlugin {
	
	public static ColeniaSec plugin;

	@Override
	public void onEnable() {
		plugin = this;
		APIManager.initialize(this);
		ConfigManager.initialize(this);
		EventManager.initialize(this);
		PermissionManager.initialize(this);
		CommandManager.initialize(this);
	}

	@Override
	public void onDisable() {
	}
	
	public void logDebug(String message) {
		if(ConfigManager.config.getBoolean("debug-mode")) getLogger().log(Level.FINE, message);
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
