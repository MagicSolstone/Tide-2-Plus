package net.minecraft.client.data.models;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.data.models.model.ModelTemplate;

@Environment(EnvType.CLIENT)
record BlockModelGenerators$BookSlotModelCacheKey(ModelTemplate template, String modelSuffix) {
}
