package io.github.pikibanana.binding;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import io.github.pikibanana.features.FinderConfiguration;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.io.IOException;

@Environment(EnvType.CLIENT)
public final class FinderConfigurationBinding extends Binding<FinderConfiguration> {
    private static final ColorBinding COLOR_BINDING = new ColorBinding(Color.BLACK);

    public FinderConfigurationBinding(@NotNull FinderConfiguration defaultValue) {
        super(defaultValue);
    }

    @Override
    public void write(JsonWriter writer) {
        try {
            writer.name("version");
            writer.value("1.0.0");

            writer.name("enabled");
            writer.value(getValue().enabled());
            writer.name("color");
            writer.beginObject();

            COLOR_BINDING.bind(getValue().color());
            COLOR_BINDING.write(writer);

            writer.endObject();
        }
        catch (IOException e) {
            LOGGER.error("Failed to write binding! Stack trace: {}", e);
        }
    }

    @Override
    public void read(JsonReader reader) {
        try {
            reader.nextName();
            String versionNumber = reader.nextString();

            if (versionNumber.equals("1.0.0")) {
                reader.nextName();
                boolean enabled = reader.nextBoolean();

                reader.nextName();
                reader.beginObject();

                COLOR_BINDING.read(reader);
                Color color = COLOR_BINDING.getValue();

                reader.endObject();

                bind(new FinderConfiguration(enabled, color));
            }
            else {
                throw new UnsupportedOperationException();
            }
        }
        catch (IOException e) {
            LOGGER.error("Failed to read binding! Stack trace: {}", e);
        }
    }
}
