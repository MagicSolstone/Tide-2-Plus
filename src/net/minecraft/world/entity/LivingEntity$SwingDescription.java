package net.minecraft.world.entity;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.component.SwingAnimation;

public record LivingEntity$SwingDescription(InteractionHand hand, SwingAnimation animation, int durationTicks) {
}
