package net.minecraft.client.resources.sounds;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public enum Sound$Type {
   FILE("file"),
   SOUND_EVENT("event");

   private final String name;

   private Sound$Type(final String name) {
      this.name = name;
   }

   @Nullable
   public static Sound$Type getByName(final String name) {
      for (Sound$Type type : values()) {
         if (type.name.equals(name)) {
            return type;
         }
      }

      return null;
   }
}
