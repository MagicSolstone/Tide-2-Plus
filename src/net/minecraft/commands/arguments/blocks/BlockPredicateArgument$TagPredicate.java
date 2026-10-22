package net.minecraft.commands.arguments.blocks;

import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.core.HolderSet;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;
import net.minecraft.world.level.block.state.properties.Property;
import org.jspecify.annotations.Nullable;

class BlockPredicateArgument$TagPredicate implements BlockPredicateArgument$Result {
   private final HolderSet<Block> tag;
   @Nullable
   private final CompoundTag nbt;
   private final Map<String, String> vagueProperties;

   private BlockPredicateArgument$TagPredicate(final HolderSet<Block> tag, final Map<String, String> vagueProperties, @Nullable final CompoundTag nbt) {
      this.tag = tag;
      this.vagueProperties = vagueProperties;
      this.nbt = nbt;
   }

   public boolean test(final BlockInWorld blockInWorld) {
      BlockState state = blockInWorld.getState();
      if (!state.is(this.tag)) {
         return false;
      } else {
         for (Entry<String, String> entry : this.vagueProperties.entrySet()) {
            Property<?> property = state.getBlock().getStateDefinition().getProperty(entry.getKey());
            if (property == null) {
               return false;
            }

            Comparable<?> value = (Comparable<?>)property.getValue(entry.getValue()).orElse(null);
            if (value == null) {
               return false;
            }

            if (state.getValue(property) != value) {
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
