package io.github.pikibanana.binding;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@Environment(EnvType.CLIENT)
public final class BooleanBinding extends Binding<Boolean> {
    private static final Logger LOGGER = LoggerFactory.getLogger(BooleanBinding.class);

    public BooleanBinding(@NotNull Boolean defaultValue) {
        super(defaultValue);
    }

    @Override
    public void write(JsonWriter writer) {
        try {
            writer.name("version");
            writer.value("1.0.0");

            writer.name("value");
            writer.value(getValue().booleanValue());
        }
        catch (IOException e) {
            LOGGER.error("Failed to write binding! Stack trace: {}", e);
        }
    }

    @Override
    public void read(JsonReader reader) {
        try {
            String versionName = reader.nextName();
            if (versionName.equals("version")) {
                String versionNumber = reader.nextString();

                if (versionNumber.equals("1.0.0")) {
                    reader.nextName();
                    bind(Boolean.valueOf(reader.nextBoolean()));
                }
                else {
                    throw new UnsupportedOperationException();
                }
            }
            else {
                bind(Boolean.valueOf(reader.nextBoolean()));
            }
        }
        catch (IOException e) {
            LOGGER.error("Failed to read binding! Stack trace: {}", e);
        }
    }
}
