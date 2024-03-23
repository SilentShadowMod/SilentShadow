package io.github.pikibanana.client.mixin;

import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalBooleanRef;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import io.github.pikibanana.dungeonapi.player.DungeonTracker;
import io.github.pikibanana.features.Features;
import io.github.pikibanana.features.FinderTracker;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.SkullBlockEntity;
import net.minecraft.client.render.OutlineVertexConsumerProvider;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.SkullBlockEntityModel;
import net.minecraft.client.render.block.entity.SkullBlockEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

import java.awt.*;
import java.util.Objects;
import java.util.Optional;

@Mixin(SkullBlockEntityRenderer.class)
public class EssenceOverlayMixin {

    @Unique
    private static final String essenceSkullTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjgwZDQ0Y2ExNWUzMDNhMTcxNGQ4ZDY4OGJjM2QwYzQ4NDhhZjQ4YmJlMTZiMzg4OTNlNjQyOThkZGNmZTEwZSJ9fX0=";
    @Unique
    private static OutlineVertexConsumerProvider outlineVertexConsumerProvider = null;

    @Unique
    private static boolean matchTexture(SkullBlockEntity skullBlockEntity, String comparedTexture) {
        Block block = skullBlockEntity.getCachedState().getBlock();
        if (block == Blocks.PLAYER_HEAD || block == Blocks.PLAYER_WALL_HEAD) {
            GameProfile owner = skullBlockEntity.getOwner();
            if (owner == null || !owner.getProperties().containsKey("textures")) {
                return false;
            }

            Optional<Property> getSkullTexture = owner.getProperties().get("textures").stream().findFirst();
            if (getSkullTexture.isPresent()) {
                String skullTexture = getSkullTexture.get().toString();
                return Objects.equals(skullTexture, comparedTexture);
            }
        }
        return false;
    }

    @Unique
    private static boolean isValidSkull(SkullBlockEntity state) {
        if (DungeonTracker.inDungeon() && Features.ESSENCE_FINDER.getValue().enabled()) {
            return !FinderTracker.isMarked(state.getPos()) && matchTexture(state, essenceSkullTexture);
        } else return false;
    }

    @Inject(
            method = "renderSkull",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/block/entity/SkullBlockEntityModel;render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;IIFFFF)V",
                    shift = At.Shift.AFTER
            )
    )
    private static void modifyRenderSkull_DrawOutline(Direction direction,
                                                      float yaw,
                                                      float animationProgress,
                                                      MatrixStack matrices,
                                                      VertexConsumerProvider vertexConsumers,
                                                      int light,
                                                      SkullBlockEntityModel model,
                                                      RenderLayer renderLayer,
                                                      CallbackInfo ci) {
        if (outlineVertexConsumerProvider != null) {
            outlineVertexConsumerProvider.draw();
        }
    }

    @Unique
    @Inject(
            method = "render(Lnet/minecraft/block/entity/SkullBlockEntity;FLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;II)V",
            at = @At("HEAD")
    )
    private void modifyRenderSkull(SkullBlockEntity skullBlockEntity,
                                   float f,
                                   MatrixStack matrixStack,
                                   VertexConsumerProvider vertexConsumerProvider,
                                   int i,
                                   int j,
                                   CallbackInfo ci,
                                   @Share("silentshadow$validSkull") LocalBooleanRef validSkull) {
        validSkull.set(isValidSkull(skullBlockEntity));
    }

    @Unique
    @ModifyArgs(
            method = "render(Lnet/minecraft/block/entity/SkullBlockEntity;FLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;II)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/block/entity/SkullBlockEntityRenderer;renderSkull(Lnet/minecraft/util/math/Direction;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/client/render/block/entity/SkullBlockEntityModel;Lnet/minecraft/client/render/RenderLayer;)V"
            )
    )
    private void modifyRenderSkull_RenderSkull(Args args, @Share("silentshadow$validSkull") LocalBooleanRef validSkull) {
        if (outlineVertexConsumerProvider == null) {
            outlineVertexConsumerProvider = new OutlineVertexConsumerProvider(
                    (VertexConsumerProvider.Immediate) args.<VertexConsumerProvider>get(4)
            );
        }

        if (validSkull.get()) {
            Color color = Features.ESSENCE_FINDER_COLOR.getValue();
            outlineVertexConsumerProvider.setColor(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());

            args.set(4, (VertexConsumerProvider) outlineVertexConsumerProvider);
            args.set(5, 15728880);
        }
    }
}
