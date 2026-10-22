package net.minecraft.world.level.storage;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.DataResult.Error;
import com.mojang.serialization.DataResult.Success;
import java.util.List;
import java.util.Optional;
import net.minecraft.core.HolderLookup$Provider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntArrayTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.NumericTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.TagType;
import net.minecraft.util.ProblemReporter;
import net.minecraft.util.ProblemReporter$FieldPathElement;
import org.jspecify.annotations.Nullable;

public class TagValueInput implements ValueInput {
   private final ProblemReporter problemReporter;
   private final ValueInputContextHelper context;
   private final CompoundTag input;

   private TagValueInput(final ProblemReporter problemReporter, final ValueInputContextHelper context, final CompoundTag input) {
      this.problemReporter = problemReporter;
      this.context = context;
      this.input = input;
   }

   public static ValueInput create(final ProblemReporter problemReporter, final HolderLookup$Provider holders, final CompoundTag tag) {
      return new TagValueInput(problemReporter, new ValueInputContextHelper(holders, NbtOps.INSTANCE), tag);
   }

   public static ValueInput$ValueInputList create(final ProblemReporter problemReporter, final HolderLookup$Provider holders, final List<CompoundTag> tags) {
      return new TagValueInput$CompoundListWrapper(problemReporter, new ValueInputContextHelper(holders, NbtOps.INSTANCE), tags);
   }

   @Override
   public <T> Optional<T> read(final String name, final Codec<T> codec) {
      Tag tag = this.input.get(name);
      if (tag == null) {
         return Optional.empty();
      } else {
         return switch (codec.parse(this.context.ops(), tag)) {
            case Success<T> success -> Optional.of(success.value());
            case Error<T> error -> {
               this.problemReporter.report(new TagValueInput$DecodeFromFieldFailedProblem(name, tag, error));
               yield error.partialValue();
            }
            default -> throw new MatchException(null, null);
         };
      }
   }

   @Override
   public <T> Optional<T> read(final MapCodec<T> codec) {
      DynamicOps<Tag> ops = this.context.ops();

      return switch (ops.getMap(this.input).flatMap(map -> codec.decode(ops, map))) {
         case Success<T> success -> Optional.of(success.value());
         case Error<T> error -> {
            this.problemReporter.report(new TagValueInput$DecodeFromMapFailedProblem(error));
            yield error.partialValue();
         }
         default -> throw new MatchException(null, null);
      };
   }

   @Nullable
   private <T extends Tag> T getOptionalTypedTag(final String name, final TagType<T> expectedType) {
      Tag tag = this.input.get(name);
      if (tag == null) {
         return null;
      } else {
         TagType<?> actualType = tag.getType();
         if (actualType != expectedType) {
            this.problemReporter.report(new TagValueInput$UnexpectedTypeProblem(name, expectedType, actualType));
            return null;
         } else {
            return (T)tag;
         }
      }
   }

   @Nullable
   private NumericTag getNumericTag(final String name) {
      Tag tag = this.input.get(name);
      if (tag == null) {
         return null;
      } else if (tag instanceof NumericTag numericTag) {
         return numericTag;
      } else {
         this.problemReporter.report(new TagValueInput$UnexpectedNonNumberProblem(name, tag.getType()));
         return null;
      }
   }

   @Override
   public Optional<ValueInput> child(final String name) {
      CompoundTag compound = this.getOptionalTypedTag(name, CompoundTag.TYPE);
      return compound != null ? Optional.of(this.wrapChild(name, compound)) : Optional.empty();
   }

   @Override
   public ValueInput childOrEmpty(final String name) {
      CompoundTag compound = this.getOptionalTypedTag(name, CompoundTag.TYPE);
      return compound != null ? this.wrapChild(name, compound) : this.context.empty();
   }

   @Override
   public Optional<ValueInput$ValueInputList> childrenList(final String name) {
      ListTag list = this.getOptionalTypedTag(name, ListTag.TYPE);
      return list != null ? Optional.of(this.wrapList(name, this.context, list)) : Optional.empty();
   }

   @Override
   public ValueInput$ValueInputList childrenListOrEmpty(final String name) {
      ListTag list = this.getOptionalTypedTag(name, ListTag.TYPE);
      return list != null ? this.wrapList(name, this.context, list) : this.context.emptyList();
   }

