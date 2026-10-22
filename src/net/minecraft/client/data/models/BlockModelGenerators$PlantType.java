package net.minecraft.client.data.models;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.data.models.model.ModelTemplate;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;

@Environment(EnvType.CLIENT)
enum BlockModelGenerators$PlantType {
   TINTED(ModelTemplates.TINTED_CROSS, ModelTemplates.TINTED_FLOWER_POT_CROSS, false),
   NOT_TINTED(ModelTemplates.CROSS, ModelTemplates.FLOWER_POT_CROSS, false),
   EMISSIVE_NOT_TINTED(ModelTemplates.CROSS_EMISSIVE, ModelTemplates.FLOWER_POT_CROSS_EMISSIVE, true);

   private final ModelTemplate blockTemplate;
   private final ModelTemplate flowerPotTemplate;
   private final boolean isEmissive;

   private BlockModelGenerators$PlantType(final ModelTemplate blockTemplate, final ModelTemplate flowerPotTemplate, final boolean isEmissive) {
      this.blockTemplate = blockTemplate;
      this.flowerPotTemplate = flowerPotTemplate;
      this.isEmissive = isEmissive;
   }

   public ModelTemplate getCross() {
      return this.blockTemplate;
   }

   public ModelTemplate getCrossPot() {
      return this.flowerPotTemplate;
   }

   public Identifier createItemModelUsingBlockTexture(final BlockModelGenerators generator, final Block block) {
      return this.createItemModelUsingBlockTexture(generator, block, "");
   }

   public Identifier createItemModelUsingBlockTexture(final BlockModelGenerators generator, final Block block, final String suffix) {
      Material material = TextureMapping.getBlockTexture(block, suffix);
      return this.isEmissive
         ? generator.createTwoLayeredItemModel(block.asItem(), material, material.withSuffix("_emissive"))
         : generator.createFlatItemModel(block.asItem(), material);
   }

   public Identifier createItemModelUsingItemTexture(final BlockModelGenerators generator, final Block block) {
      return this.createItemModelUsingItemTexture(generator, block, "");
   }

   public Identifier createItemModelUsingItemTexture(final BlockModelGenerators generator, final Block block, final String suffix) {
      Material material = TextureMapping.getItemTexture(block.asItem(), suffix);
      return generator.createFlatItemModel(block.asItem(), material);
   }

   public TextureMapping getTextureMapping(final Block block) {
      return this.isEmissive ? TextureMapping.crossEmissive(block) : TextureMapping.cross(block);
   }

   public TextureMapping getPlantTextureMapping(final Block standAlone) {
      return this.isEmissive ? TextureMapping.plantEmissive(standAlone) : TextureMapping.plant(standAlone);
   }
}
