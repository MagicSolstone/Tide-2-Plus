package net.minecraft.world.level.levelgen.structure.pools;

import com.google.common.collect.Lists;
import java.util.List;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.data.worldgen.Pools;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.util.SequencedPriorityIterator;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.block.JigsawBlock;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap$Types;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.PoolElementStructurePiece;
import net.minecraft.world.level.levelgen.structure.pools.alias.PoolAliasLookup;
import net.minecraft.world.level.levelgen.structure.templatesystem.LiquidSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate$JigsawBlockInfo;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.apache.commons.lang3.mutable.MutableObject;

final class JigsawPlacement$Placer {
   private final Registry<StructureTemplatePool> pools;
   private final int maxDepth;
   private final ChunkGenerator chunkGenerator;
   private final StructureTemplateManager structureTemplateManager;
   private final List<? super PoolElementStructurePiece> pieces;
   private final RandomSource random;
   private final SequencedPriorityIterator<JigsawPlacement$PieceState> placing = new SequencedPriorityIterator<>();

   private JigsawPlacement$Placer(
      final Registry<StructureTemplatePool> pools,
      final int maxDepth,
      final ChunkGenerator chunkGenerator,
      final StructureTemplateManager structureTemplateManager,
      final List<? super PoolElementStructurePiece> pieces,
      final RandomSource random
   ) {
      this.pools = pools;
      this.maxDepth = maxDepth;
      this.chunkGenerator = chunkGenerator;
      this.structureTemplateManager = structureTemplateManager;
      this.pieces = pieces;
      this.random = random;
   }

