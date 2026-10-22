package net.minecraft.world.level.levelgen.structure;

import com.mojang.datafixers.util.Either;
import java.util.function.Consumer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;

public record Structure$GenerationStub(BlockPos position, Either<Consumer<StructurePiecesBuilder>, StructurePiecesBuilder> generator) {
   public Structure$GenerationStub(final BlockPos position, final Consumer<StructurePiecesBuilder> generator) {
      this(position, Either.left(generator));
   }

   public StructurePiecesBuilder getPiecesBuilder() {
      return (StructurePiecesBuilder)this.generator.map(pieceGenerator -> {
         StructurePiecesBuilder newBuilder = new StructurePiecesBuilder();
         pieceGenerator.accept(newBuilder);
         return newBuilder;
      }, previousBuilder -> previousBuilder);
   }
}
