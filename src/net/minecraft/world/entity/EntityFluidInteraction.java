package net.minecraft.world.entity;

import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.Reference2ObjectArrayMap;
import it.unimi.dsi.fastutil.objects.Reference2ObjectMap;
import it.unimi.dsi.fastutil.objects.Reference2ObjectMaps;
import it.unimi.dsi.fastutil.objects.Reference2ObjectMap.Entry;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import net.minecraft.core.BlockPos$MutableBlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.SectionPos;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.chunk.status.ChunkStatus;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

public class EntityFluidInteraction {
   private final List<EntityFluidInteraction$Tracker> fluidTrackers = new ArrayList<>();
   private final Reference2ObjectMap<TagKey<Fluid>, EntityFluidInteraction$CurrentAccumulator> currentAccumulators = new Reference2ObjectArrayMap();

   public EntityFluidInteraction(final Set<TagKey<Fluid>> fluidsWithCurrent) {
      for (TagKey<Fluid> fluid : fluidsWithCurrent) {
         this.currentAccumulators.put(fluid, new EntityFluidInteraction$CurrentAccumulator());
      }
   }

   public boolean update(final Entity entity, final boolean ignoreCurrent) {
      this.fluidTrackers.removeIf(EntityFluidInteraction$Tracker::reset);
      this.currentAccumulators.values().forEach(EntityFluidInteraction$CurrentAccumulator::reset);
      AABB box = entity.getFluidInteractionBox();
      if (box == null) {
         return false;
      } else {
         int x0 = Mth.floor(box.minX);
         int y0 = Mth.floor(box.minY);
         int z0 = Mth.floor(box.minZ);
         int x1 = Mth.ceil(box.maxX) - 1;
         int y1 = Mth.ceil(box.maxY) - 1;
         int z1 = Mth.ceil(box.maxZ) - 1;
         if (!hasFluidAndLoaded(entity.level(), x0 - 1, y0, z0 - 1, x1 + 1, y1, z1 + 1)) {
            return false;
         } else {
            double entityY = entity.getBoundingBox().minY;
            int eyeBlockX = entity.getBlockX();
            double eyeY = entity.getEyeY();
            int eyeBlockZ = entity.getBlockZ();
            Holder<Fluid> lastFluidType = null;
            EntityFluidInteraction$Tracker tracker = null;
            EntityFluidInteraction$CurrentAccumulator current = null;
            BlockGetter level = entity.level();
            BlockPos$MutableBlockPos mutablePos = new BlockPos$MutableBlockPos();

            for (int x = x0; x <= x1; x++) {
               for (int y = y0; y <= y1; y++) {
                  for (int z = z0; z <= z1; z++) {
                     mutablePos.set(x, y, z);
                     FluidState fluidState = level.getFluidState(mutablePos);
                     if (!fluidState.isEmpty()) {
                        double fluidBottom = mutablePos.getY();
                        double fluidTop = fluidBottom + fluidState.getHeight(level, mutablePos);
                        if (!(fluidTop < box.minY)) {
                           Holder<Fluid> fluidType = fluidState.typeHolder();
                           if (fluidType != lastFluidType) {
                              lastFluidType = fluidType;
                              tracker = this.getOrCreateTrackerFor(fluidType);
                              if (!ignoreCurrent) {
                                 current = this.getCurrentAccumulatorFor(fluidType);
                              }
                           }

                           if (x == eyeBlockX && z == eyeBlockZ && eyeY >= fluidBottom) {
                              double fluidTopForCamera = fluidBottom + fluidState.getHeightForCamera(level, mutablePos);
                              if (eyeY <= fluidTopForCamera) {
                                 tracker.eyesInside = true;
                              }
                           }

                           tracker.height = Math.max(fluidTop - entityY, tracker.height);
                           if (current != null) {
                              Vec3 flow = fluidState.getFlow(level, mutablePos);
                              current.height = Math.max(tracker.height, current.height);
                              if (current.height < 0.4) {
                                 flow = flow.scale(current.height);
                              }

                              current.accumulate(flow);
                           }
                        }
                     }
                  }
               }
            }

            return lastFluidType != null;
         }
      }
   }

