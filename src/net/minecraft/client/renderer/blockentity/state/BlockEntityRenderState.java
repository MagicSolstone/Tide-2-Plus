package net.minecraft.client.renderer.blockentity.state;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.CrashReportCategory;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer$CrumblingOverlay;
import net.minecraft.core.BlockPos;
import net.minecraft.util.LightCoordsUtil;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.BlockEntityTypes;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class BlockEntityRenderState {
   public BlockPos blockPos = BlockPos.ZERO;
   private BlockState blockState = Blocks.AIR.defaultBlockState();
   public BlockEntityType<?> blockEntityType = BlockEntityTypes.TEST_BLOCK;
   public int lightCoords;
   @Nullable
   public ModelFeatureRenderer$CrumblingOverlay breakProgress;

   public static void extractBase(
      final BlockEntity blockEntity, final BlockEntityRenderState state, @Nullable final ModelFeatureRenderer$CrumblingOverlay breakProgress
   ) {
      state.blockPos = blockEntity.getBlockPos();
      state.blockState = blockEntity.getBlockState();
      state.blockEntityType = blockEntity.getType();
      state.lightCoords = blockEntity.getLevel() != null ? LightCoordsUtil.getLightCoords(blockEntity.getLevel(), blockEntity.getBlockPos()) : 15728880;
      state.breakProgress = breakProgress;
   }

   public void fillCrashReportCategory(final CrashReportCategory category) {
      category.setDetail("BlockEntityRenderState", this.getClass().getCanonicalName());
      category.setDetail("Position", this.blockPos);
      category.setDetail("Block state", this.blockState::toString);
   }
}
