package net.minecraft.client.renderer.block.dispatch.multipart;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public record KeyValueCondition$Term(String value, boolean negated) {
   private static final String NEGATE = "!";

   public KeyValueCondition$Term(String value, boolean negated) {
      if (value.isEmpty()) {
         throw new IllegalArgumentException("Empty term");
      } else {
         this.value = value;
         this.negated = negated;
      }
   }

   public static KeyValueCondition$Term parse(final String value) {
      return value.startsWith("!") ? new KeyValueCondition$Term(value.substring(1), true) : new KeyValueCondition$Term(value, false);
   }

   @Override
   public String toString() {
      return this.negated ? "!" + this.value : this.value;
   }
}
