package net.minecraft.world.level.levelgen.feature.stateproviders;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import org.jspecify.annotations.Nullable;

public record RuleBasedStateProvider(@Nullable Holder<BlockStateProvider> fallback, List<RuleBasedStateProvider$Rule> rules) implements BlockStateProvider {
   public static final MapCodec<RuleBasedStateProvider> CODEC = RecordCodecBuilder.mapCodec(
      i -> i.group(
            BlockStateProvider.CODEC.optionalFieldOf("fallback").forGetter(provider -> Optional.ofNullable(provider.fallback)),
            RuleBasedStateProvider$Rule.CODEC.listOf().fieldOf("rules").forGetter(p -> p.rules)
         )
         .apply(i, RuleBasedStateProvider::new)
   );

   private RuleBasedStateProvider(final Optional<Holder<BlockStateProvider>> fallback, final List<RuleBasedStateProvider$Rule> rules) {
      this(fallback.orElse(null), rules);
   }

   public static RuleBasedStateProvider ifTrueThenProvide(final BlockPredicate ifTrue, final Block thenProvide) {
      return ifTrueThenProvide(ifTrue, BlockStateProvider.of(thenProvide));
   }

   public static RuleBasedStateProvider ifTrueThenProvide(final BlockPredicate ifTrue, final BlockStateProvider thenProvide) {
      return new RuleBasedStateProvider((Holder<BlockStateProvider>)null, List.of(new RuleBasedStateProvider$Rule(ifTrue, Holder.direct(thenProvide))));
   }

   @Override
   public MapCodec<RuleBasedStateProvider> codec() {
      return CODEC;
   }

   @Override
   public BlockState getState(final LevelAccessor level, final RandomSource random, final BlockPos pos) {
      BlockState result = this.getOptionalState(level, random, pos);
      return result != null ? result : level.getBlockState(pos);
   }

   @Nullable
   @Override
   public BlockState getOptionalState(final LevelAccessor level, final RandomSource random, final BlockPos pos) {
      for (RuleBasedStateProvider$Rule rule : this.rules) {
         if (rule.ifTrue().test(level, pos)) {
            BlockState optionalState = rule.then().value().getOptionalState(level, random, pos);
            if (optionalState != null) {
               return optionalState;
            }
         }
      }

      return this.fallback == null ? null : this.fallback.value().getOptionalState(level, random, pos);
   }

   public static RuleBasedStateProvider$Builder builder() {
      return new RuleBasedStateProvider$Builder(null);
   }

   public static RuleBasedStateProvider$Builder builder(@Nullable final BlockStateProvider fallback) {
      return new RuleBasedStateProvider$Builder(fallback);
   }
}
