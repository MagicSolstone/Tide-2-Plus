package net.minecraft.world.entity;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.UUID;
import net.minecraft.core.UUIDUtil;

record Interaction$PlayerAction(UUID player, long timestamp) {
   public static final Codec<Interaction$PlayerAction> CODEC = RecordCodecBuilder.create(
      i -> i.group(
            UUIDUtil.CODEC.fieldOf("player").forGetter(Interaction$PlayerAction::player),
            Codec.LONG.fieldOf("timestamp").forGetter(Interaction$PlayerAction::timestamp)
         )
         .apply(i, Interaction$PlayerAction::new)
   );
}
