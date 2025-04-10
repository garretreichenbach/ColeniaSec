package thederpgamer.coleniaSec;

import org.bukkit.event.Listener;

/**
 * [Description]
 *
 * @author TheDerpGamer
 */
public class EventManager implements Listener {

	private static ColeniaSec plugin;

	public static void initialize(ColeniaSec plugin) {
		EventManager.plugin = plugin;
		plugin.getServer().getPluginManager().registerEvents(new EventManager(), plugin);
	}
}
