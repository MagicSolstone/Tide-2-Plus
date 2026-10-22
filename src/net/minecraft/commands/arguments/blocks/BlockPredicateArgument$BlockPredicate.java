package net.minecraft.commands.arguments.blocks;

import java.util.Set;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;
import net.minecraft.world.level.block.state.properties.Property;
import org.jspecify.annotations.Nullable;

class BlockPredicateArgument$BlockPredicate implements BlockPredicateArgument$Result {
   private final BlockState state;
   private final Set<Property<?>> properties;
   @Nullable
   private final CompoundTag nbt;

   public BlockPredicateArgument$BlockPredicate(final BlockState state, final Set<Property<?>> properties, @Nullable final CompoundTag nbt) {
      this.state = state;
      this.properties = properties;
      this.nbt = nbt;
   }

   public boolean test(final BlockInWorld blockInWorld) {
      BlockState state = blockInWorld.getState();
      if (!state.is(this.state.getBlock())) {
         return false;
      } else {
         for (Property<?> property : this.properties) {
            if (state.getValue(property) != this.state.getValue(property)) {
               return false;
            }
         }

         if (this.nbt == null) {
            return true;
         } else {
            BlockEntity entity = blockInWorld.getEntity();
            return entity != null && NbtUtils.compareNbt(this.nbt, entity.saveWithFullMetadata(blockInWorld.getLevel().registryAccess()), true);
         }
      }
   }

   @Override
   public boolean requiresNbt() {
      return this.nbt != null;
   }
}
