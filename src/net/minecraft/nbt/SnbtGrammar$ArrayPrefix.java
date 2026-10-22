package net.minecraft.nbt;

import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.JavaOps;
import java.util.List;
import java.util.Set;
import net.minecraft.util.parsing.packrat.ParseState;
import org.jspecify.annotations.Nullable;

enum SnbtGrammar$ArrayPrefix {
   BYTE(SnbtGrammar$TypeSuffix.BYTE),
   INT(SnbtGrammar$TypeSuffix.INT, SnbtGrammar$TypeSuffix.BYTE, SnbtGrammar$TypeSuffix.SHORT),
   LONG(SnbtGrammar$TypeSuffix.LONG, SnbtGrammar$TypeSuffix.BYTE, SnbtGrammar$TypeSuffix.SHORT, SnbtGrammar$TypeSuffix.INT);

   private final SnbtGrammar$TypeSuffix defaultType;
   private final Set<SnbtGrammar$TypeSuffix> additionalTypes;

   private SnbtGrammar$ArrayPrefix(final SnbtGrammar$TypeSuffix defaultType, final SnbtGrammar$TypeSuffix... additionalTypes) {
      this.additionalTypes = Set.of(additionalTypes);
      this.defaultType = defaultType;
   }

   public boolean isAllowed(final SnbtGrammar$TypeSuffix type) {
      return type == this.defaultType || this.additionalTypes.contains(type);
   }

   public abstract <T> T create(DynamicOps<T> ops);

   @Nullable
   public abstract <T> T create(DynamicOps<T> ops, List<SnbtGrammar$IntegerLiteral> entries, ParseState<?> state);

   @Nullable
   protected Number buildNumber(final SnbtGrammar$IntegerLiteral entry, final ParseState<?> state) {
      SnbtGrammar$TypeSuffix actualType = this.computeType(entry.suffix);
      if (actualType == null) {
         state.errorCollector().store(state.mark(), SnbtGrammar.ERROR_INVALID_ARRAY_ELEMENT_TYPE);
         return null;
      } else {
         return entry.create(JavaOps.INSTANCE, actualType, state);
      }
   }

   @Nullable
   private SnbtGrammar$TypeSuffix computeType(final SnbtGrammar$IntegerSuffix value) {
      SnbtGrammar$TypeSuffix type = value.type();
      if (type == null) {
         return this.defaultType;
      } else {
         return !this.isAllowed(type) ? null : type;
      }
   }
}
