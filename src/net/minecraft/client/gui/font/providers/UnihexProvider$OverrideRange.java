package net.minecraft.client.gui.font.providers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.ExtraCodecs;

@Environment(EnvType.CLIENT)
record UnihexProvider$OverrideRange(int from, int to, UnihexProvider$Dimensions dimensions) {
   private static final Codec<UnihexProvider$OverrideRange> RAW_CODEC = RecordCodecBuilder.create(
      i -> i.group(
            ExtraCodecs.CODEPOINT.fieldOf("from").forGetter(UnihexProvider$OverrideRange::from),
            ExtraCodecs.CODEPOINT.fieldOf("to").forGetter(UnihexProvider$OverrideRange::to),
            UnihexProvider$Dimensions.MAP_CODEC.forGetter(UnihexProvider$OverrideRange::dimensions)
         )
         .apply(i, UnihexProvider$OverrideRange::new)
   );
   public static final Codec<UnihexProvider$OverrideRange> CODEC = RAW_CODEC.validate(
      o -> o.from >= o.to ? DataResult.error(() -> "Invalid range: [" + o.from + ";" + o.to + "]") : DataResult.success(o)
   );
}
