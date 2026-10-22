package com.mojang.renderpearl.api.device;

import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class BackendCreationException extends Exception {
   private final BackendCreationException$Reason reason;
   private final List<String> missingCapabilities;

   public BackendCreationException(final String message, final BackendCreationException$Reason reason, final List<String> missingCapabilities) {
      super(message);
      this.reason = reason;
      this.missingCapabilities = List.copyOf(missingCapabilities);
   }

   public BackendCreationException(final String message, final BackendCreationException$Reason reason) {
      this(message, reason, List.of());
   }

   public BackendCreationException$Reason getReason() {
      return this.reason;
   }

   public List<String> getMissingCapabilities() {
      return this.missingCapabilities;
   }
}
