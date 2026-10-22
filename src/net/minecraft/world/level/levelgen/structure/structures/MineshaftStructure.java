package net.minecraft.world.level.levelgen.structure.structures;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap$Types;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.Structure$GenerationContext;
import net.minecraft.world.level.levelgen.structure.Structure$GenerationStub;
import net.minecraft.world.level.levelgen.structure.Structure$StructureSettings;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;

public class MineshaftStructure extends Structure {
   public static final MapCodec<MineshaftStructure> CODEC = RecordCodecBuilder.mapCodec(
      i -> i.group(settingsCodec(i), MineshaftStructure$Type.CODEC.fieldOf("mineshaft_type").forGetter(c -> c.type)).apply(i, MineshaftStructure::new)
   );
   private final MineshaftStructure$Type type;

   public MineshaftStructure(final Structure$StructureSettings settings, final MineshaftStructure$Type type) {
      super(settings);
      this.type = type;
   }

   @Override
   public Optional<Structure$GenerationStub> findGenerationPoint(final Structure$GenerationContext context) {
      context.random().nextDouble();
      ChunkPos chunkPos = context.chunkPos();
      BlockPos startPos = new BlockPos(chunkPos.getMiddleBlockX(), 50, chunkPos.getMinBlockZ());
      StructurePiecesBuilder mineshaftPiecesBuilder = new StructurePiecesBuilder();
      int yOffset = this.generatePiecesAndAdjust(mineshaftPiecesBuilder, context);
      return Optional.of(new Structure$GenerationStub(startPos.offset(0, yOffset, 0), Either.right(mineshaftPiecesBuilder)));
   }

   private int generatePiecesAndAdjust(final StructurePiecesBuilder builder, final Structure$GenerationContext context) {
      ChunkPos chunkPos = context.chunkPos();
      WorldgenRandom random = context.random();
      ChunkGenerator chunkGenerator = context.chunkGenerator();
      MineshaftPieces$MineShaftRoom mineShaftRoom = new MineshaftPieces$MineShaftRoom(0, random, chunkPos.getBlockX(2), chunkPos.getBlockZ(2), this.type);
      builder.addPiece(mineShaftRoom);
      mineShaftRoom.addChildren(mineShaftRoom, builder, random);
      int seaLevel = chunkGenerator.getSeaLevel();
      if (this.type == MineshaftStructure$Type.MESA) {
         BlockPos center = builder.getBoundingBox().getCenter();
         int surfaceHeight = chunkGenerator.getBaseHeight(
            center.getX(), center.getZ(), Heightmap$Types.WORLD_SURFACE_WG, context.heightAccessor(), context.randomState()
         );
         int targetYForCenter = surfaceHeight <= seaLevel ? seaLevel : Mth.randomBetweenInclusive(random, seaLevel, surfaceHeight);
         int dy = targetYForCenter - center.getY();
         builder.offsetPiecesVertically(dy);
         return dy;
      } else {
         return builder.moveBelowSeaLevel(seaLevel, chunkGenerator.getMinY(), random, 10);
      }
   }

   @Override
   public StructureType<?> type() {
      return StructureType.MINESHAFT;
   }
}
