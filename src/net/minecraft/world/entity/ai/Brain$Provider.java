package net.minecraft.world.entity.ai;

import java.util.Collection;
import java.util.List;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.sensing.SensorType;

public final class Brain$Provider<E extends LivingEntity> {
   private final Collection<? extends MemoryModuleType<?>> memoryTypes;
   private final Collection<? extends SensorType<? extends Sensor<? super E>>> sensorTypes;
   private final Brain$ActivitySupplier<E> activities;

   private Brain$Provider(
      final Collection<? extends MemoryModuleType<?>> memoryTypes,
      final Collection<? extends SensorType<? extends Sensor<? super E>>> sensorTypes,
      final Brain$ActivitySupplier<E> activities
   ) {
      this.memoryTypes = memoryTypes;
      this.sensorTypes = sensorTypes;
      this.activities = activities;
   }

   public Brain<E> makeBrain(final E body, final Brain$Packed packed) {
      List<ActivityData<E>> activities = this.activities.createActivities(body);
      return new Brain<>(this.memoryTypes, this.sensorTypes, activities, packed.memories, body.getRandom());
   }
}
