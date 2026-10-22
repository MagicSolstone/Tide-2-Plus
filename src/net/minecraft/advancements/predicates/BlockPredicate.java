package net.minecraft.advancements.predicates;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderSet;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.registries.codec.RegistryCodecs;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;
import org.jspecify.annotations.Nullable;

public record BlockPredicate(
   Optional<HolderSet<Block>> blocks, Optional<StatePropertiesPredicate> properties, Optional<NbtPredicate> nbt, DataComponentMatchers components
) {
   public static final MapCodec<BlockPredicate> MAP_CODEC = RecordCodecBuilder.mapCodec(
      i -> i.group(
            RegistryCodecs.holderSet(Registries.BLOCK).optionalFieldOf("blocks").forGetter(BlockPredicate::blocks),
            StatePropertiesPredicate.CODEC.optionalFieldOf("state").forGetter(BlockPredicate::properties),
            NbtPredicate.CODEC.optionalFieldOf("nbt").forGetter(BlockPredicate::nbt),
            DataComponentMatchers.CODEC.forGetter(BlockPredicate::components)
         )
         .apply(i, BlockPredicate::new)
   );
   public static final Codec<BlockPredicate> CODEC = MAP_CODEC.codec();
   public static final StreamCodec<RegistryFriendlyByteBuf, BlockPredicate> STREAM_CODEC = StreamCodec.composite(
      ByteBufCodecs.optional(ByteBufCodecs.holderSet(Registries.BLOCK)),
      BlockPredicate::blocks,
      ByteBufCodecs.optional(StatePropertiesPredicate.STREAM_CODEC),
      BlockPredicate::properties,
      ByteBufCodecs.optional(NbtPredicate.STREAM_CODEC),
      BlockPredicate::nbt,
      DataComponentMatchers.STREAM_CODEC,
      BlockPredicate::components,
      BlockPredicate::new
   );

   public boolean matches(final ServerLevel level, final BlockPos pos) {
      if (!level.isLoaded(pos)) {
         return false;
      } else if (!this.matchesState(level.getBlockState(pos))) {
         return false;
      } else {
         if (this.willMatchBlockEntity()) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (!this.matchesBlockEntity(level, blockEntity)) {
               return false;
            }
         }

         return true;
      }
   }

   public boolean willMatchBlockEntity() {
      return this.nbt.isPresent() || !this.components.isEmpty();
   }

   public boolean matches(final BlockInWorld blockInWorld) {
      return !this.matchesState(blockInWorld.getState())
         ? false
         : !this.nbt.isPresent() || matchesBlockEntityData(blockInWorld.getLevel(), blockInWorld.getEntity(), this.nbt.get());
   }

   public boolean matchesState(final BlockState state) {
      return this.blocks.isPresent() && !state.is(this.blocks.get()) ? false : !this.properties.isPresent() || this.properties.get().matches(state);
   }

   public boolean matchesBlockEntity(final LevelReader level, @Nullable final BlockEntity blockEntity) {
      return this.nbt.isPresent() && !matchesBlockEntityData(level, blockEntity, this.nbt.get())
         ? false
         : this.components.isEmpty() || matchesComponents(blockEntity, this.components);
   }

   private static boolean matchesBlockEntityData(final LevelReader level, @Nullable final BlockEntity entity, final NbtPredicate nbt) {
      return entity != null && nbt.matches(entity.saveWithFullMetadata(level.registryAccess()));
   }

   private static boolean matchesComponents(@Nullable final BlockEntity entity, final DataComponentMatchers components) {
      return entity != null && components.test((DataComponentGetter)entity.collectComponents());
   }

   public boolean requiresNbt() {
      return this.nbt.isPresent();
   }
}
