package net.minecraft.world.level.levelgen.structure.pools;

import com.google.common.collect.Lists;
import com.mojang.logging.LogUtils;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.Vec3i;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap$Types;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.densityfunction.SamplerContext;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.PoolElementStructurePiece;
import net.minecraft.world.level.levelgen.structure.Structure$GenerationContext;
import net.minecraft.world.level.levelgen.structure.Structure$GenerationStub;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
import net.minecraft.world.level.levelgen.structure.pools.alias.PoolAliasLookup;
import net.minecraft.world.level.levelgen.structure.structures.JigsawStructure;
import net.minecraft.world.level.levelgen.structure.structures.JigsawStructure$MaxDistance;
import net.minecraft.world.level.levelgen.structure.templatesystem.LiquidSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate$JigsawBlockInfo;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.apache.commons.lang3.mutable.MutableObject;
import org.slf4j.Logger;

public class JigsawPlacement {
   private static final Logger LOGGER = LogUtils.getLogger();
   private static final int UNSET_HEIGHT = Integer.MIN_VALUE;

   public static Optional<Structure$GenerationStub> addPieces(
      final Structure$GenerationContext context,
      final Holder<StructureTemplatePool> startPool,
      final Optional<Identifier> startJigsaw,
      final int maxDepth,
      final BlockPos position,
      final boolean doExpansionHack,
      final Optional<Heightmap$Types> projectStartToHeightmap,
      final JigsawStructure$MaxDistance maxDistanceFromCenter,
      final PoolAliasLookup poolAliasLookup,
      final DimensionPadding dimensionPadding,
      final LiquidSettings liquidSettings
   ) {
      RegistryAccess registryAccess = context.registryAccess();
      ChunkGenerator chunkGenerator = context.chunkGenerator();
      StructureTemplateManager structureTemplateManager = context.structureTemplateManager();
      LevelHeightAccessor heightAccessor = context.heightAccessor();
      WorldgenRandom random = context.random();
      Registry<StructureTemplatePool> pools = registryAccess.lookupOrThrow(Registries.TEMPLATE_POOL);
      Rotation centerRotation = Rotation.getRandom(random);
      StructureTemplatePool centerPool = startPool.unwrapKey()
         .flatMap(key -> pools.getOptional(poolAliasLookup.lookup((ResourceKey<StructureTemplatePool>)key)))
         .orElse(startPool.value());
      StructurePoolElement centerElement = centerPool.getRandomTemplate(random);
      if (centerElement == EmptyPoolElement.INSTANCE) {
         return Optional.empty();
      } else {
         BlockPos anchoredPosition;
         if (startJigsaw.isPresent()) {
            Identifier targetJigsawId = startJigsaw.get();
            Optional<BlockPos> anchor = getRandomNamedJigsaw(centerElement, targetJigsawId, position, centerRotation, structureTemplateManager, random);
            if (anchor.isEmpty()) {
               LOGGER.error(
                  "No starting jigsaw {} found in start pool {}",
                  targetJigsawId,
                  startPool.unwrapKey().map(key -> key.identifier().toString()).orElse("<unregistered>")
               );
               return Optional.empty();
            }

            anchoredPosition = anchor.get();
         } else {
            anchoredPosition = position;
         }

         Vec3i localAnchorPosition = anchoredPosition.subtract(position);
         BlockPos adjustedPosition = position.subtract(localAnchorPosition);
         PoolElementStructurePiece centerPiece = new PoolElementStructurePiece(
            structureTemplateManager,
            centerElement,
            adjustedPosition,
            centerElement.getGroundLevelDelta(),
            centerRotation,
            centerElement.getBoundingBox(structureTemplateManager, adjustedPosition, centerRotation),
            liquidSettings
         );
         BoundingBox box = centerPiece.getBoundingBox();
         int centerX = (box.maxX() + box.minX()) / 2;
         int centerZ = (box.maxZ() + box.minZ()) / 2;
         int bottomY;
         if (projectStartToHeightmap.isEmpty()) {
            bottomY = adjustedPosition.getY();
         } else {
            if (!context.couldStructureExistInColumn(centerX, centerZ, heightAccessor.getMinY(), heightAccessor.getMaxY())) {
               return Optional.empty();
            }

            bottomY = position.getY()
               + chunkGenerator.getFirstFreeHeight(centerX, centerZ, projectStartToHeightmap.get(), heightAccessor, context.randomState());
         }

         int oldAbsoluteGroundY = box.minY() + centerPiece.getGroundLevelDelta();
         centerPiece.move(0, bottomY - oldAbsoluteGroundY, 0);
         if (isStartTooCloseToWorldHeightLimits(heightAccessor, dimensionPadding, centerPiece.getBoundingBox())) {
            LOGGER.debug(
               "Center piece {} with bounding box {} does not fit dimension padding {}",
               new Object[]{centerElement, centerPiece.getBoundingBox(), dimensionPadding}
            );
            return Optional.empty();
         } else {
            int centerY = bottomY + localAnchorPosition.getY();
            return Optional.of(
               new Structure$GenerationStub(
                  new BlockPos(centerX, centerY, centerZ),
                  (Consumer<StructurePiecesBuilder>)(builder -> {
                     List<PoolElementStructurePiece> pieces = Lists.newArrayList();
                     pieces.add(centerPiece);
                     if (maxDepth > 0) {
                        AABB aabb = new AABB(
                           centerX - maxDistanceFromCenter.horizontal(),
                           Math.max(centerY - maxDistanceFromCenter.vertical(), heightAccessor.getMinY() + dimensionPadding.bottom()),
                           centerZ - maxDistanceFromCenter.horizontal(),
                           centerX + maxDistanceFromCenter.horizontal() + 1,
                           Math.min(centerY + maxDistanceFromCenter.vertical() + 1, heightAccessor.getMaxY() + 1 - dimensionPadding.top()),
                           centerZ + maxDistanceFromCenter.horizontal() + 1
                        );
                        VoxelShape shape = Shapes.join(Shapes.create(aabb), Shapes.create(AABB.of(box)), BooleanOp.ONLY_FIRST);
                        addPieces(
                           context.randomState(),
                           maxDepth,
                           doExpansionHack,
                           chunkGenerator,
                           structureTemplateManager,
                           heightAccessor,
                           random,
                           pools,
                           centerPiece,
                           pieces,
                           shape,
                           poolAliasLookup,
                           liquidSettings
                        );
                        pieces.forEach(builder::addPiece);
                     }
                  })
               )
            );
         }
      }
   }

