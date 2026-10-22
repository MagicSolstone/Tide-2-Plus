package net.minecraft.world;

import com.mojang.serialization.Codec;
import net.minecraft.util.StringRepresentable;

public enum BossEvent$BossBarOverlay implements StringRepresentable {
   PROGRESS("progress"),
   NOTCHED_6("notched_6"),
   NOTCHED_10("notched_10"),
   NOTCHED_12("notched_12"),
   NOTCHED_20("notched_20");

   public static final Codec<BossEvent$BossBarOverlay> CODEC = StringRepresentable.fromEnum(BossEvent$BossBarOverlay::values);
   private final String name;

   private BossEvent$BossBarOverlay(final String name) {
      this.name = name;
   }

   public String getName() {
      return this.name;
   }

   @Override
   public String getSerializedName() {
      return this.name;
   }
}
