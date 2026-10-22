package net.minecraft.core.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import java.util.Optional;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.RuleBasedStateProvider;
import net.minecraft.world.level.storage.loot.LootTable;

public record BlockTransformer$BlockTransformData(
   Holder<BlockStateProvider> blockStateProvider,
   Holder<SoundEvent> sound,
   BlockTransformer$TransformParticle particle,
   List<Direction> disallowedFaces,
   Optional<ResourceKey<LootTable>> loot,
   BlockTransformer$DropStrategy dropStrategy,
   boolean updateFromNeighbors,
   BlockTransformer$TransformType transformType,
   boolean consumeOnUse,
   int itemDamagePerUse
) {
   public static final Codec<BlockTransformer$BlockTransformData> CODEC = RecordCodecBuilder.create(
      i -> i.group(
            BlockStateProvider.CODEC.fieldOf("block_state_provider").forGetter(BlockTransformer$BlockTransformData::blockStateProvider),
            SoundEvent.CODEC
               .optionalFieldOf("sound", BuiltInRegistries.SOUND_EVENT.wrapAsHolder(SoundEvents.EMPTY))
               .forGetter(BlockTransformer$BlockTransformData::sound),
            BlockTransformer$TransformParticle.CODEC
               .optionalFieldOf("particle", BlockTransformer$TransformParticle.NONE)
               .forGetter(BlockTransformer$BlockTransformData::particle),
            Direction.CODEC.listOf().optionalFieldOf("disallowed_faces", List.of()).forGetter(BlockTransformer$BlockTransformData::disallowedFaces),
            LootTable.KEY_CODEC.optionalFieldOf("loot").forGetter(BlockTransformer$BlockTransformData::loot),
            BlockTransformer$DropStrategy.CODEC
               .optionalFieldOf("drop_strategy", BlockTransformer$DropStrategy.FROM_MIDDLE)
               .forGetter(BlockTransformer$BlockTransformData::dropStrategy),
            Codec.BOOL.optionalFieldOf("update_from_neighbors", true).forGetter(BlockTransformer$BlockTransformData::updateFromNeighbors),
            BlockTransformer$TransformType.CODEC
               .optionalFieldOf("transform_type", BlockTransformer$TransformType.SINGLE_BLOCK)
               .forGetter(BlockTransformer$BlockTransformData::transformType),
            Codec.BOOL.optionalFieldOf("consume_on_use", true).forGetter(BlockTransformer$BlockTransformData::consumeOnUse),
            ExtraCodecs.NON_NEGATIVE_INT.optionalFieldOf("item_damage_per_use", 0).forGetter(BlockTransformer$BlockTransformData::itemDamagePerUse)
         )
         .apply(i, BlockTransformer$BlockTransformData::new)
   );
   public static final StreamCodec<RegistryFriendlyByteBuf, BlockTransformer$BlockTransformData> STREAM_CODEC = StreamCodec.composite(
      ByteBufCodecs.fromCodecWithRegistries(BlockStateProvider.CODEC),
      BlockTransformer$BlockTransformData::blockStateProvider,
      SoundEvent.STREAM_CODEC,
      BlockTransformer$BlockTransformData::sound,
      BlockTransformer$TransformParticle.STREAM_CODEC,
      BlockTransformer$BlockTransformData::particle,
      Direction.STREAM_CODEC.apply(ByteBufCodecs.list()),
      BlockTransformer$BlockTransformData::disallowedFaces,
      ResourceKey.streamCodec(Registries.LOOT_TABLE).apply(ByteBufCodecs::optional),
      BlockTransformer$BlockTransformData::loot,
      BlockTransformer$DropStrategy.STREAM_CODEC,
      BlockTransformer$BlockTransformData::dropStrategy,
      ByteBufCodecs.BOOL,
      BlockTransformer$BlockTransformData::updateFromNeighbors,
      BlockTransformer$TransformType.STREAM_CODEC,
      BlockTransformer$BlockTransformData::transformType,
      ByteBufCodecs.BOOL,
      BlockTransformer$BlockTransformData::consumeOnUse,
      ByteBufCodecs.VAR_INT,
      BlockTransformer$BlockTransformData::itemDamagePerUse,
      BlockTransformer$BlockTransformData::new
   );

   public static BlockTransformer$BlockTransformData$Builder builder(final Holder<BlockStateProvider> targetStateProvider) {
      return new BlockTransformer$BlockTransformData$Builder(targetStateProvider);
   }

   public static BlockTransformer$BlockTransformData$Builder builder(final BlockStateProvider targetStateProvider) {
      return builder(Holder.direct(targetStateProvider));
   }

   public static BlockTransformer$BlockTransformData$Builder builder(final BlockPredicate predicate, final Block block) {
      return builder(RuleBasedStateProvider.builder().ifTrueThenProvide(predicate, block).build());
   }
}
