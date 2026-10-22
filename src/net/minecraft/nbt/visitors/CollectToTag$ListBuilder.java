package net.minecraft.nbt.visitors;

import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;

class CollectToTag$ListBuilder implements CollectToTag$ContainerBuilder {
   private final ListTag list = new ListTag();

   private CollectToTag$ListBuilder() {
   }

   @Override
   public void acceptValue(final Tag tag) {
      this.list.addAndUnwrap(tag);
   }

   @Override
   public Tag build() {
      return this.list;
   }
}
