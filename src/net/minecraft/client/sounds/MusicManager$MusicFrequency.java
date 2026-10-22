package net.minecraft.client.sounds;

import com.mojang.serialization.Codec;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.Music;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public enum MusicManager$MusicFrequency implements StringRepresentable {
   DEFAULT("DEFAULT", "options.music_frequency.default", 20),
   FREQUENT("FREQUENT", "options.music_frequency.frequent", 10),
   CONSTANT("CONSTANT", "options.music_frequency.constant", 0);

   public static final Codec<MusicManager$MusicFrequency> CODEC = StringRepresentable.fromEnum(MusicManager$MusicFrequency::values);
   private final String name;
   private final int maxFrequency;
   private final Component caption;

   private MusicManager$MusicFrequency(final String name, final String translationKey, final int maxFrequencyMinutes) {
      this.name = name;
      this.maxFrequency = maxFrequencyMinutes * 1200;
      this.caption = Component.translatable(translationKey);
   }

   private int getNextSongDelay(@Nullable final Music music, final RandomSource random) {
      if (music == null) {
         return this.maxFrequency;
      } else if (this == CONSTANT) {
         return 100;
      } else {
         int minFrequency = Math.min(music.minDelay(), this.maxFrequency);
         int maxFrequency = Math.min(music.maxDelay(), this.maxFrequency);
         return Mth.nextInt(random, minFrequency, maxFrequency);
      }
   }

   public Component caption() {
      return this.caption;
   }

   @Override
   public String getSerializedName() {
      return this.name;
   }
}
