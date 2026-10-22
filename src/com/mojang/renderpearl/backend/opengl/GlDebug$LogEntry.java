package com.mojang.renderpearl.backend.opengl;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
class GlDebug$LogEntry {
   private final int id;
   private final int source;
   private final int type;
   private final int severity;
   private final String message;
   private int count = 1;

   private GlDebug$LogEntry(final int source, final int type, final int id, final int severity, final String message) {
      this.id = id;
      this.source = source;
      this.type = type;
      this.severity = severity;
      this.message = message;
   }

   private boolean isSame(final int source, final int type, final int id, final int severity, final String message) {
      return type == this.type && source == this.source && id == this.id && severity == this.severity && message.equals(this.message);
   }

   @Override
   public String toString() {
      return "id="
         + this.id
         + ", source="
         + GlDebug.sourceToString(this.source)
         + ", type="
         + GlDebug.typeToString(this.type)
         + ", severity="
         + GlDebug.severityToString(this.severity)
         + ", message='"
         + this.message
         + "'";
   }
}
