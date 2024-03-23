package io.github.pikibanana.client;

import com.mojang.brigadier.tree.LiteralCommandNode;
import io.github.pikibanana.FeatureIO;
import io.github.pikibanana.dungeonapi.player.DungeonTracker;
import io.github.pikibanana.features.Feature;
import io.github.pikibanana.features.Features;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;

public class SilentShadowClient implements ClientModInitializer {
    private static final int BRACKETS_COLOR = 0x2F_4F_4F;
    private static final int MOD_NAME_COLOR = 0x1A_2C_35;
    private static final int MESSAGE_COLOR = 0xC0_C0_C0;
    private static final MutableText commandText =
            Text.literal("[").setStyle(Style.EMPTY.withColor(TextColor.fromRgb(BRACKETS_COLOR)))
                    .append(Text.literal("SilentShadow").setStyle(Style.EMPTY.withColor(TextColor.fromRgb(MOD_NAME_COLOR))))
                    .append(Text.literal("] ").setStyle(Style.EMPTY.withColor(TextColor.fromRgb(BRACKETS_COLOR))));

    private static void initFeatures() {
        Features.init();
        FeatureIO.create();

        for (Feature<?> feature : Feature.getFeatures()) {
            feature.init();
        }

        ClientPlayConnectionEvents.DISCONNECT.register(((handler, client) -> FeatureIO.save()));
    }

    private static void registerTrackers() {
        DungeonTracker.init();
    }

    public static Text systemText(MutableText text) {
        return commandText.copy().append(text.setStyle(Style.EMPTY.withColor(TextColor.fromRgb(MESSAGE_COLOR))));
    }

    @Override
    public void onInitializeClient() {
        String version = MinecraftClient.getInstance().getGameVersion();
        if (!version.startsWith("1.20.")) {
            throw new IllegalStateException("Unsupported Version: " + version + "\nSupported Versions:  1.20.1-1.20.4");
        }
        // This entrypoint is suitable for setting up client-specific logic, such as rendering.

        initFeatures();

        registerTrackers();

        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            LiteralCommandNode<FabricClientCommandSource> rootNode = ClientCommandManager.literal("silentshadow").build();
            for (Feature<?> feature : Feature.getFeatures()) {
                feature.registerAsCommand(rootNode);
            }
            dispatcher.getRoot().addChild(rootNode);

        });

    }
}

