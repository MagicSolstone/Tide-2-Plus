package net.minecraft.client.resources.model.sprite;

import java.util.function.Function;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.resources.Identifier;

@Environment(EnvType.CLIENT)
public record SpriteId(Identifier atlasLocation, Identifier texture) {
   public RenderType renderType(final Function<Identifier, RenderType> renderType) {
      return renderType.apply(this.atlasLocation);
   }
}
