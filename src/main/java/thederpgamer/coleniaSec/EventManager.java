package thederpgamer.coleniaSec;

import com.onarandombox.multiverseinventories.event.WorldChangeShareHandlingEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.potion.PotionEffect;
import thederpgamer.coleniaSec.data.player.PlayerData;

import java.util.ArrayList;

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

	@EventHandler
	public void onPlayerWorldChange(WorldChangeShareHandlingEvent event) {
		String buildingWorldName = ConfigManager.config.getString("building-world-name");
		PlayerData playerData = PlayerData.getDataFromPlayer(event.getPlayer());
		if(event.getToWorld().equals(buildingWorldName)) {
			ArrayList<PotionEffect> potionEffects = new ArrayList<>(event.getPlayer().getActivePotionEffects());
			playerData.getWorldData().getPotionEffects().clear();
			playerData.getWorldData().getPotionEffects().addAll(potionEffects);
			event.getPlayer().clearActivePotionEffects();
		} else if(event.getFromWorld().equals(buildingWorldName)) {
			ArrayList<PotionEffect> potionEffects = playerData.getWorldData().getPotionEffects();
			for(PotionEffect potionEffect : potionEffects) event.getPlayer().addPotionEffect(potionEffect);
		}
		playerData.getWorldData().setLastWorld(event.getToWorld());
		playerData.save();
	}
}
