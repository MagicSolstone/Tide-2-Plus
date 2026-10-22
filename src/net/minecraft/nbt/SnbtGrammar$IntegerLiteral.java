package net.minecraft.nbt;

import com.google.common.primitives.UnsignedBytes;
import com.mojang.serialization.DynamicOps;
import java.util.Objects;
import net.minecraft.util.parsing.packrat.ParseState;
import org.jspecify.annotations.Nullable;

record SnbtGrammar$IntegerLiteral(SnbtGrammar$Sign sign, SnbtGrammar$Base base, String digits, SnbtGrammar$IntegerSuffix suffix) {
   private SnbtGrammar$SignedPrefix signedOrDefault() {
      if (this.suffix.signed != null) {
         return this.suffix.signed;
      } else {
         return switch (this.base) {
            case BINARY, HEX -> SnbtGrammar$SignedPrefix.UNSIGNED;
            case DECIMAL -> SnbtGrammar$SignedPrefix.SIGNED;
         };
      }
   }

   private String cleanupDigits(final SnbtGrammar$Sign sign) {
      boolean needsUnderscoreRemoval = SnbtGrammar.needsUnderscoreRemoval(this.digits);
      if (sign != SnbtGrammar$Sign.MINUS && !needsUnderscoreRemoval) {
         return this.digits;
      } else {
         StringBuilder result = new StringBuilder();
         sign.append(result);
         SnbtGrammar.cleanAndAppend(result, this.digits, needsUnderscoreRemoval);
         return result.toString();
      }
   }

   @Nullable
   public <T> T create(final DynamicOps<T> ops, final ParseState<?> state) {
      return this.create(ops, Objects.requireNonNullElse(this.suffix.type, SnbtGrammar$TypeSuffix.INT), state);
   }

   @Nullable
   public <T> T create(final DynamicOps<T> ops, final SnbtGrammar$TypeSuffix type, final ParseState<?> state) {
      boolean isSigned = this.signedOrDefault() == SnbtGrammar$SignedPrefix.SIGNED;
      if (!isSigned && this.sign == SnbtGrammar$Sign.MINUS) {
         state.errorCollector().store(state.mark(), SnbtGrammar.ERROR_EXPECTED_NON_NEGATIVE_NUMBER);
         return null;
      } else {
         String fixedDigits = this.cleanupDigits(this.sign);

         int radix = switch (this.base) {
            case BINARY -> 2;
            case DECIMAL -> 10;
            case HEX -> 16;
         };

         try {
            if (isSigned) {
               return (T)(switch (type) {
                  case BYTE -> ops.createByte(Byte.parseByte(fixedDigits, radix));
                  case SHORT -> ops.createShort(Short.parseShort(fixedDigits, radix));
                  case INT -> ops.createInt(Integer.parseInt(fixedDigits, radix));
                  case LONG -> ops.createLong(Long.parseLong(fixedDigits, radix));
                  default -> {
                     state.errorCollector().store(state.mark(), SnbtGrammar.ERROR_EXPECTED_INTEGER_TYPE);
                     yield null;
                  }
               });
            } else {
               return (T)(switch (type) {
                  case BYTE -> ops.createByte(UnsignedBytes.parseUnsignedByte(fixedDigits, radix));
                  case SHORT -> ops.createShort(SnbtGrammar.parseUnsignedShort(fixedDigits, radix));
                  case INT -> ops.createInt(Integer.parseUnsignedInt(fixedDigits, radix));
                  case LONG -> ops.createLong(Long.parseUnsignedLong(fixedDigits, radix));
                  default -> {
                     state.errorCollector().store(state.mark(), SnbtGrammar.ERROR_EXPECTED_INTEGER_TYPE);
                     yield null;
                  }
               });
            }
         } catch (NumberFormatException var8) {
            state.errorCollector().store(state.mark(), SnbtGrammar.createNumberParseError(var8));
            return null;
         }
      }
   }
}
