package net.minecraft.world.level.border;

import java.util.Objects;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

class WorldBorder$MovingBorderExtent implements WorldBorder$BorderExtent {
   private final double from;
   private final double to;
   private final long lerpEnd;
   private final long lerpBegin;
   private final double lerpDuration;
   private long lerpProgress;
   private double size;
   private double previousSize;

   private WorldBorder$MovingBorderExtent(final WorldBorder this$0, final double from, final double to, final long duration, final long gameTime) {
      Objects.requireNonNull(this$0x);
      this.this$0 = this$0x;
      super();
      this.from = from;
      this.to = to;
      this.lerpDuration = duration;
      this.lerpProgress = duration;
      this.lerpBegin = gameTime;
      this.lerpEnd = this.lerpBegin + duration;
      double size = this.calculateSize();
      this.size = size;
      this.previousSize = size;
   }

   @Override
   public double getMinX(final float deltaPartialTick) {
      return Mth.clamp(
         this.this$0.getCenterX() - Mth.lerp((double)deltaPartialTick, this.getPreviousSize(), this.getSize()) / 2.0,
         (double)(-this.this$0.absoluteMaxSize),
         (double)this.this$0.absoluteMaxSize
      );
   }

   @Override
   public double getMinZ(final float deltaPartialTick) {
      return Mth.clamp(
         this.this$0.getCenterZ() - Mth.lerp((double)deltaPartialTick, this.getPreviousSize(), this.getSize()) / 2.0,
         (double)(-this.this$0.absoluteMaxSize),
         (double)this.this$0.absoluteMaxSize
      );
   }

   @Override
   public double getMaxX(final float deltaPartialTick) {
      return Mth.clamp(
         this.this$0.getCenterX() + Mth.lerp((double)deltaPartialTick, this.getPreviousSize(), this.getSize()) / 2.0,
         (double)(-this.this$0.absoluteMaxSize),
         (double)this.this$0.absoluteMaxSize
      );
   }

   @Override
   public double getMaxZ(final float deltaPartialTick) {
      return Mth.clamp(
         this.this$0.getCenterZ() + Mth.lerp((double)deltaPartialTick, this.getPreviousSize(), this.getSize()) / 2.0,
         (double)(-this.this$0.absoluteMaxSize),
         (double)this.this$0.absoluteMaxSize
      );
   }

   @Override
   public double getSize() {
      return this.size;
   }

   public double getPreviousSize() {
      return this.previousSize;
   }

   private double calculateSize() {
      double progress = (this.lerpDuration - this.lerpProgress) / this.lerpDuration;
      return progress < 1.0 ? Mth.lerp(progress, this.from, this.to) : this.to;
   }

   @Override
   public double getLerpSpeed() {
      return Math.abs(this.from - this.to) / (this.lerpEnd - this.lerpBegin);
   }

   @Override
   public long getLerpTime() {
      return this.lerpProgress;
   }

   @Override
   public double getLerpTarget() {
      return this.to;
   }

   @Override
   public BorderStatus getStatus() {
      return this.to < this.from ? BorderStatus.SHRINKING : BorderStatus.GROWING;
   }

   @Override
   public void onCenterChange() {
   }

   @Override
   public void onAbsoluteMaxSizeChange() {
   }

   @Override
   public WorldBorder$BorderExtent update() {
      this.lerpProgress--;
      this.previousSize = this.size;
      this.size = this.calculateSize();
      this.this$0.setDirty();
      return (WorldBorder$BorderExtent)(this.lerpProgress <= 0L ? new WorldBorder$StaticBorderExtent(this.this$0, this.to) : this);
   }

   @Override
   public VoxelShape getCollisionShape() {
      return Shapes.join(
         Shapes.INFINITY,
         Shapes.box(
            Math.floor(this.getMinX(0.0F)),
            Double.NEGATIVE_INFINITY,
            Math.floor(this.getMinZ(0.0F)),
            Math.ceil(this.getMaxX(0.0F)),
            Double.POSITIVE_INFINITY,
            Math.ceil(this.getMaxZ(0.0F))
         ),
         BooleanOp.ONLY_FIRST
      );
   }
}
