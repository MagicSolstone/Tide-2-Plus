package net.minecraft.client.data.models;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiConsumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.client.data.models.model.ModelLocationUtils;
import net.minecraft.client.data.models.model.ModelTemplate;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.client.data.models.model.TexturedModel;
import net.minecraft.client.renderer.block.dispatch.Variant;
import net.minecraft.data.BlockFamilies;
import net.minecraft.data.BlockFamily;
import net.minecraft.data.BlockFamily$Variant;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
class BlockModelGenerators$BlockFamilyProvider {
   private final TextureMapping mapping;
   private final Map<ModelTemplate, Identifier> models;
   @Nullable
   private BlockFamily family;
   @Nullable
   private Variant fullBlock;
   private final Set<Block> skipGeneratingModelsFor;

   public BlockModelGenerators$BlockFamilyProvider(final BlockModelGenerators this$0, final TextureMapping mapping) {
      Objects.requireNonNull(this$0x);
      this.this$0 = this$0x;
      super();
      this.models = new HashMap<>();
      this.skipGeneratingModelsFor = new HashSet<>();
      this.mapping = mapping;
   }

   public BlockModelGenerators$BlockFamilyProvider fullBlock(final Block block, final ModelTemplate template) {
      this.fullBlock = BlockModelGenerators.plainModel(template.create(block, this.mapping, this.this$0.modelOutput));
      if (BlockModelGenerators.FULL_BLOCK_MODEL_CUSTOM_GENERATORS.containsKey(block)) {
         this.this$0
            .blockStateOutput
            .accept(BlockModelGenerators.FULL_BLOCK_MODEL_CUSTOM_GENERATORS.get(block).create(block, this.fullBlock, this.mapping, this.this$0.modelOutput));
      } else {
         this.this$0.blockStateOutput.accept(BlockModelGenerators.createSimpleBlock(block, BlockModelGenerators.variant(this.fullBlock)));
      }

      return this;
   }

   public BlockModelGenerators$BlockFamilyProvider donateModelTo(final Block donor, final Block copyTo) {
      Identifier donorModelLocation = ModelLocationUtils.getModelLocation(donor);
      this.this$0.blockStateOutput.accept(BlockModelGenerators.createSimpleBlock(copyTo, BlockModelGenerators.plainVariant(donorModelLocation)));
      this.this$0.itemModelOutput.copy(donor.asItem(), copyTo.asItem());
      this.skipGeneratingModelsFor.add(copyTo);
      return this;
   }

   public BlockModelGenerators$BlockFamilyProvider button(final Block block) {
      MultiVariant normal = BlockModelGenerators.plainVariant(ModelTemplates.BUTTON.create(block, this.mapping, this.this$0.modelOutput));
      MultiVariant pressed = BlockModelGenerators.plainVariant(ModelTemplates.BUTTON_PRESSED.create(block, this.mapping, this.this$0.modelOutput));
      this.this$0.blockStateOutput.accept(BlockModelGenerators.createButton(block, normal, pressed));
      Identifier inventory = ModelTemplates.BUTTON_INVENTORY.create(block, this.mapping, this.this$0.modelOutput);
      this.this$0.registerSimpleItemModel(block, inventory);
      return this;
   }

   public BlockModelGenerators$BlockFamilyProvider carpet(final Block block) {
      TextureMapping carpetMapping = new TextureMapping().put(TextureSlot.WOOL, this.mapping.get(TextureSlot.ALL));
      Identifier model = ModelTemplates.CARPET.create(block, carpetMapping, this.this$0.modelOutput);
      this.this$0.blockStateOutput.accept(BlockModelGenerators.createSimpleBlock(block, BlockModelGenerators.plainVariant(model)));
      this.this$0.registerSimpleItemModel(block, model);
      return this;
   }

