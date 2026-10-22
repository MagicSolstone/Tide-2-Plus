package net.minecraft.core.dispenser;

import com.mojang.logging.LogUtils;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DispenserBlock;
import org.slf4j.Logger;

public interface DispenseItemBehavior {
   Logger LOGGER = LogUtils.getLogger();
   DispenseItemBehavior NOOP = (source, dispensed) -> dispensed;

   ItemStack dispense(BlockSource source, ItemStack dispensed);

   static void bootStrap() {
      DispenserBlock.registerProjectileBehavior(Items.ARROW);
      DispenserBlock.registerProjectileBehavior(Items.TIPPED_ARROW);
      DispenserBlock.registerProjectileBehavior(Items.SPECTRAL_ARROW);
      DispenserBlock.registerProjectileBehavior(Items.EGG);
      DispenserBlock.registerProjectileBehavior(Items.BLUE_EGG);
      DispenserBlock.registerProjectileBehavior(Items.BROWN_EGG);
      DispenserBlock.registerProjectileBehavior(Items.SNOWBALL);
      DispenserBlock.registerProjectileBehavior(Items.EXPERIENCE_BOTTLE);
      DispenserBlock.registerProjectileBehavior(Items.SPLASH_POTION);
      DispenserBlock.registerProjectileBehavior(Items.LINGERING_POTION);
      DispenserBlock.registerProjectileBehavior(Items.FIREWORK_ROCKET);
      DispenserBlock.registerProjectileBehavior(Items.FIRE_CHARGE);
      DispenserBlock.registerProjectileBehavior(Items.WIND_CHARGE);
      DispenserBlock.registerBehavior(Items.ARMOR_STAND, new DispenseItemBehavior$1());
      DispenserBlock.registerBehavior(Items.CHEST, new DispenseItemBehavior$2());
      DispenserBlock.registerBehavior(Items.OAK_BOAT, new BoatDispenseItemBehavior(EntityTypes.OAK_BOAT));
      DispenserBlock.registerBehavior(Items.SPRUCE_BOAT, new BoatDispenseItemBehavior(EntityTypes.SPRUCE_BOAT));
      DispenserBlock.registerBehavior(Items.BIRCH_BOAT, new BoatDispenseItemBehavior(EntityTypes.BIRCH_BOAT));
      DispenserBlock.registerBehavior(Items.JUNGLE_BOAT, new BoatDispenseItemBehavior(EntityTypes.JUNGLE_BOAT));
      DispenserBlock.registerBehavior(Items.DARK_OAK_BOAT, new BoatDispenseItemBehavior(EntityTypes.DARK_OAK_BOAT));
      DispenserBlock.registerBehavior(Items.ACACIA_BOAT, new BoatDispenseItemBehavior(EntityTypes.ACACIA_BOAT));
      DispenserBlock.registerBehavior(Items.CHERRY_BOAT, new BoatDispenseItemBehavior(EntityTypes.CHERRY_BOAT));
      DispenserBlock.registerBehavior(Items.MANGROVE_BOAT, new BoatDispenseItemBehavior(EntityTypes.MANGROVE_BOAT));
      DispenserBlock.registerBehavior(Items.POPLAR_BOAT, new BoatDispenseItemBehavior(EntityTypes.POPLAR_BOAT));
      DispenserBlock.registerBehavior(Items.PALE_OAK_BOAT, new BoatDispenseItemBehavior(EntityTypes.PALE_OAK_BOAT));
      DispenserBlock.registerBehavior(Items.BAMBOO_RAFT, new BoatDispenseItemBehavior(EntityTypes.BAMBOO_RAFT));
      DispenserBlock.registerBehavior(Items.OAK_CHEST_BOAT, new BoatDispenseItemBehavior(EntityTypes.OAK_CHEST_BOAT));
      DispenserBlock.registerBehavior(Items.SPRUCE_CHEST_BOAT, new BoatDispenseItemBehavior(EntityTypes.SPRUCE_CHEST_BOAT));
      DispenserBlock.registerBehavior(Items.BIRCH_CHEST_BOAT, new BoatDispenseItemBehavior(EntityTypes.BIRCH_CHEST_BOAT));
      DispenserBlock.registerBehavior(Items.JUNGLE_CHEST_BOAT, new BoatDispenseItemBehavior(EntityTypes.JUNGLE_CHEST_BOAT));
      DispenserBlock.registerBehavior(Items.DARK_OAK_CHEST_BOAT, new BoatDispenseItemBehavior(EntityTypes.DARK_OAK_CHEST_BOAT));
      DispenserBlock.registerBehavior(Items.ACACIA_CHEST_BOAT, new BoatDispenseItemBehavior(EntityTypes.ACACIA_CHEST_BOAT));
      DispenserBlock.registerBehavior(Items.CHERRY_CHEST_BOAT, new BoatDispenseItemBehavior(EntityTypes.CHERRY_CHEST_BOAT));
      DispenserBlock.registerBehavior(Items.MANGROVE_CHEST_BOAT, new BoatDispenseItemBehavior(EntityTypes.MANGROVE_CHEST_BOAT));
      DispenserBlock.registerBehavior(Items.POPLAR_CHEST_BOAT, new BoatDispenseItemBehavior(EntityTypes.POPLAR_CHEST_BOAT));
      DispenserBlock.registerBehavior(Items.PALE_OAK_CHEST_BOAT, new BoatDispenseItemBehavior(EntityTypes.PALE_OAK_CHEST_BOAT));
      DispenserBlock.registerBehavior(Items.BAMBOO_CHEST_RAFT, new BoatDispenseItemBehavior(EntityTypes.BAMBOO_CHEST_RAFT));
      DispenseItemBehavior filledBucketBehavior = new DispenseItemBehavior$3();
      DispenserBlock.registerBehavior(Items.LAVA_BUCKET, filledBucketBehavior);
      DispenserBlock.registerBehavior(Items.WATER_BUCKET, filledBucketBehavior);
      DispenserBlock.registerBehavior(Items.POWDER_SNOW_BUCKET, filledBucketBehavior);
      DispenserBlock.registerBehavior(Items.SALMON_BUCKET, filledBucketBehavior);
      DispenserBlock.registerBehavior(Items.COD_BUCKET, filledBucketBehavior);
      DispenserBlock.registerBehavior(Items.PUFFERFISH_BUCKET, filledBucketBehavior);
      DispenserBlock.registerBehavior(Items.TROPICAL_FISH_BUCKET, filledBucketBehavior);
      DispenserBlock.registerBehavior(Items.AXOLOTL_BUCKET, filledBucketBehavior);
      DispenserBlock.registerBehavior(Items.SULFUR_CUBE_BUCKET, filledBucketBehavior);
      DispenserBlock.registerBehavior(Items.TADPOLE_BUCKET, filledBucketBehavior);
      DispenserBlock.registerBehavior(Items.BUCKET, new DispenseItemBehavior$4());
      DispenserBlock.registerBehavior(Items.FLINT_AND_STEEL, new FlintAndSteelDispenseItemBehavior());
      DispenserBlock.registerBehavior(Items.BONE_MEAL, new DispenseItemBehavior$5());
      DispenserBlock.registerBehavior(Blocks.TNT, new DispenseItemBehavior$6());
      DispenserBlock.registerBehavior(Items.WITHER_SKELETON_SKULL, new DispenseItemBehavior$7());
      DispenserBlock.registerBehavior(Blocks.CARVED_PUMPKIN, new DispenseItemBehavior$8());
      ShulkerBoxDispenseBehavior shulkerBoxDispenseBehavior = new ShulkerBoxDispenseBehavior();
      DispenserBlock.registerBehavior(Items.SHULKER_BOX, shulkerBoxDispenseBehavior);
      Items.DYED_SHULKER_BOX.forEach(item -> DispenserBlock.registerBehavior(item, shulkerBoxDispenseBehavior));
      DispenserBlock.registerBehavior(Items.GLASS_BOTTLE, new DispenseItemBehavior$9());
      DispenserBlock.registerBehavior(Items.GLOWSTONE, new DispenseItemBehavior$10());
      DispenserBlock.registerBehavior(Items.SHEARS, new ShearsDispenseItemBehavior());
      DispenserBlock.registerBehavior(Items.BRUSH, new DispenseItemBehavior$11());
      DispenserBlock.registerBehavior(Items.HONEYCOMB, new DispenseItemBehavior$12());
      DispenserBlock.registerBehavior(Items.POTION, new DispenseItemBehavior$13());
      DispenserBlock.registerBehavior(Items.MINECART, new MinecartDispenseItemBehavior(EntityTypes.MINECART));
      DispenserBlock.registerBehavior(Items.CHEST_MINECART, new MinecartDispenseItemBehavior(EntityTypes.CHEST_MINECART));
      DispenserBlock.registerBehavior(Items.FURNACE_MINECART, new MinecartDispenseItemBehavior(EntityTypes.FURNACE_MINECART));
      DispenserBlock.registerBehavior(Items.TNT_MINECART, new MinecartDispenseItemBehavior(EntityTypes.TNT_MINECART));
      DispenserBlock.registerBehavior(Items.HOPPER_MINECART, new MinecartDispenseItemBehavior(EntityTypes.HOPPER_MINECART));
      DispenserBlock.registerBehavior(Items.COMMAND_BLOCK_MINECART, new MinecartDispenseItemBehavior(EntityTypes.COMMAND_BLOCK_MINECART));
   }
}
