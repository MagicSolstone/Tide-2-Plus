package net.minecraft.world.level.storage.loot.entries;

import java.util.Objects;
import net.minecraft.util.Mth;

public abstract class UniformContainerBase$EntryBase implements LootPoolEntry {
   protected UniformContainerBase$EntryBase(final UniformContainerBase this$0) {
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super();
   }

   @Override
   public int getWeight(final float luck) {
      return Math.max(Mth.floor(this.this$0.weight + this.this$0.quality * luck), 0);
   }
}