   public BlockModelGenerators$BlockFamilyProvider wall(final Block block) {
      MultiVariant post = BlockModelGenerators.plainVariant(ModelTemplates.WALL_POST.create(block, this.mapping, this.this$0.modelOutput));
      MultiVariant low = BlockModelGenerators.plainVariant(ModelTemplates.WALL_LOW_SIDE.create(block, this.mapping, this.this$0.modelOutput));
      MultiVariant high = BlockModelGenerators.plainVariant(ModelTemplates.WALL_TALL_SIDE.create(block, this.mapping, this.this$0.modelOutput));
      this.this$0.blockStateOutput.accept(BlockModelGenerators.createWall(block, post, low, high));
      Identifier inventory = ModelTemplates.WALL_INVENTORY.create(block, this.mapping, this.this$0.modelOutput);
      this.this$0.registerSimpleItemModel(block, inventory);
      return this;
   }

   public BlockModelGenerators$BlockFamilyProvider customFence(final Block block) {
      TextureMapping mapping = TextureMapping.customParticle(block);
      MultiVariant post = BlockModelGenerators.plainVariant(ModelTemplates.CUSTOM_FENCE_POST.create(block, mapping, this.this$0.modelOutput));
      MultiVariant north = BlockModelGenerators.plainVariant(ModelTemplates.CUSTOM_FENCE_SIDE_NORTH.create(block, mapping, this.this$0.modelOutput));
      MultiVariant east = BlockModelGenerators.plainVariant(ModelTemplates.CUSTOM_FENCE_SIDE_EAST.create(block, mapping, this.this$0.modelOutput));
      MultiVariant south = BlockModelGenerators.plainVariant(ModelTemplates.CUSTOM_FENCE_SIDE_SOUTH.create(block, mapping, this.this$0.modelOutput));
      MultiVariant west = BlockModelGenerators.plainVariant(ModelTemplates.CUSTOM_FENCE_SIDE_WEST.create(block, mapping, this.this$0.modelOutput));
      this.this$0.blockStateOutput.accept(BlockModelGenerators.createCustomFence(block, post, north, east, south, west));
      Identifier inventory = ModelTemplates.CUSTOM_FENCE_INVENTORY.create(block, mapping, this.this$0.modelOutput);
      this.this$0.registerSimpleItemModel(block, inventory);
      return this;
   }

   public BlockModelGenerators$BlockFamilyProvider fence(final Block block) {
      MultiVariant post = BlockModelGenerators.plainVariant(ModelTemplates.FENCE_POST.create(block, this.mapping, this.this$0.modelOutput));
      MultiVariant side = BlockModelGenerators.plainVariant(ModelTemplates.FENCE_SIDE.create(block, this.mapping, this.this$0.modelOutput));
      this.this$0.blockStateOutput.accept(BlockModelGenerators.createFence(block, post, side));
      Identifier inventory = ModelTemplates.FENCE_INVENTORY.create(block, this.mapping, this.this$0.modelOutput);
      this.this$0.registerSimpleItemModel(block, inventory);
      return this;
   }

   public BlockModelGenerators$BlockFamilyProvider customFenceGate(final Block block) {
      TextureMapping mapping = TextureMapping.customParticle(block);
      MultiVariant open = BlockModelGenerators.plainVariant(ModelTemplates.CUSTOM_FENCE_GATE_OPEN.create(block, mapping, this.this$0.modelOutput));
      MultiVariant closed = BlockModelGenerators.plainVariant(ModelTemplates.CUSTOM_FENCE_GATE_CLOSED.create(block, mapping, this.this$0.modelOutput));
      MultiVariant openWall = BlockModelGenerators.plainVariant(ModelTemplates.CUSTOM_FENCE_GATE_WALL_OPEN.create(block, mapping, this.this$0.modelOutput));
      MultiVariant closedWall = BlockModelGenerators.plainVariant(ModelTemplates.CUSTOM_FENCE_GATE_WALL_CLOSED.create(block, mapping, this.this$0.modelOutput));
      this.this$0.blockStateOutput.accept(BlockModelGenerators.createFenceGate(block, open, closed, openWall, closedWall, false));
      return this;
   }

