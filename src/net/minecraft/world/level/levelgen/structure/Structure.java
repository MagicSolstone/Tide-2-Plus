package net.minecraft.world.level.levelgen.structure;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mojang.serialization.codecs.RecordCodecBuilder.Instance;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.registries.codec.RegistryCodecs;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.util.profiling.jfr.JvmProfiler;
import net.minecraft.util.profiling.jfr.callback.ProfiledDuration;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.biome.Climate$Sampler;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.GenerationStep$Decoration;
import net.minecraft.world.level.levelgen.Heightmap$Types;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.structure.pieces.PiecesContainer;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;

public abstract class Structure {
   public static final Codec<Structure> DIRECT_CODEC = BuiltInRegistries.STRUCTURE_TYPE.byNameCodec().dispatch(Structure::type, StructureType::codec);
   public static final Codec<Holder<Structure>> CODEC = RegistryCodecs.holder(Registries.STRUCTURE, DIRECT_CODEC);
   protected final Structure$StructureSettings settings;

   public static <S extends Structure> RecordCodecBuilder<S, Structure$StructureSettings> settingsCodec(final Instance<S> i) {
      return Structure$StructureSettings.CODEC.forGetter(e -> e.settings);
   }

   public static <S extends Structure> MapCodec<S> simpleCodec(final Function<Structure$StructureSettings, S> constructor) {
      return RecordCodecBuilder.mapCodec(i -> i.group(settingsCodec(i)).apply(i, constructor));
   }

   protected Structure(final Structure$StructureSettings settings) {
      this.settings = settings;
   }

   public HolderSet<Biome> biomes() {
      return this.settings.biomes;
   }

   public Map<MobCategory, StructureSpawnOverride> spawnOverrides() {
      return this.settings.spawnOverrides;
   }

   public GenerationStep$Decoration step() {
      return this.settings.step;
   }

   public TerrainAdjustment terrainAdaptation() {
      return this.settings.terrainAdaptation;
   }

   public BoundingBox adjustBoundingBox(final BoundingBox boundingBox) {
      return this.terrainAdaptation() != TerrainAdjustment.NONE ? boundingBox.inflatedBy(12) : boundingBox;
   }

   public StructureStart generate(
      final Holder<Structure> selected,
      final ResourceKey<Level> dimension,
      final RegistryAccess registryAccess,
      final ChunkGenerator chunkGenerator,
      final BiomeSource biomeSource,
      final Climate$Sampler climateSampler,
      final RandomState randomState,
      final StructureTemplateManager structureTemplateManager,
      final long seed,
      final ChunkPos sourceChunkPos,
      final int references,
      final LevelHeightAccessor heightAccessor,
      final Predicate<Holder<Biome>> validBiome
   ) {
      ProfiledDuration profiled = JvmProfiler.INSTANCE.onStructureGenerate(sourceChunkPos, dimension, selected);
      Structure$GenerationContext context = new Structure$GenerationContext(
         registryAccess, chunkGenerator, biomeSource, climateSampler, randomState, structureTemplateManager, seed, sourceChunkPos, heightAccessor, validBiome
      );
      Optional<Structure$GenerationStub> generation = this.findValidGenerationPoint(context);
      if (generation.isPresent()) {
         StructurePiecesBuilder builder = generation.get().getPiecesBuilder();
         StructureStart testStart = new StructureStart(this, sourceChunkPos, references, builder.build());
         if (testStart.isValid()) {
            if (profiled != null) {
               profiled.finish(true);
            }

            return testStart;
         }
      }

      if (profiled != null) {
         profiled.finish(false);
      }

      return StructureStart.INVALID_START;
   }

   protected static Optional<Structure$GenerationStub> onTopOfChunkCenter(
      final Structure$GenerationContext context, final Heightmap$Types heightmap, final Consumer<StructurePiecesBuilder> generator
   ) {
      return !context.couldValidBiomeExistOnTopOfChunkCenter() ? Optional.empty() : onTopOfChunkCenterWithoutBiomeCheck(context, heightmap, generator);
   }

