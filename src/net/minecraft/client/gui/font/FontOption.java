package net.minecraft.client.gui.font;

import com.mojang.serialization.Codec;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.StringRepresentable;

@Environment(EnvType.CLIENT)
public enum FontOption implements StringRepresentable {
   UNIFORM("uniform"),
   JAPANESE_VARIANTS("jp");

   public static final Codec<FontOption> CODEC = StringRepresentable.fromEnum(FontOption::values);
   private final String name;

   private FontOption(final String name) {
      this.name = name;
   }

   @Override
   public String getSerializedName() {
      return this.name;
   }
}