   public BlockModelGenerators$BlockFamilyProvider fenceGate(final Block block) {
      MultiVariant open = BlockModelGenerators.plainVariant(ModelTemplates.FENCE_GATE_OPEN.create(block, this.mapping, this.this$0.modelOutput));
      MultiVariant closed = BlockModelGenerators.plainVariant(ModelTemplates.FENCE_GATE_CLOSED.create(block, this.mapping, this.this$0.modelOutput));
      MultiVariant openWall = BlockModelGenerators.plainVariant(ModelTemplates.FENCE_GATE_WALL_OPEN.create(block, this.mapping, this.this$0.modelOutput));
      MultiVariant closedWall = BlockModelGenerators.plainVariant(ModelTemplates.FENCE_GATE_WALL_CLOSED.create(block, this.mapping, this.this$0.modelOutput));
      this.this$0.blockStateOutput.accept(BlockModelGenerators.createFenceGate(block, open, closed, openWall, closedWall, true));
      return this;
   }

   public BlockModelGenerators$BlockFamilyProvider pressurePlate(final Block block) {
      MultiVariant off = BlockModelGenerators.plainVariant(ModelTemplates.PRESSURE_PLATE_UP.create(block, this.mapping, this.this$0.modelOutput));
      MultiVariant on = BlockModelGenerators.plainVariant(ModelTemplates.PRESSURE_PLATE_DOWN.create(block, this.mapping, this.this$0.modelOutput));
      this.this$0.blockStateOutput.accept(BlockModelGenerators.createPressurePlate(block, off, on));
      return this;
   }

   public BlockModelGenerators$BlockFamilyProvider sign(final Block sign) {
      if (this.family == null) {
         throw new IllegalStateException("Family not defined");
      } else {
         TextureMapping mapping = new TextureMapping()
            .put(TextureSlot.ALL, TextureMapping.getBlockTexture(sign))
            .put(TextureSlot.PARTICLE, TextureMapping.getBlockTexture(this.family.getBaseBlock()));
         MultiVariant standingRot0 = BlockModelGenerators.plainVariant(
            ModelTemplates.SIGN_ROT_0.create(ModelLocationUtils.getModelLocation(sign, "_rot_0"), mapping, this.this$0.modelOutput)
         );
         MultiVariant standingRot1 = BlockModelGenerators.plainVariant(
            ModelTemplates.SIGN_ROT_1.create(ModelLocationUtils.getModelLocation(sign, "_rot_1"), mapping, this.this$0.modelOutput)
         );
         MultiVariant standingRot2 = BlockModelGenerators.plainVariant(
            ModelTemplates.SIGN_ROT_2.create(ModelLocationUtils.getModelLocation(sign, "_rot_2"), mapping, this.this$0.modelOutput)
         );
         MultiVariant standingRot3 = BlockModelGenerators.plainVariant(
            ModelTemplates.SIGN_ROT_3.create(ModelLocationUtils.getModelLocation(sign, "_rot_3"), mapping, this.this$0.modelOutput)
         );
         this.this$0.blockStateOutput.accept(BlockModelGenerators.createSign(sign, standingRot0, standingRot1, standingRot2, standingRot3));
         Block wallSign = this.family.getVariants().get(BlockFamily$Variant.WALL_SIGN);
         MultiVariant wallModel = BlockModelGenerators.plainVariant(ModelTemplates.WALL_SIGN.create(wallSign, mapping, this.this$0.modelOutput));
         this.this$0.blockStateOutput.accept(MultiVariantGenerator.dispatch(wallSign, wallModel).with(BlockModelGenerators.ROTATION_HORIZONTAL_FACING_ALT));
         this.this$0.registerSimpleFlatItemModel(sign.asItem());
         return this;
      }
   }

