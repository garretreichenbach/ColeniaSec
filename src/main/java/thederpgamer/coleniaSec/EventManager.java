package thederpgamer.coleniaSec;

import com.onarandombox.multiverseinventories.event.WorldChangeShareHandlingEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.potion.PotionEffect;
import thederpgamer.coleniaSec.data.DataManager;
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
		if(event.getToWorld().equals(buildingWorldName)) {
			PlayerData playerData = new PlayerData(DataManager.getData("player_data", event.getPlayer().getUniqueId().toString()));
			ArrayList<PotionEffect> potionEffects = new ArrayList<>(event.getPlayer().getActivePotionEffects());
			playerData.getWorldData().getPotionEffects().clear();
			playerData.getWorldData().getPotionEffects().addAll(potionEffects);
			event.getPlayer().clearActivePotionEffects();
		} else if(event.getFromWorld().equals(buildingWorldName)) {
			PlayerData playerData = new PlayerData(DataManager.getData("player_data", event.getPlayer().getUniqueId().toString()));
			ArrayList<PotionEffect> potionEffects = playerData.getWorldData().getPotionEffects();
			for(PotionEffect potionEffect : potionEffects) event.getPlayer().addPotionEffect(potionEffect);
		}
	}
}
