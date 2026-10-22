package net.minecraft.world.item.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.registries.codec.RegistryCodecs;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.level.block.Block;

public record Tool$Rule(HolderSet<Block> blocks, Optional<Float> speed, Optional<Boolean> correctForDrops) {
   public static final Codec<Tool$Rule> CODEC = RecordCodecBuilder.create(
      i -> i.group(
            RegistryCodecs.holderSet(Registries.BLOCK).fieldOf("blocks").forGetter(Tool$Rule::blocks),
            ExtraCodecs.POSITIVE_FLOAT.optionalFieldOf("speed").forGetter(Tool$Rule::speed),
            Codec.BOOL.optionalFieldOf("correct_for_drops").forGetter(Tool$Rule::correctForDrops)
         )
         .apply(i, Tool$Rule::new)
   );
   public static final StreamCodec<RegistryFriendlyByteBuf, Tool$Rule> STREAM_CODEC = StreamCodec.composite(
      ByteBufCodecs.holderSet(Registries.BLOCK),
      Tool$Rule::blocks,
      ByteBufCodecs.FLOAT.apply(ByteBufCodecs::optional),
      Tool$Rule::speed,
      ByteBufCodecs.BOOL.apply(ByteBufCodecs::optional),
      Tool$Rule::correctForDrops,
      Tool$Rule::new
   );

   public static Tool$Rule minesAndDrops(final HolderSet<Block> blocks, final float speed) {
      return new Tool$Rule(blocks, Optional.of(speed), Optional.of(true));
   }

   public static Tool$Rule deniesDrops(final HolderSet<Block> blocks) {
      return new Tool$Rule(blocks, Optional.empty(), Optional.of(false));
   }

   public static Tool$Rule overrideSpeed(final HolderSet<Block> blocks, final float speed) {
      return new Tool$Rule(blocks, Optional.of(speed), Optional.empty());
   }
}
