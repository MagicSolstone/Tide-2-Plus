package net.minecraft.client.resources.model;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.equipment.trim.ArmorTrim;
import net.minecraft.world.item.equipment.trim.TrimMaterial;
import net.minecraft.world.item.equipment.trim.TrimPattern;

@Environment(EnvType.CLIENT)
public record EquipmentClientInfo$TrimPredicate(Optional<ResourceKey<TrimMaterial>> material, Optional<ResourceKey<TrimPattern>> pattern) {
   public static final Codec<EquipmentClientInfo$TrimPredicate> CODEC = RecordCodecBuilder.create(
         i -> i.group(
               ResourceKey.codec(Registries.TRIM_MATERIAL).optionalFieldOf("material").forGetter(EquipmentClientInfo$TrimPredicate::material),
               ResourceKey.codec(Registries.TRIM_PATTERN).optionalFieldOf("pattern").forGetter(EquipmentClientInfo$TrimPredicate::pattern)
            )
            .apply(i, EquipmentClientInfo$TrimPredicate::new)
      )
      .validate(EquipmentClientInfo$TrimPredicate::validate);

   private static DataResult<EquipmentClientInfo$TrimPredicate> validate(final EquipmentClientInfo$TrimPredicate override) {
      return override.material.isEmpty() && override.pattern.isEmpty()
         ? DataResult.error(() -> "One of material or pattern must be specified")
         : DataResult.success(override);
   }

   public boolean matches(final ArmorTrim trim) {
      return (this.material.isEmpty() || trim.material().is(this.material.get())) && (this.pattern.isEmpty() || trim.pattern().is(this.pattern.get()));
   }
}
