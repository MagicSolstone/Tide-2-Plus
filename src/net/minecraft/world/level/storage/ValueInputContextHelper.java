package net.minecraft.world.level.storage;

import com.mojang.serialization.DynamicOps;
import net.minecraft.core.HolderLookup$Provider;
import net.minecraft.nbt.Tag;

public class ValueInputContextHelper {
   private final HolderLookup$Provider lookup;
   private final DynamicOps<Tag> ops;
   private final ValueInput$ValueInputList emptyChildList = new ValueInputContextHelper$1(this);
   private final ValueInput$TypedInputList<Object> emptyTypedList = new ValueInputContextHelper$2(this);
   private final ValueInput empty = new ValueInputContextHelper$3(this);

   public ValueInputContextHelper(final HolderLookup$Provider lookup, final DynamicOps<Tag> ops) {
      this.lookup = lookup;
      this.ops = lookup.createSerializationContext(ops);
   }

   public DynamicOps<Tag> ops() {
      return this.ops;
   }

   public HolderLookup$Provider lookup() {
      return this.lookup;
   }

   public ValueInput empty() {
      return this.empty;
   }

   public ValueInput$ValueInputList emptyList() {
      return this.emptyChildList;
   }

   public <T> ValueInput$TypedInputList<T> emptyTypedList() {
      return (ValueInput$TypedInputList<T>)this.emptyTypedList;
   }
}
