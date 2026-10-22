package net.minecraft.network.protocol.game;

import io.netty.buffer.ByteBuf;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.gamerules.GameRule;

public record ServerboundSetGameRulePacket$Entry(ResourceKey<GameRule<?>> gameRuleKey, String value) {
   public static final StreamCodec<ByteBuf, ServerboundSetGameRulePacket$Entry> STREAM_CODEC = StreamCodec.composite(
      ResourceKey.streamCodec(Registries.GAME_RULE),
      ServerboundSetGameRulePacket$Entry::gameRuleKey,
      ByteBufCodecs.STRING_UTF8,
      ServerboundSetGameRulePacket$Entry::value,
      ServerboundSetGameRulePacket$Entry::new
   );
}
