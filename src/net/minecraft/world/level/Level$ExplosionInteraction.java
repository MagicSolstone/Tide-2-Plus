package net.minecraft.world.level;

import com.mojang.serialization.Codec;
import net.minecraft.util.StringRepresentable;

public enum Level$ExplosionInteraction implements StringRepresentable {
   NONE("none"),
   BLOCK("block"),
   MOB("mob"),
   TNT("tnt"),
   TRIGGER("trigger");

   public static final Codec<Level$ExplosionInteraction> CODEC = StringRepresentable.fromEnum(Level$ExplosionInteraction::values);
   private final String id;

   private Level$ExplosionInteraction(final String id) {
      this.id = id;
   }

   @Override
   public String getSerializedName() {
      return this.id;
   }
}
