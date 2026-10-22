package net.minecraft.nbt.visitors;

import net.minecraft.nbt.Tag;
import org.jspecify.annotations.Nullable;

class CollectToTag$RootBuilder implements CollectToTag$ContainerBuilder {
   @Nullable
   private Tag result;

   private CollectToTag$RootBuilder() {
   }

   @Override
   public void acceptValue(final Tag tag) {
      this.result = tag;
   }

   @Nullable
   @Override
   public Tag build() {
      return this.result;
   }
}
