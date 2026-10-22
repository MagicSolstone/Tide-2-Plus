package net.minecraft.world.level.levelgen.structure.structures;

import com.mojang.serialization.Codec;
import java.util.function.IntFunction;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.ByIdMap$OutOfBoundsStrategy;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public enum MineshaftStructure$Type implements StringRepresentable {
   NORMAL("normal", Blocks.OAK_LOG, Blocks.OAK_PLANKS, Blocks.OAK_FENCE),
   MESA("mesa", Blocks.DARK_OAK_LOG, Blocks.DARK_OAK_PLANKS, Blocks.DARK_OAK_FENCE);

   public static final Codec<MineshaftStructure$Type> CODEC = StringRepresentable.fromEnum(MineshaftStructure$Type::values);
   private static final IntFunction<MineshaftStructure$Type> BY_ID = ByIdMap.continuous(Enum::ordinal, values(), ByIdMap$OutOfBoundsStrategy.ZERO);
   private final String name;
   private final BlockState woodState;
   private final BlockState planksState;
   private final BlockState fenceState;

   private MineshaftStructure$Type(final String name, final Block wood, final Block plank, final Block fence) {
      this.name = name;
      this.woodState = wood.defaultBlockState();
      this.planksState = plank.defaultBlockState();
      this.fenceState = fence.defaultBlockState();
   }

   public String getName() {
      return this.name;
   }

   public static MineshaftStructure$Type byId(final int id) {
      return BY_ID.apply(id);
   }

   public BlockState getWoodState() {
      return this.woodState;
   }

   public BlockState getPlanksState() {
      return this.planksState;
   }

   public BlockState getFenceState() {
      return this.fenceState;
   }

   @Override
   public String getSerializedName() {
      return this.name;
   }
}
