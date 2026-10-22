package net.minecraft.client.main;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public record GameConfig$QuickPlayData(@Nullable String logPath, GameConfig$QuickPlayVariant variant) {
   public boolean isEnabled() {
      return this.variant.isEnabled();
   }
}
