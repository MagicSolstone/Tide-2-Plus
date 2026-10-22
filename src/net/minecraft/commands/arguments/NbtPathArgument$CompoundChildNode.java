package net.minecraft.commands.arguments;

import java.util.List;
import java.util.function.Supplier;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;

class NbtPathArgument$CompoundChildNode implements NbtPathArgument$Node {
   private final String name;

   public NbtPathArgument$CompoundChildNode(final String name) {
      this.name = name;
   }

   @Override
   public void getTag(final Tag parent, final List<Tag> output) {
      if (parent instanceof CompoundTag compoundTag) {
         Tag result = compoundTag.get(this.name);
         if (result != null) {
            output.add(result);
         }
      }
   }

   @Override
   public void getOrCreateTag(final Tag parent, final Supplier<Tag> child, final List<Tag> output) {
      if (parent instanceof CompoundTag compound) {
         Tag result;
         if (compound.contains(this.name)) {
            result = compound.get(this.name);
         } else {
            result = child.get();
            compound.put(this.name, result);
         }

         output.add(result);
      }
   }

   @Override
   public Tag createPreferredParentTag() {
      return new CompoundTag();
   }

   @Override
   public int setTag(final Tag parent, final Supplier<Tag> toAdd) {
      if (parent instanceof CompoundTag compound) {
         Tag newValue = toAdd.get();
         Tag previousValue = compound.put(this.name, newValue);
         if (!newValue.equals(previousValue)) {
            return 1;
         }
      }

      return 0;
   }

   @Override
   public int removeTag(final Tag parent) {
      if (parent instanceof CompoundTag compound && compound.contains(this.name)) {
         compound.remove(this.name);
         return 1;
      } else {
         return 0;
      }
   }
}
