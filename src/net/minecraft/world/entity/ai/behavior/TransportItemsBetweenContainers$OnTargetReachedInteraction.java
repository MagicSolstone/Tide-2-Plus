package net.minecraft.world.entity.ai.behavior;

import net.minecraft.world.entity.PathfinderMob;
import org.apache.commons.lang3.function.TriConsumer;

@FunctionalInterface
public interface TransportItemsBetweenContainers$OnTargetReachedInteraction
   extends TriConsumer<PathfinderMob, TransportItemsBetweenContainers$TransportItemTarget, Integer> {
}
