package net.minecraft.advancements.predicates;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class BlockPredicate$Builder {
   private Optional<HolderSet<Block>> blocks = Optional.empty();
   private Optional<StatePropertiesPredicate> properties = Optional.empty();
   private Optional<NbtPredicate> nbt = Optional.empty();
   private DataComponentMatchers components = DataComponentMatchers.ANY;

   private BlockPredicate$Builder() {
   }

   public static BlockPredicate$Builder block() {
      return new BlockPredicate$Builder();
   }

   public BlockPredicate$Builder of(final HolderGetter<Block> lookup, final Block... blocks) {
      return this.of(lookup, Arrays.asList(blocks));
   }

   public BlockPredicate$Builder of(final HolderGetter<Block> lookup, final Collection<Block> blocks) {
      this.blocks = Optional.of(HolderSet.direct(Block::builtInRegistryHolder, blocks));
      return this;
   }

   public BlockPredicate$Builder of(final HolderGetter<Block> lookup, final TagKey<Block> tag) {
      this.blocks = Optional.of(lookup.getOrThrow(tag));
      return this;
   }

   public BlockPredicate$Builder hasNbt(final CompoundTag nbt) {
      this.nbt = Optional.of(new NbtPredicate(nbt));
      return this;
   }

   public BlockPredicate$Builder setProperties(final StatePropertiesPredicate$Builder properties) {
      this.properties = properties.build();
      return this;
   }

   public BlockPredicate$Builder components(final DataComponentMatchers components) {
      this.components = components;
      return this;
   }

   public BlockPredicate build() {
      return new BlockPredicate(this.blocks, this.properties, this.nbt, this.components);
   }
}