   public BlockModelGenerators$BlockFamilyProvider customHangingSign(final Block hangingSign) {
      return this.hangingSign(hangingSign, this.family.getBaseBlock(), BlockFamily$Variant.CUSTOM_WALL_HANGING_SIGN);
   }

   public BlockModelGenerators$BlockFamilyProvider hangingSign(final Block hangingSign) {
      return this.hangingSign(hangingSign, this.family.get(BlockFamily$Variant.STRIPPED_LOG), BlockFamily$Variant.WALL_HANGING_SIGN);
   }

   private BlockModelGenerators$BlockFamilyProvider hangingSign(final Block hangingSign, final Block particleBlock, final BlockFamily$Variant wallVarient) {
      TextureMapping mapping = new TextureMapping()
         .put(TextureSlot.ALL, TextureMapping.getBlockTexture(hangingSign))
         .put(TextureSlot.PARTICLE, TextureMapping.getBlockTexture(particleBlock));
      this.this$0
         .blockStateOutput
         .accept(
            BlockModelGenerators.createHangingSign(
               hangingSign,
               BlockModelGenerators.plainVariant(
                  ModelTemplates.HANGING_SIGN_ROT_0.create(ModelLocationUtils.getModelLocation(hangingSign, "_rot_0"), mapping, this.this$0.modelOutput)
               ),
               BlockModelGenerators.plainVariant(
                  ModelTemplates.HANGING_SIGN_ROT_1.create(ModelLocationUtils.getModelLocation(hangingSign, "_rot_1"), mapping, this.this$0.modelOutput)
               ),
               BlockModelGenerators.plainVariant(
                  ModelTemplates.HANGING_SIGN_ROT_2.create(ModelLocationUtils.getModelLocation(hangingSign, "_rot_2"), mapping, this.this$0.modelOutput)
               ),
               BlockModelGenerators.plainVariant(
                  ModelTemplates.HANGING_SIGN_ROT_3.create(ModelLocationUtils.getModelLocation(hangingSign, "_rot_3"), mapping, this.this$0.modelOutput)
               ),
               BlockModelGenerators.plainVariant(
                  ModelTemplates.ATTACHED_HANGING_SIGN_ROT_0
                     .create(ModelLocationUtils.getModelLocation(hangingSign, "_attached_rot_0"), mapping, this.this$0.modelOutput)
               ),
               BlockModelGenerators.plainVariant(
                  ModelTemplates.ATTACHED_HANGING_SIGN_ROT_1
                     .create(ModelLocationUtils.getModelLocation(hangingSign, "_attached_rot_1"), mapping, this.this$0.modelOutput)
               ),
               BlockModelGenerators.plainVariant(
                  ModelTemplates.ATTACHED_HANGING_SIGN_ROT_2
                     .create(ModelLocationUtils.getModelLocation(hangingSign, "_attached_rot_2"), mapping, this.this$0.modelOutput)
               ),
               BlockModelGenerators.plainVariant(
                  ModelTemplates.ATTACHED_HANGING_SIGN_ROT_3
                     .create(ModelLocationUtils.getModelLocation(hangingSign, "_attached_rot_3"), mapping, this.this$0.modelOutput)
               )
            )
         );
      Block wallSign = this.family.get(wallVarient);
      MultiVariant wallModel = BlockModelGenerators.plainVariant(ModelTemplates.WALL_HANGING_SIGN.create(wallSign, mapping, this.this$0.modelOutput));
      this.this$0.blockStateOutput.accept(MultiVariantGenerator.dispatch(wallSign, wallModel).with(BlockModelGenerators.ROTATION_HORIZONTAL_FACING_ALT));
      this.this$0.registerSimpleFlatItemModel(hangingSign.asItem());
      return this;
   }

