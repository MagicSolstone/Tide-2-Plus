package net.minecraft.world.level;

import com.mojang.logging.LogUtils;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos$MutableBlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.VisibleForDebug;
import net.minecraft.util.profiling.Profiler;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.util.random.WeightedList;
import net.minecraft.world.attribute.EnvironmentAttributeReader;
import net.minecraft.world.attribute.EnvironmentAttributes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.biome.MobSpawnSettings$MobSpawnCost;
import net.minecraft.world.level.biome.MobSpawnSettings$SpawnerData;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.gamerules.GameRules;
import net.minecraft.world.level.levelgen.Heightmap$Types;
import net.minecraft.world.level.levelgen.structure.BuiltinStructures;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.structures.NetherFortressStructure;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.storage.LevelData$RespawnData;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;

public final class NaturalSpawner {
   private static final Logger LOGGER = LogUtils.getLogger();
   private static final int MIN_SPAWN_DISTANCE = 24;
   public static final int SPAWN_DISTANCE_CHUNK = 8;
   public static final int SPAWN_DISTANCE_BLOCK = 128;
   public static final int INSCRIBED_SQUARE_SPAWN_DISTANCE_CHUNK = Mth.floor(8.0F / Mth.SQRT_OF_TWO);
   private static final int MAGIC_NUMBER = (int)Math.pow(17.0, 2.0);
   private static final MobCategory[] SPAWNING_CATEGORIES = Stream.of(MobCategory.values()).filter(c -> c != MobCategory.MISC).toArray(MobCategory[]::new);

   private NaturalSpawner() {
   }

   public static NaturalSpawner$SpawnState createState(
      final int spawnableChunkCount, final ServerLevel level, final NaturalSpawner$ChunkGetter chunkGetter, final LocalMobCapCalculator localMobCapCalculator
   ) {
      Iterable<Entity> entities = level.getAllEntities();
      PotentialCalculator spawnPotential = new PotentialCalculator();
      Object2IntOpenHashMap<MobCategory> mobCounts = new Object2IntOpenHashMap();

      for (Entity entity : entities) {
         if (!(entity instanceof Mob mob && (mob.isPersistenceRequired() || mob.requiresCustomPersistence()))) {
            MobCategory category = entity.getType().getCategory();
            if (category != MobCategory.MISC) {
               BlockPos pos = entity.blockPosition();
               chunkGetter.query(ChunkPos.pack(pos), chunk -> {
                  MobSpawnSettings naturalMobSpawns = level.environmentAttributes().getValue(EnvironmentAttributes.NATURAL_MOB_SPAWNS, pos);
                  MobSpawnSettings$MobSpawnCost mobSpawnCost = naturalMobSpawns.getMobSpawnCost(entity.getType());
                  if (mobSpawnCost != null) {
                     spawnPotential.addCharge(entity.blockPosition(), mobSpawnCost.charge());
                  }

                  if (entity instanceof Mob) {
                     localMobCapCalculator.addMob(chunk.getPos(), category);
                  }

                  mobCounts.addTo(category, 1);
               });
            }
         }
      }

      return new NaturalSpawner$SpawnState(spawnableChunkCount, mobCounts, spawnPotential, localMobCapCalculator);
   }

   public static List<MobCategory> getFilteredSpawningCategories(
      final NaturalSpawner$SpawnState state, final boolean spawnEnemies, final boolean spawnPersistent
   ) {
      List<MobCategory> spawningCategories = new ArrayList<>(SPAWNING_CATEGORIES.length);

      for (MobCategory mobCategory : SPAWNING_CATEGORIES) {
         if ((spawnEnemies || mobCategory.isFriendly()) && (spawnPersistent || !mobCategory.isPersistent()) && state.canSpawnForCategoryGlobal(mobCategory)) {
            spawningCategories.add(mobCategory);
         }
      }

      return spawningCategories;
   }

   public static void spawnForChunk(
      final ServerLevel level, final LevelChunk chunk, final NaturalSpawner$SpawnState state, final List<MobCategory> spawningCategories
   ) {
      ProfilerFiller profiler = Profiler.get();
      profiler.push("spawner");

      for (MobCategory mobCategory : spawningCategories) {
         if (state.canSpawnForCategoryLocal(mobCategory, chunk.getPos())) {
            spawnCategoryForChunk(mobCategory, level, chunk, state::canSpawn, state::afterSpawn);
         }
      }

      profiler.pop();
   }

   public static void spawnCategoryForChunk(
      final MobCategory mobCategory,
      final ServerLevel level,
      final LevelChunk chunk,
      final NaturalSpawner$SpawnPredicate extraTest,
      final NaturalSpawner$AfterSpawnCallback spawnCallback
   ) {
      BlockPos start = getRandomPosWithin(level, chunk);
      if (start.getY() >= level.getMinY() + 1) {
         spawnCategoryForPosition(mobCategory, level, chunk, start, extraTest, spawnCallback);
      }
   }

