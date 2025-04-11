package thederpgamer.coleniaSec.data;

import com.google.common.io.Files;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.jetbrains.annotations.NotNull;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.nio.charset.StandardCharsets;

public class DataManager {

	private static final File dataDirectory = new File("plugins/ColeniaSec/data/");

	public static void initialize() {
		if(!dataDirectory.exists()) dataDirectory.mkdirs();
	}

	private static File getDataDirectory(String type) {
		return new File(dataDirectory, type);
	}

	public static JsonArray getAllData(@NotNull String type) {
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
					exception.printStackTrace();
				}
			}
		}
		return jsonArray;
	}

	public static JsonElement getData(@NotNull String type, @NotNull String uuid) {
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
					exception.printStackTrace();
				}
			}
		}
		return null;
	}
}