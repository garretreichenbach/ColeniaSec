package thederpgamer.coleniaSec.data.player;

import com.google.gson.*;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.UUID;

public class PlayerData implements JsonDeserializer<PlayerData>, JsonSerializer<PlayerData>, Comparable<PlayerData> {

	protected String uuid;
	protected String name;
	protected PlayerWorldData worldData;

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
			uuid = jsonObject.get("uuid").getAsString();
			name = jsonObject.get("name").getAsString();
			worldData = new PlayerWorldData(jsonObject.get("worldData"));
		} else throw new JsonParseException("Invalid JSON format for PlayerData");
		return this;
	}

	@Override
	public JsonElement serialize(PlayerData playerData, Type type, JsonSerializationContext jsonSerializationContext) {
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("uuid", uuid);
		jsonObject.addProperty("name", name);
		jsonObject.add("worldData", jsonSerializationContext.serialize(worldData));
		return jsonObject;
	}

	@Override
	public int compareTo(@NotNull PlayerData o) {
		return uuid.compareTo(o.uuid);
	}

	public static class PlayerWorldData implements JsonDeserializer<PlayerWorldData>, JsonSerializer<PlayerWorldData>, Comparable<PlayerWorldData> {

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

		public ArrayList<PotionEffect> getPotionEffects() {
			return potionEffects;
		}
	}
}