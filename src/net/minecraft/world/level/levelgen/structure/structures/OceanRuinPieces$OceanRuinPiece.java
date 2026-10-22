package net.minecraft.world.level.levelgen.structure.structures;

import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos$MutableBlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.monster.zombie.Drowned;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChestBlock;
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
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockRotProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;

public class OceanRuinPieces$OceanRuinPiece extends TemplateStructurePiece {
   private final OceanRuinStructure$Type biomeType;
   private final float integrity;
   private final boolean isLarge;

   public OceanRuinPieces$OceanRuinPiece(
      final StructureTemplateManager structureTemplateManager,
      final Identifier templateLocation,
      final BlockPos position,
      final Rotation rotation,
      final float integrity,
      final OceanRuinStructure$Type biomeType,
      final boolean isLarge
   ) {
      super(
         StructurePieceType.OCEAN_RUIN,
         0,
         structureTemplateManager,
         templateLocation,
         templateLocation.toString(),
         makeSettings(rotation, integrity, biomeType),
         position
      );
      this.integrity = integrity;
      this.biomeType = biomeType;
      this.isLarge = isLarge;
   }

   private OceanRuinPieces$OceanRuinPiece(
      final StructureTemplateManager structureTemplateManager,
      final CompoundTag tag,
      final Rotation rotation,
      final float integrity,
      final OceanRuinStructure$Type biomeType,
      final boolean isLarge
   ) {
      super(StructurePieceType.OCEAN_RUIN, tag, structureTemplateManager, location -> makeSettings(rotation, integrity, biomeType));
      this.integrity = integrity;
      this.biomeType = biomeType;
      this.isLarge = isLarge;
   }

   private static StructurePlaceSettings makeSettings(final Rotation rotation, final float integrity, final OceanRuinStructure$Type biomeType) {
      StructureProcessor suspiciousBlockProcessor = biomeType == OceanRuinStructure$Type.COLD
         ? OceanRuinPieces.COLD_SUSPICIOUS_BLOCK_PROCESSOR
         : OceanRuinPieces.WARM_SUSPICIOUS_BLOCK_PROCESSOR;
      return new StructurePlaceSettings()
         .setRotation(rotation)
         .setMirror(Mirror.NONE)
         .addProcessor(new BlockRotProcessor(integrity))
         .addProcessor(BlockIgnoreProcessor.STRUCTURE_AND_AIR)
         .addProcessor(suspiciousBlockProcessor);
   }

   public static OceanRuinPieces$OceanRuinPiece create(final StructureTemplateManager structureTemplateManager, final CompoundTag tag) {
      Rotation rotation = tag.<Rotation>read("Rot", Rotation.LEGACY_CODEC).orElseThrow();
      float integrity = tag.getFloatOr("Integrity", 0.0F);
      OceanRuinStructure$Type biomeType = tag.<OceanRuinStructure$Type>read("BiomeType", OceanRuinStructure$Type.LEGACY_CODEC).orElseThrow();
      boolean isLarge = tag.getBooleanOr("IsLarge", false);
      return new OceanRuinPieces$OceanRuinPiece(structureTemplateManager, tag, rotation, integrity, biomeType, isLarge);
   }

   @Override
   protected void addAdditionalSaveData(final StructurePieceSerializationContext context, final CompoundTag tag) {
      super.addAdditionalSaveData(context, tag);
      tag.store("Rot", Rotation.LEGACY_CODEC, this.placeSettings.getRotation());
      tag.putFloat("Integrity", this.integrity);
      tag.store("BiomeType", OceanRuinStructure$Type.LEGACY_CODEC, this.biomeType);
      tag.putBoolean("IsLarge", this.isLarge);
   }

   @Override
   protected void handleDataMarker(
      final String markerId, final BlockPos position, final ServerLevelAccessor level, final RandomSource random, final BoundingBox chunkBB
   ) {
      if ("chest".equals(markerId)) {
         level.setBlock(position, Blocks.CHEST.defaultBlockState().setValue(ChestBlock.WATERLOGGED, level.getFluidState(position).is(FluidTags.WATER)), 2);
         if (level.getBlockEntity(position) instanceof ChestBlockEntity chestBlockEntity) {
            chestBlockEntity.setLootTable(this.isLarge ? BuiltInLootTables.UNDERWATER_RUIN_BIG : BuiltInLootTables.UNDERWATER_RUIN_SMALL, random.nextLong());
         }
      } else if ("drowned".equals(markerId)) {
         Drowned drowned = EntityTypes.DROWNED.create(level.getLevel(), EntitySpawnReason.STRUCTURE);
         if (drowned != null) {
            drowned.setPersistenceRequired();
            drowned.snapTo(position, 0.0F, 0.0F);
            drowned.finalizeSpawn(level, level.getCurrentDifficultyAt(position), EntitySpawnReason.STRUCTURE, null);
            level.addFreshEntityWithPassengers(drowned);
            if (position.getY() > level.getSeaLevel()) {
               level.setBlock(position, Blocks.AIR.defaultBlockState(), 2);
            } else {
               level.setBlock(position, Blocks.WATER.defaultBlockState(), 2);
            }
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
      int height = level.getHeight(Heightmap$Types.OCEAN_FLOOR_WG, this.templatePosition.getX(), this.templatePosition.getZ());
      this.templatePosition = new BlockPos(this.templatePosition.getX(), height, this.templatePosition.getZ());
      BlockPos corner = StructureTemplate.transform(
            new BlockPos(this.template.getSize().getX() - 1, 0, this.template.getSize().getZ() - 1),
            Mirror.NONE,
            this.placeSettings.getRotation(),
            BlockPos.ZERO
         )
         .offset(this.templatePosition);
      this.templatePosition = new BlockPos(this.templatePosition.getX(), this.getHeight(this.templatePosition, level, corner), this.templatePosition.getZ());
      super.postProcess(level, structureManager, generator, random, chunkBB, chunkPos, referencePos);
   }

   private int getHeight(final BlockPos pos, final BlockGetter level, final BlockPos corner) {
      int newY = pos.getY();
      int minY = 512;
      int topY = newY - 1;
      int area = 0;

      for (BlockPos p : BlockPos.betweenClosed(pos, corner)) {
         int x = p.getX();
         int z = p.getZ();
         int floorY = pos.getY() - 1;
         BlockPos$MutableBlockPos tempPos = new BlockPos$MutableBlockPos(x, floorY, z);
         BlockState tempState = level.getBlockState(tempPos);

         for (FluidState tempFluid = level.getFluidState(tempPos);
            (tempState.isAir() || tempFluid.is(FluidTags.WATER) || tempState.is(BlockTags.ICE)) && floorY > level.getMinY() + 1;
            tempFluid = level.getFluidState(tempPos)
         ) {
            tempPos.set(x, --floorY, z);
            tempState = level.getBlockState(tempPos);
         }

         minY = Math.min(minY, floorY);
         if (floorY < topY - 2) {
            area++;
         }
      }

      int width = Math.abs(pos.getX() - corner.getX());
      if (topY - minY > 2 && area > width - 2) {
         newY = minY + 1;
      }

      return newY;
   }
}
