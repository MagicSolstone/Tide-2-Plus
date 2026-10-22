package net.minecraft.client.resources.model;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableList.Builder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Map.Entry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.equipment.trim.TrimMaterial;

@Environment(EnvType.CLIENT)
public class EquipmentClientInfo$Builder {
   private final Map<EquipmentClientInfo$LayerType, List<EquipmentClientInfo$Layer>> layersByType = new EnumMap<>(EquipmentClientInfo$LayerType.class);
   private final Builder<EquipmentClientInfo$TrimOverride> trimOverrides = ImmutableList.builder();

   private EquipmentClientInfo$Builder() {
   }

   public EquipmentClientInfo$Builder addHumanoidLayers(final Identifier textureId) {
      return this.addHumanoidLayers(textureId, false);
   }

   public EquipmentClientInfo$Builder addHumanoidLayers(final Identifier textureId, final boolean dyeable) {
      this.addLayers(EquipmentClientInfo$LayerType.HUMANOID_LEGGINGS, EquipmentClientInfo$Layer.leatherDyeable(textureId, dyeable));
      this.addMainHumanoidLayer(textureId, dyeable);
      return this;
   }

   public EquipmentClientInfo$Builder addMainHumanoidLayer(final Identifier textureId, final boolean dyeable) {
      this.addLayers(EquipmentClientInfo$LayerType.HUMANOID, EquipmentClientInfo$Layer.leatherDyeable(textureId, dyeable));
      this.addLayers(EquipmentClientInfo$LayerType.HUMANOID_BABY, EquipmentClientInfo$Layer.leatherDyeable(textureId, dyeable));
      return this;
   }

   public EquipmentClientInfo$Builder addLayers(final EquipmentClientInfo$LayerType type, final EquipmentClientInfo$Layer... layers) {
      Collections.addAll(this.layersByType.computeIfAbsent(type, t -> new ArrayList<>()), layers);
      return this;
   }

   public EquipmentClientInfo$Builder replaceTrimPalette(final ResourceKey<TrimMaterial> fromMaterial, final Identifier toPaletteId) {
      EquipmentClientInfo$TrimPredicate predicate = new EquipmentClientInfo$TrimPredicate(Optional.of(fromMaterial), Optional.empty());
      this.trimOverrides.add(new EquipmentClientInfo$TrimOverride(predicate, Optional.empty(), Optional.of(toPaletteId)));
      return this;
   }

   public EquipmentClientInfo build() {
      return new EquipmentClientInfo(
         this.layersByType.entrySet().stream().collect(ImmutableMap.toImmutableMap(Entry::getKey, entry -> List.copyOf((Collection)entry.getValue()))),
         this.trimOverrides.build()
      );
   }
}
