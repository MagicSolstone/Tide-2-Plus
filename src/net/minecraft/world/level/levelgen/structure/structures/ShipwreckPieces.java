package net.minecraft.world.level.levelgen.structure.structures;

import java.util.Map;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Util;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootTable;

public class ShipwreckPieces {
   private static final int NUMBER_OF_BLOCKS_ALLOWED_IN_WORLD_GEN_REGION = 32;
   private static final BlockPos PIVOT = new BlockPos(4, 0, 15);
   private static final Identifier[] STRUCTURE_LOCATION_BEACHED = new Identifier[]{
      Identifier.withDefaultNamespace("shipwreck/with_mast"),
      Identifier.withDefaultNamespace("shipwreck/sideways_full"),
      Identifier.withDefaultNamespace("shipwreck/sideways_fronthalf"),
      Identifier.withDefaultNamespace("shipwreck/sideways_backhalf"),
      Identifier.withDefaultNamespace("shipwreck/rightsideup_full"),
      Identifier.withDefaultNamespace("shipwreck/rightsideup_fronthalf"),
      Identifier.withDefaultNamespace("shipwreck/rightsideup_backhalf"),
      Identifier.withDefaultNamespace("shipwreck/with_mast_degraded"),
      Identifier.withDefaultNamespace("shipwreck/rightsideup_full_degraded"),
      Identifier.withDefaultNamespace("shipwreck/rightsideup_fronthalf_degraded"),
      Identifier.withDefaultNamespace("shipwreck/rightsideup_backhalf_degraded")
   };
   private static final Identifier[] STRUCTURE_LOCATION_OCEAN = new Identifier[]{
      Identifier.withDefaultNamespace("shipwreck/with_mast"),
      Identifier.withDefaultNamespace("shipwreck/upsidedown_full"),
      Identifier.withDefaultNamespace("shipwreck/upsidedown_fronthalf"),
      Identifier.withDefaultNamespace("shipwreck/upsidedown_backhalf"),
      Identifier.withDefaultNamespace("shipwreck/sideways_full"),
      Identifier.withDefaultNamespace("shipwreck/sideways_fronthalf"),
      Identifier.withDefaultNamespace("shipwreck/sideways_backhalf"),
      Identifier.withDefaultNamespace("shipwreck/rightsideup_full"),
      Identifier.withDefaultNamespace("shipwreck/rightsideup_fronthalf"),
      Identifier.withDefaultNamespace("shipwreck/rightsideup_backhalf"),
      Identifier.withDefaultNamespace("shipwreck/with_mast_degraded"),
      Identifier.withDefaultNamespace("shipwreck/upsidedown_full_degraded"),
      Identifier.withDefaultNamespace("shipwreck/upsidedown_fronthalf_degraded"),
      Identifier.withDefaultNamespace("shipwreck/upsidedown_backhalf_degraded"),
      Identifier.withDefaultNamespace("shipwreck/sideways_full_degraded"),
      Identifier.withDefaultNamespace("shipwreck/sideways_fronthalf_degraded"),
      Identifier.withDefaultNamespace("shipwreck/sideways_backhalf_degraded"),
      Identifier.withDefaultNamespace("shipwreck/rightsideup_full_degraded"),
      Identifier.withDefaultNamespace("shipwreck/rightsideup_fronthalf_degraded"),
      Identifier.withDefaultNamespace("shipwreck/rightsideup_backhalf_degraded")
   };
   private static final Map<String, ResourceKey<LootTable>> MARKERS_TO_LOOT = Map.of(
      "map_chest", BuiltInLootTables.SHIPWRECK_MAP, "treasure_chest", BuiltInLootTables.SHIPWRECK_TREASURE, "supply_chest", BuiltInLootTables.SHIPWRECK_SUPPLY
   );

   public static ShipwreckPieces$ShipwreckPiece addRandomPiece(
      final StructureTemplateManager structureTemplateManager,
      final BlockPos position,
      final Rotation rotation,
      final StructurePiecesBuilder builder,
      final RandomSource random,
      final boolean isBeached
   ) {
      Identifier identifier = Util.getRandom(isBeached ? STRUCTURE_LOCATION_BEACHED : STRUCTURE_LOCATION_OCEAN, random);
      ShipwreckPieces$ShipwreckPiece piece = new ShipwreckPieces$ShipwreckPiece(structureTemplateManager, identifier, position, rotation, isBeached);
      builder.addPiece(piece);
      return piece;
   }
}
