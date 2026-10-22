package net.minecraft.world.level.levelgen.structure.templatesystem;

import java.util.Locale;
import java.util.Objects;
import net.minecraft.core.BlockPos;
import net.minecraft.data.worldgen.Pools;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.entity.JigsawBlockEntity;
import net.minecraft.world.level.block.entity.JigsawBlockEntity$JointType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import org.jspecify.annotations.Nullable;

public record StructureTemplate$JigsawBlockInfo(
   BlockPos pos,
   BlockState state,
   JigsawBlockEntity$JointType jointType,
   @Nullable Identifier name,
   ResourceKey<StructureTemplatePool> pool,
   Identifier target,
   int placementPriority,
   int selectionPriority
) {
   public static StructureTemplate$JigsawBlockInfo parse(final StructureTemplate$StructureBlockInfo info) {
      CompoundTag nbt = Objects.requireNonNull(info.nbt(), () -> info + " nbt was null");
      return new StructureTemplate$JigsawBlockInfo(
         info.pos(),
         info.state(),
         StructureTemplate.getJointType(nbt, info.state()),
         nbt.<Identifier>read("name", Identifier.CODEC).orElse(JigsawBlockEntity.EMPTY_ID),
         nbt.<ResourceKey<StructureTemplatePool>>read("pool", JigsawBlockEntity.POOL_CODEC).orElse(Pools.EMPTY),
         nbt.<Identifier>read("target", Identifier.CODEC).orElse(JigsawBlockEntity.EMPTY_ID),
         nbt.getIntOr("placement_priority", 0),
         nbt.getIntOr("selection_priority", 0)
      );
   }

   @Override
   public String toString() {
      return String.format(
         Locale.ROOT,
         "<JigsawBlockInfo | %s | %s | name: %s | pool: %s | target: %s | placement: %d | selection: %d>",
         this.pos,
         this.state,
         this.name,
         this.pool.identifier(),
         this.target,
         this.placementPriority,
         this.selectionPriority
      );
   }

   public StructureTemplate$JigsawBlockInfo withInfo(final BlockPos pos, final BlockState state) {
      return new StructureTemplate$JigsawBlockInfo(
         pos, state, this.jointType, this.name, this.pool, this.target, this.placementPriority, this.selectionPriority
      );
   }
}
