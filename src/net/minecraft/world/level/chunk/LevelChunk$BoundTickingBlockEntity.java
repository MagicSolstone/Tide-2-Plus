package net.minecraft.world.level.chunk;

import com.mojang.logging.LogUtils;
import java.util.Objects;
import net.minecraft.CrashReport;
import net.minecraft.CrashReportCategory;
import net.minecraft.ReportedException;
import net.minecraft.core.BlockPos;
import net.minecraft.util.profiling.Profiler;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.TickingBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

class LevelChunk$BoundTickingBlockEntity<T extends BlockEntity> implements TickingBlockEntity {
   private final T blockEntity;
   private final BlockEntityTicker<T> ticker;
   private boolean loggedInvalidBlockState;

   private LevelChunk$BoundTickingBlockEntity(final T this$0, final BlockEntityTicker<T> blockEntity, final BlockEntityTicker ticker) {
      Objects.requireNonNull(this$0x);
      this.this$0 = this$0x;
      super();
      this.blockEntity = blockEntity;
      this.ticker = ticker;
   }

   @Override
   public void tick() {
      if (!this.blockEntity.isRemoved() && this.blockEntity.hasLevel()) {
         BlockPos pos = this.blockEntity.getBlockPos();
         if (this.this$0.isTicking(pos)) {
            try {
               ProfilerFiller profiler = Profiler.get();
               profiler.push(this::getType);
               BlockState blockState = this.this$0.getBlockState(pos);
               if (this.blockEntity.getType().isValid(blockState)) {
                  this.ticker.tick(this.this$0.level, this.blockEntity.getBlockPos(), blockState, this.blockEntity);
                  this.loggedInvalidBlockState = false;
               } else if (!this.loggedInvalidBlockState) {
                  this.loggedInvalidBlockState = true;
                  LevelChunk.LOGGER
                     .warn(
                        "Block entity {} @ {} state {} invalid for ticking:",
                        new Object[]{LogUtils.defer(this::getType), LogUtils.defer(this::getPos), blockState}
                     );
               }

               profiler.pop();
            } catch (Throwable var5) {
               CrashReport report = CrashReport.forThrowable(var5, "Ticking block entity");
               CrashReportCategory category = report.addCategory("Block entity being ticked");
               this.blockEntity.fillCrashReportCategory(category);
               throw new ReportedException(report);
            }
         }
      }
   }

   @Override
   public boolean isRemoved() {
      return this.blockEntity.isRemoved();
   }

   @Override
   public BlockPos getPos() {
      return this.blockEntity.getBlockPos();
   }

   @Override
   public String getType() {
      return this.blockEntity.typeHolder().getRegisteredName();
   }

   @Override
   public String toString() {
      return "Level ticker for " + this.getType() + "@" + this.getPos();
   }
}
