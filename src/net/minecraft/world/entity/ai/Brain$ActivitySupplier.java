package net.minecraft.world.entity.ai;

import java.util.List;
import net.minecraft.world.entity.LivingEntity;

@FunctionalInterface
public interface Brain$ActivitySupplier<E extends LivingEntity> {
   List<ActivityData<E>> createActivities(E body);
}
