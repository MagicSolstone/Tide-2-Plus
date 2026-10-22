package net.minecraft.world.level.storage;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import java.util.Objects;
import java.util.Optional;
import net.minecraft.core.HolderLookup$Provider;

class ValueInputContextHelper$3 implements ValueInput {
   ValueInputContextHelper$3(final ValueInputContextHelper this$0) {
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super();
   }

   @Override
   public <T> Optional<T> read(final String name, final Codec<T> codec) {
      return Optional.empty();
   }

   @Override
   public <T> Optional<T> read(final MapCodec<T> codec) {
      return Optional.empty();
   }

   @Override
   public Optional<ValueInput> child(final String name) {
      return Optional.empty();
   }

   @Override
   public ValueInput childOrEmpty(final String name) {
      return this;
   }

   @Override
   public Optional<ValueInput$ValueInputList> childrenList(final String name) {
      return Optional.empty();
   }

   @Override
   public ValueInput$ValueInputList childrenListOrEmpty(final String name) {
      return this.this$0.emptyChildList;
   }

   @Override
   public <T> Optional<ValueInput$TypedInputList<T>> list(final String name, final Codec<T> codec) {
      return Optional.empty();
   }

   @Override
   public <T> ValueInput$TypedInputList<T> listOrEmpty(final String name, final Codec<T> codec) {
      return this.this$0.emptyTypedList();
   }

   @Override
   public boolean getBooleanOr(final String name, final boolean defaultValue) {
      return defaultValue;
   }

   @Override
   public byte getByteOr(final String name, final byte defaultValue) {
      return defaultValue;
   }

   @Override
   public int getShortOr(final String name, final short defaultValue) {
      return defaultValue;
   }

   @Override
   public Optional<Integer> getInt(final String name) {
      return Optional.empty();
   }

   @Override
   public int getIntOr(final String name, final int defaultValue) {
      return defaultValue;
   }

   @Override
   public long getLongOr(final String name, final long defaultValue) {
      return defaultValue;
   }

   @Override
   public Optional<Long> getLong(final String name) {
      return Optional.empty();
   }

   @Override
   public float getFloatOr(final String name, final float defaultValue) {
      return defaultValue;
   }

   @Override
   public double getDoubleOr(final String name, final double defaultValue) {
      return defaultValue;
   }

   @Override
   public Optional<String> getString(final String name) {
      return Optional.empty();
   }

   @Override
   public String getStringOr(final String name, final String defaultValue) {
      return defaultValue;
   }

   @Override
   public HolderLookup$Provider lookup() {
      return this.this$0.lookup;
   }

   @Override
   public Optional<int[]> getIntArray(final String name) {
      return Optional.empty();
   }
}
