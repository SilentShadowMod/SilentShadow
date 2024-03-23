package io.github.pikibanana.dungeonapi.events;


import io.github.pikibanana.dungeonapi.DungeonType;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.MinecraftClient;

@Environment(EnvType.CLIENT)
public class DungeonEventsAPI {
    @FunctionalInterface
    public interface onEnterEvent {
        void onDungeonEnter(MinecraftClient client, DungeonType type);
    }

    public static final Event<onEnterEvent> ENTER_EVENT = EventFactory.createArrayBacked(onEnterEvent.class,(events) -> ((client, type) -> {
        for (onEnterEvent event: events){
            event.onDungeonEnter(client,type);
        }
    }));

    @FunctionalInterface
    public interface onDeathEvent {
        void onDungeonDeath(MinecraftClient client, DungeonType type);
    }

    public static final Event<onDeathEvent> DEATH_EVENT = EventFactory.createArrayBacked(onDeathEvent.class,(events) -> ((client, type) -> {
        for (onDeathEvent event: events){
            event.onDungeonDeath(client,type);
        }
    }));

    @FunctionalInterface
    public interface onLeaveEvent {
        void onDungeonLeave(MinecraftClient client, DungeonType type);
    }

    public static final Event<onLeaveEvent> LEAVE_EVENT = EventFactory.createArrayBacked(onLeaveEvent.class,(events) -> ((client, type) -> {
        for (onLeaveEvent event: events){
            event.onDungeonLeave(client,type);
        }
    }));


}
