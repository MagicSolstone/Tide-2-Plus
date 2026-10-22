package net.minecraft.client.renderer.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.resources.Identifier;

@Environment(EnvType.CLIENT)
public enum DonkeyRenderer$Type {
   DONKEY(Identifier.withDefaultNamespace("textures/entity/horse/donkey.png"), ModelLayers.DONKEY),
   DONKEY_BABY(Identifier.withDefaultNamespace("textures/entity/horse/donkey_baby.png"), ModelLayers.DONKEY_BABY),
   MULE(Identifier.withDefaultNamespace("textures/entity/horse/mule.png"), ModelLayers.MULE),
   MULE_BABY(Identifier.withDefaultNamespace("textures/entity/horse/mule_baby.png"), ModelLayers.MULE_BABY);

   private final Identifier texture;
   private final ModelLayerLocation model;

   private DonkeyRenderer$Type(final Identifier texture, final ModelLayerLocation model) {
      this.texture = texture;
      this.model = model;
   }
}
