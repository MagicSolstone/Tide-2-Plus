package net.minecraft.world.level.storage.loot.entries;

import net.minecraft.world.level.storage.loot.functions.FunctionUserBuilder;

public abstract class UniformContainerBase$Builder<T extends UniformContainerBase$Builder<T>>
   extends LootPoolEntryContainer$Builder<T>
   implements FunctionUserBuilder<T> {
   protected int weight = 1;
   protected int quality = 0;

   public T setWeight(final int weight) {
      this.weight = weight;
      return this.getThis();
   }

   public T setQuality(final int quality) {
      this.quality = quality;
      return this.getThis();
   }
}
