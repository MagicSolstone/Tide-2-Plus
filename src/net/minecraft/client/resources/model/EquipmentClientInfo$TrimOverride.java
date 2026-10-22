package net.minecraft.client.resources.model;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resources.Identifier;

@Environment(EnvType.CLIENT)
public record EquipmentClientInfo$TrimOverride(EquipmentClientInfo$TrimPredicate predicate, Optional<Identifier> textureId, Optional<Identifier> paletteId) {
   public static final Codec<EquipmentClientInfo$TrimOverride> CODEC = RecordCodecBuilder.create(
         i -> i.group(
               EquipmentClientInfo$TrimPredicate.CODEC.fieldOf("when").forGetter(EquipmentClientInfo$TrimOverride::predicate),
               Identifier.CODEC.optionalFieldOf("texture").forGetter(EquipmentClientInfo$TrimOverride::textureId),
               Identifier.CODEC.optionalFieldOf("palette").forGetter(EquipmentClientInfo$TrimOverride::paletteId)
            )
            .apply(i, EquipmentClientInfo$TrimOverride::new)
      )
      .validate(EquipmentClientInfo$TrimOverride::validate);

   private static DataResult<EquipmentClientInfo$TrimOverride> validate(final EquipmentClientInfo$TrimOverride override) {
      return override.textureId.isEmpty() && override.paletteId.isEmpty()
         ? DataResult.error(() -> "One of texture or palette must be specified")
         : DataResult.success(override);
   }
}
