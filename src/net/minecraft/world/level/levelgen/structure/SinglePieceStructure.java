package net.minecraft.world.level.levelgen.structure;

import java.util.Optional;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.levelgen.Heightmap$Types;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;

public abstract class SinglePieceStructure extends Structure {
   private final SinglePieceStructure$PieceConstructor constructor;
   private final int width;
   private final int depth;

   protected SinglePieceStructure(
      final SinglePieceStructure$PieceConstructor constructor, final int width, final int depth, final Structure$StructureSettings settings
   ) {
      super(settings);
      this.constructor = constructor;
      this.width = width;
      this.depth = depth;
   }

   @Override
   public Optional<Structure$GenerationStub> findGenerationPoint(final Structure$GenerationContext context) {
      if (!context.couldValidBiomeExistOnTopOfChunkCenter()) {
         return Optional.empty();
      } else {
         return getLowestY(context, this.width, this.depth) < context.chunkGenerator().getSeaLevel()
            ? Optional.empty()
            : onTopOfChunkCenterWithoutBiomeCheck(context, Heightmap$Types.WORLD_SURFACE_WG, builder -> this.generatePieces(builder, context));
      }
   }

   private void generatePieces(final StructurePiecesBuilder builder, final Structure$GenerationContext context) {
      ChunkPos chunkPos = context.chunkPos();
      builder.addPiece(this.constructor.construct(context.random(), chunkPos.getMinBlockX(), chunkPos.getMinBlockZ()));
   }
}