   private void tryPlacingChildren(
      final PoolElementStructurePiece sourcePiece,
      final MutableObject<VoxelShape> contextFree,
      final int depth,
      final boolean doExpansionHack,
      final LevelHeightAccessor heightAccessor,
      final RandomState randomState,
      final PoolAliasLookup poolAliasLookup,
      final LiquidSettings liquidSettings
   ) {
      StructurePoolElement sourceElement = sourcePiece.getElement();
      BlockPos sourceBoxPosition = sourcePiece.getPosition();
      Rotation sourceRotation = sourcePiece.getRotation();
      StructureTemplatePool$Projection sourceProjection = sourceElement.getProjection();
      boolean sourceRigid = sourceProjection == StructureTemplatePool$Projection.RIGID;
      MutableObject<VoxelShape> sourceFree = new MutableObject();
      BoundingBox sourceBB = sourcePiece.getBoundingBox();
      int sourceBoxY = sourceBB.minY();

      label129:
      for (StructureTemplate$JigsawBlockInfo sourceJigsaw : sourceElement.getShuffledJigsawBlocks(
         this.structureTemplateManager, sourceBoxPosition, sourceRotation, this.random
      )) {
         Direction sourceDirection = JigsawBlock.getFrontFacing(sourceJigsaw.state());
         BlockPos sourceJigsawPos = sourceJigsaw.pos();
         BlockPos targetJigsawPos = sourceJigsawPos.relative(sourceDirection);
         int sourceJigsawLocalY = sourceJigsawPos.getY() - sourceBoxY;
         int sourceJigsawBaseHeight = Integer.MIN_VALUE;
         ResourceKey<StructureTemplatePool> poolName = poolAliasLookup.lookup(sourceJigsaw.pool());
         Optional<? extends Holder<StructureTemplatePool>> maybeTargetPool = this.pools.get(poolName);
         if (maybeTargetPool.isEmpty()) {
            JigsawPlacement.LOGGER.warn("Empty or non-existent pool: {}", poolName.identifier());
         } else {
            Holder<StructureTemplatePool> targetPool = (Holder<StructureTemplatePool>)maybeTargetPool.get();
            if (targetPool.value().size() == 0 && !targetPool.is(Pools.EMPTY)) {
               JigsawPlacement.LOGGER.warn("Empty or non-existent pool: {}", poolName.identifier());
            } else {
               Holder<StructureTemplatePool> fallback = targetPool.value().getFallback();
               if (fallback.value().size() == 0 && !fallback.is(Pools.EMPTY)) {
                  JigsawPlacement.LOGGER
                     .warn("Empty or non-existent fallback pool: {}", fallback.unwrapKey().map(e -> e.identifier().toString()).orElse("<unregistered>"));
               } else {
                  boolean attachInsideSource = sourceBB.isInside(targetJigsawPos);
                  MutableObject<VoxelShape> childrenFree;
                  if (attachInsideSource) {
                     childrenFree = sourceFree;
                     if (sourceFree.get() == null) {
                        sourceFree.setValue(Shapes.create(AABB.of(sourceBB)));
                     }
                  } else {
                     childrenFree = contextFree;
                  }

                  List<StructurePoolElement> targetPieces = Lists.newArrayList();
                  if (depth != this.maxDepth) {
                     targetPieces.addAll(targetPool.value().getShuffledTemplates(this.random));
                  }

                  targetPieces.addAll(fallback.value().getShuffledTemplates(this.random));
                  int placementPriority = sourceJigsaw.placementPriority();

                  for (StructurePoolElement targetElement : targetPieces) {
                     if (targetElement == EmptyPoolElement.INSTANCE) {
                        break;
                     }

                     for (Rotation targetRotation : Rotation.getShuffled(this.random)) {
                        List<StructureTemplate$JigsawBlockInfo> targetJigsaws = targetElement.getShuffledJigsawBlocks(
                           this.structureTemplateManager, BlockPos.ZERO, targetRotation, this.random
                        );
                        BoundingBox hackBox = targetElement.getBoundingBox(this.structureTemplateManager, BlockPos.ZERO, targetRotation);
                        int expandTo;
                        if (doExpansionHack && hackBox.getYSpan() <= 16) {
                           expandTo = targetJigsaws.stream().mapToInt(targetJigsawx -> {
                              if (!hackBox.isInside(targetJigsawx.pos().relative(JigsawBlock.getFrontFacing(targetJigsawx.state())))) {
                                 return 0;
                              } else {
                                 ResourceKey<StructureTemplatePool> childPoolName = poolAliasLookup.lookup(targetJigsawx.pool());
                                 Optional<? extends Holder<StructureTemplatePool>> childPool = this.pools.get(childPoolName);
                                 Optional<Holder<StructureTemplatePool>> childFallbackPool = childPool.map(p -> p.value().getFallback());
                                 int childPoolSize = childPool.<Integer>map(p -> p.value().getMaxSize(this.structureTemplateManager)).orElse(0);
                                 int childFallbackSize = childFallbackPool.<Integer>map(p -> p.value().getMaxSize(this.structureTemplateManager)).orElse(0);
                                 return Math.max(childPoolSize, childFallbackSize);
                              }
                           }).max().orElse(0);
                        } else {
                           expandTo = 0;
                        }

                        for (StructureTemplate$JigsawBlockInfo targetJigsaw : targetJigsaws) {
                           if (JigsawBlock.canAttach(sourceJigsaw, targetJigsaw)) {
                              BlockPos targetJigsawLocalPos = targetJigsaw.pos();
                              BlockPos rawTargetBoxPos = targetJigsawPos.subtract(targetJigsawLocalPos);
                              BoundingBox rawTargetBB = targetElement.getBoundingBox(this.structureTemplateManager, rawTargetBoxPos, targetRotation);
                              int rawTargetY = rawTargetBB.minY();
                              StructureTemplatePool$Projection targetProjection = targetElement.getProjection();
                              boolean targetRigid = targetProjection == StructureTemplatePool$Projection.RIGID;
                              int targetJigsawLocalY = targetJigsawLocalPos.getY();
                              int deltaY = sourceJigsawLocalY - targetJigsawLocalY + JigsawBlock.getFrontFacing(sourceJigsaw.state()).getStepY();
                              int targetBoxY;
                              if (sourceRigid && targetRigid) {
                                 targetBoxY = sourceBoxY + deltaY;
                              } else {
                                 if (sourceJigsawBaseHeight == Integer.MIN_VALUE) {
                                    sourceJigsawBaseHeight = this.chunkGenerator
                                       .getFirstFreeHeight(
                                          sourceJigsawPos.getX(), sourceJigsawPos.getZ(), Heightmap$Types.WORLD_SURFACE_WG, heightAccessor, randomState
                                       );
                                 }

                                 targetBoxY = sourceJigsawBaseHeight - targetJigsawLocalY;
                              }

                              int yOffset = targetBoxY - rawTargetY;
                              BoundingBox targetBB = rawTargetBB.moved(0, yOffset, 0);
                              BlockPos targetBoxPosition = rawTargetBoxPos.offset(0, yOffset, 0);
                              if (expandTo > 0) {
                                 int newSize = Math.max(expandTo + 1, targetBB.maxY() - targetBB.minY());
                                 targetBB.encapsulate(new BlockPos(targetBB.minX(), targetBB.minY() + newSize, targetBB.minZ()));
                              }

                              if (!Shapes.joinIsNotEmpty((VoxelShape)childrenFree.get(), Shapes.create(AABB.of(targetBB).deflate(0.25)), BooleanOp.ONLY_SECOND)
                                 )
                               {
                                 childrenFree.setValue(
                                    Shapes.joinUnoptimized((VoxelShape)childrenFree.get(), Shapes.create(AABB.of(targetBB)), BooleanOp.ONLY_FIRST)
                                 );
                                 int sourceGroundLevelDelta = sourcePiece.getGroundLevelDelta();
                                 int targetGroundLevelDelta;
                                 if (targetRigid) {
                                    targetGroundLevelDelta = sourceGroundLevelDelta - deltaY;
                                 } else {
                                    targetGroundLevelDelta = targetElement.getGroundLevelDelta();
                                 }

                                 PoolElementStructurePiece targetPiece = new PoolElementStructurePiece(
                                    this.structureTemplateManager,
                                    targetElement,
                                    targetBoxPosition,
                                    targetGroundLevelDelta,
                                    targetRotation,
                                    targetBB,
                                    liquidSettings
                                 );
                                 int junctionY;
                                 if (sourceRigid) {
                                    junctionY = sourceBoxY + sourceJigsawLocalY;
                                 } else if (targetRigid) {
                                    junctionY = targetBoxY + targetJigsawLocalY;
                                 } else {
                                    if (sourceJigsawBaseHeight == Integer.MIN_VALUE) {
                                       sourceJigsawBaseHeight = this.chunkGenerator
                                          .getFirstFreeHeight(
                                             sourceJigsawPos.getX(), sourceJigsawPos.getZ(), Heightmap$Types.WORLD_SURFACE_WG, heightAccessor, randomState
                                          );
                                    }

                                    junctionY = sourceJigsawBaseHeight + deltaY / 2;
                                 }

                                 sourcePiece.addJunction(
                                    new JigsawJunction(
                                       targetJigsawPos.getX(),
                                       junctionY - sourceJigsawLocalY + sourceGroundLevelDelta,
                                       targetJigsawPos.getZ(),
                                       deltaY,
                                       targetProjection
                                    )
                                 );
                                 targetPiece.addJunction(
                                    new JigsawJunction(
                                       sourceJigsawPos.getX(),
                                       junctionY - targetJigsawLocalY + targetGroundLevelDelta,
                                       sourceJigsawPos.getZ(),
                                       -deltaY,
                                       sourceProjection
                                    )
                                 );
                                 this.pieces.add(targetPiece);
                                 if (depth + 1 <= this.maxDepth) {
                                    JigsawPlacement$PieceState state = new JigsawPlacement$PieceState(targetPiece, childrenFree, depth + 1);
                                    this.placing.add(state, placementPriority);
                                 }
                                 continue label129;
                              }
                           }
                        }
                     }
                  }
               }
            }
         }
      }
   }
}
