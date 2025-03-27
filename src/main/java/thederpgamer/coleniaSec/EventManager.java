package thederpgamer.coleniaSec;

import net.luckperms.api.model.group.Group;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import thederpgamer.coleniaSec.commands.CommandManager;
import thederpgamer.coleniaSec.data.StaffModeManager;
import thederpgamer.coleniaSec.data.permissions.PermissionManager;

import java.util.Arrays;
import java.util.Objects;

/**
 * [Description]
 *
 * @author Garret Reichenbach
 */
public class EventManager implements Listener {

	private static ColeniaSec plugin;

	public static void initialize(ColeniaSec plugin) {
		EventManager.plugin = plugin;
		plugin.getServer().getPluginManager().registerEvents(new EventManager(), plugin);
	}

	/**
	 * Command event handler. Intercepts any admin commands and sends them to the staff logger to verify if the sender is in staff mode.
	 * <br/>If the sender is not in staff mode, it cancels the event and notifies the user.
	 */
	@EventHandler(priority = EventPriority.LOWEST)
	public void onCommand(PlayerCommandPreprocessEvent event) {
		if(!ConfigManager.config.getBoolean("staffmode-enabled")) {
			plugin.logDebug("Not checking command event as staff mode is disabled.");
			return;
		}
		String commandName = event.getMessage().split(" ")[0].replace("/", "");
		Command command = CommandManager.getCommand(commandName);
		if(command != null) {
			try {
				String permissionRaw = command.getPermission();
				if(Objects.requireNonNull(ConfigManager.config.getList("staffmode-exempted-permissions")).contains(permissionRaw)) return;
				if(permissionRaw != null) {
					Permission permission = Bukkit.getPluginManager().getPermission(permissionRaw);
					if(permission != null) {
						if(permission.getDefault() != PermissionDefault.OP || !event.getPlayer().isOp()) {
							plugin.logDebug("Command \"" + commandName + "\" requires permission \"" + permission.getName() + "\"");
							return;
						}
						Group group = PermissionManager.getGroup(event.getPlayer());
						if(Objects.requireNonNull(ConfigManager.config.getList("staffmode-exempted-roles")).contains(group.getName())) {
							plugin.logDebug("Player \"" + event.getPlayer().getName() + "\" is exempted from having to use staff mode, skipping command check.");
							return;
						}
						if(!commandName.equals("staffmode")) {
							if(!StaffModeManager.isStaffModeEnabled(event.getPlayer())) {
								plugin.logInfo("Denying command \"" + commandName + "\" usage for player \"" + event.getPlayer().getName() + "\" as they are not in staff mode.");
								event.getPlayer().sendMessage(Objects.requireNonNull(ConfigManager.messagesConfig.getString("staff-mode-required")));
								event.setCancelled(true);
							} else {
								plugin.logInfo("Staff Command Usage: " + event.getPlayer().getName() + " used command \"" + commandName + "\" with arguments \"" + Arrays.toString(event.getMessage().replaceFirst("/" + commandName, "").split(" ")) + "\"");
								WebHookManager.sendStaffModeCommandUsageWebHook(event.getPlayer().getName(), commandName, event.getMessage().replaceFirst("/" + commandName, "").split(" "));
							}
						} else {
							plugin.logInfo("Staff Command Usage: " + event.getPlayer().getName() + " used command \"" + commandName + "\" with arguments \"" + Arrays.toString(event.getMessage().replaceFirst("/" + commandName, "").split(" ")) + "\"");
							WebHookManager.sendStaffModeCommandUsageWebHook(event.getPlayer().getName(), commandName, event.getMessage().replaceFirst("/" + commandName, "").split(" "));
						}
					}
				}
			} catch(Exception exception) {
				plugin.logWarning("Failed to check command permission for command \"" + commandName + "\"");
			}
		}
	}
}