   private static boolean isStartTooCloseToWorldHeightLimits(
      final LevelHeightAccessor heightAccessor, final DimensionPadding dimensionPadding, final BoundingBox centerPieceBb
   ) {
      if (dimensionPadding == DimensionPadding.ZERO) {
         return false;
      } else {
         int minYWithPadding = heightAccessor.getMinY() + dimensionPadding.bottom();
         int maxYWithPadding = heightAccessor.getMaxY() - dimensionPadding.top();
         return centerPieceBb.minY() < minYWithPadding || centerPieceBb.maxY() > maxYWithPadding;
      }
   }

   private static Optional<BlockPos> getRandomNamedJigsaw(
      final StructurePoolElement element,
      final Identifier targetJigsawId,
      final BlockPos position,
      final Rotation rotation,
      final StructureTemplateManager structureTemplateManager,
      final WorldgenRandom random
   ) {
      for (StructureTemplate$JigsawBlockInfo jigsaw : element.getShuffledJigsawBlocks(structureTemplateManager, position, rotation, random)) {
         if (targetJigsawId.equals(jigsaw.name())) {
            return Optional.of(jigsaw.pos());
         }
      }

      return Optional.empty();
   }

   private static void addPieces(
      final RandomState randomState,
      final int maxDepth,
      final boolean doExpansionHack,
      final ChunkGenerator chunkGenerator,
      final StructureTemplateManager structureTemplateManager,
      final LevelHeightAccessor heightAccessor,
      final RandomSource random,
      final Registry<StructureTemplatePool> pools,
      final PoolElementStructurePiece centerPiece,
      final List<PoolElementStructurePiece> pieces,
      final VoxelShape shape,
      final PoolAliasLookup poolAliasLookup,
      final LiquidSettings liquidSettings
   ) {
      JigsawPlacement$Placer placer = new JigsawPlacement$Placer(pools, maxDepth, chunkGenerator, structureTemplateManager, pieces, random);
      placer.tryPlacingChildren(centerPiece, new MutableObject(shape), 0, doExpansionHack, heightAccessor, randomState, poolAliasLookup, liquidSettings);

      while (placer.placing.hasNext()) {
         JigsawPlacement$PieceState state = (JigsawPlacement$PieceState)placer.placing.next();
         placer.tryPlacingChildren(state.piece, state.free, state.depth, doExpansionHack, heightAccessor, randomState, poolAliasLookup, liquidSettings);
      }
   }

   public static boolean generateJigsaw(
      final ServerLevel level,
      final Holder<StructureTemplatePool> pool,
      final Identifier target,
      final int maxDepth,
      final BlockPos position,
      final boolean keepJigsaws
   ) {
      ChunkGenerator generator = level.getChunkSource().getGenerator();
      StructureTemplateManager structureTemplateManager = level.getStructureTemplateManager();
      StructureManager structureManager = level.structureManager();
      RandomSource random = level.getRandom();
      RandomState randomState = level.getChunkSource().randomState();
      Structure$GenerationContext generationContext = new Structure$GenerationContext(
         level.registryAccess(),
         generator,
         generator.getBiomeSource(),
         randomState.createClimateSampler(SamplerContext.EMPTY_UNCACHED),
         randomState,
         structureTemplateManager,
         level.getSeed(),
         ChunkPos.containing(position),
         level,
         b -> true
      );
      Optional<Structure$GenerationStub> stub = addPieces(
         generationContext,
         pool,
         Optional.of(target),
         maxDepth,
         position,
         false,
         Optional.empty(),
         new JigsawStructure$MaxDistance(128),
         PoolAliasLookup.EMPTY,
         JigsawStructure.DEFAULT_DIMENSION_PADDING,
         JigsawStructure.DEFAULT_LIQUID_SETTINGS
      );
      if (stub.isPresent()) {
         StructurePiecesBuilder builder = stub.get().getPiecesBuilder();

         for (StructurePiece piece : builder.build().pieces()) {
            if (piece instanceof PoolElementStructurePiece poolPiece) {
               poolPiece.place(level, structureManager, generator, random, BoundingBox.infinite(), position, keepJigsaws);
            }
         }

         return true;
      } else {
         return false;
      }
   }
}
