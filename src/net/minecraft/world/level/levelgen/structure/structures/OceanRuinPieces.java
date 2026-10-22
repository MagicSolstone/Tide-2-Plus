package net.minecraft.world.level.levelgen.structure.structures;

import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Util;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
import net.minecraft.world.level.levelgen.structure.templatesystem.AlwaysTrueTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockMatchTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.CappedProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.PosAlwaysTrueTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.ProcessorRule;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import net.minecraft.world.level.levelgen.structure.templatesystem.rule.blockentity.AppendLoot;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootTable;

public class OceanRuinPieces {
   private static final StructureProcessor WARM_SUSPICIOUS_BLOCK_PROCESSOR = archyRuleProcessor(
      Blocks.SAND, Blocks.SUSPICIOUS_SAND, BuiltInLootTables.OCEAN_RUIN_WARM_ARCHAEOLOGY
   );
   private static final StructureProcessor COLD_SUSPICIOUS_BLOCK_PROCESSOR = archyRuleProcessor(
      Blocks.GRAVEL, Blocks.SUSPICIOUS_GRAVEL, BuiltInLootTables.OCEAN_RUIN_COLD_ARCHAEOLOGY
   );
   private static final Identifier[] WARM_RUINS = new Identifier[]{
      Identifier.withDefaultNamespace("underwater_ruin/warm_1"),
      Identifier.withDefaultNamespace("underwater_ruin/warm_2"),
      Identifier.withDefaultNamespace("underwater_ruin/warm_3"),
      Identifier.withDefaultNamespace("underwater_ruin/warm_4"),
      Identifier.withDefaultNamespace("underwater_ruin/warm_5"),
      Identifier.withDefaultNamespace("underwater_ruin/warm_6"),
      Identifier.withDefaultNamespace("underwater_ruin/warm_7"),
      Identifier.withDefaultNamespace("underwater_ruin/warm_8")
   };
   private static final Identifier[] RUINS_BRICK = new Identifier[]{
      Identifier.withDefaultNamespace("underwater_ruin/brick_1"),
      Identifier.withDefaultNamespace("underwater_ruin/brick_2"),
      Identifier.withDefaultNamespace("underwater_ruin/brick_3"),
      Identifier.withDefaultNamespace("underwater_ruin/brick_4"),
      Identifier.withDefaultNamespace("underwater_ruin/brick_5"),
      Identifier.withDefaultNamespace("underwater_ruin/brick_6"),
      Identifier.withDefaultNamespace("underwater_ruin/brick_7"),
      Identifier.withDefaultNamespace("underwater_ruin/brick_8")
   };
   private static final Identifier[] RUINS_CRACKED = new Identifier[]{
      Identifier.withDefaultNamespace("underwater_ruin/cracked_1"),
      Identifier.withDefaultNamespace("underwater_ruin/cracked_2"),
      Identifier.withDefaultNamespace("underwater_ruin/cracked_3"),
      Identifier.withDefaultNamespace("underwater_ruin/cracked_4"),
      Identifier.withDefaultNamespace("underwater_ruin/cracked_5"),
      Identifier.withDefaultNamespace("underwater_ruin/cracked_6"),
      Identifier.withDefaultNamespace("underwater_ruin/cracked_7"),
      Identifier.withDefaultNamespace("underwater_ruin/cracked_8")
   };
   private static final Identifier[] RUINS_MOSSY = new Identifier[]{
      Identifier.withDefaultNamespace("underwater_ruin/mossy_1"),
      Identifier.withDefaultNamespace("underwater_ruin/mossy_2"),
      Identifier.withDefaultNamespace("underwater_ruin/mossy_3"),
      Identifier.withDefaultNamespace("underwater_ruin/mossy_4"),
      Identifier.withDefaultNamespace("underwater_ruin/mossy_5"),
      Identifier.withDefaultNamespace("underwater_ruin/mossy_6"),
      Identifier.withDefaultNamespace("underwater_ruin/mossy_7"),
      Identifier.withDefaultNamespace("underwater_ruin/mossy_8")
   };
   private static final Identifier[] BIG_RUINS_BRICK = new Identifier[]{
      Identifier.withDefaultNamespace("underwater_ruin/big_brick_1"),
      Identifier.withDefaultNamespace("underwater_ruin/big_brick_2"),
      Identifier.withDefaultNamespace("underwater_ruin/big_brick_3"),
      Identifier.withDefaultNamespace("underwater_ruin/big_brick_8")
   };
   private static final Identifier[] BIG_RUINS_MOSSY = new Identifier[]{
      Identifier.withDefaultNamespace("underwater_ruin/big_mossy_1"),
      Identifier.withDefaultNamespace("underwater_ruin/big_mossy_2"),
      Identifier.withDefaultNamespace("underwater_ruin/big_mossy_3"),
      Identifier.withDefaultNamespace("underwater_ruin/big_mossy_8")
   };
   private static final Identifier[] BIG_RUINS_CRACKED = new Identifier[]{
      Identifier.withDefaultNamespace("underwater_ruin/big_cracked_1"),
      Identifier.withDefaultNamespace("underwater_ruin/big_cracked_2"),
      Identifier.withDefaultNamespace("underwater_ruin/big_cracked_3"),
      Identifier.withDefaultNamespace("underwater_ruin/big_cracked_8")
   };
   private static final Identifier[] BIG_WARM_RUINS = new Identifier[]{
      Identifier.withDefaultNamespace("underwater_ruin/big_warm_4"),
      Identifier.withDefaultNamespace("underwater_ruin/big_warm_5"),
      Identifier.withDefaultNamespace("underwater_ruin/big_warm_6"),
      Identifier.withDefaultNamespace("underwater_ruin/big_warm_7")
   };

