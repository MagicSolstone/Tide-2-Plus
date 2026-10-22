package net.minecraft.network.chat;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import java.util.BitSet;
import net.minecraft.ChatFormatting;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.StringRepresentable;
import org.apache.commons.lang3.StringUtils;
import org.jspecify.annotations.Nullable;

public class FilterMask {
   public static final Codec<FilterMask> CODEC = StringRepresentable.fromEnum(FilterMask$Type::values).dispatch(FilterMask::type, FilterMask$Type::codec);
   public static final StreamCodec<ByteBuf, FilterMask> STREAM_CODEC = FilterMask$Type.STREAM_CODEC.dispatch(FilterMask::type, FilterMask$Type::streamCodec);
   public static final FilterMask FULLY_FILTERED = new FilterMask(new BitSet(0), FilterMask$Type.FULLY_FILTERED);
   public static final FilterMask PASS_THROUGH = new FilterMask(new BitSet(0), FilterMask$Type.PASS_THROUGH);
   public static final Style FILTERED_STYLE = Style.EMPTY
      .withColor(ChatFormatting.DARK_GRAY)
      .withHoverEvent(new HoverEvent$ShowText(Component.translatable("chat.filtered")));
   private static final char HASH = '#';
   private final BitSet mask;
   private final FilterMask$Type type;

   private FilterMask(final BitSet mask, final FilterMask$Type type) {
      this.mask = mask;
      this.type = type;
   }

   private FilterMask(final BitSet mask) {
      this.mask = mask;
      this.type = FilterMask$Type.PARTIALLY_FILTERED;
   }

   public FilterMask(final int length) {
      this(new BitSet(length), FilterMask$Type.PARTIALLY_FILTERED);
   }

   private FilterMask$Type type() {
      return this.type;
   }

   private BitSet mask() {
      return this.mask;
   }

   public void setFiltered(final int index) {
      this.mask.set(index);
   }

   @Nullable
   public String apply(final String text) {
      return switch (this.type) {
         case PASS_THROUGH -> text;
         case FULLY_FILTERED -> null;
         case PARTIALLY_FILTERED -> {
            char[] chars = text.toCharArray();

            for (int i = 0; i < chars.length && i < this.mask.length(); i++) {
               if (this.mask.get(i)) {
                  chars[i] = '#';
               }
            }

            yield new String(chars);
         }
      };
   }

   @Nullable
   public Component applyWithFormatting(final String text) {
      return switch (this.type) {
         case PASS_THROUGH -> Component.literal(text);
         case FULLY_FILTERED -> null;
         case PARTIALLY_FILTERED -> {
            MutableComponent result = Component.empty();
            int previousIndex = 0;
            boolean filtered = this.mask.get(0);

            while (true) {
               int nextIndex = filtered ? this.mask.nextClearBit(previousIndex) : this.mask.nextSetBit(previousIndex);
               nextIndex = nextIndex < 0 ? text.length() : nextIndex;
               if (nextIndex == previousIndex) {
                  yield result;
               }

               if (filtered) {
                  result.append(Component.literal(StringUtils.repeat('#', nextIndex - previousIndex)).withStyle(FILTERED_STYLE));
               } else {
                  result.append(text.substring(previousIndex, nextIndex));
               }

               filtered = !filtered;
               previousIndex = nextIndex;
            }
         }
      };
   }

   public boolean isEmpty() {
      return this.type == FilterMask$Type.PASS_THROUGH;
   }

   public boolean isFullyFiltered() {
      return this.type == FilterMask$Type.FULLY_FILTERED;
   }

   @Override
   public boolean equals(final Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         FilterMask that = (FilterMask)o;
         return this.mask.equals(that.mask) && this.type == that.type;
      } else {
         return false;
      }
   }

   @Override
   public int hashCode() {
      int result = this.mask.hashCode();
      return 31 * result + this.type.hashCode();
   }
}
