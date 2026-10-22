package net.minecraft.nbt;

import com.mojang.logging.LogUtils;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import org.slf4j.Logger;

public class TextComponentTagVisitor implements TagVisitor {
   private static final Logger LOGGER = LogUtils.getLogger();
   private static final int INLINE_LIST_THRESHOLD = 8;
   private static final int MAX_DEPTH = 64;
   private static final int MAX_LENGTH = 128;
   private static final Pattern SIMPLE_VALUE = Pattern.compile("[A-Za-z0-9._+-]+");
   private static final Component NEWLINE = Component.literal("\n");
   private static final Component ELEMENT_SPACING = Component.literal(" ");
   private final String indentation;
   private final TextComponentTagVisitor$Styling styling;
   private final boolean sortKeys;
   private int indentDepth;
   private int depth;
   private final MutableComponent result = Component.empty();

   public TextComponentTagVisitor(final String indentation) {
      this(indentation, TextComponentTagVisitor$RichStyling.INSTANCE);
   }

   public TextComponentTagVisitor(final String indentation, final TextComponentTagVisitor$Styling styling) {
      this(indentation, styling, LOGGER.isDebugEnabled());
   }

   public TextComponentTagVisitor(final String indentation, final TextComponentTagVisitor$Styling styling, final boolean sortKeys) {
      this.indentation = indentation;
      this.styling = styling;
      this.sortKeys = sortKeys;
   }

   public Component visit(final Tag tag) {
      tag.accept(this);
      return this.result;
   }

   private TextComponentTagVisitor append(final String string, final Style style) {
      this.result.append(Component.literal(string).withStyle(style));
      return this;
   }

   private TextComponentTagVisitor append(final Component component) {
      this.result.append(component);
      return this;
   }

   private TextComponentTagVisitor append(final TextComponentTagVisitor$Token token) {
      this.result.append(this.styling.token(token));
      return this;
   }

   @Override
   public void visitString(final StringTag tag) {
      String quoted = StringTag.quoteAndEscape(tag.value());
      Component quote = Component.literal(quoted.substring(0, 1));
      this.append(quote).append(quoted.substring(1, quoted.length() - 1), this.styling.stringStyle()).append(quote);
   }

   @Override
   public void visitByte(final ByteTag tag) {
      this.append(String.valueOf(tag.value()), this.styling.numberStyle()).append(TextComponentTagVisitor$Token.BYTE_SUFFIX);
   }

   @Override
   public void visitShort(final ShortTag tag) {
      this.append(String.valueOf(tag.value()), this.styling.numberStyle()).append(TextComponentTagVisitor$Token.SHORT_SUFFIX);
   }

   @Override
   public void visitInt(final IntTag tag) {
      this.append(String.valueOf(tag.value()), this.styling.numberStyle());
   }

   @Override
   public void visitLong(final LongTag tag) {
      this.append(String.valueOf(tag.value()), this.styling.numberStyle()).append(TextComponentTagVisitor$Token.LONG_SUFFIX);
   }

   @Override
   public void visitFloat(final FloatTag tag) {
      this.append(String.valueOf(tag.value()), this.styling.numberStyle()).append(TextComponentTagVisitor$Token.FLOAT_SUFFIX);
   }

   @Override
   public void visitDouble(final DoubleTag tag) {
      this.append(String.valueOf(tag.value()), this.styling.numberStyle()).append(TextComponentTagVisitor$Token.DOUBLE_SUFFIX);
   }

   @Override
   public void visitByteArray(final ByteArrayTag tag) {
      this.append(TextComponentTagVisitor$Token.LIST_OPEN)
         .append(TextComponentTagVisitor$Token.BYTE_ARRAY_PREFIX)
         .append(TextComponentTagVisitor$Token.LIST_TYPE_SEPARATOR);
      byte[] data = tag.getAsByteArray();

      for (int i = 0; i < data.length && i < 128; i++) {
         this.append(ELEMENT_SPACING).append(String.valueOf(data[i]), this.styling.numberStyle()).append(TextComponentTagVisitor$Token.BYTE_SUFFIX);
         if (i != data.length - 1) {
            this.append(TextComponentTagVisitor$Token.ELEMENT_SEPARATOR);
         }
      }

      if (data.length > 128) {
         this.append(TextComponentTagVisitor$Token.FOLDED);
      }

      this.append(TextComponentTagVisitor$Token.LIST_CLOSE);
   }

   @Override
   public void visitIntArray(final IntArrayTag tag) {
      this.append(TextComponentTagVisitor$Token.LIST_OPEN)
         .append(TextComponentTagVisitor$Token.INT_ARRAY_PREFIX)
         .append(TextComponentTagVisitor$Token.LIST_TYPE_SEPARATOR);
      int[] data = tag.getAsIntArray();

      for (int i = 0; i < data.length && i < 128; i++) {
         this.append(ELEMENT_SPACING).append(String.valueOf(data[i]), this.styling.numberStyle());
         if (i != data.length - 1) {
            this.append(TextComponentTagVisitor$Token.ELEMENT_SEPARATOR);
         }
      }

      if (data.length > 128) {
         this.append(TextComponentTagVisitor$Token.FOLDED);
      }

      this.append(TextComponentTagVisitor$Token.LIST_CLOSE);
   }

