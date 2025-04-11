package thederpgamer.coleniaSec.data;

import com.google.common.io.Files;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.jetbrains.annotations.NotNull;
import org.json.simple.parser.JSONParser;
import thederpgamer.coleniaSec.ColeniaSec;
import thederpgamer.coleniaSec.data.player.PlayerData;

import java.io.File;
import java.nio.charset.StandardCharsets;

/**
 * DataManager is responsible for managing data files in the plugin.
 * <p>Technically, we should be keeping an in-memory map rather than reading and writing from disk all the time,
 * but (as of 4/11/2025) none of the data being saved is very large (just small json strings), so we can probably
 * get away with this for now.</p>
 * Todo: Eventually, data needs to be cached in some sort of static map so that we are only reading and writing from disk on server start and shutdown.
 */
public class DataManager {

	/**
	 * Enum representing the different types of data.
	 */
	public enum DataTypes {

		PLAYER(PlayerData.class, args -> {
			JsonObject jsonObject = new JsonObject();
			jsonObject.addProperty("uuid", (String) args[0]);
			jsonObject.addProperty("name", (String) args[1]);
			jsonObject.add("worldData", new JsonObject());
			return jsonObject;
		});

		public final Class<?> dataClass;
		public final IDataFactory dataFactory;

		DataTypes(Class<?> dataClass, IDataFactory dataFactory) {
			this.dataClass = dataClass;
			this.dataFactory = dataFactory;
		}
	}

	/**
	 * Functional interface for creating new data.
	 */
	@FunctionalInterface
	public interface IDataFactory {

		/**
		 * Creates new data.
		 * @param args Arguments to create the data.
		 * @return The created data as a JsonElement.
		 */
		JsonElement createNewData(@NotNull Object... args);
	}

	private static final File dataDirectory = new File("plugins/ColeniaSec/data/");

	private static ColeniaSec plugin;

	public static void initialize(ColeniaSec plugin) {
		DataManager.plugin = plugin;
		if(!dataDirectory.exists()) dataDirectory.mkdirs();
	}

	private static File getDataDirectory(DataTypes type) {
		return new File(dataDirectory, type.name());
	}

	/**
	 * Gets all data of the specified type.
	 * @param type The type of data to get.
	 * @return A JsonArray containing all data of the specified type.
	 */
	public static JsonArray getAllData(@NotNull DataTypes type) {
		JsonArray jsonArray = new JsonArray();
		File[] files = getDataDirectory(type).listFiles();
		if(files != null) {
			for(File file : files) {
				try {
					if(file.isFile() && file.getName().endsWith(".json")) {
						String rawJson = Files.asCharSource(file, StandardCharsets.UTF_8).read();
						jsonArray.add(JsonParser.parseString(rawJson));
					}
				} catch(Exception exception) {
					plugin.logError("Failed to load data from file: " + file.getName(), exception);
				}
			}
		}
		return jsonArray;
	}

	/**
	 * Gets the data of the specified type for the given UUID.
	 * @param type The type of data to get.
	 * @param uuid The UUID of the data to get.
	 * @return The data as a JsonElement, or null if not found.
	 */
	public static JsonElement getData(@NotNull DataTypes type, @NotNull String uuid) {
		File[] files = getDataDirectory(type).listFiles();
		if(files != null) {
			for(File file : files) {
				try {
					if(file.isFile() && file.getName().endsWith(".json")) {
						String rawJson = Files.asCharSource(file, StandardCharsets.UTF_8).read();
						JsonObject jsonObject = (JsonObject) new JSONParser().parse(rawJson);
						if(jsonObject.get("uuid").getAsString().equals(uuid)) return jsonObject;
					}
				} catch(Exception exception) {
					plugin.logError("Failed to load data for " + uuid + " from file: " + file.getName(), exception);
				}
			}
		}
		return null;
	}

	/**
	 * Saves the given data to a file.
	 * @param type The type of data to save.
	 * @param uuid The UUID of the data to save.
	 * @param jsonElement The data to save as a JsonElement.
	 */
	public static void saveData(@NotNull DataTypes type, @NotNull String uuid, @NotNull JsonElement jsonElement) {
		File dataDirectory = getDataDirectory(type);
		if(!dataDirectory.exists()) dataDirectory.mkdirs();
		File file = new File(dataDirectory, uuid + ".json");
		try {
			Files.write(jsonElement.toString().getBytes(StandardCharsets.UTF_8), file);
		} catch(Exception exception) {
			plugin.logError("Failed to save data for " + uuid + " to file: " + file.getName(), exception);
		}
	}

	/**
	 * Checks if data of the specified type exists for the given UUID.
	 * @param type The type of data to check.
	 * @param uuid The UUID of the data to check.
	 * @return True if data exists, false otherwise.
	 */
	public static boolean dataExists(@NotNull DataTypes type, @NotNull String uuid) {
		File[] files = getDataDirectory(type).listFiles();
		if(files != null) {
			for(File file : files) {
				if(file.isFile() && file.getName().equals(uuid + ".json")) return true;
			}
		}
		return false;
	}

	/**
	 * Creates new data of the specified type and saves it to a file.
	 * @param type The type of data to create.
	 * @param args The arguments to create the data. <b>{@code args[0]} must be a UUID string!</b>
	 * @return The created data as a JsonElement.
	 */
	public static JsonElement createNewData(DataTypes type, Object... args) {
		JsonElement jsonElement = type.dataFactory.createNewData(args);
		saveData(type, (String) args[0], jsonElement);
		return jsonElement;
	}
}