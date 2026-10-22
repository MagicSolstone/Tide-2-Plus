package net.minecraft.client.sounds;

import com.google.common.collect.Maps;
import java.util.Map;
import java.util.Map.Entry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.resources.sounds.Sound;
import net.minecraft.client.resources.sounds.SoundEventRegistration;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceProvider;

@Environment(EnvType.CLIENT)
public class SoundManager$Preparations {
   private final Map<Identifier, WeighedSoundEvents> registry = Maps.newHashMap();
   private Map<Identifier, Resource> soundCache = Map.of();

   protected SoundManager$Preparations() {
   }

   private void listResources(final ResourceManager resourceManager) {
      this.soundCache = Sound.SOUND_LISTER.listMatchingResources(resourceManager);
   }

   private void handleRegistration(final Identifier eventLocation, final SoundEventRegistration soundEventRegistration) {
      WeighedSoundEvents registration = this.registry.get(eventLocation);
      boolean missesRegistration = registration == null;
      if (missesRegistration || soundEventRegistration.isReplace()) {
         if (!missesRegistration) {
            SoundManager.LOGGER.debug("Replaced sound event location {}", eventLocation);
         }

         registration = new WeighedSoundEvents(eventLocation, soundEventRegistration.getSubtitle());
         this.registry.put(eventLocation, registration);
      }

      ResourceProvider cachedProvider = ResourceProvider.fromMap(this.soundCache);

      for (Sound sound : soundEventRegistration.getSounds()) {
         Identifier soundLocation = sound.getLocation();
         Weighted<Sound> weighted;
         switch (sound.getType()) {
            case FILE:
               if (!SoundManager.validateSoundResource(sound, eventLocation, cachedProvider)) {
                  continue;
               }

               weighted = sound;
               break;
            case SOUND_EVENT:
               weighted = new SoundManager$Preparations$1(this, soundLocation, sound);
               break;
            default:
               throw new IllegalStateException("Unknown SoundEventRegistration type: " + sound.getType());
         }

         registration.addSound(weighted);
      }
   }

   public void apply(final Map<Identifier, WeighedSoundEvents> registry, final Map<Identifier, Resource> soundCache, final SoundEngine engine) {
      registry.clear();
      soundCache.clear();
      soundCache.putAll(this.soundCache);

      for (Entry<Identifier, WeighedSoundEvents> entry : this.registry.entrySet()) {
         registry.put(entry.getKey(), entry.getValue());
         entry.getValue().preloadIfRequired(engine);
      }
   }
}
