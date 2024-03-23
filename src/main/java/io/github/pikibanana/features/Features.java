package io.github.pikibanana.features;

import com.mojang.brigadier.tree.LiteralCommandNode;
import io.github.pikibanana.binding.ColorBinding;
import io.github.pikibanana.binding.FinderConfigurationBinding;
import io.github.pikibanana.features.commands.EssenceFinderCommands;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;

import java.awt.*;

@Environment(EnvType.CLIENT)
public final class Features {

    public static final Feature<FinderConfiguration> ESSENCE_FINDER = new Feature<>(
            "essence_finder",
            "essenceFinder",
            new FinderConfigurationBinding(new FinderConfiguration(true, new Color(101, 31, 73, 128)))) {
        @Override
        public void registerAsCommand(LiteralCommandNode<FabricClientCommandSource> rootNode) {
            EssenceFinderCommands.registerBool(rootNode);
        }
    };

    public static final Feature<Color> ESSENCE_FINDER_COLOR = new Feature<>(
            "essence_finder_color",
            "essenceFinderColor",
            new ColorBinding(new Color(101, 31, 73))
    ) {
        @Override
        public void registerAsCommand(LiteralCommandNode<FabricClientCommandSource> rootNode) {
            EssenceFinderCommands.registerColor(rootNode);
        }
    };

    public static void init() {

    }
}