   public BlockModelGenerators$BlockFamilyProvider slab(final Block slab) {
      if (this.fullBlock == null) {
         throw new IllegalStateException("Full block not generated yet");
      } else {
         Identifier bottom = this.getOrCreateModel(ModelTemplates.SLAB_BOTTOM, slab);
         MultiVariant top = BlockModelGenerators.plainVariant(this.getOrCreateModel(ModelTemplates.SLAB_TOP, slab));
         this.this$0
            .blockStateOutput
            .accept(BlockModelGenerators.createSlab(slab, BlockModelGenerators.plainVariant(bottom), top, BlockModelGenerators.variant(this.fullBlock)));
         this.this$0.registerSimpleItemModel(slab, bottom);
         return this;
      }
   }

   public BlockModelGenerators$BlockFamilyProvider stairs(final Block stairs) {
      MultiVariant inner = BlockModelGenerators.plainVariant(this.getOrCreateModel(ModelTemplates.STAIRS_INNER, stairs));
      Identifier straight = this.getOrCreateModel(ModelTemplates.STAIRS_STRAIGHT, stairs);
      MultiVariant outer = BlockModelGenerators.plainVariant(this.getOrCreateModel(ModelTemplates.STAIRS_OUTER, stairs));
      this.this$0.blockStateOutput.accept(BlockModelGenerators.createStairs(stairs, inner, BlockModelGenerators.plainVariant(straight), outer));
      this.this$0.registerSimpleItemModel(stairs, straight);
      return this;
   }

   private BlockModelGenerators$BlockFamilyProvider fullBlockVariant(final Block variant) {
      TexturedModel model = BlockModelGenerators.TEXTURED_MODELS.getOrDefault(variant, TexturedModel.CUBE.get(variant));
      MultiVariant variantModel = BlockModelGenerators.plainVariant(model.create(variant, this.this$0.modelOutput));
      this.this$0.blockStateOutput.accept(BlockModelGenerators.createSimpleBlock(variant, variantModel));
      return this;
   }

   private BlockModelGenerators$BlockFamilyProvider pillar(final Block variant) {
      MultiVariant verticalModel = BlockModelGenerators.plainVariant(TexturedModel.COLUMN.create(variant, this.this$0.modelOutput));
      MultiVariant horizontalModel = BlockModelGenerators.plainVariant(TexturedModel.COLUMN_HORIZONTAL.create(variant, this.this$0.modelOutput));
      this.this$0.blockStateOutput.accept(BlockModelGenerators.createRotatedPillarWithHorizontalVariant(variant, verticalModel, horizontalModel));
      return this;
   }

   private BlockModelGenerators$BlockFamilyProvider door(final Block door) {
      this.this$0.createDoor(door);
      return this;
   }

   private void trapdoor(final Block result) {
      if (BlockModelGenerators.NON_ORIENTABLE_TRAPDOOR.contains(result)) {
         this.this$0.createTrapdoor(result);
      } else {
         this.this$0.createOrientableTrapdoor(result);
      }
   }

   private Identifier getOrCreateModel(final ModelTemplate modelTemplate, final Block block) {
      return this.models.computeIfAbsent(modelTemplate, template -> template.create(block, this.mapping, this.this$0.modelOutput));
   }

   public BlockModelGenerators$BlockFamilyProvider generateFor(final BlockFamily family) {
      this.family = family;
      family.getVariants().forEach((variant, result) -> {
         boolean modelAlreadyRegisteredAsAnotherFamilyBase = BlockFamilies.getAllFamilies().anyMatch(b -> b.getBaseBlock() == result);
         if (!this.skipGeneratingModelsFor.contains(result) && !modelAlreadyRegisteredAsAnotherFamilyBase) {
            BiConsumer<BlockModelGenerators$BlockFamilyProvider, Block> consumer = BlockModelGenerators.SHAPE_CONSUMERS.get(variant);
            if (consumer != null) {
               consumer.accept(this, result);
            }
         }
      });
      return this;
   }
}
