package net.minecraft.client.sounds;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.audio.SoundBuffer;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.resources.sounds.Sound;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.ResourceProvider;
import net.minecraft.util.Util;

@Environment(EnvType.CLIENT)
public class SoundBufferLibrary {
   private final ResourceProvider resourceManager;
   private final Map<Identifier, CompletableFuture<SoundBuffer>> cache = Maps.newHashMap();

   public SoundBufferLibrary(final ResourceProvider resourceProvider) {
      this.resourceManager = resourceProvider;
   }

   public CompletableFuture<SoundBuffer> getCompleteBuffer(final Identifier location) {
      return this.cache.computeIfAbsent(location, l -> CompletableFuture.supplyAsync(() -> {
         try {
            SoundBuffer x2;
            try (
               InputStream is = this.resourceManager.open(l);
               FiniteAudioStream as = new JOrbisAudioStream(is);
            ) {
               ByteBuffer data = as.readAll();
               x2 = new SoundBuffer(data, as.getFormat());
            }

            return x2;
         } catch (IOException var10) {
            throw new CompletionException(var10);
         }
      }, Util.nonCriticalIoPool()));
   }

   public CompletableFuture<AudioStream> getStream(final Identifier location, final boolean looping) {
      return CompletableFuture.supplyAsync(() -> {
         try {
            InputStream is = this.resourceManager.open(location);
            return (AudioStream)(looping ? new LoopingAudioStream(JOrbisAudioStream::new, is) : new JOrbisAudioStream(is));
         } catch (IOException var4) {
            throw new CompletionException(var4);
         }
      }, Util.nonCriticalIoPool());
   }

   public void clear() {
      this.cache.values().forEach(future -> future.thenAccept(SoundBuffer::discardAlBuffer));
      this.cache.clear();
   }

   public CompletableFuture<?> preload(final Collection<Sound> sounds) {
      return CompletableFuture.allOf(sounds.stream().map(sound -> this.getCompleteBuffer(sound.getPath())).toArray(CompletableFuture[]::new));
   }

   public void enumerate(final SoundBufferLibrary$DebugOutput debugOutput) {
      this.cache.forEach((id, bufferFuture) -> {
         SoundBuffer buffer = bufferFuture.getNow(null);
         if (buffer != null && buffer.isValid()) {
            debugOutput.accountBuffer(id, buffer.size(), buffer.format());
         }
      });
   }
}
