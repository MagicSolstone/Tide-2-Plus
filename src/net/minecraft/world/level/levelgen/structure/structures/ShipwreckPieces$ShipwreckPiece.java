package net.minecraft.world.level.levelgen.structure.structures;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.RandomizableContainer;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap$Types;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.TemplateStructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import net.minecraft.world.level.storage.loot.LootTable;

public class ShipwreckPieces$ShipwreckPiece extends TemplateStructurePiece {
   private final boolean isBeached;
   private boolean heightAdjusted;

   public ShipwreckPieces$ShipwreckPiece(
      final StructureTemplateManager structureTemplateManager,
      final Identifier templateLocation,
      final BlockPos position,
      final Rotation rotation,
      final boolean isBeached
   ) {
      super(StructurePieceType.SHIPWRECK_PIECE, 0, structureTemplateManager, templateLocation, templateLocation.toString(), makeSettings(rotation), position);
      this.isBeached = isBeached;
   }

   public ShipwreckPieces$ShipwreckPiece(final StructureTemplateManager structureTemplateManager, final CompoundTag tag) {
      super(
         StructurePieceType.SHIPWRECK_PIECE,
         tag,
         structureTemplateManager,
         location -> makeSettings(tag.<Rotation>read("Rot", Rotation.LEGACY_CODEC).orElseThrow())
      );
      this.isBeached = tag.getBooleanOr("isBeached", false);
      this.heightAdjusted = tag.getBooleanOr("height_adjusted", false);
   }

   @Override
   protected void addAdditionalSaveData(final StructurePieceSerializationContext context, final CompoundTag tag) {
      super.addAdditionalSaveData(context, tag);
      tag.putBoolean("isBeached", this.isBeached);
      tag.store("Rot", Rotation.LEGACY_CODEC, this.placeSettings.getRotation());
      tag.putBoolean("height_adjusted", this.heightAdjusted);
   }

   private static StructurePlaceSettings makeSettings(final Rotation rotation) {
      return new StructurePlaceSettings()
         .setRotation(rotation)
         .setMirror(Mirror.NONE)
         .setRotationPivot(ShipwreckPieces.PIVOT)
         .addProcessor(BlockIgnoreProcessor.STRUCTURE_AND_AIR);
   }

   @Override
   protected void handleDataMarker(
      final String markerId, final BlockPos position, final ServerLevelAccessor level, final RandomSource random, final BoundingBox chunkBB
   ) {
      ResourceKey<LootTable> lootTable = ShipwreckPieces.MARKERS_TO_LOOT.get(markerId);
      if (lootTable != null) {
         RandomizableContainer.setBlockEntityLootTable(level, random, position.below(), lootTable);
      }
   }

   @Override
   public void postProcess(
      final WorldGenLevel level,
      final StructureManager structureManager,
      final ChunkGenerator generator,
      final RandomSource random,
      final BoundingBox chunkBB,
      final ChunkPos chunkPos,
      final BlockPos referencePos
   ) {
      if (!this.heightAdjusted && !this.isTooBigToFitInWorldGenRegion()) {
         int minY = level.getMaxY() + 1;
         int mean = 0;
         Vec3i templateSize = this.template.getSize();
         Heightmap$Types heightmapType = this.isBeached ? Heightmap$Types.WORLD_SURFACE_WG : Heightmap$Types.OCEAN_FLOOR_WG;
         int baseSize = templateSize.getX() * templateSize.getZ();
         if (baseSize == 0) {
            mean = level.getHeight(heightmapType, this.templatePosition.getX(), this.templatePosition.getZ());
         } else {
            BlockPos corner = this.templatePosition.offset(templateSize.getX() - 1, 0, templateSize.getZ() - 1);

            for (BlockPos p : BlockPos.betweenClosed(this.templatePosition, corner)) {
               int heightmap = level.getHeight(heightmapType, p.getX(), p.getZ());
               mean += heightmap;
               minY = Math.min(minY, heightmap);
            }

            mean /= baseSize;
         }

         this.adjustPositionHeight(this.isBeached ? this.calculateBeachedPosition(minY, random) : mean);
         super.postProcess(level, structureManager, generator, random, chunkBB, chunkPos, referencePos);
      } else {
         super.postProcess(level, structureManager, generator, random, chunkBB, chunkPos, referencePos);
      }
   }

   public boolean isTooBigToFitInWorldGenRegion() {
      Vec3i size = this.template.getSize();
      return size.getX() > 32 || size.getY() > 32;
   }

   public int calculateBeachedPosition(final int minY, final RandomSource random) {
      return minY - this.template.getSize().getY() / 2 - random.nextInt(3);
   }

   public void adjustPositionHeight(final int newHeight) {
      this.heightAdjusted = true;
      this.templatePosition = new BlockPos(this.templatePosition.getX(), newHeight, this.templatePosition.getZ());
   }
}
