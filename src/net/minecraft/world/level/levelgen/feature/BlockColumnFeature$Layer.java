package net.minecraft.world.level.levelgen.feature;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.util.valueproviders.IntProviders;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;

public record BlockColumnFeature$Layer(IntProvider height, Holder<BlockStateProvider> state) {
   public static final Codec<BlockColumnFeature$Layer> CODEC = RecordCodecBuilder.create(
      i -> i.group(
            IntProviders.NON_NEGATIVE_CODEC.fieldOf("height").forGetter(BlockColumnFeature$Layer::height),
            BlockStateProvider.CODEC.fieldOf("provider").forGetter(BlockColumnFeature$Layer::state)
         )
         .apply(i, BlockColumnFeature$Layer::new)
   );
}