   @VisibleForDebug
   public static void spawnCategoryForPosition(final MobCategory mobCategory, final ServerLevel level, final BlockPos start) {
      spawnCategoryForPosition(mobCategory, level, level.getChunk(start), start, (var0, var1, var2, var3) -> true, (var0, var1) -> {});
   }

   public static void spawnCategoryForPosition(
      final MobCategory mobCategory,
      final ServerLevel level,
      final ChunkAccess chunk,
      final BlockPos start,
      final NaturalSpawner$SpawnPredicate extraTest,
      final NaturalSpawner$AfterSpawnCallback spawnCallback
   ) {
      StructureManager structureManager = level.structureManager();
      ChunkGenerator generator = level.getChunkSource().getGenerator();
      int yStart = start.getY();
      BlockState state = chunk.getBlockState(start);
      if (!state.isRedstoneConductor(chunk, start)) {
         BlockPos$MutableBlockPos pos = new BlockPos$MutableBlockPos();
         int clusterSize = 0;

         for (int groupCount = 0; groupCount < 3; groupCount++) {
            int x = start.getX();
            int z = start.getZ();
            int ss = 6;
            MobSpawnSettings$SpawnerData currentSpawnData = null;
            SpawnGroupData groupData = null;
            int max = Mth.ceil(level.random.nextFloat() * 4.0F);
            int groupSize = 0;

            for (int ll = 0; ll < max; ll++) {
               x += level.random.nextInt(6) - level.random.nextInt(6);
               z += level.random.nextInt(6) - level.random.nextInt(6);
               pos.set(x, yStart, z);
               double xx = x + 0.5;
               double zz = z + 0.5;
               Player nearestPlayer = level.getNearestPlayer(xx, yStart, zz, -1.0, false);
               if (nearestPlayer != null) {
                  double nearestPlayerDistanceSqr = nearestPlayer.distanceToSqr(xx, yStart, zz);
                  if (isRightDistanceToPlayerAndSpawnPoint(level, chunk, pos, nearestPlayerDistanceSqr)) {
                     if (currentSpawnData == null) {
                        Optional<MobSpawnSettings$SpawnerData> nextSpawnData = getRandomSpawnMobAt(
                           level, structureManager, generator, mobCategory, level.random, pos
                        );
                        if (nextSpawnData.isEmpty()) {
                           break;
                        }

                        currentSpawnData = nextSpawnData.get();
                        max = currentSpawnData.count().sample(level.random);
                     }

                     if (isValidSpawnPostitionForType(level, mobCategory, structureManager, generator, currentSpawnData, pos, nearestPlayerDistanceSqr)
                        && extraTest.test(currentSpawnData.type(), level, pos, chunk)) {
                        Mob mob = getMobForSpawn(level, currentSpawnData.type());
                        if (mob == null) {
                           return;
                        }

                        mob.snapTo(xx, yStart, zz, level.random.nextFloat() * 360.0F, 0.0F);
                        if (isValidPositionForMob(level, mob, nearestPlayerDistanceSqr)) {
                           groupData = mob.finalizeSpawn(level, level.getCurrentDifficultyAt(mob.blockPosition()), EntitySpawnReason.NATURAL, groupData);
                           clusterSize++;
                           groupSize++;
                           level.addFreshEntityWithPassengers(mob);
                           spawnCallback.run(mob, chunk);
                           if (clusterSize >= mob.getMaxSpawnClusterSize()) {
                              return;
                           }

                           if (mob.isMaxGroupSizeReached(groupSize)) {
                              break;
                           }
                        }
                     }
                  }
               }
            }
         }
      }
   }

   private static boolean isRightDistanceToPlayerAndSpawnPoint(
      final ServerLevel level, final ChunkAccess chunk, final BlockPos$MutableBlockPos pos, final double nearestPlayerDistanceSqr
   ) {
      if (nearestPlayerDistanceSqr <= 576.0) {
         return false;
      } else {
         LevelData$RespawnData respawnData = level.getRespawnData();
         if (respawnData.dimension() == level.dimension()
            && respawnData.pos().closerToCenterThan(new Vec3(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5), 24.0)) {
            return false;
         } else {
            ChunkPos chunkPos = ChunkPos.containing(pos);
            return Objects.equals(chunkPos, chunk.getPos()) || level.canSpawnEntitiesInChunk(chunkPos);
         }
      }
   }

