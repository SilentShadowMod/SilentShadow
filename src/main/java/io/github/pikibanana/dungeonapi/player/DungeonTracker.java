package io.github.pikibanana.dungeonapi.player;

import io.github.pikibanana.SilentShadow;
import io.github.pikibanana.dungeonapi.DungeonType;
import io.github.pikibanana.dungeonapi.events.ChatEventsAPI;
import io.github.pikibanana.dungeonapi.events.DungeonEventsAPI;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Environment(EnvType.CLIENT)
public class DungeonTracker {

    private static final Logger test = LoggerFactory.getLogger("SilentShadow");
    private static final Pattern dungeonEntryRegex = Pattern.compile("You have entered the (\\w*) dungeon!");
    private static final Map<String, DungeonMessage> MESSAGE_MAP = new HashMap<>();
    private static boolean isInDungeon = false;
    private static DungeonType dungeonType = DungeonType.UNKNOWN;

    static {
        MESSAGE_MAP.put("You have entered the", DungeonMessage.ENTER);
        MESSAGE_MAP.put("Dungeon failed! The whole team died!", DungeonMessage.DEATH);
        MESSAGE_MAP.put("Teleported you to spawn!", DungeonMessage.LEAVE);
        MESSAGE_MAP.put("The boss has been defeated! The dungeon will end in  10 seconds!", DungeonMessage.LEAVE);
        MESSAGE_MAP.put("Teleporting...", DungeonMessage.TELEPORTING);
    }

    public static boolean inDungeon() {
        return isInDungeon;
    }

    public static DungeonType getDungeonType() {
        return dungeonType;
    }

    public static void init() {
        ChatEventsAPI.CHAT_ON.register((DungeonTracker::handleMessage));
    }

    private static ChatEventsAPI.ReturnState handleMessage(MinecraftClient client, String message) {
        DungeonMessage msgType = MESSAGE_MAP.getOrDefault(message, null);
        if (msgType == null) {
            return ChatEventsAPI.ReturnState.DEFAULT;
        }

        switch (msgType) {
            case ENTER:
                handleEnter(client, message);
                test.warn("Entered Dungeon");
                break;
            case DEATH:
                handleDeath(client);
                test.warn("Died in a dungeon");
                break;
            case LEAVE, TELEPORTING:
                handleLeave(client);
                test.warn("Leaving dungeon");
                break;
            default:
                break;
        }
        return ChatEventsAPI.ReturnState.DEFAULT;
    }

    private static void handleEnter(MinecraftClient client, String message) {
        Matcher matcher = dungeonEntryRegex.matcher(message);
        if (matcher.find()) {
            isInDungeon = true;
            dungeonType = DungeonType.valueOf(matcher.group(1).toUpperCase());
            invokeEnterEvent(client, dungeonType);
        }
    }

    private static void handleDeath(MinecraftClient client) {
        isInDungeon = false;
        invokeDeathEvent(client, dungeonType);
    }

    private static void handleLeave(MinecraftClient client) {
        isInDungeon = false;
        invokeLeaveEvent(client, dungeonType);
    }

    private static void invokeEnterEvent(MinecraftClient client, DungeonType type) {
        DungeonEventsAPI.ENTER_EVENT.invoker().onDungeonEnter(client, type);
    }

    private static void invokeDeathEvent(MinecraftClient client, DungeonType type) {
        DungeonEventsAPI.DEATH_EVENT.invoker().onDungeonDeath(client, type);
    }

    private static void invokeLeaveEvent(MinecraftClient client, DungeonType type) {
        DungeonEventsAPI.LEAVE_EVENT.invoker().onDungeonLeave(client, type);
    }

    private enum DungeonMessage {
        ENTER, DEATH, LEAVE, TELEPORTING
    }
}
