package net.minecraft.client.resources.model;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import java.util.Map;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.ExtraCodecs;

@Environment(EnvType.CLIENT)
public record EquipmentClientInfo(
   Map<EquipmentClientInfo$LayerType, List<EquipmentClientInfo$Layer>> layers, List<EquipmentClientInfo$TrimOverride> trimOverrides
) {
   private static final Codec<List<EquipmentClientInfo$Layer>> LAYER_LIST_CODEC = ExtraCodecs.nonEmptyList(EquipmentClientInfo$Layer.CODEC.listOf());
   public static final Codec<EquipmentClientInfo> CODEC = RecordCodecBuilder.create(
      i -> i.group(
            ExtraCodecs.nonEmptyMap(Codec.unboundedMap(EquipmentClientInfo$LayerType.CODEC, LAYER_LIST_CODEC))
               .fieldOf("layers")
               .forGetter(EquipmentClientInfo::layers),
            EquipmentClientInfo$TrimOverride.CODEC.listOf().optionalFieldOf("trim_overrides", List.of()).forGetter(EquipmentClientInfo::trimOverrides)
         )
         .apply(i, EquipmentClientInfo::new)
   );

   public static EquipmentClientInfo$Builder builder() {
      return new EquipmentClientInfo$Builder();
   }

   public List<EquipmentClientInfo$Layer> getLayers(final EquipmentClientInfo$LayerType type) {
      return this.layers.getOrDefault(type, List.of());
   }
}
