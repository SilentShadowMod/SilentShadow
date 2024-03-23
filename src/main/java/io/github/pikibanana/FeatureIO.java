package io.github.pikibanana;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.mojang.logging.LogUtils;
import io.github.pikibanana.features.Feature;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

@Environment(EnvType.CLIENT)
public final class FeatureIO {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final File FILE = new File(FabricLoader.getInstance().getConfigDir().toFile(), "silentshadow/options.json");

    public static void init() {
        create();
    }

    public static void create() {
        FILE.getParentFile().mkdirs();
        try {
            if (FILE.createNewFile()) {
                save();
            } else {
                load();
            }
        }
        catch (Exception e) {
            LOGGER.error("Failed to create options! Stack trace: %s", e);
        }
    }

    public static void save() {
        try (FileWriter fileWriter = new FileWriter(FILE);
             JsonWriter jsonWriter = GSON.newJsonWriter(fileWriter)) {
            jsonWriter.beginObject();
            for (Feature<?> feature : Feature.getFeatures()) {
                jsonWriter.name(feature.getId());

                jsonWriter.beginObject();
                feature.getBinding().write(jsonWriter);
                jsonWriter.endObject();
            }
            jsonWriter.endObject();
        }
        catch (Exception e) {
            LOGGER.error("Failed to save option file! Stack trace: %s", e);
        }
    }

    public static void load() {
        try (FileReader fileReader = new FileReader(FILE);
             JsonReader jsonReader = GSON.newJsonReader(fileReader)) {
            jsonReader.beginObject();
            while (jsonReader.hasNext()) {
                String id = jsonReader.nextName();

                jsonReader.beginObject();
                Feature.getFeatureByID(id).getBinding().read(jsonReader);
                jsonReader.endObject();
            }
            jsonReader.endObject();
        }
        catch (Exception e) {
            LOGGER.error("Failed to load option file! Stack trace: %s", e);
        }
    }
}