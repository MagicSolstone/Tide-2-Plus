package net.minecraft.world.item.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.level.block.state.BlockState;

public record Tool(List<Tool$Rule> rules, float defaultMiningSpeed, int damagePerBlock, boolean canDestroyBlocksInCreative) {
   public static final Codec<Tool> CODEC = RecordCodecBuilder.create(
      i -> i.group(
            Tool$Rule.CODEC.listOf().fieldOf("rules").forGetter(Tool::rules),
            Codec.FLOAT.optionalFieldOf("default_mining_speed", 1.0F).forGetter(Tool::defaultMiningSpeed),
            ExtraCodecs.NON_NEGATIVE_INT.optionalFieldOf("damage_per_block", 1).forGetter(Tool::damagePerBlock),
            Codec.BOOL.optionalFieldOf("can_destroy_blocks_in_creative", true).forGetter(Tool::canDestroyBlocksInCreative)
         )
         .apply(i, Tool::new)
   );
   public static final StreamCodec<RegistryFriendlyByteBuf, Tool> STREAM_CODEC = StreamCodec.composite(
      Tool$Rule.STREAM_CODEC.apply(ByteBufCodecs.list()),
      Tool::rules,
      ByteBufCodecs.FLOAT,
      Tool::defaultMiningSpeed,
      ByteBufCodecs.VAR_INT,
      Tool::damagePerBlock,
      ByteBufCodecs.BOOL,
      Tool::canDestroyBlocksInCreative,
      Tool::new
   );

   public float getMiningSpeed(final BlockState state) {
      for (Tool$Rule rule : this.rules) {
         if (rule.speed.isPresent() && state.is(rule.blocks)) {
            return rule.speed.get();
         }
      }

      return this.defaultMiningSpeed;
   }

   public boolean isCorrectForDrops(final BlockState state) {
      for (Tool$Rule rule : this.rules) {
         if (rule.correctForDrops.isPresent() && state.is(rule.blocks)) {
            return rule.correctForDrops.get();
         }
      }

      return false;
   }
}
