package net.minecraft.world.entity.raid;

import com.mojang.serialization.Codec;
import net.minecraft.util.StringRepresentable;

enum Raid$RaidStatus implements StringRepresentable {
   ONGOING("ongoing"),
   VICTORY("victory"),
   LOSS("loss"),
   STOPPED("stopped");

   public static final Codec<Raid$RaidStatus> CODEC = StringRepresentable.fromEnum(Raid$RaidStatus::values);
   private final String name;

   private Raid$RaidStatus(final String name) {
      this.name = name;
   }

   @Override
   public String getSerializedName() {
      return this.name;
   }
}
