package thederpgamer.coleniaSec.commands;

import org.bukkit.command.Command;
import thederpgamer.coleniaSec.ColeniaSec;

import java.util.Objects;

/**
 * [Description]
 *
 * @author Garret Reichenbach
 */
public class CommandManager {

	private static ColeniaSec plugin;

	public static void initialize(ColeniaSec plugin) {
		CommandManager.plugin = plugin;
		Objects.requireNonNull(plugin.getCommand("staffmode")).setExecutor(new StaffModeCommand());
	}

	public static Command getCommand(String commandName) {
		return plugin.getServer().getCommandMap().getCommand(commandName);
	}
}
