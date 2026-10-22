package com.li64.tide.registries.items;

import com.li64.tide.registries.entities.misc.StarArrow;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import org.jetbrains.annotations.Nullable;
import net.minecraft.world.InteractionHand;
import net.minecraft.server.level.ServerLevel;

import java.util.List;
import java.util.function.Consumer;

public class StarlightBowItem extends BowItem implements TooltipItem {
    public StarlightBowItem(Properties properties) {
        super(properties);
    }

    @Override
    public void addTooltip(ItemStack stack, Consumer<Component> tooltip) {
        Style gold = Component.empty().getStyle().withColor(ChatFormatting.GOLD);
        tooltip.accept(Component.translatable("item.tide.starlight_bow.desc_0").setStyle(gold));
        tooltip.accept(Component.translatable("item.tide.starlight_bow.desc_1").setStyle(gold));
    }

    protected @NotNull Projectile createProjectile(@NotNull Level level, @NotNull LivingEntity shooter, @NotNull ItemStack weapon, @NotNull ItemStack ammo, boolean isCrit) {
        AbstractArrow projectile = new StarArrow(level, shooter, ammo, weapon);
        if (isCrit) projectile.setCritArrow(true);
        return projectile;
    }

    @Override
    protected void shoot(@NotNull ServerLevel level, @NotNull LivingEntity shooter, @NotNull InteractionHand hand, @NotNull ItemStack weapon, @NotNull List<ItemStack> projectileItems, float velocity, float inaccuracy, boolean isCrit, @Nullable LivingEntity target) {
        super.shoot(level, shooter, hand, weapon, projectileItems, velocity, inaccuracy, isCrit, target);
        level.playSound(null, shooter.blockPosition(), SoundEvents.AMETHYST_BLOCK_RESONATE, SoundSource.PLAYERS, 1.5f, 1.0f - (level.random.nextFloat() - level.random.nextFloat()) * 0.1f);
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> lines, TooltipFlag flag) {
        super.appendHoverText(stack, context, lines, flag);
        this.addTooltip(stack, lines::add);
    }

}
