package net.minecraft.data;

import org.jspecify.annotations.Nullable;

public enum BlockFamily$Variant {
   BUTTON("button"),
   CARPET("carpet"),
   CHISELED("chiseled"),
   CRACKED("cracked"),
   CUT("cut"),
   DOOR("door"),
   CUSTOM_FENCE("fence"),
   FENCE("fence"),
   CUSTOM_FENCE_GATE("fence_gate"),
   FENCE_GATE("fence_gate"),
   CUSTOM_HANGING_SIGN("hanging_sign"),
   HANGING_SIGN("hanging_sign"),
   LOG("log"),
   STRIPPED_LOG("stripped_log"),
   MOSAIC("mosaic"),
   SIGN("sign"),
   SLAB("slab"),
   STAIRS("stairs"),
   PRESSURE_PLATE("pressure_plate"),
   POLISHED("polished"),
   TRAPDOOR("trapdoor"),
   WALL("wall"),
   WALL_SIGN("wall_sign"),
   CUSTOM_WALL_HANGING_SIGN("wall_hanging_sign"),
   WALL_HANGING_SIGN("wall_hanging_sign"),
   BRICKS("bricks"),
   COBBLED("cobbled"),
   TILES("tiles"),
   PILLAR("pillar");

   private final String recipeGroup;

   private BlockFamily$Variant(final String recipeGroup) {
      this.recipeGroup = recipeGroup;
   }

   public String getRecipeGroup() {
      return this.recipeGroup;
   }

   public String getPrefixedRecipeGroup(final String prefix) {
      return prefix + "_" + this.getRecipeGroup();
   }

   @Nullable
   public BlockFamily$Variant getBaseVariantForCrafting() {
      return null;
   }
}
