package net.minecraft.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.main.SilentInitException;

@Environment(EnvType.CLIENT)
public class GameNarrator$NarratorInitException extends SilentInitException {
   public GameNarrator$NarratorInitException(final String message) {
      super(message);
   }
}
