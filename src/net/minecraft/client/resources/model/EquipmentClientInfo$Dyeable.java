package net.minecraft.client.resources.model;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.ExtraCodecs;

@Environment(EnvType.CLIENT)
public record EquipmentClientInfo$Dyeable(Optional<Integer> colorWhenUndyed) {
   public static final Codec<EquipmentClientInfo$Dyeable> CODEC = RecordCodecBuilder.create(
      i -> i.group(ExtraCodecs.RGB_COLOR_CODEC.optionalFieldOf("color_when_undyed").forGetter(EquipmentClientInfo$Dyeable::colorWhenUndyed))
         .apply(i, EquipmentClientInfo$Dyeable::new)
   );
}
