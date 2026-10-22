package net.minecraft.world.level.levelgen.densityfunction;

import net.minecraft.util.context.ContextMap;

public class SamplerContext$Builder {
   private ContextMap userFields = ContextMap.EMPTY;
   private DensityBufferArena bufferArena = DensityBufferArena.GLOBAL;
   private boolean enableCaches;

   private SamplerContext$Builder() {
   }

   public SamplerContext$Builder setUserFields(final ContextMap userFields) {
      this.userFields = userFields;
      return this;
   }

   public SamplerContext$Builder useBufferArena(final DensityBufferArena bufferArena) {
      this.bufferArena = bufferArena;
      return this;
   }

   public SamplerContext$Builder enableCaches() {
      this.enableCaches = true;
      return this;
   }

   public SamplerContext build() {
      return new SamplerContext(this.userFields, this.bufferArena, this.enableCaches);
   }
}
