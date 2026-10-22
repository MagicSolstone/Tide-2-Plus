package net.minecraft.client.sounds;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.RandomSource;

@Environment(EnvType.CLIENT)
public interface Weighted<T> {
   int getWeight();

   T getSound(RandomSource random);

   void preloadIfRequired(SoundEngine soundEngine);
}
