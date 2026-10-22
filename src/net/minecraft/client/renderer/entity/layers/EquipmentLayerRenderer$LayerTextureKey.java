package net.minecraft.client.renderer.entity.layers;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.resources.model.EquipmentClientInfo$Layer;
import net.minecraft.client.resources.model.EquipmentClientInfo$LayerType;

@Environment(EnvType.CLIENT)
record EquipmentLayerRenderer$LayerTextureKey(EquipmentClientInfo$LayerType layerType, EquipmentClientInfo$Layer layer) {
}
