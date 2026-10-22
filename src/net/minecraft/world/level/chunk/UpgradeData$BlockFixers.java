package net.minecraft.world.level.chunk;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

enum UpgradeData$BlockFixers implements UpgradeData$BlockFixer {
   BLACKLIST(
      Blocks.OBSERVER,
      Blocks.NETHER_PORTAL,
      Blocks.CONCRETE_POWDER.white(),
      Blocks.CONCRETE_POWDER.orange(),
      Blocks.CONCRETE_POWDER.magenta(),
      Blocks.CONCRETE_POWDER.lightBlue(),
      Blocks.CONCRETE_POWDER.yellow(),
      Blocks.CONCRETE_POWDER.lime(),
      Blocks.CONCRETE_POWDER.pink(),
      Blocks.CONCRETE_POWDER.gray(),
      Blocks.CONCRETE_POWDER.lightGray(),
      Blocks.CONCRETE_POWDER.cyan(),
      Blocks.CONCRETE_POWDER.purple(),
      Blocks.CONCRETE_POWDER.blue(),
      Blocks.CONCRETE_POWDER.brown(),
      Blocks.CONCRETE_POWDER.green(),
      Blocks.CONCRETE_POWDER.red(),
      Blocks.CONCRETE_POWDER.black(),
      Blocks.ANVIL,
      Blocks.CHIPPED_ANVIL,
      Blocks.DAMAGED_ANVIL,
      Blocks.DRAGON_EGG,
      Blocks.GRAVEL,
      Blocks.SAND,
      Blocks.RED_SAND,
      Blocks.OAK_SIGN,
      Blocks.SPRUCE_SIGN,
      Blocks.BIRCH_SIGN,
      Blocks.ACACIA_SIGN,
      Blocks.CHERRY_SIGN,
      Blocks.JUNGLE_SIGN,
      Blocks.DARK_OAK_SIGN,
      Blocks.PALE_OAK_SIGN,
      Blocks.POPLAR_SIGN,
      Blocks.OAK_WALL_SIGN,
      Blocks.SPRUCE_WALL_SIGN,
      Blocks.BIRCH_WALL_SIGN,
      Blocks.ACACIA_WALL_SIGN,
      Blocks.JUNGLE_WALL_SIGN,
      Blocks.DARK_OAK_WALL_SIGN,
      Blocks.PALE_OAK_WALL_SIGN,
      Blocks.POPLAR_WALL_SIGN,
      Blocks.OAK_HANGING_SIGN,
      Blocks.SPRUCE_HANGING_SIGN,
      Blocks.BIRCH_HANGING_SIGN,
      Blocks.ACACIA_HANGING_SIGN,
      Blocks.JUNGLE_HANGING_SIGN,
      Blocks.DARK_OAK_HANGING_SIGN,
      Blocks.PALE_OAK_HANGING_SIGN,
      Blocks.POPLAR_HANGING_SIGN,
      Blocks.OAK_WALL_HANGING_SIGN,
      Blocks.SPRUCE_WALL_HANGING_SIGN,
      Blocks.BIRCH_WALL_HANGING_SIGN,
      Blocks.ACACIA_WALL_HANGING_SIGN,
      Blocks.JUNGLE_WALL_HANGING_SIGN,
      Blocks.DARK_OAK_WALL_HANGING_SIGN,
      Blocks.PALE_OAK_WALL_HANGING_SIGN,
      Blocks.POPLAR_WALL_HANGING_SIGN
   ),
   DEFAULT(),
   CHEST(Blocks.CHEST, Blocks.TRAPPED_CHEST),
   LEAVES(
      true,
      Blocks.ACACIA_LEAVES,
      Blocks.CHERRY_LEAVES,
      Blocks.BIRCH_LEAVES,
      Blocks.PALE_OAK_LEAVES,
      Blocks.RED_POPLAR_LEAVES,
      Blocks.ORANGE_POPLAR_LEAVES,
      Blocks.YELLOW_POPLAR_LEAVES,
      Blocks.DARK_OAK_LEAVES,
      Blocks.JUNGLE_LEAVES,
      Blocks.OAK_LEAVES,
      Blocks.SPRUCE_LEAVES
   ),
   STEM_BLOCK(Blocks.MELON_STEM, Blocks.PUMPKIN_STEM);

   public static final Direction[] DIRECTIONS = Direction.values();

   private UpgradeData$BlockFixers(final Block... blocks) {
      this(false, blocks);
   }

   private UpgradeData$BlockFixers(final boolean chunky, final Block... blocks) {
      for (Block block : blocks) {
         UpgradeData.MAP.put(block, this);
      }

      if (chunky) {
         UpgradeData.CHUNKY_FIXERS.add(this);
      }
   }
}
