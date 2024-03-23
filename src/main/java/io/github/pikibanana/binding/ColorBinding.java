package io.github.pikibanana.binding;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.awt.Color;
import java.io.IOException;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Environment(EnvType.CLIENT)
public final class ColorBinding extends Binding<Color> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ColorBinding.class);

    public ColorBinding(@NotNull Color defaultValue) {
        super(defaultValue);
    }

    @Override
    public void write(JsonWriter writer) {
        try {
            writer.name("version");
            writer.value("1.0.0");

            writer.name("color");
            writer.value(getValue().getRGB());
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
                    int rgb = reader.nextInt();

                    bind(new Color(rgb));
                }
                else {
                    throw new UnsupportedOperationException();
                }
            }
            else {
                int rgb = reader.nextInt();

                bind(new Color(rgb));
            }
        }
        catch (IOException e) {
            LOGGER.error("Failed to read binding! Stack trace: {}", e);
        }
    }
}
