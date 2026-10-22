package net.minecraft.client.renderer.entity.state;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.world.entity.Display$TextDisplay$CachedInfo;
import net.minecraft.world.entity.Display$TextDisplay$TextRenderState;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class TextDisplayEntityRenderState extends DisplayEntityRenderState {
   @Nullable
   public Display$TextDisplay$TextRenderState textRenderState;
   @Nullable
   public Display$TextDisplay$CachedInfo cachedInfo;

   @Override
   public boolean hasSubState() {
      return this.textRenderState != null;
   }
}
