package net.minecraft.world.level;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntMaps;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.SharedConstants;
import net.minecraft.core.BlockPos;
import net.minecraft.world.attribute.EnvironmentAttributes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.biome.MobSpawnSettings$MobSpawnCost;
import net.minecraft.world.level.chunk.ChunkAccess;
import org.jspecify.annotations.Nullable;

public class NaturalSpawner$SpawnState {
   private final int spawnableChunkCount;
   private final Object2IntOpenHashMap<MobCategory> mobCategoryCounts;
   private final PotentialCalculator spawnPotential;
   private final Object2IntMap<MobCategory> unmodifiableMobCategoryCounts;
   private final LocalMobCapCalculator localMobCapCalculator;
   @Nullable
   private BlockPos lastCheckedPos;
   @Nullable
   private EntityType<?> lastCheckedType;
   private double lastCharge;

   private NaturalSpawner$SpawnState(
      final int spawnableChunkCount,
      final Object2IntOpenHashMap<MobCategory> mobCategoryCounts,
      final PotentialCalculator spawnPotential,
      final LocalMobCapCalculator localMobCapCalculator
   ) {
      this.spawnableChunkCount = spawnableChunkCount;
      this.mobCategoryCounts = mobCategoryCounts;
      this.spawnPotential = spawnPotential;
      this.localMobCapCalculator = localMobCapCalculator;
      this.unmodifiableMobCategoryCounts = Object2IntMaps.unmodifiable(mobCategoryCounts);
   }

   private boolean canSpawn(final EntityType<?> type, final Level level, final BlockPos testPos, final ChunkAccess chunk) {
      this.lastCheckedPos = testPos;
      this.lastCheckedType = type;
      MobSpawnSettings naturalMobSpawns = level.environmentAttributes().getValue(EnvironmentAttributes.NATURAL_MOB_SPAWNS, testPos);
      MobSpawnSettings$MobSpawnCost mobSpawnCost = naturalMobSpawns.getMobSpawnCost(type);
      if (mobSpawnCost == null) {
         this.lastCharge = 0.0;
         return true;
      } else {
         double charge = mobSpawnCost.charge();
         this.lastCharge = charge;
         double energyChange = this.spawnPotential.getPotentialEnergyChange(testPos, charge);
         return energyChange <= mobSpawnCost.energyBudget();
      }
   }

   private void afterSpawn(final Mob mob, final ChunkAccess chunk) {
      EntityType<?> type = mob.getType();
      BlockPos pos = mob.blockPosition();
      double charge;
      if (pos.equals(this.lastCheckedPos) && type == this.lastCheckedType) {
         charge = this.lastCharge;
      } else {
         MobSpawnSettings naturalMobSpawns = mob.level().environmentAttributes().getValue(EnvironmentAttributes.NATURAL_MOB_SPAWNS, pos);
         MobSpawnSettings$MobSpawnCost mobSpawnCost = naturalMobSpawns.getMobSpawnCost(type);
         if (mobSpawnCost != null) {
            charge = mobSpawnCost.charge();
         } else {
            charge = 0.0;
         }
      }

      this.spawnPotential.addCharge(pos, charge);
      MobCategory category = type.getCategory();
      this.mobCategoryCounts.addTo(category, 1);
      this.localMobCapCalculator.addMob(ChunkPos.containing(pos), category);
   }

   public int getSpawnableChunkCount() {
      return this.spawnableChunkCount;
   }

   public Object2IntMap<MobCategory> getMobCategoryCounts() {
      return this.unmodifiableMobCategoryCounts;
   }

   private boolean canSpawnForCategoryGlobal(final MobCategory mobCategory) {
      int maxMobCount = mobCategory.getMaxInstancesPerChunk() * this.spawnableChunkCount / NaturalSpawner.MAGIC_NUMBER;
      return this.mobCategoryCounts.getInt(mobCategory) < maxMobCount;
   }

   private boolean canSpawnForCategoryLocal(final MobCategory mobCategory, final ChunkPos chunkPos) {
      return this.localMobCapCalculator.canSpawn(mobCategory, chunkPos) || SharedConstants.DEBUG_IGNORE_LOCAL_MOB_CAP;
   }
}
