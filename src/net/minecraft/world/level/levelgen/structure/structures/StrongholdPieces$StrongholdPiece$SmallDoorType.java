package net.minecraft.world.level.levelgen.structure.structures;

import com.mojang.serialization.Codec;
import net.minecraft.util.ExtraCodecs;

public enum StrongholdPieces$StrongholdPiece$SmallDoorType {
   OPENING,
   WOOD_DOOR,
   GRATES,
   IRON_DOOR;

   @Deprecated
   public static final Codec<StrongholdPieces$StrongholdPiece$SmallDoorType> LEGACY_CODEC = ExtraCodecs.legacyEnum(
      StrongholdPieces$StrongholdPiece$SmallDoorType::valueOf
   );
}
