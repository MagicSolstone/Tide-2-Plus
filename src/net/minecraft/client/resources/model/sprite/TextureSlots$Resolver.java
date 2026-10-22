package net.minecraft.client.resources.model.sprite;

import com.google.common.collect.Lists;
import com.mojang.logging.LogUtils;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectMaps;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap.Entry;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.resources.model.ModelDebugName;
import org.slf4j.Logger;

@Environment(EnvType.CLIENT)
public class TextureSlots$Resolver {
   private static final Logger LOGGER = LogUtils.getLogger();
   private final List<TextureSlots$Data> entries = new ArrayList<>();

   public TextureSlots$Resolver addLast(final TextureSlots$Data data) {
      this.entries.addLast(data);
      return this;
   }

   public TextureSlots$Resolver addFirst(final TextureSlots$Data data) {
      this.entries.addFirst(data);
      return this;
   }

   public TextureSlots resolve(final ModelDebugName debugNameProvider) {
      if (this.entries.isEmpty()) {
         return TextureSlots.EMPTY;
      } else {
         Object2ObjectMap<String, Material> resolved = new Object2ObjectArrayMap();
         Object2ObjectMap<String, TextureSlots$Reference> unresolved = new Object2ObjectArrayMap();

         for (TextureSlots$Data data : Lists.reverse(this.entries)) {
            data.values.forEach((slot, contents) -> {
               switch (contents) {
                  case TextureSlots$Value value:
                     unresolved.remove(slot);
                     resolved.put(slot, value.material());
                     break;
                  case TextureSlots$Reference reference:
                     resolved.remove(slot);
                     unresolved.put(slot, reference);
                     break;
                  default:
                     throw new MatchException(null, null);
               }
            });
         }

         if (unresolved.isEmpty()) {
            return new TextureSlots(resolved);
         } else {
            boolean hasChanges = true;

            while (hasChanges) {
               hasChanges = false;
               ObjectIterator<Entry<String, TextureSlots$Reference>> iterator = Object2ObjectMaps.fastIterator(unresolved);

               while (iterator.hasNext()) {
                  Entry<String, TextureSlots$Reference> entry = (Entry<String, TextureSlots$Reference>)iterator.next();
                  Material maybeResolved = (Material)resolved.get(((TextureSlots$Reference)entry.getValue()).target);
                  if (maybeResolved != null) {
                     resolved.put((String)entry.getKey(), maybeResolved);
                     iterator.remove();
                     hasChanges = true;
                  }
               }
            }

            if (!unresolved.isEmpty()) {
               LOGGER.warn(
                  "Unresolved texture references in {}:\n{}",
                  debugNameProvider.debugName(),
                  unresolved.entrySet()
                     .stream()
                     .map(e -> "\t#" + (String)e.getKey() + "-> #" + ((TextureSlots$Reference)e.getValue()).target + "\n")
                     .collect(Collectors.joining())
               );
            }

            return new TextureSlots(resolved);
         }
      }
   }
}
