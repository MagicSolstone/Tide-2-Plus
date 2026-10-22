package com.li64.tide.registries.entities.fish;

import com.li64.tide.registries.entities.fish.lava.TideSchoolingLavaFish;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Witherfin extends TideSchoolingLavaFish {
    public Witherfin(EntityType<? extends TideSchoolingLavaFish> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected @Nullable SoundEvent getHurtSound(@NotNull DamageSource source) {
        return SoundEvents.TURTLE_EGG_CRACK;
    }

    @Override
    protected @Nullable SoundEvent getDeathSound() {
        return SoundEvents.TURTLE_EGG_BREAK;
    }

    @Override
    public void playerTouch(Player player) {
        if (player instanceof ServerPlayer sp && player.hurtServer(sp.level(), this.damageSources().noAggroMobAttack(this), 1f)) {
            player.addEffect(new MobEffectInstance(MobEffects.WITHER, 100, 0), this);
            this.playSound(SoundEvents.WITHER_SKELETON_AMBIENT, 1.0f, 1.0f);
        }
    }
}
