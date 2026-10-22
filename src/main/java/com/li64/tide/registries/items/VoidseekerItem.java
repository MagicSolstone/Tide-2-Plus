package com.li64.tide.registries.items;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import net.minecraft.world.level.portal.DimensionTransition;

import java.util.Optional;
import java.util.function.Consumer;

public class VoidseekerItem extends AbstractTooltipItem {
    public VoidseekerItem(Properties properties) {
        super(properties);
    }

    public @NotNull ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        ItemStack result = super.finishUsingItem(stack, level, entity);
        if (!(entity instanceof ServerPlayer player)) return result;

        player.getCooldowns().addCooldown(this, 20);
        player.resetFallDistance(); // secret tech?!?
        ResourceKey<Level> dimension = player.getRespawnDimension();

        if (player.getServer() != null && player.getRespawnPosition() != null) {
            ServerLevel respawnLevel = player.getServer().getLevel(dimension);
            if (respawnLevel == null) return result;

            Optional<Vec3> respawnPos = Optional.of(player.findRespawnPositionAndUseSpawnBlock(true, DimensionTransition.DO_NOTHING).pos());
            respawnPos.ifPresent(pos -> player.teleportTo(respawnLevel,
                    pos.x(), pos.y(), pos.z(), player.getRespawnAngle(), 0f));
        }
        return result;
    }

    @Override
    public void addTooltip(ItemStack stack, Consumer<Component> tooltip) {
        Style gray = Component.empty().getStyle().withColor(ChatFormatting.GRAY);
        tooltip.accept(Component.translatable("item.tide.voidseeker.desc").setStyle(gray));
    }
}