package net.minecraft.client.gui.components.debug;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Map;
import java.util.Optional;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resources.Identifier;

@Environment(EnvType.CLIENT)
record DebugScreenEntryList$SerializedOptions(Optional<DebugScreenProfile> profile, Optional<Map<Identifier, DebugScreenEntryStatus>> custom) {
   private static final Codec<Map<Identifier, DebugScreenEntryStatus>> CUSTOM_ENTRIES_CODEC = Codec.unboundedMap(Identifier.CODEC, DebugScreenEntryStatus.CODEC);
   public static final Codec<DebugScreenEntryList$SerializedOptions> CODEC = RecordCodecBuilder.create(
      i -> i.group(
            DebugScreenProfile.CODEC.optionalFieldOf("profile").forGetter(DebugScreenEntryList$SerializedOptions::profile),
            CUSTOM_ENTRIES_CODEC.optionalFieldOf("custom").forGetter(DebugScreenEntryList$SerializedOptions::custom)
         )
         .apply(i, DebugScreenEntryList$SerializedOptions::new)
   );
}
