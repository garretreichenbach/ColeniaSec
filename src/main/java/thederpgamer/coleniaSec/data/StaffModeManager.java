package thederpgamer.coleniaSec.data;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import thederpgamer.coleniaSec.ConfigManager;

import java.util.HashMap;
import java.util.Objects;

/**
 * [Description]
 *
 * @author Garret Reichenbach
 */
public class StaffModeManager {

	private static final HashMap<String, Boolean> staffMode = new HashMap<>();
	private static final HashMap<String, Long> staffModeTimeout = new HashMap<>();

	public static boolean isStaffModeEnabled(Player player) {
		return staffMode.getOrDefault(player, false);
	}

	public static void setStaffMode(String player, boolean enabled) {
		staffMode.put(player, enabled);
		if(enabled) staffModeTimeout.put(player, System.currentTimeMillis());
		else staffModeTimeout.remove(player);
	}
	
	public static void update() {
		for(String staff : staffMode.keySet()) {
			if(staffMode.get(staff) && System.currentTimeMillis() - staffModeTimeout.get(staff) > ConfigManager.config.getLong("staffmode-no-action-timeout")) {
				setStaffMode(staff, false);
				Player player = Bukkit.getPlayer(staff);
				if(player != null) {
					if(ConfigManager.config.getBoolean("staffmode-auto-reset-gamemode")) player.setGameMode(org.bukkit.GameMode.SURVIVAL);
					player.sendMessage(Objects.requireNonNull(ConfigManager.messagesConfig.getString("staff-mode-disabled-timeout")));
				}
			}
		}
	}
}