   private static boolean isValidSpawnPostitionForType(
      final ServerLevel level,
      final MobCategory mobCategory,
      final StructureManager structureManager,
      final ChunkGenerator generator,
      final MobSpawnSettings$SpawnerData currentSpawnData,
      final BlockPos$MutableBlockPos pos,
      final double nearestPlayerDistanceSqr
   ) {
      EntityType<?> type = currentSpawnData.type();
      if (type.getCategory() == MobCategory.MISC) {
         return false;
      } else if (!type.canSpawnFarFromPlayer() && nearestPlayerDistanceSqr > type.getCategory().getDespawnDistance() * type.getCategory().getDespawnDistance()) {
         return false;
      } else if (!type.canSummon() || !canSpawnMobAt(level, structureManager, generator, mobCategory, currentSpawnData, pos)) {
         return false;
      } else if (!SpawnPlacements.isSpawnPositionOk(type, level, pos)) {
         return false;
      } else {
         return !SpawnPlacements.checkSpawnRules(type, level, EntitySpawnReason.NATURAL, pos, level.random)
            ? false
            : level.noCollision(type.getSpawnAABB(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5));
      }
   }

   @Nullable
   private static Mob getMobForSpawn(final ServerLevel level, final EntityType<?> type) {
      try {
         if (type.create(level, EntitySpawnReason.NATURAL) instanceof Mob mob) {
            return mob;
         }

         LOGGER.warn("Can't spawn entity of type: {}", BuiltInRegistries.ENTITY_TYPE.getKey(type));
      } catch (Exception var4) {
         LOGGER.warn("Failed to create mob", var4);
      }

      return null;
   }

   private static boolean isValidPositionForMob(final ServerLevel level, final Mob mob, final double nearestPlayerDistanceSqr) {
      return nearestPlayerDistanceSqr > mob.getType().getCategory().getDespawnDistance() * mob.getType().getCategory().getDespawnDistance()
            && mob.removeWhenFarAway(nearestPlayerDistanceSqr)
         ? false
         : mob.checkSpawnRules(level, EntitySpawnReason.NATURAL) && mob.checkSpawnObstruction(level);
   }

   private static Optional<MobSpawnSettings$SpawnerData> getRandomSpawnMobAt(
      final ServerLevel level,
      final StructureManager structureManager,
      final ChunkGenerator generator,
      final MobCategory mobCategory,
      final RandomSource random,
      final BlockPos pos
   ) {
      Holder<Biome> biome = level.getBiome(pos);
      return mobCategory == MobCategory.WATER_AMBIENT && biome.is(BiomeTags.REDUCED_WATER_AMBIENT_SPAWNS) && random.nextFloat() < 0.98F
         ? Optional.empty()
         : mobsAt(level, structureManager, generator, mobCategory, pos).getRandom(random);
   }

   private static boolean canSpawnMobAt(
      final ServerLevel level,
      final StructureManager structureManager,
      final ChunkGenerator generator,
      final MobCategory mobCategory,
      final MobSpawnSettings$SpawnerData spawnerData,
      final BlockPos pos
   ) {
      return mobsAt(level, structureManager, generator, mobCategory, pos).contains(spawnerData);
   }

   private static WeightedList<MobSpawnSettings$SpawnerData> mobsAt(
      final ServerLevel level, final StructureManager structureManager, final ChunkGenerator generator, final MobCategory mobCategory, final BlockPos pos
   ) {
      return isInNetherFortressBounds(pos, level, mobCategory, structureManager)
         ? NetherFortressStructure.FORTRESS_ENEMIES
         : generator.getMobsAt(level, structureManager, mobCategory, pos);
   }

   public static boolean isInNetherFortressBounds(
      final BlockPos pos, final ServerLevel level, final MobCategory category, final StructureManager structureManager
   ) {
      if (category == MobCategory.MONSTER && level.getBlockState(pos.below()).is(Blocks.NETHER_BRICKS)) {
         Structure fortress = structureManager.registryAccess().lookupOrThrow(Registries.STRUCTURE).getValue(BuiltinStructures.FORTRESS);
         return fortress == null ? false : structureManager.getStructureAt(pos, fortress).isValid();
      } else {
         return false;
      }
   }

   private static BlockPos getRandomPosWithin(final Level level, final LevelChunk chunk) {
      ChunkPos pos = chunk.getPos();
      int x = pos.getMinBlockX() + level.random.nextInt(16);
      int z = pos.getMinBlockZ() + level.random.nextInt(16);
      int topEmptyY = chunk.getHeight(Heightmap$Types.WORLD_SURFACE, x, z) + 1;
      int y = Mth.randomBetweenInclusive(level.random, level.getMinY(), topEmptyY);
      return new BlockPos(x, y, z);
   }

   public static boolean isValidEmptySpawnBlock(
      final BlockGetter level, final BlockPos pos, final BlockState blockState, final FluidState fluidState, final EntityType<?> type
   ) {
      if (blockState.isCollisionShapeFullBlock(level, pos)) {
         return false;
      } else if (blockState.isSignalSource()) {
         return false;
      } else if (!fluidState.isEmpty()) {
         return false;
      } else {
         return blockState.is(BlockTags.PREVENT_MOB_SPAWNING_INSIDE) ? false : !type.isBlockDangerous(blockState);
      }
   }