   private static StructureProcessor archyRuleProcessor(final Block candidateBlock, final Block replacementBlock, final ResourceKey<LootTable> lootTable) {
      return new CappedProcessor(
         new RuleProcessor(
            List.of(
               new ProcessorRule(
                  new BlockMatchTest(candidateBlock),
                  AlwaysTrueTest.INSTANCE,
                  PosAlwaysTrueTest.INSTANCE,
                  replacementBlock.defaultBlockState(),
                  new AppendLoot(lootTable)
               )
            )
         ),
         ConstantInt.of(5)
      );
   }

   private static Identifier getSmallWarmRuin(final RandomSource random) {
      return Util.getRandom(WARM_RUINS, random);
   }

   private static Identifier getBigWarmRuin(final RandomSource random) {
      return Util.getRandom(BIG_WARM_RUINS, random);
   }

   public static void addPieces(
      final StructureTemplateManager structureTemplateManager,
      final BlockPos position,
      final Rotation rotation,
      final StructurePiecesBuilder builder,
      final RandomSource random,
      final OceanRuinStructure structure
   ) {
      boolean isLarge = random.nextFloat() <= structure.largeProbability;
      float baseIntegrity = isLarge ? 0.9F : 0.8F;
      addPiece(structureTemplateManager, position, rotation, builder, random, structure, isLarge, baseIntegrity);
      if (isLarge && random.nextFloat() <= structure.clusterProbability) {
         addClusterRuins(structureTemplateManager, random, rotation, position, structure, builder);
      }
   }

