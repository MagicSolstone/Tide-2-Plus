package net.minecraft.world.level.border;

import java.util.Objects;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

class WorldBorder$StaticBorderExtent implements WorldBorder$BorderExtent {
   private final double size;
   private double minX;
   private double minZ;
   private double maxX;
   private double maxZ;
   private VoxelShape shape;

   public WorldBorder$StaticBorderExtent(final WorldBorder this$0, final double size) {
      Objects.requireNonNull(this$0x);
      this.this$0 = this$0x;
      super();
      this.size = size;
      this.updateBox();
   }

   @Override
   public double getMinX(final float deltaPartialTick) {
      return this.minX;
   }

   @Override
   public double getMaxX(final float deltaPartialTick) {
      return this.maxX;
   }

   @Override
   public double getMinZ(final float deltaPartialTick) {
      return this.minZ;
   }

   @Override
   public double getMaxZ(final float deltaPartialTick) {
      return this.maxZ;
   }

   @Override
   public double getSize() {
      return this.size;
   }

   @Override
   public BorderStatus getStatus() {
      return BorderStatus.STATIONARY;
   }

   @Override
   public double getLerpSpeed() {
      return 0.0;
   }

   @Override
   public long getLerpTime() {
      return 0L;
   }

   @Override
   public double getLerpTarget() {
      return this.size;
   }

   private void updateBox() {
      this.minX = Mth.clamp(this.this$0.getCenterX() - this.size / 2.0, (double)(-this.this$0.absoluteMaxSize), (double)this.this$0.absoluteMaxSize);
      this.minZ = Mth.clamp(this.this$0.getCenterZ() - this.size / 2.0, (double)(-this.this$0.absoluteMaxSize), (double)this.this$0.absoluteMaxSize);
      this.maxX = Mth.clamp(this.this$0.getCenterX() + this.size / 2.0, (double)(-this.this$0.absoluteMaxSize), (double)this.this$0.absoluteMaxSize);
      this.maxZ = Mth.clamp(this.this$0.getCenterZ() + this.size / 2.0, (double)(-this.this$0.absoluteMaxSize), (double)this.this$0.absoluteMaxSize);
      this.shape = Shapes.join(
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

   @Override
   public void onAbsoluteMaxSizeChange() {
      this.updateBox();
   }

   @Override
   public void onCenterChange() {
      this.updateBox();
   }

   @Override
   public WorldBorder$BorderExtent update() {
      return this;
   }

   @Override
   public VoxelShape getCollisionShape() {
      return this.shape;
   }
}
