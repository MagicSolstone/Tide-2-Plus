package net.minecraft.client.data.models;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.equipment.trim.TrimMaterial;
import net.minecraft.world.item.equipment.trim.TrimMaterials$Palette;

@Environment(EnvType.CLIENT)
public record ItemModelGenerators$TrimMaterialData(TrimMaterials$Palette palette, ResourceKey<TrimMaterial> materialKey) {
}