   private static void addClusterRuins(
      final StructureTemplateManager structureTemplateManager,
      final RandomSource random,
      final Rotation rotation,
      final BlockPos p,
      final OceanRuinStructure structure,
      final StructurePiecesBuilder builder
   ) {
      BlockPos parentPos = new BlockPos(p.getX(), 90, p.getZ());
      BlockPos parentCorner = StructureTemplate.transform(new BlockPos(15, 0, 15), Mirror.NONE, rotation, BlockPos.ZERO).offset(parentPos);
      BoundingBox parentBB = BoundingBox.fromCorners(parentPos, parentCorner);
      BlockPos parentBottomLeft = new BlockPos(
         Math.min(parentPos.getX(), parentCorner.getX()), parentPos.getY(), Math.min(parentPos.getZ(), parentCorner.getZ())
      );
      List<BlockPos> allPositions = allPositions(random, parentBottomLeft);
      int ruins = Mth.nextInt(random, 4, 8);

      for (int i = 0; i < ruins; i++) {
         if (!allPositions.isEmpty()) {
            int idx = random.nextInt(allPositions.size());
            BlockPos pos = allPositions.remove(idx);
            Rotation nextRotation = Rotation.getRandom(random);
            BlockPos nextCorner = StructureTemplate.transform(new BlockPos(5, 0, 6), Mirror.NONE, nextRotation, BlockPos.ZERO).offset(pos);
            BoundingBox nextBB = BoundingBox.fromCorners(pos, nextCorner);
            if (!nextBB.intersects(parentBB)) {
               addPiece(structureTemplateManager, pos, nextRotation, builder, random, structure, false, 0.8F);
            }
         }
      }
   }

   private static List<BlockPos> allPositions(final RandomSource random, final BlockPos origin) {
      List<BlockPos> positions = Lists.newArrayList();
      positions.add(origin.offset(-16 + Mth.nextInt(random, 1, 8), 0, 16 + Mth.nextInt(random, 1, 7)));
      positions.add(origin.offset(-16 + Mth.nextInt(random, 1, 8), 0, Mth.nextInt(random, 1, 7)));
      positions.add(origin.offset(-16 + Mth.nextInt(random, 1, 8), 0, -16 + Mth.nextInt(random, 4, 8)));
      positions.add(origin.offset(Mth.nextInt(random, 1, 7), 0, 16 + Mth.nextInt(random, 1, 7)));
      positions.add(origin.offset(Mth.nextInt(random, 1, 7), 0, -16 + Mth.nextInt(random, 4, 6)));
      positions.add(origin.offset(16 + Mth.nextInt(random, 1, 7), 0, 16 + Mth.nextInt(random, 3, 8)));
      positions.add(origin.offset(16 + Mth.nextInt(random, 1, 7), 0, Mth.nextInt(random, 1, 7)));
      positions.add(origin.offset(16 + Mth.nextInt(random, 1, 7), 0, -16 + Mth.nextInt(random, 4, 8)));
      return positions;
   }

   private static void addPiece(
      final StructureTemplateManager structureTemplateManager,
      final BlockPos position,
      final Rotation rotation,
      final StructurePiecesBuilder builder,
      final RandomSource random,
      final OceanRuinStructure structure,
      final boolean isLarge,
      final float baseIntegrity
   ) {
      switch (structure.biomeTemp) {
         case WARM:
         default:
            Identifier startPieceLocation = isLarge ? getBigWarmRuin(random) : getSmallWarmRuin(random);
            builder.addPiece(
               new OceanRuinPieces$OceanRuinPiece(structureTemplateManager, startPieceLocation, position, rotation, baseIntegrity, structure.biomeTemp, isLarge)
            );
            break;
         case COLD:
            Identifier[] bricks = isLarge ? BIG_RUINS_BRICK : RUINS_BRICK;
            Identifier[] cracked = isLarge ? BIG_RUINS_CRACKED : RUINS_CRACKED;
            Identifier[] mossy = isLarge ? BIG_RUINS_MOSSY : RUINS_MOSSY;
            int idx = random.nextInt(bricks.length);
            builder.addPiece(
               new OceanRuinPieces$OceanRuinPiece(structureTemplateManager, bricks[idx], position, rotation, baseIntegrity, structure.biomeTemp, isLarge)
            );
            builder.addPiece(new OceanRuinPieces$OceanRuinPiece(structureTemplateManager, cracked[idx], position, rotation, 0.7F, structure.biomeTemp, isLarge));
            builder.addPiece(new OceanRuinPieces$OceanRuinPiece(structureTemplateManager, mossy[idx], position, rotation, 0.5F, structure.biomeTemp, isLarge));
      }
   }
}
