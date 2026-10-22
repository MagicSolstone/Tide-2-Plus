package net.minecraft.world.level.levelgen.structure.structures;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.Identifier;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap$Types;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.TemplateStructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.LiquidSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;

public class IglooPieces$IglooPiece extends TemplateStructurePiece {
   public IglooPieces$IglooPiece(
      final StructureTemplateManager structureTemplateManager,
      final Identifier templateLocation,
      final BlockPos position,
      final Rotation rotation,
      final int depth
   ) {
      super(
         StructurePieceType.IGLOO,
         0,
         structureTemplateManager,
         templateLocation,
         templateLocation.toString(),
         makeSettings(rotation, templateLocation),
         makePosition(templateLocation, position, depth)
      );
   }

   public IglooPieces$IglooPiece(final StructureTemplateManager structureTemplateManager, final CompoundTag tag) {
      super(
         StructurePieceType.IGLOO,
         tag,
         structureTemplateManager,
         location -> makeSettings(tag.<Rotation>read("Rot", Rotation.LEGACY_CODEC).orElseThrow(), location)
      );
   }

   private static StructurePlaceSettings makeSettings(final Rotation rotation, final Identifier templateLocation) {
      return new StructurePlaceSettings()
         .setRotation(rotation)
         .setMirror(Mirror.NONE)
         .setRotationPivot(IglooPieces.PIVOTS.get(templateLocation))
         .addProcessor(BlockIgnoreProcessor.STRUCTURE_BLOCK)
         .setLiquidSettings(LiquidSettings.IGNORE_WATERLOGGING);
   }

   private static BlockPos makePosition(final Identifier templateLocation, final BlockPos position, final int depth) {
      return position.offset(IglooPieces.OFFSETS.get(templateLocation)).below(depth);
   }

   @Override
   protected void addAdditionalSaveData(final StructurePieceSerializationContext context, final CompoundTag tag) {
      super.addAdditionalSaveData(context, tag);
      tag.store("Rot", Rotation.LEGACY_CODEC, this.placeSettings.getRotation());
   }

   @Override
   protected void handleDataMarker(
      final String markerId, final BlockPos position, final ServerLevelAccessor level, final RandomSource random, final BoundingBox chunkBB
   ) {
      if ("chest".equals(markerId)) {
         level.setBlockAndUpdate(position, Blocks.AIR.defaultBlockState());
         if (level.getBlockEntity(position.below()) instanceof ChestBlockEntity chestBlockEntity) {
            chestBlockEntity.setLootTable(BuiltInLootTables.IGLOO_CHEST, random.nextLong());
         }
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
      Identifier templateLocation = Identifier.parse(this.templateName);
      StructurePlaceSettings settings = makeSettings(this.placeSettings.getRotation(), templateLocation);
      BlockPos offset = IglooPieces.OFFSETS.get(templateLocation);
      BlockPos entrancePos = this.templatePosition
         .offset(StructureTemplate.calculateRelativePosition(settings, new BlockPos(3 - offset.getX(), 0, -offset.getZ())));
      int height = level.getHeight(Heightmap$Types.WORLD_SURFACE_WG, entrancePos.getX(), entrancePos.getZ());
      BlockPos oldTemplatePos = this.templatePosition;
      this.templatePosition = this.templatePosition.offset(0, height - 90 - 1, 0);
      super.postProcess(level, structureManager, generator, random, chunkBB, chunkPos, referencePos);
      if (templateLocation.equals(IglooPieces.STRUCTURE_LOCATION_IGLOO)) {
         BlockPos trapDoorPos = this.templatePosition.offset(StructureTemplate.calculateRelativePosition(settings, new BlockPos(3, 0, 5)));
         BlockState belowState = level.getBlockState(trapDoorPos.below());
         if (!belowState.isAir() && !belowState.is(Blocks.LADDER)) {
            level.setBlockAndUpdate(trapDoorPos, Blocks.SNOW_BLOCK.defaultBlockState());
         }
      }

      this.templatePosition = oldTemplatePos;
   }
}