   private static boolean hasFluidAndLoaded(final Level level, final int x0, final int y0, final int z0, final int x1, final int y1, final int z1) {
      int sectionX0 = SectionPos.blockToSectionCoord(x0);
      int sectionY0 = SectionPos.blockToSectionCoord(y0);
      int sectionZ0 = SectionPos.blockToSectionCoord(z0);
      int sectionX1 = SectionPos.blockToSectionCoord(x1);
      int sectionY1 = SectionPos.blockToSectionCoord(y1);
      int sectionZ1 = SectionPos.blockToSectionCoord(z1);
      boolean hasFluid = false;

      for (int chunkZ = sectionZ0; chunkZ <= sectionZ1; chunkZ++) {
         for (int chunkX = sectionX0; chunkX <= sectionX1; chunkX++) {
            ChunkAccess chunk = level.getChunk(chunkX, chunkZ, ChunkStatus.FULL, false);
            if (chunk == null) {
               return false;
            }

            LevelChunkSection[] sections = chunk.getSections();

            for (int sectionY = sectionY0; sectionY <= sectionY1; sectionY++) {
               int sectionIndex = chunk.getSectionIndexFromSectionY(sectionY);
               if (sectionIndex >= 0 && sectionIndex < sections.length) {
                  hasFluid |= sections[sectionIndex].hasFluid();
               }
            }
         }
      }

      return hasFluid;
   }

   private EntityFluidInteraction$Tracker getOrCreateTrackerFor(final Holder<Fluid> fluidType) {
      for (EntityFluidInteraction$Tracker tracker : this.fluidTrackers) {
         if (tracker.fluidType.equals(fluidType)) {
            return tracker;
         }
      }

      EntityFluidInteraction$Tracker trackerx = new EntityFluidInteraction$Tracker(fluidType);
      this.fluidTrackers.add(trackerx);
      return trackerx;
   }

   @Nullable
   private EntityFluidInteraction$CurrentAccumulator getCurrentAccumulatorFor(final Holder<Fluid> fluid) {
      ObjectIterator var2 = Reference2ObjectMaps.fastIterable(this.currentAccumulators).iterator();

      while (var2.hasNext()) {
         Entry<TagKey<Fluid>, EntityFluidInteraction$CurrentAccumulator> entry = (Entry<TagKey<Fluid>, EntityFluidInteraction$CurrentAccumulator>)var2.next();
         if (fluid.is((TagKey<Fluid>)entry.getKey())) {
            return (EntityFluidInteraction$CurrentAccumulator)entry.getValue();
         }
      }

      return null;
   }

   public void applyCurrentTo(final TagKey<Fluid> fluid, final Entity entity, final double scale) {
      EntityFluidInteraction$CurrentAccumulator current = (EntityFluidInteraction$CurrentAccumulator)this.currentAccumulators.get(fluid);
      if (current != null) {
         current.applyTo(entity, scale);
      }
   }

   public double getFluidHeight(final TagKey<Fluid> fluid) {
      double height = 0.0;

      for (EntityFluidInteraction$Tracker tracker : this.fluidTrackers) {
         if (tracker.height > height && tracker.fluidType.is(fluid)) {
            height = tracker.height;
         }
      }

      return height;
   }

   public boolean isInFluid(final TagKey<Fluid> fluid) {
      return this.getFluidHeight(fluid) > 0.0;
   }

   public boolean isEyeInFluid(final TagKey<Fluid> fluid) {
      for (EntityFluidInteraction$Tracker tracker : this.fluidTrackers) {
         if (tracker.eyesInside && tracker.fluidType.is(fluid)) {
            return true;
         }
      }

      return false;
   }
}
