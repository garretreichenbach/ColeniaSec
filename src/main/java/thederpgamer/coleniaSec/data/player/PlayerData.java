package thederpgamer.coleniaSec.data.player;

import com.google.gson.*;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.jetbrains.annotations.NotNull;
import thederpgamer.coleniaSec.data.DataManager;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.UUID;

/**
 * Class representing stored player data.
 */
public class PlayerData implements JsonDeserializer<PlayerData>, JsonSerializer<PlayerData>, Comparable<PlayerData> {

	private static final byte VERSION = 0;
	protected String uuid;
	protected String name;
	protected PlayerWorldData worldData;

	public static PlayerData getDataFromPlayer(Player player) {
		if(!DataManager.dataExists(DataManager.DataTypes.PLAYER, player.getUniqueId().toString())) {
			DataManager.createNewData(DataManager.DataTypes.PLAYER, player.getUniqueId().toString(), player.getName());
		}
		return new PlayerData(DataManager.getData(DataManager.DataTypes.PLAYER, player.getUniqueId().toString()));
	}

	public PlayerData(Player player) {
		uuid = player.getUniqueId().toString();
		name = player.getName();
		worldData = new PlayerWorldData(player.getWorld().getName(), player.getActivePotionEffects());
	}

	public PlayerData(JsonElement jsonElement) {
		deserialize(jsonElement, PlayerData.class, null);
	}

	public String getUUID() {
		return uuid;
	}

	public String getName() {
		return name;
	}

	public PlayerWorldData getWorldData() {
		return worldData;
	}

	@Override
	public PlayerData deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
		if(jsonElement.isJsonObject()) {
			JsonObject jsonObject = jsonElement.getAsJsonObject();
			byte version = jsonObject.get("version").getAsByte();
			uuid = jsonObject.get("uuid").getAsString();
			name = jsonObject.get("name").getAsString();
			worldData = new PlayerWorldData(jsonObject.get("world_data"));
		} else throw new JsonParseException("Invalid JSON format for PlayerData");
		return this;
	}

	@Override
	public JsonElement serialize(PlayerData playerData, Type type, JsonSerializationContext jsonSerializationContext) {
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("version", VERSION);
		jsonObject.addProperty("uuid", uuid);
		jsonObject.addProperty("name", name);
		jsonObject.add("world_data", jsonSerializationContext.serialize(worldData));
		return jsonObject;
	}

	@Override
	public int compareTo(@NotNull PlayerData o) {
		return uuid.compareTo(o.uuid);
	}
	
	public void save() {
		DataManager.saveData(DataManager.DataTypes.PLAYER, uuid, serialize(this, PlayerData.class, null));
	}

	public static class PlayerWorldData implements JsonDeserializer<PlayerWorldData>, JsonSerializer<PlayerWorldData>, Comparable<PlayerWorldData> {

		private static final byte VERSION = 0;
		private String uuid;
		private String lastWorld;
		private ArrayList<PotionEffect> potionEffects = new ArrayList<>();

		public PlayerWorldData(@NotNull String lastWorld, @NotNull Collection<PotionEffect> potionEffects) {
			uuid = UUID.randomUUID().toString();
			this.lastWorld = lastWorld;
			this.potionEffects.addAll(potionEffects);
		}

		public PlayerWorldData(JsonElement json) {
			deserialize(json, PlayerWorldData.class, null);
		}

		@Override
		public PlayerWorldData deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
			if(jsonElement.isJsonObject()) {
				JsonObject jsonObject = jsonElement.getAsJsonObject();
				byte version = jsonObject.get("version").getAsByte();
				uuid = jsonObject.get("uuid").getAsString();
				lastWorld = jsonObject.get("world_name").getAsString();
				JsonArray potionEffectsArray = jsonObject.getAsJsonArray("potion_effects");
				for(JsonElement potionEffectElement : potionEffectsArray) {
					JsonObject potionEffectObject = potionEffectElement.getAsJsonObject();
					String typeName = potionEffectObject.get("type").getAsString();
					int duration = potionEffectObject.get("duration").getAsInt();
					int amplifier = potionEffectObject.get("amplifier").getAsInt();
					PotionEffect potionEffect = new PotionEffect(Objects.requireNonNull(Registry.EFFECT.get(Objects.requireNonNull(NamespacedKey.fromString(typeName)))), duration, amplifier);
					potionEffects.add(potionEffect);
				}
			} else throw new JsonParseException("Invalid JSON format for PlayerWorldData");
			return this;
		}

		@Override
		public JsonElement serialize(PlayerWorldData playerData, Type type, JsonSerializationContext jsonSerializationContext) {
			JsonObject jsonObject = new JsonObject();
			jsonObject.addProperty("version", VERSION);
			jsonObject.addProperty("uuid", uuid);
			jsonObject.addProperty("last_world", lastWorld);
			JsonArray potionEffectsArray = new JsonArray();
			for(PotionEffect potionEffect : potionEffects) {
				JsonObject potionEffectObject = new JsonObject();
				potionEffectObject.addProperty("type", potionEffect.getType().getKey().toString());
				potionEffectObject.addProperty("duration", potionEffect.getDuration());
				potionEffectObject.addProperty("amplifier", potionEffect.getAmplifier());
				potionEffectsArray.add(potionEffectObject);
			}
			return jsonObject;
		}

		@Override
		public int compareTo(@NotNull PlayerWorldData o) {
			return uuid.compareTo(o.uuid);
		}

		@Override
		public String toString() {
			return serialize(this, PlayerWorldData.class, null).toString();
		}

		public String getUUID() {
			return uuid;
		}

		public String getLastWorld() {
			return lastWorld;
		}
		
		public void setLastWorld(String lastWorld) {
			this.lastWorld = lastWorld;
		}

		public ArrayList<PotionEffect> getPotionEffects() {
			return potionEffects;
		}
	}
}