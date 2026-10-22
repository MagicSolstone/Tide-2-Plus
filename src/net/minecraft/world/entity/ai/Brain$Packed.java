package net.minecraft.world.entity.ai;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.entity.ai.memory.MemoryMap;

public record Brain$Packed(MemoryMap memories) {
   public static final Brain$Packed EMPTY = new Brain$Packed(MemoryMap.EMPTY);
   public static final Codec<Brain$Packed> CODEC = RecordCodecBuilder.create(
      i -> i.group(MemoryMap.CODEC.fieldOf("memories").forGetter(Brain$Packed::memories)).apply(i, Brain$Packed::new)
   );
}
