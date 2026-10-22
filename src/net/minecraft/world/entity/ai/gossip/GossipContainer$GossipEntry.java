package net.minecraft.world.entity.ai.gossip;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.UUID;
import net.minecraft.core.UUIDUtil;
import net.minecraft.util.ExtraCodecs;

record GossipContainer$GossipEntry(UUID target, GossipType type, int value) {
   public static final Codec<GossipContainer$GossipEntry> CODEC = RecordCodecBuilder.create(
      i -> i.group(
            UUIDUtil.CODEC.fieldOf("Target").forGetter(GossipContainer$GossipEntry::target),
            GossipType.CODEC.fieldOf("Type").forGetter(GossipContainer$GossipEntry::type),
            ExtraCodecs.POSITIVE_INT.fieldOf("Value").forGetter(GossipContainer$GossipEntry::value)
         )
         .apply(i, GossipContainer$GossipEntry::new)
   );

   public int weightedValue() {
      return this.value * this.type.weight;
   }
}
