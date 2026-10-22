package net.minecraft.world.level.levelgen.structure.structures;

import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import org.jspecify.annotations.Nullable;

public class StrongholdPieces$StartPiece extends StrongholdPieces$StairsDown {
   @Nullable
   public StrongholdPieces$PieceWeight previousPiece;
   @Nullable
   public StrongholdPieces$PortalRoom portalRoomPiece;
   public final List<StructurePiece> pendingChildren = Lists.newArrayList();

   public StrongholdPieces$StartPiece(final RandomSource random, final int west, final int north) {
      super(StructurePieceType.STRONGHOLD_START, 0, west, north, getRandomHorizontalDirection(random));
   }

   public StrongholdPieces$StartPiece(final CompoundTag tag) {
      super(StructurePieceType.STRONGHOLD_START, tag);
   }

   @Override
   public BlockPos getLocatorPosition() {
      return this.portalRoomPiece != null ? this.portalRoomPiece.getLocatorPosition() : super.getLocatorPosition();
   }
}
