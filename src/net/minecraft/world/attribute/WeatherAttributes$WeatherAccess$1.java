package net.minecraft.world.attribute;

import net.minecraft.world.level.Level;

class WeatherAttributes$WeatherAccess$1 implements WeatherAttributes$WeatherAccess {
   WeatherAttributes$WeatherAccess$1(final Level val$level) {
      this.val$level = val$levelx;
   }

   @Override
   public float rainLevel() {
      return this.val$level.getRainLevel(1.0F);
   }

   @Override
   public float thunderLevel() {
      return this.val$level.getThunderLevel(1.0F);
   }
}
