package thederpgamer.coleniaSec.commands;

import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import thederpgamer.coleniaSec.ConfigManager;
import thederpgamer.coleniaSec.WebHookManager;
import thederpgamer.coleniaSec.data.StaffModeManager;

import java.util.Objects;

/**
 * [Description]
 *
 * @author Garret Reichenbach
 */
public class StaffModeCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(sender instanceof Player player) {
			if(args.length < 3) {
				player.sendMessage("§cUsage: /staffmode <reason>");
				return true;
			}
			if(StaffModeManager.isStaffModeEnabled(player)) {
				StaffModeManager.setStaffMode(player.getName(), false);
				if(ConfigManager.config.getBoolean("staffmode-auto-reset-gamemode")) player.setGameMode(GameMode.SURVIVAL);
				WebHookManager.sendStaffModeWebHookDisable(player.getName());
				player.sendMessage(Objects.requireNonNull(ConfigManager.messagesConfig.getString("staff-mode-disabled")));
			} else {
				String reason = String.join(" ", args).replace("\"", "");
				StaffModeManager.setStaffMode(player.getName(), true);
				WebHookManager.sendStaffModeWebHookEnable(player.getName(), reason);
				player.sendMessage(Objects.requireNonNull(ConfigManager.messagesConfig.getString("staff-mode-enabled")));
			}
			return true;
		} else return false;
	}
}