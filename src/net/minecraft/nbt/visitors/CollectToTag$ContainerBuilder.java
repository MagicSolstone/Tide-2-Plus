package net.minecraft.nbt.visitors;

import net.minecraft.nbt.Tag;
import org.jspecify.annotations.Nullable;

interface CollectToTag$ContainerBuilder {
   default void acceptKey(final String id) {
   }

   void acceptValue(Tag tag);

   @Nullable
   Tag build();
}
