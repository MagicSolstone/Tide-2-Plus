package net.minecraft.world.level.levelgen.structure.structures;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.Identifier;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.TemplateStructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;

public class WoodlandMansionPieces$WoodlandMansionPiece extends TemplateStructurePiece {
   public WoodlandMansionPieces$WoodlandMansionPiece(
      final StructureTemplateManager structureTemplateManager, final String templateName, final BlockPos position, final Rotation rotation
   ) {
      this(structureTemplateManager, templateName, position, rotation, Mirror.NONE);
   }

   public WoodlandMansionPieces$WoodlandMansionPiece(
      final StructureTemplateManager structureTemplateManager, final String templateName, final BlockPos position, final Rotation rotation, final Mirror mirror
   ) {
      super(
         StructurePieceType.WOODLAND_MANSION_PIECE,
         0,
         structureTemplateManager,
         makeLocation(templateName),
         templateName,
         makeSettings(mirror, rotation),
         position
      );
   }

   public WoodlandMansionPieces$WoodlandMansionPiece(final StructureTemplateManager structureTemplateManager, final CompoundTag tag) {
      super(
         StructurePieceType.WOODLAND_MANSION_PIECE,
         tag,
         structureTemplateManager,
         location -> makeSettings(tag.<Mirror>read("Mi", Mirror.LEGACY_CODEC).orElseThrow(), tag.<Rotation>read("Rot", Rotation.LEGACY_CODEC).orElseThrow())
      );
   }

   @Override
   protected Identifier makeTemplateLocation() {
      return makeLocation(this.templateName);
   }

   private static Identifier makeLocation(final String templateName) {
      return Identifier.withDefaultNamespace("woodland_mansion/" + templateName);
   }

   private static StructurePlaceSettings makeSettings(final Mirror mirror, final Rotation rotation) {
      return new StructurePlaceSettings().setIgnoreEntities(true).setRotation(rotation).setMirror(mirror).addProcessor(BlockIgnoreProcessor.STRUCTURE_BLOCK);
   }

   @Override
   protected void addAdditionalSaveData(final StructurePieceSerializationContext context, final CompoundTag tag) {
      super.addAdditionalSaveData(context, tag);
      tag.store("Rot", Rotation.LEGACY_CODEC, this.placeSettings.getRotation());
      tag.store("Mi", Mirror.LEGACY_CODEC, this.placeSettings.getMirror());
   }

   @Override
   protected void handleDataMarker(
      final String markerId, final BlockPos position, final ServerLevelAccessor level, final RandomSource random, final BoundingBox chunkBB
   ) {
      if (markerId.startsWith("Chest")) {
         Rotation rot = this.placeSettings.getRotation();
         BlockState chestState = Blocks.CHEST.defaultBlockState();
         if ("ChestWest".equals(markerId)) {
            chestState = chestState.setValue(ChestBlock.FACING, rot.rotate(Direction.WEST));
         } else if ("ChestEast".equals(markerId)) {
            chestState = chestState.setValue(ChestBlock.FACING, rot.rotate(Direction.EAST));
         } else if ("ChestSouth".equals(markerId)) {
            chestState = chestState.setValue(ChestBlock.FACING, rot.rotate(Direction.SOUTH));
         } else if ("ChestNorth".equals(markerId)) {
            chestState = chestState.setValue(ChestBlock.FACING, rot.rotate(Direction.NORTH));
         }

         this.createChest(level, chunkBB, random, position, BuiltInLootTables.WOODLAND_MANSION, chestState);
      } else {
         List<Mob> mobs = new ArrayList<>();
         switch (markerId) {
            case "Mage":
               mobs.add(EntityTypes.EVOKER.create(level.getLevel(), EntitySpawnReason.STRUCTURE));
               break;
            case "Warrior":
               mobs.add(EntityTypes.VINDICATOR.create(level.getLevel(), EntitySpawnReason.STRUCTURE));
               break;
            case "Group of Allays":
               int numberOfAllays = level.getRandom().nextInt(3) + 1;

               for (int i = 0; i < numberOfAllays; i++) {
                  mobs.add(EntityTypes.ALLAY.create(level.getLevel(), EntitySpawnReason.STRUCTURE));
               }
               break;
            default:
               return;
         }

         for (Mob mob : mobs) {
            if (mob != null) {
               mob.setPersistenceRequired();
               mob.snapTo(position, 0.0F, 0.0F);
               mob.finalizeSpawn(level, level.getCurrentDifficultyAt(mob.blockPosition()), EntitySpawnReason.STRUCTURE, null);
               level.addFreshEntityWithPassengers(mob);
               level.setBlock(position, Blocks.AIR.defaultBlockState(), 2);
            }
         }
      }
   }
}
