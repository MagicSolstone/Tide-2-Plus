package net.minecraft.world.attribute;

import com.mojang.serialization.Codec;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.Level;

public enum BedRule$Rule implements StringRepresentable {
   ALWAYS("always"),
   WHEN_DARK("when_dark"),
   NEVER("never");

   public static final Codec<BedRule$Rule> CODEC = StringRepresentable.fromEnum(BedRule$Rule::values);
   private final String name;

   private BedRule$Rule(final String name) {
      this.name = name;
   }

   public boolean test(final Level level) {
      return switch (this) {
         case ALWAYS -> true;
         case WHEN_DARK -> level.isDarkOutside();
         case NEVER -> false;
      };
   }

   @Override
   public String getSerializedName() {
      return this.name;
   }
}
