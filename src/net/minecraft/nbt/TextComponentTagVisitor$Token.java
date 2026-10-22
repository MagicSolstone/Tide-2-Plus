package net.minecraft.nbt;

public enum TextComponentTagVisitor$Token {
   FOLDED("<...>"),
   ELEMENT_SEPARATOR(","),
   LIST_CLOSE("]"),
   LIST_OPEN("["),
   LIST_TYPE_SEPARATOR(";"),
   STRUCT_CLOSE("}"),
   STRUCT_OPEN("{"),
   NAME_VALUE_SEPARATOR(":"),
   BYTE_SUFFIX("b"),
   BYTE_ARRAY_PREFIX("B"),
   SHORT_SUFFIX("s"),
   INT_ARRAY_PREFIX("I"),
   LONG_SUFFIX("L"),
   LONG_ARRAY_PREFIX("L"),
   FLOAT_SUFFIX("f"),
   DOUBLE_SUFFIX("d");

   public final String text;

   private TextComponentTagVisitor$Token(final String text) {
      this.text = text;
   }
}
