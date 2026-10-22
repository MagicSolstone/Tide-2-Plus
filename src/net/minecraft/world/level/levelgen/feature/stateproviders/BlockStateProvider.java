package net.minecraft.world.level.levelgen.feature.stateproviders;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.registries.codec.RegistryCodecs;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;

public interface BlockStateProvider {
   Codec<BlockStateProvider> TYPED_CODEC = BuiltInRegistries.BLOCK_STATE_PROVIDER_TYPE.byNameCodec().dispatch(BlockStateProvider::codec, c -> c);
   Codec<Either<BlockState, BlockStateProvider>> STATE_OR_PROVIDER_CODEC = Codec.xor(BlockState.FULL_CODEC, TYPED_CODEC);
   Codec<BlockStateProvider> DIRECT_CODEC = STATE_OR_PROVIDER_CODEC.xmap(
      e -> (BlockStateProvider)e.map(SimpleStateProvider::new, s -> s),
      provider -> provider instanceof SimpleStateProvider(BlockState patt1$temp) ? Either.left(patt1$temp) : Either.right(provider)
   );
   Codec<Holder<BlockStateProvider>> CODEC = RegistryCodecs.holder(Registries.BLOCK_STATE_PROVIDER, DIRECT_CODEC);

   static SimpleStateProvider of(final BlockState state) {
      return new SimpleStateProvider(state);
   }

   static SimpleStateProvider of(final Block block) {
      return new SimpleStateProvider(block.defaultBlockState());
   }

   static Holder<BlockStateProvider> holderOf(final BlockState state) {
      return Holder.direct(of(state));
   }

   static Holder<BlockStateProvider> holderOf(final Block block) {
      return Holder.direct(of(block));
   }

   MapCodec<? extends BlockStateProvider> codec();

   BlockState getState(final LevelAccessor level, final RandomSource random, final BlockPos pos);

   @Nullable
   default BlockState getOptionalState(final LevelAccessor level, final RandomSource random, final BlockPos pos) {
      return this.getState(level, random, pos);
   }
}
