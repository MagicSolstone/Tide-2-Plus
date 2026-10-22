package net.minecraft.client.renderer.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.resources.Identifier;

@Environment(EnvType.CLIENT)
public enum UndeadHorseRenderer$Type {
   SKELETON(Identifier.withDefaultNamespace("textures/entity/horse/horse_skeleton.png"), ModelLayers.SKELETON_HORSE),
   SKELETON_BABY(Identifier.withDefaultNamespace("textures/entity/horse/horse_skeleton_baby.png"), ModelLayers.SKELETON_HORSE_BABY),
   ZOMBIE(Identifier.withDefaultNamespace("textures/entity/horse/horse_zombie.png"), ModelLayers.ZOMBIE_HORSE),
   ZOMBIE_BABY(Identifier.withDefaultNamespace("textures/entity/horse/horse_zombie_baby.png"), ModelLayers.ZOMBIE_HORSE_BABY);

   private final Identifier texture;
   private final ModelLayerLocation model;

   private UndeadHorseRenderer$Type(final Identifier texture, final ModelLayerLocation model) {
      this.texture = texture;
      this.model = model;
   }
}
