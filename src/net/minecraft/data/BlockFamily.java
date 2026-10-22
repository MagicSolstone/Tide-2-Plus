package net.minecraft.data;

import com.google.common.collect.Maps;
import java.util.Map;
import java.util.Optional;
import net.minecraft.util.StringUtil;
import net.minecraft.world.level.block.Block;
import org.jspecify.annotations.Nullable;

public class BlockFamily {
   private final Block baseBlock;
   private final Map<BlockFamily$Variant, Block> variants = Maps.newHashMap();
   private boolean generateModel = true;
   private boolean generateCraftingRecipe = true;
   private boolean generateSmeltingRecipe = true;
   private boolean generateStonecutterRecipe = false;
   @Nullable
   private String recipeGroupPrefix;
   @Nullable
   private String recipeUnlockedBy;

   private BlockFamily(final Block baseBlock) {
      this.baseBlock = baseBlock;
   }

   public Block getBaseBlock() {
      return this.baseBlock;
   }

   public Map<BlockFamily$Variant, Block> getVariants() {
      return this.variants;
   }

   public Block get(final BlockFamily$Variant variant) {
      return this.variants.get(variant);
   }

   public boolean shouldGenerateModel() {
      return this.generateModel;
   }

   public boolean shouldGenerateCraftingRecipe() {
      return this.generateCraftingRecipe;
   }

   public boolean shouldGenerateSmeltingRecipe() {
      return this.generateSmeltingRecipe;
   }

   public boolean shouldGenerateStonecutterRecipe() {
      return this.generateStonecutterRecipe;
   }

   public Optional<String> getRecipeGroupPrefix() {
      return StringUtil.isBlank(this.recipeGroupPrefix) ? Optional.empty() : Optional.of(this.recipeGroupPrefix);
   }

   public Optional<String> getRecipeUnlockedBy() {
      return StringUtil.isBlank(this.recipeUnlockedBy) ? Optional.empty() : Optional.of(this.recipeUnlockedBy);
   }
}
