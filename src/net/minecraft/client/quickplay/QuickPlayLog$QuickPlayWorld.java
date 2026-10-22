package net.minecraft.client.quickplay;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.ExtraCodecs;

@Environment(EnvType.CLIENT)
record QuickPlayLog$QuickPlayWorld(QuickPlayLog$Type type, String id, String name) {
   public static final MapCodec<QuickPlayLog$QuickPlayWorld> MAP_CODEC = RecordCodecBuilder.mapCodec(
      i -> i.group(
            QuickPlayLog$Type.CODEC.fieldOf("type").forGetter(QuickPlayLog$QuickPlayWorld::type),
            ExtraCodecs.ESCAPED_STRING.fieldOf("id").forGetter(QuickPlayLog$QuickPlayWorld::id),
            Codec.STRING.fieldOf("name").forGetter(QuickPlayLog$QuickPlayWorld::name)
         )
         .apply(i, QuickPlayLog$QuickPlayWorld::new)
   );
}
