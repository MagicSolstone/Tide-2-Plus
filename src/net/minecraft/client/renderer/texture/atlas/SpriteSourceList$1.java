package net.minecraft.client.renderer.texture.atlas;

import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Map.Entry;
import java.util.function.Predicate;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resources.Identifier;

@Environment(EnvType.CLIENT)
class SpriteSourceList$1 implements SpriteSource$Output {
   SpriteSourceList$1(final SpriteSourceList this$0, final Map val$sprites) {
      this.val$sprites = val$spritesx;
      Objects.requireNonNull(this$0);
      super();
   }

   @Override
   public void add(final Identifier id, final SpriteSource$DiscardableLoader sprite) {
      SpriteSource$DiscardableLoader previous = this.val$sprites.put(id, sprite);
      if (previous != null) {
         previous.discard();
      }
   }

   @Override
   public void removeAll(final Predicate<Identifier> predicate) {
      Iterator<Entry<Identifier, SpriteSource$DiscardableLoader>> it = this.val$sprites.entrySet().iterator();

      while (it.hasNext()) {
         Entry<Identifier, SpriteSource$DiscardableLoader> entry = it.next();
         if (predicate.test(entry.getKey())) {
            entry.getValue().discard();
            it.remove();
         }
      }
   }
}
