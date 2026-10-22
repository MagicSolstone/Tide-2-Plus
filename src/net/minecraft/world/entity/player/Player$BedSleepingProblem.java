package net.minecraft.world.entity.player;

import net.minecraft.network.chat.Component;
import org.jspecify.annotations.Nullable;

public record Player$BedSleepingProblem(@Nullable Component message) {
   public static final Player$BedSleepingProblem TOO_FAR_AWAY = new Player$BedSleepingProblem(Component.translatable("block.minecraft.bed.too_far_away"));
   public static final Player$BedSleepingProblem OBSTRUCTED = new Player$BedSleepingProblem(Component.translatable("block.minecraft.bed.obstructed"));
   public static final Player$BedSleepingProblem OTHER_PROBLEM = new Player$BedSleepingProblem(null);
   public static final Player$BedSleepingProblem NOT_SAFE = new Player$BedSleepingProblem(Component.translatable("block.minecraft.bed.not_safe"));
}