   public static void spawnMobsForChunkGeneration(final ServerLevelAccessor level, final BlockPos sourcePos, final ChunkPos chunkPos, final RandomSource random) {
      if (level.getLevel().getGameRules().get(GameRules.SPAWN_MOBS)) {
         EnvironmentAttributeReader attributes = level.environmentAttributes();
         MobSpawnSettings mobSettings = attributes.getValue(EnvironmentAttributes.NATURAL_MOB_SPAWNS, sourcePos);
         WeightedList<MobSpawnSettings$SpawnerData> mobs = mobSettings.getMobsToSpawn(MobCategory.CREATURE);
         if (!mobs.isEmpty()) {
            float creatureProbability = attributes.getValue(EnvironmentAttributes.CREATURE_WORLD_GEN_SPAWN_PROBABILITY, sourcePos);
            int xo = chunkPos.getMinBlockX();
            int zo = chunkPos.getMinBlockZ();

            while (random.nextFloat() < creatureProbability) {
               Optional<MobSpawnSettings$SpawnerData> nextSpawnerData = mobs.getRandom(random);
               if (!nextSpawnerData.isEmpty()) {
                  MobSpawnSettings$SpawnerData spawnerData = nextSpawnerData.get();
                  int count = spawnerData.count().sample(random);
                  SpawnGroupData groupSpawnData = null;
                  int x = xo + random.nextInt(16);
                  int z = zo + random.nextInt(16);
                  int startX = x;
                  int startZ = z;

                  for (int i = 0; i < count; i++) {
                     boolean success = false;

                     for (int attempts = 0; !success && attempts < 4; attempts++) {
                        BlockPos pos = getTopNonCollidingPos(level, spawnerData.type(), x, z);
                        if (spawnerData.type().canSummon() && SpawnPlacements.isSpawnPositionOk(spawnerData.type(), level, pos)) {
                           float width = spawnerData.type().getWidth();
                           double fx = Mth.clamp((double)x, (double)xo + width, xo + 16.0 - width);
                           double fz = Mth.clamp((double)z, (double)zo + width, zo + 16.0 - width);
                           if (!level.noCollision(spawnerData.type().getSpawnAABB(fx, pos.getY(), fz))
                              || !SpawnPlacements.checkSpawnRules(
                                 spawnerData.type(), level, EntitySpawnReason.CHUNK_GENERATION, BlockPos.containing(fx, pos.getY(), fz), level.getRandom()
                              )) {
                              continue;
                           }

                           Entity entity;
                           try {
                              entity = spawnerData.type().create(level.getLevel(), EntitySpawnReason.NATURAL);
                           } catch (Exception var29) {
                              LOGGER.warn("Failed to create mob", var29);
                              continue;
                           }

                           if (entity == null) {
                              continue;
                           }

                           entity.snapTo(fx, pos.getY(), fz, random.nextFloat() * 360.0F, 0.0F);
                           if (entity instanceof Mob mob && mob.checkSpawnRules(level, EntitySpawnReason.CHUNK_GENERATION) && mob.checkSpawnObstruction(level)) {
                              groupSpawnData = mob.finalizeSpawn(
                                 level, level.getCurrentDifficultyAt(mob.blockPosition()), EntitySpawnReason.CHUNK_GENERATION, groupSpawnData
                              );
                              level.addFreshEntityWithPassengers(mob);
                              success = true;
                           }
                        }

                        x += random.nextInt(5) - random.nextInt(5);

                        for (z += random.nextInt(5) - random.nextInt(5);
                           x < xo || x >= xo + 16 || z < zo || z >= zo + 16;
                           z = startZ + random.nextInt(5) - random.nextInt(5)
                        ) {
                           x = startX + random.nextInt(5) - random.nextInt(5);
                        }
                     }
                  }
               }
            }
         }
      }
   }

   private static BlockPos getTopNonCollidingPos(final LevelReader level, final EntityType<?> type, final int x, final int z) {
      int levelHeight = level.getHeight(SpawnPlacements.getHeightmapType(type), x, z);
      BlockPos$MutableBlockPos pos = new BlockPos$MutableBlockPos(x, levelHeight, z);
      if (level.dimensionType().hasCeiling()) {
         do {
            pos.move(Direction.DOWN);
         } while (!level.getBlockState(pos).isAir());

         do {
            pos.move(Direction.DOWN);
         } while (level.getBlockState(pos).isAir() && pos.getY() > level.getMinY());
      }

      return SpawnPlacements.getPlacementType(type).adjustSpawnPosition(level, pos.immutable());
   }
}
