package net.minecraft.client.renderer.block.dispatch.multipart;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.function.Predicate;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Util;
import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.world.level.block.state.properties.Property;

@Environment(EnvType.CLIENT)
public record KeyValueCondition$Terms(List<KeyValueCondition$Term> entries) {
   private static final char SEPARATOR = '|';
   private static final Joiner JOINER = Joiner.on('|');
   private static final Splitter SPLITTER = Splitter.on('|');
   private static final Codec<String> LEGACY_REPRESENTATION_CODEC = Codec.either(Codec.INT, Codec.BOOL)
      .flatComapMap(either -> (String)either.map(String::valueOf, String::valueOf), o -> DataResult.error(() -> "This codec can't be used for encoding"));
   public static final Codec<KeyValueCondition$Terms> CODEC = Codec.withAlternative(Codec.STRING, LEGACY_REPRESENTATION_CODEC)
      .comapFlatMap(KeyValueCondition$Terms::parse, KeyValueCondition$Terms::toString);

   public KeyValueCondition$Terms(List<KeyValueCondition$Term> entries) {
      if (entries.isEmpty()) {
         throw new IllegalArgumentException("Empty value for property");
      } else {
         this.entries = entries;
      }
   }

   public static DataResult<KeyValueCondition$Terms> parse(final String value) {
      List<KeyValueCondition$Term> terms = SPLITTER.splitToStream(value).map(KeyValueCondition$Term::parse).toList();
      if (terms.isEmpty()) {
         return DataResult.error(() -> "Empty value for property");
      } else {
         for (KeyValueCondition$Term entry : terms) {
            if (entry.value.isEmpty()) {
               return DataResult.error(() -> "Empty term in value '" + value + "'");
            }
         }

         return DataResult.success(new KeyValueCondition$Terms(terms));
      }
   }

   @Override
   public String toString() {
      return JOINER.join(this.entries);
   }

   public <O, S extends StateHolder<O, S>, T extends Comparable<T>> Predicate<S> instantiate(final O owner, final Property<T> property) {
      Predicate<T> allowedValueTest = Util.anyOf(Lists.transform(this.entries, t -> this.instantiate(owner, property, t)));
      List<T> allowedValues = new ArrayList<>(property.getPossibleValues());
      int allValuesCount = allowedValues.size();
      allowedValues.removeIf(allowedValueTest.negate());
      int allowedValuesCount = allowedValues.size();
      if (allowedValuesCount == 0) {
         KeyValueCondition.LOGGER.warn("Condition {} for property {} on {} is always false", new Object[]{this, property.getName(), owner});
         return blockState -> false;
      } else {
         int rejectedValuesCount = allValuesCount - allowedValuesCount;
         if (rejectedValuesCount == 0) {
            KeyValueCondition.LOGGER.warn("Condition {} for property {} on {} is always true", new Object[]{this, property.getName(), owner});
            return blockState -> true;
         } else {
            boolean negate;
            List<T> valuesToMatch;
            if (allowedValuesCount <= rejectedValuesCount) {
               negate = false;
               valuesToMatch = allowedValues;
            } else {
               negate = true;
               List<T> rejectedValues = new ArrayList<>(property.getPossibleValues());
               rejectedValues.removeIf(allowedValueTest);
               valuesToMatch = rejectedValues;
            }

            if (valuesToMatch.size() == 1) {
               T expectedValue = (T)valuesToMatch.getFirst();
               return state -> {
                  T value = state.getValue(property);
                  return expectedValue.equals(value) ^ negate;
               };
            } else {
               return state -> {
                  T value = state.getValue(property);
                  return valuesToMatch.contains(value) ^ negate;
               };
            }
         }
      }
   }

   private <T extends Comparable<T>> T getValueOrThrow(final Object owner, final Property<T> property, final String input) {
      Optional<T> value = property.getValue(input);
      if (value.isEmpty()) {
         throw new RuntimeException(String.format(Locale.ROOT, "Unknown value '%s' for property '%s' on '%s' in '%s'", input, property, owner, this));
      } else {
         return value.get();
      }
   }

   private <T extends Comparable<T>> Predicate<T> instantiate(final Object owner, final Property<T> property, final KeyValueCondition$Term term) {
      T parsedValue = this.getValueOrThrow(owner, property, term.value);
      return term.negated ? value -> !value.equals(parsedValue) : value -> value.equals(parsedValue);
   }
}