   @Override
   public <T> Optional<ValueInput$TypedInputList<T>> list(final String name, final Codec<T> codec) {
      ListTag list = this.getOptionalTypedTag(name, ListTag.TYPE);
      return list != null ? Optional.of(this.wrapTypedList(name, list, codec)) : Optional.empty();
   }

   @Override
   public <T> ValueInput$TypedInputList<T> listOrEmpty(final String name, final Codec<T> codec) {
      ListTag list = this.getOptionalTypedTag(name, ListTag.TYPE);
      return list != null ? this.wrapTypedList(name, list, codec) : this.context.emptyTypedList();
   }

   @Override
   public boolean getBooleanOr(final String name, final boolean defaultValue) {
      NumericTag numericTag = this.getNumericTag(name);
      return numericTag != null ? numericTag.byteValue() != 0 : defaultValue;
   }

   @Override
   public byte getByteOr(final String name, final byte defaultValue) {
      NumericTag numericTag = this.getNumericTag(name);
      return numericTag != null ? numericTag.byteValue() : defaultValue;
   }

   @Override
   public int getShortOr(final String name, final short defaultValue) {
      NumericTag numericTag = this.getNumericTag(name);
      return numericTag != null ? numericTag.shortValue() : defaultValue;
   }

   @Override
   public Optional<Integer> getInt(final String name) {
      NumericTag numericTag = this.getNumericTag(name);
      return numericTag != null ? Optional.of(numericTag.intValue()) : Optional.empty();
   }

   @Override
   public int getIntOr(final String name, final int defaultValue) {
      NumericTag numericTag = this.getNumericTag(name);
      return numericTag != null ? numericTag.intValue() : defaultValue;
   }

   @Override
   public long getLongOr(final String name, final long defaultValue) {
      NumericTag numericTag = this.getNumericTag(name);
      return numericTag != null ? numericTag.longValue() : defaultValue;
   }

   @Override
   public Optional<Long> getLong(final String name) {
      NumericTag numericTag = this.getNumericTag(name);
      return numericTag != null ? Optional.of(numericTag.longValue()) : Optional.empty();
   }

   @Override
   public float getFloatOr(final String name, final float defaultValue) {
      NumericTag numericTag = this.getNumericTag(name);
      return numericTag != null ? numericTag.floatValue() : defaultValue;
   }

   @Override
   public double getDoubleOr(final String name, final double defaultValue) {
      NumericTag numericTag = this.getNumericTag(name);
      return numericTag != null ? numericTag.doubleValue() : defaultValue;
   }

   @Override
   public Optional<String> getString(final String name) {
      StringTag tag = this.getOptionalTypedTag(name, StringTag.TYPE);
      return tag != null ? Optional.of(tag.value()) : Optional.empty();
   }

   @Override
   public String getStringOr(final String name, final String defaultValue) {
      StringTag tag = this.getOptionalTypedTag(name, StringTag.TYPE);
      return tag != null ? tag.value() : defaultValue;
   }

   @Override
   public Optional<int[]> getIntArray(final String name) {
      IntArrayTag tag = this.getOptionalTypedTag(name, IntArrayTag.TYPE);
      return tag != null ? Optional.of(tag.getAsIntArray()) : Optional.empty();
   }

   @Override
   public HolderLookup$Provider lookup() {
      return this.context.lookup();
   }

   private ValueInput wrapChild(final String name, final CompoundTag compoundTag) {
      return (ValueInput)(compoundTag.isEmpty()
         ? this.context.empty()
         : new TagValueInput(this.problemReporter.forChild(new ProblemReporter$FieldPathElement(name)), this.context, compoundTag));
   }

   private static ValueInput wrapChild(final ProblemReporter problemReporter, final ValueInputContextHelper context, final CompoundTag compoundTag) {
      return (ValueInput)(compoundTag.isEmpty() ? context.empty() : new TagValueInput(problemReporter, context, compoundTag));
   }

   private ValueInput$ValueInputList wrapList(final String name, final ValueInputContextHelper context, final ListTag list) {
      return (ValueInput$ValueInputList)(list.isEmpty() ? context.emptyList() : new TagValueInput$ListWrapper(this.problemReporter, name, context, list));
   }

   private <T> ValueInput$TypedInputList<T> wrapTypedList(final String name, final ListTag list, final Codec<T> codec) {
      return (ValueInput$TypedInputList<T>)(list.isEmpty()
         ? this.context.emptyTypedList()
         : new TagValueInput$TypedListWrapper<>(this.problemReporter, name, this.context, codec, list));
   }
}
