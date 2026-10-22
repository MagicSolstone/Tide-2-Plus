package net.minecraft.client.gui.components;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.chat.GuiMessage$Line;
import net.minecraft.util.Mth;

@FunctionalInterface
@Environment(EnvType.CLIENT)
interface ChatComponent$AlphaCalculator {
   ChatComponent$AlphaCalculator FULLY_VISIBLE = var0 -> 1.0F;

   static ChatComponent$AlphaCalculator timeBased(final int currentTickTime) {
      return message -> {
         int tickDelta = currentTickTime - message.addedTime();
         double t = tickDelta / 200.0;
         t = 1.0 - t;
         t *= 10.0;
         t = Mth.clamp(t, 0.0, 1.0);
         t *= t;
         return (float)t;
      };
   }

   float calculate(GuiMessage$Line message);
}
