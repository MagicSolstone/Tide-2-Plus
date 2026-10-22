package net.minecraft.world.attribute;

import net.minecraft.world.level.Level;

public interface WeatherAttributes$WeatherAccess {
   static WeatherAttributes$WeatherAccess from(final Level level) {
      return new WeatherAttributes$WeatherAccess$1(level);
   }

   float rainLevel();

   float thunderLevel();
}
