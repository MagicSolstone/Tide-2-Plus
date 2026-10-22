package net.minecraft.client.multiplayer.chat;

import java.util.function.BooleanSupplier;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.chat.MessageSignature;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
record ChatListener$Message(@Nullable MessageSignature signature, BooleanSupplier handler) {
   public boolean accept() {
      return this.handler.getAsBoolean();
   }
}
