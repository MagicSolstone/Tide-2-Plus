package net.minecraft.world.entity.raid;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap.Entry;

record Raids$RaidWithId(int id, Raid raid) {
   public static final Codec<Raids$RaidWithId> CODEC = RecordCodecBuilder.create(
      i -> i.group(Codec.INT.fieldOf("id").forGetter(Raids$RaidWithId::id), Raid.MAP_CODEC.forGetter(Raids$RaidWithId::raid)).apply(i, Raids$RaidWithId::new)
   );

   public static Raids$RaidWithId from(final Entry<Raid> entry) {
      return new Raids$RaidWithId(entry.getIntKey(), (Raid)entry.getValue());
   }
}
