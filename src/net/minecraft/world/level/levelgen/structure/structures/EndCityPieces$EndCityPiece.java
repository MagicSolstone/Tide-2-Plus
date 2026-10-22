package net.minecraft.world.level.levelgen.structure.structures;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.Identifier;
import net.minecraft.util.RandomSource;
import net.minecraft.world.RandomizableContainer;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.entity.monster.Shulker;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.TemplateStructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;

public class EndCityPieces$EndCityPiece extends TemplateStructurePiece {
   public EndCityPieces$EndCityPiece(
      final StructureTemplateManager structureTemplateManager,
      final String templateName,
      final BlockPos position,
      final Rotation rotation,
      final boolean overwrite
   ) {
      super(
         StructurePieceType.END_CITY_PIECE,
         0,
         structureTemplateManager,
         makeIdentifier(templateName),
         templateName,
         makeSettings(overwrite, rotation),
         position
      );
   }

   public EndCityPieces$EndCityPiece(final StructureTemplateManager structureTemplateManager, final CompoundTag tag) {
      super(
         StructurePieceType.END_CITY_PIECE,
         tag,
         structureTemplateManager,
         location -> makeSettings(tag.getBooleanOr("OW", false), tag.<Rotation>read("Rot", Rotation.LEGACY_CODEC).orElseThrow())
      );
   }

   private static StructurePlaceSettings makeSettings(final boolean overwrite, final Rotation rotation) {
      BlockIgnoreProcessor processor = overwrite ? BlockIgnoreProcessor.STRUCTURE_BLOCK : BlockIgnoreProcessor.STRUCTURE_AND_AIR;
      return new StructurePlaceSettings().setIgnoreEntities(true).addProcessor(processor).setRotation(rotation);
   }

   @Override
   protected Identifier makeTemplateLocation() {
      return makeIdentifier(this.templateName);
   }

   private static Identifier makeIdentifier(final String templateName) {
      return Identifier.withDefaultNamespace("end_city/" + templateName);
   }

   @Override
   protected void addAdditionalSaveData(final StructurePieceSerializationContext context, final CompoundTag tag) {
      super.addAdditionalSaveData(context, tag);
      tag.store("Rot", Rotation.LEGACY_CODEC, this.placeSettings.getRotation());
      tag.putBoolean("OW", this.placeSettings.getProcessors().get(0) == BlockIgnoreProcessor.STRUCTURE_BLOCK);
   }

   @Override
   protected void handleDataMarker(
      final String markerId, final BlockPos position, final ServerLevelAccessor level, final RandomSource random, final BoundingBox chunkBB
   ) {
      if (markerId.startsWith("Chest")) {
         BlockPos chestPosition = position.below();
         if (chunkBB.isInside(chestPosition)) {
            RandomizableContainer.setBlockEntityLootTable(level, random, chestPosition, BuiltInLootTables.END_CITY_TREASURE);
         }
      } else if (chunkBB.isInside(position) && Level.isInSpawnableBounds(position)) {
         if (markerId.startsWith("Sentry")) {
            Shulker sentry = EntityTypes.SHULKER.create(level.getLevel(), EntitySpawnReason.STRUCTURE);
            if (sentry != null) {
               sentry.setPos(position.getX() + 0.5, position.getY(), position.getZ() + 0.5);
               level.addFreshEntity(sentry);
            }
         } else if (markerId.startsWith("Elytra")) {
            ItemFrame itemFrame = new ItemFrame(level.getLevel(), position, this.placeSettings.getRotation().rotate(Direction.SOUTH));
            itemFrame.setItem(new ItemStack(Items.ELYTRA), false);
            level.addFreshEntity(itemFrame);
         }
      }
   }
}
