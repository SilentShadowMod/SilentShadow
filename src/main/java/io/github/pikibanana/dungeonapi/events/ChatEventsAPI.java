package io.github.pikibanana.dungeonapi.events;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Environment(EnvType.CLIENT)
public final class ChatEventsAPI {
    public enum ReturnState {
        DEFAULT,
        CANCEL;
    }

    @FunctionalInterface
    public interface OnSentMessage {
        ReturnState onSentMessage(MinecraftClient client, String message);
    }

    public static final Event<OnSentMessage> CHAT_ON = EventFactory.createArrayBacked(OnSentMessage.class, (events) -> (client, message) -> {
        for (OnSentMessage event : events) {
            ReturnState value = event.onSentMessage(client, message);
            if (value != ReturnState.DEFAULT)
                return value;
        }

        return ReturnState.DEFAULT;
    });

    public static final Event<OnSentMessage> OVERLAY_ON = EventFactory.createArrayBacked(OnSentMessage.class, (events) -> (client, message) -> {
        for (OnSentMessage event : events) {
            ReturnState value = event.onSentMessage(client, message);
            if (value != ReturnState.DEFAULT)
                return value;
        }

        return ReturnState.DEFAULT;
    });

    @FunctionalInterface
    public interface ModifySentMessage {
        @Nullable Text modifySentMessage(MinecraftClient client, @NotNull Text message);
    }

    public static final Event<ModifySentMessage> CHAT_MODIFY = EventFactory.createArrayBacked(ModifySentMessage.class, (events) -> (client, message) -> {
        for (ModifySentMessage event : events) {
            Text value = event.modifySentMessage(client, message);
            if (value != null)
                return value;
        }

        return null;
    });

    // Additional chat events
    @FunctionalInterface
    public interface OnChatReceived {
        void onChatReceived(MinecraftClient client, Text message);
    }

    public static final Event<OnChatReceived> CHAT_RECEIVED = EventFactory.createArrayBacked(OnChatReceived.class, (events) -> (client, message) -> {
        for (OnChatReceived event : events) {
            event.onChatReceived(client, message);
        }
    });

    @FunctionalInterface
    public interface OnChatSent {
        void onChatSent(MinecraftClient client, String message);
    }

    public static final Event<OnChatSent> CHAT_SENT = EventFactory.createArrayBacked(OnChatSent.class, (events) -> (client, message) -> {
        for (OnChatSent event : events) {
            event.onChatSent(client, message);
        }
    });

    // Additional chat listener
    @FunctionalInterface
    public interface ChatListener {
        void onChatEvent(MinecraftClient client, String message);
    }

    public static final Event<ChatListener> CHAT_LISTENER = EventFactory.createArrayBacked(ChatListener.class, (events) -> (client, message) -> {
        for (ChatListener event : events) {
            event.onChatEvent(client, message);
        }
    });
}
