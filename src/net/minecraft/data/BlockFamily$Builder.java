package net.minecraft.data;

import net.minecraft.world.level.block.Block;

public class BlockFamily$Builder {
   private final BlockFamily family;

   public BlockFamily$Builder(final Block baseBlock) {
      this.family = new BlockFamily(baseBlock);
   }

   public BlockFamily getFamily() {
      return this.family;
   }

   public BlockFamily$Builder button(final Block button) {
      this.family.variants.put(BlockFamily$Variant.BUTTON, button);
      return this;
   }

   public BlockFamily$Builder carpet(final Block carpet) {
      this.family.variants.put(BlockFamily$Variant.CARPET, carpet);
      return this;
   }

   public BlockFamily$Builder chiseled(final Block chiseled) {
      this.family.variants.put(BlockFamily$Variant.CHISELED, chiseled);
      return this;
   }

   public BlockFamily$Builder mosaic(final Block mosaic) {
      this.family.variants.put(BlockFamily$Variant.MOSAIC, mosaic);
      return this;
   }

   public BlockFamily$Builder cracked(final Block cracked) {
      this.family.variants.put(BlockFamily$Variant.CRACKED, cracked);
      return this;
   }

   public BlockFamily$Builder tiles(final Block tiles) {
      this.family.variants.put(BlockFamily$Variant.TILES, tiles);
      return this;
   }

   public BlockFamily$Builder pillar(final Block pillar) {
      this.family.variants.put(BlockFamily$Variant.PILLAR, pillar);
      return this;
   }

   public BlockFamily$Builder cut(final Block cut) {
      this.family.variants.put(BlockFamily$Variant.CUT, cut);
      return this;
   }

   public BlockFamily$Builder door(final Block door) {
      this.family.variants.put(BlockFamily$Variant.DOOR, door);
      return this;
   }

   public BlockFamily$Builder customFence(final Block fence) {
      this.family.variants.put(BlockFamily$Variant.CUSTOM_FENCE, fence);
      return this;
   }

   public BlockFamily$Builder fence(final Block fence) {
      this.family.variants.put(BlockFamily$Variant.FENCE, fence);
      return this;
   }

   public BlockFamily$Builder customFenceGate(final Block fenceGate) {
      this.family.variants.put(BlockFamily$Variant.CUSTOM_FENCE_GATE, fenceGate);
      return this;
   }

   public BlockFamily$Builder fenceGate(final Block fenceGate) {
      this.family.variants.put(BlockFamily$Variant.FENCE_GATE, fenceGate);
      return this;
   }

   public BlockFamily$Builder sign(final Block sign, final Block wallSign) {
      this.family.variants.put(BlockFamily$Variant.SIGN, sign);
      this.family.variants.put(BlockFamily$Variant.WALL_SIGN, wallSign);
      return this;
   }

   public BlockFamily$Builder customHangingSign(final Block sign, final Block wallSign) {
      this.family.variants.put(BlockFamily$Variant.CUSTOM_HANGING_SIGN, sign);
      this.family.variants.put(BlockFamily$Variant.CUSTOM_WALL_HANGING_SIGN, wallSign);
      return this;
   }

   public BlockFamily$Builder hangingSign(final Block sign, final Block wallSign) {
      this.family.variants.put(BlockFamily$Variant.HANGING_SIGN, sign);
      this.family.variants.put(BlockFamily$Variant.WALL_HANGING_SIGN, wallSign);
      return this;
   }

   public BlockFamily$Builder log(final Block log) {
      this.family.variants.put(BlockFamily$Variant.LOG, log);
      return this;
   }

   public BlockFamily$Builder strippedLog(final Block strippedLog) {
      this.family.variants.put(BlockFamily$Variant.STRIPPED_LOG, strippedLog);
      return this;
   }

   public BlockFamily$Builder slab(final Block slab) {
      this.family.variants.put(BlockFamily$Variant.SLAB, slab);
      return this;
   }

   public BlockFamily$Builder stairs(final Block stairs) {
      this.family.variants.put(BlockFamily$Variant.STAIRS, stairs);
      return this;
   }

   public BlockFamily$Builder pressurePlate(final Block pressurePlate) {
      this.family.variants.put(BlockFamily$Variant.PRESSURE_PLATE, pressurePlate);
      return this;
   }

   public BlockFamily$Builder polished(final Block polished) {
      this.family.variants.put(BlockFamily$Variant.POLISHED, polished);
      return this;
   }

   public BlockFamily$Builder trapdoor(final Block trapdoor) {
      this.family.variants.put(BlockFamily$Variant.TRAPDOOR, trapdoor);
      return this;
   }

   public BlockFamily$Builder wall(final Block wall) {
      this.family.variants.put(BlockFamily$Variant.WALL, wall);
      return this;
   }

   public BlockFamily$Builder cobbled(final Block cobble) {
      this.family.variants.put(BlockFamily$Variant.COBBLED, cobble);
      return this;
   }

   public BlockFamily$Builder bricks(final Block bricks) {
      this.family.variants.put(BlockFamily$Variant.BRICKS, bricks);
      return this;
   }

   public BlockFamily$Builder dontGenerateModel() {
      this.family.generateModel = false;
      return this;
   }

   public BlockFamily$Builder dontGenerateCraftingRecipe() {
      this.family.generateCraftingRecipe = false;
      return this;
   }

   public BlockFamily$Builder dontGenerateSmeltingRecipe() {
      this.family.generateSmeltingRecipe = false;
      return this;
   }

   public BlockFamily$Builder generateStonecutterRecipe() {
      this.family.generateStonecutterRecipe = true;
      return this;
   }

   public BlockFamily$Builder recipeGroupPrefix(final String recipeGroupPrefix) {
      this.family.recipeGroupPrefix = recipeGroupPrefix;
      return this;
   }

   public BlockFamily$Builder recipeUnlockedBy(final String recipeUnlockedBy) {
      this.family.recipeUnlockedBy = recipeUnlockedBy;
      return this;
   }
}
