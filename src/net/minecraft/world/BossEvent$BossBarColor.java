package net.minecraft.world;

import com.mojang.serialization.Codec;
import net.minecraft.ChatFormatting;
import net.minecraft.util.StringRepresentable;

public enum BossEvent$BossBarColor implements StringRepresentable {
   PINK("pink", ChatFormatting.RED),
   BLUE("blue", ChatFormatting.BLUE),
   RED("red", ChatFormatting.DARK_RED),
   GREEN("green", ChatFormatting.GREEN),
   YELLOW("yellow", ChatFormatting.YELLOW),
   PURPLE("purple", ChatFormatting.DARK_BLUE),
   WHITE("white", ChatFormatting.WHITE);

   public static final Codec<BossEvent$BossBarColor> CODEC = StringRepresentable.fromEnum(BossEvent$BossBarColor::values);
   private final String name;
   private final ChatFormatting formatting;

   private BossEvent$BossBarColor(final String name, final ChatFormatting formatting) {
      this.name = name;
      this.formatting = formatting;
   }

   public ChatFormatting getFormatting() {
      return this.formatting;
   }

   public String getName() {
      return this.name;
   }

   @Override
   public String getSerializedName() {
      return this.name;
   }
}
