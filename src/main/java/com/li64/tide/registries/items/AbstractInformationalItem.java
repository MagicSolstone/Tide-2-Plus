package com.li64.tide.registries.items;

import net.minecraft.world.InteractionResult;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.protocol.game.ClientboundSetActionBarTextPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public abstract class AbstractInformationalItem extends AbstractTooltipItem implements InformationalItem {
    private final List<MutableComponent> description;

    public AbstractInformationalItem(Properties properties, MutableComponent... description) {
        super(properties);
        this.description = Arrays.asList(description);
    }

    public SoundEvent useItemSound() {
        return SoundEvents.STONE_BUTTON_CLICK_ON;
    }

    @Override
    public @NotNull InteractionResult use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        if (level instanceof ServerLevel serverLevel && player instanceof ServerPlayer serverPlayer) {
            serverPlayer.connection.send(new ClientboundSetActionBarTextPacket(
                    this.parseResult(this.getResult(serverLevel, serverPlayer))));
            serverPlayer.level().playSound(null, serverPlayer.blockPosition(),
                    useItemSound(), SoundSource.PLAYERS, 1.0f, 2.0f);
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public void addTooltip(ItemStack stack, Consumer<Component> tooltip) {
        Style gray = Component.empty().getStyle().withColor(ChatFormatting.GRAY);
        this.description.forEach(component -> tooltip.accept(component.setStyle(gray)));
    }
}
