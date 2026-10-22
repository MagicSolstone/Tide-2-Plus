package net.minecraft.nbt.visitors;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;

class CollectToTag$CompoundBuilder implements CollectToTag$ContainerBuilder {
   private final CompoundTag compound = new CompoundTag();
   private String lastId = "";

   private CollectToTag$CompoundBuilder() {
   }

   @Override
   public void acceptKey(final String id) {
      this.lastId = id;
   }

   @Override
   public void acceptValue(final Tag tag) {
      this.compound.put(this.lastId, tag);
   }

   @Override
   public Tag build() {
      return this.compound;
   }
}
