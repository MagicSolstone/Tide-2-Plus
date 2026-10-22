package net.minecraft.client.renderer.texture.atlas;

import java.util.function.Predicate;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.Resource;

@Environment(EnvType.CLIENT)
public interface SpriteSource$Output {
   default void add(final Identifier id, final Resource resource) {
      this.add(id, loader -> loader.loadSprite(id, resource));
   }

   void add(Identifier id, SpriteSource$DiscardableLoader sprite);

   void removeAll(Predicate<Identifier> predicate);
}
