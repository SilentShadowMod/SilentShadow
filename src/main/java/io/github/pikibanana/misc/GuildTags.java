package io.github.pikibanana.misc;

import io.github.pikibanana.misc.Guilds;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.message.SentMessage;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.List;

@Environment(EnvType.CLIENT)
public class GuildTags {

    private Identifier guildTagTexture = new Identifier("SilentShadow", "guildtags/astral");

    private boolean isPlayerInGuild(String playerName){
        List<String> astralMembers = Guilds.ASTRAL.getMembersNames();
        for (String member : astralMembers){
            if (member.equalsIgnoreCase(playerName)) {
                return true;
            }
        }
        return false;
    }

    public void onChat(SentMessage.Chat event){
        MinecraftClient client = MinecraftClient.getInstance();
        Text originalMessage = event.getContent();
        Text newMessage = Text.of(event.getContent().toString() + "Cookie");
    }



}
