package net.minecraft.client.data.models;

import java.util.Objects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;

@Environment(EnvType.CLIENT)
class BlockModelGenerators$WoodProvider {
   private final TextureMapping logMapping;

   public BlockModelGenerators$WoodProvider(final BlockModelGenerators this$0, final TextureMapping logMapping) {
      Objects.requireNonNull(this$0x);
      this.this$0 = this$0x;
      super();
      this.logMapping = logMapping;
   }

   public BlockModelGenerators$WoodProvider wood(final Block block) {
      TextureMapping woodMapping = this.logMapping.copyAndUpdate(TextureSlot.END, this.logMapping.get(TextureSlot.SIDE));
      Identifier model = ModelTemplates.CUBE_COLUMN.create(block, woodMapping, this.this$0.modelOutput);
      this.this$0.blockStateOutput.accept(BlockModelGenerators.createAxisAlignedPillarBlock(block, BlockModelGenerators.plainVariant(model)));
      this.this$0.registerSimpleItemModel(block, model);
      return this;
   }

   public BlockModelGenerators$WoodProvider log(final Block block) {
      Identifier model = ModelTemplates.CUBE_COLUMN.create(block, this.logMapping, this.this$0.modelOutput);
      this.this$0.blockStateOutput.accept(BlockModelGenerators.createAxisAlignedPillarBlock(block, BlockModelGenerators.plainVariant(model)));
      this.this$0.registerSimpleItemModel(block, model);
      return this;
   }

   public BlockModelGenerators$WoodProvider logWithHorizontal(final Block block) {
      Identifier model = ModelTemplates.CUBE_COLUMN.create(block, this.logMapping, this.this$0.modelOutput);
      MultiVariant horizontalModel = BlockModelGenerators.plainVariant(
         ModelTemplates.CUBE_COLUMN_HORIZONTAL.create(block, this.logMapping, this.this$0.modelOutput)
      );
      this.this$0
         .blockStateOutput
         .accept(BlockModelGenerators.createRotatedPillarWithHorizontalVariant(block, BlockModelGenerators.plainVariant(model), horizontalModel));
      this.this$0.registerSimpleItemModel(block, model);
      return this;
   }

   public BlockModelGenerators$WoodProvider logUVLocked(final Block block) {
      this.this$0.blockStateOutput.accept(BlockModelGenerators.createPillarBlockUVLocked(block, this.logMapping, this.this$0.modelOutput));
      this.this$0.registerSimpleItemModel(block, ModelTemplates.CUBE_COLUMN.create(block, this.logMapping, this.this$0.modelOutput));
      return this;
   }
}
