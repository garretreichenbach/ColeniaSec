package thederpgamer.coleniaSec.data.permissions;

import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.user.User;
import org.bukkit.entity.Player;
import thederpgamer.coleniaSec.APIManager;
import thederpgamer.coleniaSec.ColeniaSec;

/**
 * Manages permissions.
 *
 * @author Garret Reichenbach
 */
public class PermissionManager {

	private static ColeniaSec plugin;

	public static void initialize(ColeniaSec plugin) {
		PermissionManager.plugin = plugin;
	}

	public static boolean hasPermission(Player player, String permission) {
		User user = APIManager.getLuckPerms().getUserManager().getUser(player.getUniqueId());
		if(user != null) return user.getCachedData().getPermissionData().checkPermission(permission).asBoolean();
		else return player.hasPermission(permission);
	}

	public static boolean hasPermission(Group group, String permission) {
		return group.getCachedData().getPermissionData().checkPermission(permission).asBoolean();
	}

	public static Group getGroup(Player player) {
		User user = APIManager.getLuckPerms().getUserManager().getUser(player.getUniqueId());
		String groupName = "default";
		if(user != null) groupName = user.getPrimaryGroup();
		return APIManager.getLuckPerms().getGroupManager().getGroup(groupName);
	}
}
