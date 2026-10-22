package net.minecraft.client.quickplay;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.time.Instant;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.level.GameType;

@Environment(EnvType.CLIENT)
record QuickPlayLog$QuickPlayEntry(QuickPlayLog$QuickPlayWorld quickPlayWorld, Instant lastPlayedTime, GameType gamemode) {
   public static final Codec<QuickPlayLog$QuickPlayEntry> CODEC = RecordCodecBuilder.create(
      i -> i.group(
            QuickPlayLog$QuickPlayWorld.MAP_CODEC.forGetter(QuickPlayLog$QuickPlayEntry::quickPlayWorld),
            ExtraCodecs.INSTANT_ISO8601.fieldOf("lastPlayedTime").forGetter(QuickPlayLog$QuickPlayEntry::lastPlayedTime),
            GameType.CODEC.fieldOf("gamemode").forGetter(QuickPlayLog$QuickPlayEntry::gamemode)
         )
         .apply(i, QuickPlayLog$QuickPlayEntry::new)
   );
}
