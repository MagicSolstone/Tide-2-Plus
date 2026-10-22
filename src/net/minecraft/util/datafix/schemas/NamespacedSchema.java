package net.minecraft.util.datafix.schemas;

import com.mojang.datafixers.DSL.TypeReference;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.types.Type;
import com.mojang.datafixers.types.templates.Const.PrimitiveType;
import com.mojang.serialization.codecs.PrimitiveCodec;
import net.minecraft.resources.Identifier;

public class NamespacedSchema extends Schema {
   public static final PrimitiveCodec<String> NAMESPACED_STRING_CODEC = new NamespacedSchema$1();
   private static final Type<String> NAMESPACED_STRING = new PrimitiveType(NAMESPACED_STRING_CODEC);

   public NamespacedSchema(final int versionKey, final Schema parent) {
      super(versionKey, parent);
   }

   public static String ensureNamespaced(final String input) {
      Identifier identifier = Identifier.tryParse(input);
      return identifier != null ? identifier.toString() : input;
   }

   public static Type<String> namespacedString() {
      return NAMESPACED_STRING;
   }

   public Type<?> getChoiceType(final TypeReference type, final String choiceName) {
      return super.getChoiceType(type, ensureNamespaced(choiceName));
   }
}
