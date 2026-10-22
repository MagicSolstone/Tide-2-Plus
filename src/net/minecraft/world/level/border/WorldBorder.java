package net.minecraft.world.level.border;

import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;

public class WorldBorder extends SavedData {
   public static final double MAX_SIZE = 5.999997E7F;
   public static final double MAX_CENTER_COORDINATE = 2.9999984E7;
   public static final Codec<WorldBorder> CODEC = WorldBorder$Settings.CODEC.xmap(WorldBorder::new, WorldBorder$Settings::new);
   public static final SavedDataType<WorldBorder> TYPE = new SavedDataType<>(
      Identifier.withDefaultNamespace("world_border"), WorldBorder::new, CODEC, DataFixTypes.SAVED_DATA_WORLD_BORDER
   );
   private final WorldBorder$Settings settings;
   private boolean initialized;
   private final List<BorderChangeListener> listeners = Lists.newArrayList();
   private double damagePerBlock = 0.2;
   private double safeZone = 5.0;
   private int warningTime = 15;
   private int warningBlocks = 5;
   private double centerX;
   private double centerZ;
   private int absoluteMaxSize = 29999984;
   private WorldBorder$BorderExtent extent = new WorldBorder$StaticBorderExtent(this, 5.999997E7F);

   public WorldBorder() {
      this(WorldBorder$Settings.DEFAULT);
   }

   public WorldBorder(final WorldBorder$Settings settings) {
      this.settings = settings;
   }

   public boolean isWithinBounds(final BlockPos pos) {
      return this.isWithinBounds(pos.getX(), pos.getZ());
   }

   public boolean isWithinBounds(final Vec3 pos) {
      return this.isWithinBounds(pos.x, pos.z);
   }

   public boolean isWithinBounds(final ChunkPos pos) {
      return this.isWithinBounds(pos.getMinBlockX(), pos.getMinBlockZ()) && this.isWithinBounds(pos.getMaxBlockX(), pos.getMaxBlockZ());
   }

   public boolean isWithinBounds(final AABB aabb) {
      return this.isWithinBounds(aabb.minX, aabb.minZ, aabb.maxX - 1.0E-5F, aabb.maxZ - 1.0E-5F);
   }

   private boolean isWithinBounds(final double minX, final double minZ, final double maxX, final double maxZ) {
      return this.isWithinBounds(minX, minZ) && this.isWithinBounds(maxX, maxZ);
   }

   public boolean isWithinBounds(final double x, final double z) {
      return this.isWithinBounds(x, z, 0.0);
   }

   public boolean isWithinBounds(final double x, final double z, final double margin) {
      return x >= this.getMinX() - margin && x < this.getMaxX() + margin && z >= this.getMinZ() - margin && z < this.getMaxZ() + margin;
   }

   public BlockPos clampToBounds(final BlockPos position) {
      return this.clampToBounds(position.getX(), position.getY(), position.getZ());
   }

   public BlockPos clampToBounds(final Vec3 position) {
      return this.clampToBounds(position.x(), position.y(), position.z());
   }

   public BlockPos clampToBounds(final double x, final double y, final double z) {
      return BlockPos.containing(this.clampVec3ToBound(x, y, z));
   }

   public Vec3 clampVec3ToBound(final Vec3 position) {
      return this.clampVec3ToBound(position.x, position.y, position.z);
   }

   public Vec3 clampVec3ToBound(final double x, final double y, final double z) {
      return new Vec3(Mth.clamp(x, this.getMinX(), this.getMaxX() - 1.0E-5F), y, Mth.clamp(z, this.getMinZ(), this.getMaxZ() - 1.0E-5F));
   }

   public double getDistanceToBorder(final Entity entity) {
      return this.getDistanceToBorder(entity.getX(), entity.getZ());
   }

   public VoxelShape getCollisionShape() {
      return this.extent.getCollisionShape();
   }

   public double getDistanceToBorder(final double x, final double z) {
      double fromNorth = z - this.getMinZ();
      double fromSouth = this.getMaxZ() - z;
      double fromWest = x - this.getMinX();
      double fromEast = this.getMaxX() - x;
      double min = Math.min(fromWest, fromEast);
      min = Math.min(min, fromNorth);
      return Math.min(min, fromSouth);
   }

   public boolean isInsideCloseToBorder(final Entity source, final AABB boundingBox) {
      double bbMax = Math.max(Mth.absMax(boundingBox.getXsize(), boundingBox.getZsize()), 1.0);
      return this.getDistanceToBorder(source) < bbMax * 2.0 && this.isWithinBounds(source.getX(), source.getZ(), bbMax);
   }

   public BorderStatus getStatus() {
      return this.extent.getStatus();
   }

   public double getMinX() {
      return this.getMinX(0.0F);
   }

