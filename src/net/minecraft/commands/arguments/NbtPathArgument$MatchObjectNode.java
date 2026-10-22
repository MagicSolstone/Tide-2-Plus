package net.minecraft.commands.arguments;

import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;

class NbtPathArgument$MatchObjectNode implements NbtPathArgument$Node {
   private final String name;
   private final CompoundTag pattern;
   private final Predicate<Tag> predicate;

   public NbtPathArgument$MatchObjectNode(final String name, final CompoundTag pattern) {
      this.name = name;
      this.pattern = pattern;
      this.predicate = NbtPathArgument.createTagPredicate(pattern);
   }

   @Override
   public void getTag(final Tag parent, final List<Tag> output) {
      if (parent instanceof CompoundTag compoundTag) {
         Tag result = compoundTag.get(this.name);
         if (this.predicate.test(result)) {
            output.add(result);
         }
      }
   }

   @Override
   public void getOrCreateTag(final Tag parent, final Supplier<Tag> child, final List<Tag> output) {
      if (parent instanceof CompoundTag compound) {
         Tag result = compound.get(this.name);
         if (result == null) {
            Tag var6 = this.pattern.copy();
            compound.put(this.name, var6);
            output.add(var6);
         } else if (this.predicate.test(result)) {
            output.add(result);
         }
      }
   }

   @Override
   public Tag createPreferredParentTag() {
      return new CompoundTag();
   }

   @Override
   public int setTag(final Tag parent, final Supplier<Tag> toAdd) {
      if (parent instanceof CompoundTag compound) {
         Tag currentValue = compound.get(this.name);
         if (this.predicate.test(currentValue)) {
            Tag newValue = toAdd.get();
            if (!newValue.equals(currentValue)) {
               compound.put(this.name, newValue);
               return 1;
            }
         }
      }

      return 0;
   }

   @Override
   public int removeTag(final Tag parent) {
      if (parent instanceof CompoundTag compound) {
         Tag current = compound.get(this.name);
         if (this.predicate.test(current)) {
            compound.remove(this.name);
            return 1;
         }
      }

      return 0;
   }
}
