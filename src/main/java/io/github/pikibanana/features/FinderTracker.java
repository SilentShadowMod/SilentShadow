package io.github.pikibanana.features;

import io.github.pikibanana.dungeonapi.player.DungeonTracker;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.util.math.BlockPos;

import java.util.HashSet;
import java.util.Set;

public class FinderTracker {
    private static final Set<BlockPos> found = new HashSet<>();

    public static void init() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (!DungeonTracker.inDungeon()) {
                clear();
            }
        });
    }

    public static boolean isMarked(BlockPos blockPos){
        return found.contains(blockPos);
    }

    public static void mark(BlockPos blockPos){
        found.add(blockPos);
    }

    public static void clear() {
        found.clear();
    }
}