   @Override
   public void visitLongArray(final LongArrayTag tag) {
      this.append(TextComponentTagVisitor$Token.LIST_OPEN)
         .append(TextComponentTagVisitor$Token.LONG_ARRAY_PREFIX)
         .append(TextComponentTagVisitor$Token.LIST_TYPE_SEPARATOR);
      long[] data = tag.getAsLongArray();

      for (int i = 0; i < data.length && i < 128; i++) {
         this.append(ELEMENT_SPACING).append(String.valueOf(data[i]), this.styling.numberStyle()).append(TextComponentTagVisitor$Token.LONG_SUFFIX);
         if (i != data.length - 1) {
            this.append(TextComponentTagVisitor$Token.ELEMENT_SEPARATOR);
         }
      }

      if (data.length > 128) {
         this.append(TextComponentTagVisitor$Token.FOLDED);
      }

      this.append(TextComponentTagVisitor$Token.LIST_CLOSE);
   }

   private static boolean shouldWrapListElements(final ListTag list) {
      if (list.size() >= 8) {
         return false;
      } else {
         for (Tag element : list) {
            if (!(element instanceof NumericTag)) {
               return true;
            }
         }

         return false;
      }
   }

   @Override
   public void visitList(final ListTag tag) {
      if (tag.isEmpty()) {
         this.append(TextComponentTagVisitor$Token.LIST_OPEN).append(TextComponentTagVisitor$Token.LIST_CLOSE);
      } else if (this.depth >= 64) {
         this.append(TextComponentTagVisitor$Token.LIST_OPEN).append(TextComponentTagVisitor$Token.FOLDED).append(TextComponentTagVisitor$Token.LIST_CLOSE);
      } else if (!shouldWrapListElements(tag)) {
         this.append(TextComponentTagVisitor$Token.LIST_OPEN);

         for (int i = 0; i < tag.size(); i++) {
            if (i != 0) {
               this.append(TextComponentTagVisitor$Token.ELEMENT_SEPARATOR).append(ELEMENT_SPACING);
            }

            this.appendSubTag(tag.get(i), false);
         }

         this.append(TextComponentTagVisitor$Token.LIST_CLOSE);
      } else {
         this.append(TextComponentTagVisitor$Token.LIST_OPEN);
         if (!this.indentation.isEmpty()) {
            this.append(NEWLINE);
         }

         Component entryIndent = Component.literal(this.indentation.repeat(this.indentDepth + 1));
         Component elementSpacing = this.indentation.isEmpty() ? ELEMENT_SPACING : NEWLINE;

         for (int i = 0; i < tag.size() && i < 128; i++) {
            this.append(entryIndent);
            this.appendSubTag(tag.get(i), true);
            if (i != tag.size() - 1) {
               this.append(TextComponentTagVisitor$Token.ELEMENT_SEPARATOR).append(elementSpacing);
            }
         }

         if (tag.size() > 128) {
            this.append(entryIndent).append(TextComponentTagVisitor$Token.FOLDED);
         }

         if (!this.indentation.isEmpty()) {
            this.append(NEWLINE).append(Component.literal(this.indentation.repeat(this.indentDepth)));
         }

         this.append(TextComponentTagVisitor$Token.LIST_CLOSE);
      }
   }

   @Override
   public void visitCompound(final CompoundTag tag) {
      if (tag.isEmpty()) {
         this.append(TextComponentTagVisitor$Token.STRUCT_OPEN).append(TextComponentTagVisitor$Token.STRUCT_CLOSE);
      } else if (this.depth >= 64) {
         this.append(TextComponentTagVisitor$Token.STRUCT_OPEN).append(TextComponentTagVisitor$Token.FOLDED).append(TextComponentTagVisitor$Token.STRUCT_CLOSE);
      } else {
         this.append(TextComponentTagVisitor$Token.STRUCT_OPEN);
         Collection<String> keys;
         if (this.sortKeys) {
            List<String> keyCopy = new ArrayList<>(tag.keySet());
            Collections.sort(keyCopy);
            keys = keyCopy;
         } else {
            keys = tag.keySet();
         }

         if (!this.indentation.isEmpty()) {
            this.append(NEWLINE);
         }

         Component entryIndent = Component.literal(this.indentation.repeat(this.indentDepth + 1));
         Component elementSpacing = this.indentation.isEmpty() ? ELEMENT_SPACING : NEWLINE;
         Iterator<String> iterator = keys.iterator();

         while (iterator.hasNext()) {
            String key = iterator.next();
            this.append(entryIndent).append(this.handleEscapePretty(key)).append(TextComponentTagVisitor$Token.NAME_VALUE_SEPARATOR).append(ELEMENT_SPACING);
            this.appendSubTag(tag.get(key), true);
            if (iterator.hasNext()) {
               this.append(TextComponentTagVisitor$Token.ELEMENT_SEPARATOR).append(elementSpacing);
            }
         }

         if (!this.indentation.isEmpty()) {
            this.append(NEWLINE).append(Component.literal(this.indentation.repeat(this.indentDepth)));
         }

         this.append(TextComponentTagVisitor$Token.STRUCT_CLOSE);
      }
   }

   private void appendSubTag(final Tag tag, final boolean indent) {
      if (indent) {
         this.indentDepth++;
      }

      this.depth++;

      try {
         tag.accept(this);
      } finally {
         if (indent) {
            this.indentDepth--;
         }

         this.depth--;
      }
   }

   private Component handleEscapePretty(final String input) {
      if (SIMPLE_VALUE.matcher(input).matches()) {
         return Component.literal(input).withStyle(this.styling.keyStyle());
      } else {
         String quoted = StringTag.quoteAndEscape(input);
         String quote = quoted.substring(0, 1);
         Component inner = Component.literal(quoted.substring(1, quoted.length() - 1)).withStyle(this.styling.keyStyle());
         return Component.literal(quote).append(inner).append(quote);
      }
   }

   @Override
   public void visitEnd(final EndTag tag) {
   }
}
