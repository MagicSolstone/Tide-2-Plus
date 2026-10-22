package net.minecraft.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public enum ResourceLoadStateTracker$ReloadReason {
   INITIAL("initial"),
   MANUAL("manual"),
   UNKNOWN("unknown");

   private final String name;

   private ResourceLoadStateTracker$ReloadReason(final String name) {
      this.name = name;
   }
}
