package net.minecraft.world.level.levelgen.structure.structures;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import org.jspecify.annotations.Nullable;

public class NetherFortressPieces$StartPiece extends NetherFortressPieces$BridgeCrossing {
   @Nullable
   private NetherFortressPieces$PieceWeight previousPiece;
   private final List<NetherFortressPieces$PieceWeight> availableBridgePieces = new ArrayList<>();
   private final List<NetherFortressPieces$PieceWeight> availableCastlePieces = new ArrayList<>();
   public final List<StructurePiece> pendingChildren = Lists.newArrayList();

   public NetherFortressPieces$StartPiece(final RandomSource random, final int west, final int north) {
      super(west, north, getRandomHorizontalDirection(random));

      for (NetherFortressPieces$PieceWeight piece : NetherFortressPieces.BRIDGE_PIECE_WEIGHTS) {
         piece.placeCount = 0;
         this.availableBridgePieces.add(piece);
      }

      for (NetherFortressPieces$PieceWeight piece : NetherFortressPieces.CASTLE_PIECE_WEIGHTS) {
         piece.placeCount = 0;
         this.availableCastlePieces.add(piece);
      }
   }

   public NetherFortressPieces$StartPiece(final CompoundTag tag) {
      super(StructurePieceType.NETHER_FORTRESS_START, tag);
   }
}
