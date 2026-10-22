package net.minecraft.world.level.levelgen.feature.stateproviders;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;

public record RuleBasedStateProvider$Rule(BlockPredicate ifTrue, Holder<BlockStateProvider> then) {
   public static final Codec<RuleBasedStateProvider$Rule> CODEC = RecordCodecBuilder.create(
      i -> i.group(
            BlockPredicate.CODEC.fieldOf("if_true").forGetter(RuleBasedStateProvider$Rule::ifTrue),
            BlockStateProvider.CODEC.fieldOf("then").forGetter(RuleBasedStateProvider$Rule::then)
         )
         .apply(i, RuleBasedStateProvider$Rule::new)
   );
}