   protected static Optional<Structure$GenerationStub> onTopOfChunkCenterWithoutBiomeCheck(
      final Structure$GenerationContext context, final Heightmap$Types heightmap, final Consumer<StructurePiecesBuilder> generator
   ) {
      ChunkPos chunkPos = context.chunkPos();
      int blockX = chunkPos.getMiddleBlockX();
      int blockZ = chunkPos.getMiddleBlockZ();
      int blockY = context.chunkGenerator().getFirstOccupiedHeight(blockX, blockZ, heightmap, context.heightAccessor(), context.randomState());
      return Optional.of(new Structure$GenerationStub(new BlockPos(blockX, blockY, blockZ), generator));
   }

   public void afterPlace(
      final WorldGenLevel level,
      final StructureManager structureManager,
      final ChunkGenerator generator,
      final RandomSource random,
      final BoundingBox chunkBB,
      final ChunkPos chunkPos,
      final PiecesContainer pieces
   ) {
   }

   private static int[] getCornerHeights(final Structure$GenerationContext context, final int minX, final int sizeX, final int minZ, final int sizeZ) {
      ChunkGenerator chunkGenerator = context.chunkGenerator();
      LevelHeightAccessor heightAccessor = context.heightAccessor();
      RandomState randomState = context.randomState();
      return new int[]{
         chunkGenerator.getFirstOccupiedHeight(minX, minZ, Heightmap$Types.WORLD_SURFACE_WG, heightAccessor, randomState),
         chunkGenerator.getFirstOccupiedHeight(minX, minZ + sizeZ, Heightmap$Types.WORLD_SURFACE_WG, heightAccessor, randomState),
         chunkGenerator.getFirstOccupiedHeight(minX + sizeX, minZ, Heightmap$Types.WORLD_SURFACE_WG, heightAccessor, randomState),
         chunkGenerator.getFirstOccupiedHeight(minX + sizeX, minZ + sizeZ, Heightmap$Types.WORLD_SURFACE_WG, heightAccessor, randomState)
      };
   }

   public static int getMeanFirstOccupiedHeight(final Structure$GenerationContext context, final int minX, final int sizeX, final int minZ, final int sizeZ) {
      int[] cornerHeights = getCornerHeights(context, minX, sizeX, minZ, sizeZ);
      return (cornerHeights[0] + cornerHeights[1] + cornerHeights[2] + cornerHeights[3]) / 4;
   }

   protected static int getLowestY(final Structure$GenerationContext context, final int sizeX, final int sizeZ) {
      ChunkPos chunkPos = context.chunkPos();
      int minX = chunkPos.getMinBlockX();
      int minZ = chunkPos.getMinBlockZ();
      return getLowestY(context, minX, minZ, sizeX, sizeZ);
   }

   protected static int getLowestY(final Structure$GenerationContext context, final int minX, final int minZ, final int sizeX, final int sizeZ) {
      int[] cornerHeights = getCornerHeights(context, minX, sizeX, minZ, sizeZ);
      return Math.min(Math.min(cornerHeights[0], cornerHeights[1]), Math.min(cornerHeights[2], cornerHeights[3]));
   }

   @Deprecated
   protected BlockPos getLowestYIn5by5Box(final Structure$GenerationContext context, final int blockX, final int blockZ, final Rotation rotation) {
      int offsetX = 5;
      int offsetZ = 5;
      if (rotation == Rotation.CLOCKWISE_90) {
         offsetX = -5;
      } else if (rotation == Rotation.CLOCKWISE_180) {
         offsetX = -5;
         offsetZ = -5;
      } else if (rotation == Rotation.COUNTERCLOCKWISE_90) {
         offsetZ = -5;
      }

      return new BlockPos(blockX, getLowestY(context, blockX, blockZ, offsetX, offsetZ), blockZ);
   }

   protected abstract Optional<Structure$GenerationStub> findGenerationPoint(final Structure$GenerationContext context);

   public Optional<Structure$GenerationStub> findValidGenerationPoint(final Structure$GenerationContext context) {
      return this.findGenerationPoint(context).filter(context::isValidBiome);
   }

   public abstract StructureType<?> type();
}