   public double getMinX(final float deltaPartialTick) {
      return this.extent.getMinX(deltaPartialTick);
   }

   public double getMinZ() {
      return this.getMinZ(0.0F);
   }

   public double getMinZ(final float deltaPartialTick) {
      return this.extent.getMinZ(deltaPartialTick);
   }

   public double getMaxX() {
      return this.getMaxX(0.0F);
   }

   public double getMaxX(final float deltaPartialTick) {
      return this.extent.getMaxX(deltaPartialTick);
   }

   public double getMaxZ() {
      return this.getMaxZ(0.0F);
   }

   public double getMaxZ(final float deltaPartialTick) {
      return this.extent.getMaxZ(deltaPartialTick);
   }

   public double getCenterX() {
      return this.centerX;
   }

   public double getCenterZ() {
      return this.centerZ;
   }

   public void setCenter(final double x, final double z) {
      this.centerX = x;
      this.centerZ = z;
      this.extent.onCenterChange();
      this.setDirty();

      for (BorderChangeListener listener : this.getListeners()) {
         listener.onSetCenter(this, x, z);
      }
   }

   public double getSize() {
      return this.extent.getSize();
   }

   public long getLerpTime() {
      return this.extent.getLerpTime();
   }

   public double getLerpTarget() {
      return this.extent.getLerpTarget();
   }

   public void setSize(final double size) {
      this.extent = new WorldBorder$StaticBorderExtent(this, size);
      this.setDirty();

      for (BorderChangeListener listener : this.getListeners()) {
         listener.onSetSize(this, size);
      }
   }

   public void lerpSizeBetween(final double from, final double to, final long ticks, final long gameTime) {
      this.extent = (WorldBorder$BorderExtent)(from == to
         ? new WorldBorder$StaticBorderExtent(this, to)
         : new WorldBorder$MovingBorderExtent(this, from, to, ticks, gameTime));
      this.setDirty();

      for (BorderChangeListener listener : this.getListeners()) {
         listener.onLerpSize(this, from, to, ticks, gameTime);
      }
   }

   protected List<BorderChangeListener> getListeners() {
      return Lists.newArrayList(this.listeners);
   }

   public void addListener(final BorderChangeListener listener) {
      this.listeners.add(listener);
   }

   public void removeListener(final BorderChangeListener listener) {
      this.listeners.remove(listener);
   }

   public void setAbsoluteMaxSize(final int absoluteMaxSize) {
      this.absoluteMaxSize = absoluteMaxSize;
      this.extent.onAbsoluteMaxSizeChange();
   }

   public int getAbsoluteMaxSize() {
      return this.absoluteMaxSize;
   }

   public double getSafeZone() {
      return this.safeZone;
   }

   public void setSafeZone(final double safeZone) {
      this.safeZone = safeZone;
      this.setDirty();

      for (BorderChangeListener listener : this.getListeners()) {
         listener.onSetSafeZone(this, safeZone);
      }
   }

   public double getDamagePerBlock() {
      return this.damagePerBlock;
   }

   public void setDamagePerBlock(final double damagePerBlock) {
      this.damagePerBlock = damagePerBlock;
      this.setDirty();

      for (BorderChangeListener listener : this.getListeners()) {
         listener.onSetDamagePerBlock(this, damagePerBlock);
      }
   }

   public double getLerpSpeed() {
      return this.extent.getLerpSpeed();
   }

   public int getWarningTime() {
      return this.warningTime;
   }

   public void setWarningTime(final int warningTime) {
      this.warningTime = warningTime;
      this.setDirty();

      for (BorderChangeListener listener : this.getListeners()) {
         listener.onSetWarningTime(this, warningTime);
      }
   }

   public int getWarningBlocks() {
      return this.warningBlocks;
   }

   public void setWarningBlocks(final int warningBlocks) {
      this.warningBlocks = warningBlocks;
      this.setDirty();

      for (BorderChangeListener listener : this.getListeners()) {
         listener.onSetWarningBlocks(this, warningBlocks);
      }
   }

   public void tick() {
      this.extent = this.extent.update();
   }

   public void applyInitialSettings(final long gameTime) {
      if (!this.initialized) {
         this.setCenter(this.settings.centerX(), this.settings.centerZ());
         this.setDamagePerBlock(this.settings.damagePerBlock());
         this.setSafeZone(this.settings.safeZone());
         this.setWarningBlocks(this.settings.warningBlocks());
         this.setWarningTime(this.settings.warningTime());
         if (this.settings.lerpTime() > 0L) {
            this.lerpSizeBetween(this.settings.size(), this.settings.lerpTarget(), this.settings.lerpTime(), gameTime);
         } else {
            this.setSize(this.settings.size());
         }

         this.initialized = true;
      }
   }
}
