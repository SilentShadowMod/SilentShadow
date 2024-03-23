package io.github.pikibanana.features.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.ArgumentCommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.github.pikibanana.features.Features;
import io.github.pikibanana.features.FinderConfiguration;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.text.Style;
import net.minecraft.text.Text;

import java.awt.*;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.argument;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

public final class EssenceFinderCommands {
    // usage: finder (get|set (true|false))
    public static void registerBool(LiteralCommandNode<FabricClientCommandSource> rootNode) {
        LiteralCommandNode<FabricClientCommandSource> mainNode = literal("finder").build();

        LiteralCommandNode<FabricClientCommandSource> getNode = literal("get")
                .executes(EssenceFinderCommands::getBoolState)
                .build();

        LiteralCommandNode<FabricClientCommandSource> setNode = literal("set").build();
        ArgumentCommandNode<FabricClientCommandSource, Boolean> valueNode = argument("value", BoolArgumentType.bool())
                .executes(context -> setBoolState(context, BoolArgumentType.getBool(context, "value")))
                .build();

        setNode.addChild(valueNode);

        mainNode.addChild(getNode);
        mainNode.addChild(setNode);

        rootNode.addChild(mainNode);
    }

    // usage: finderColor (get|set red green blue <alpha>)
    public static void registerColor(LiteralCommandNode<FabricClientCommandSource> rootNode) {
        LiteralCommandNode<FabricClientCommandSource> mainNode = literal("finderColor").build();

        LiteralCommandNode<FabricClientCommandSource> getNode = literal("get")
                .executes(EssenceFinderCommands::getColorState)
                .build();

        LiteralCommandNode<FabricClientCommandSource> setNode = literal("set").build();
        ArgumentCommandNode<FabricClientCommandSource, Integer> redNode = argument("red", IntegerArgumentType.integer(0, 255)).build();
        ArgumentCommandNode<FabricClientCommandSource, Integer> greenNode = argument("green", IntegerArgumentType.integer(0, 255)).build();
        ArgumentCommandNode<FabricClientCommandSource, Integer> blueNode = argument("blue", IntegerArgumentType.integer(0, 255))
                .executes(context -> {
                    int r = IntegerArgumentType.getInteger(context, "red");
                    int g = IntegerArgumentType.getInteger(context, "green");
                    int b = IntegerArgumentType.getInteger(context, "blue");

                    return setColorState(context, new Color(r, g, b));
                })
                .build();

        ArgumentCommandNode<FabricClientCommandSource, Integer> alphaNode = argument("alpha", IntegerArgumentType.integer(0, 255))
                .executes(context -> {
                    int r = IntegerArgumentType.getInteger(context, "red");
                    int g = IntegerArgumentType.getInteger(context, "green");
                    int b = IntegerArgumentType.getInteger(context, "blue");
                    int a = IntegerArgumentType.getInteger(context, "alpha");

                    return setColorState(context, new Color(r, g, b, a));
                })
                .build();

        setNode.addChild(redNode);
        redNode.addChild(greenNode);
        greenNode.addChild(blueNode);
        blueNode.addChild(alphaNode);

        mainNode.addChild(getNode);
        mainNode.addChild(setNode);

        rootNode.addChild(mainNode);
    }

    private static int getBoolState(CommandContext<FabricClientCommandSource> ctx) {
//        sendBoolStateMessage(ctx.getSource(), Features.ESSENCE_FINDER.getValue().enabled());

        return Command.SINGLE_SUCCESS;
    }

    private static int setBoolState(CommandContext<FabricClientCommandSource> ctx, boolean value) {
        Features.ESSENCE_FINDER.bind(new FinderConfiguration(value, Features.ESSENCE_FINDER.getValue().color()));
//        sendBoolStateMessage(ctx.getSource(), value);

        return Command.SINGLE_SUCCESS;
    }

//    private static void sendBoolStateMessage(FabricClientCommandSource source, boolean value) {
////        if (value)
////            source.sendFeedback(systemText(Text.translatable("dungeonmew.feature.finder.enabled")));
////        else
////            source.sendFeedback(systemText(Text.translatable("dungeonmew.feature.finder.disabled")));
//    }

    private static int getColorState(CommandContext<FabricClientCommandSource> ctx) {
//        sendColorStateMessage(ctx.getSource(), Features.ESSENCE_FINDER.getValue());

        return Command.SINGLE_SUCCESS;
    }

    private static int setColorState(CommandContext<FabricClientCommandSource> ctx, Color value) {
        Features.ESSENCE_FINDER_COLOR.bind(value);
//        sendColorStateMessage(ctx.getSource(), value);

        return Command.SINGLE_SUCCESS;
    }

//    private static void sendColorStateMessage(FabricClientCommandSource source, Color value) {
//        source.sendFeedback(systemText(Text.translatable("dungeonmew.feature.finder_color.set",
//                Text.translatable("dungeonmew.feature.finder_color.this").fillStyle(Style.EMPTY.withColor(value.getRGB()))
//        )));
//    }
}
