package net.minecraft.world.level.block.entity;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;

public record TestInstanceBlockEntity$ErrorMarker(BlockPos pos, Component text) {
   public static final Codec<TestInstanceBlockEntity$ErrorMarker> CODEC = RecordCodecBuilder.create(
      i -> i.group(
            BlockPos.CODEC.fieldOf("pos").forGetter(TestInstanceBlockEntity$ErrorMarker::pos),
            ComponentSerialization.CODEC.fieldOf("text").forGetter(TestInstanceBlockEntity$ErrorMarker::text)
         )
         .apply(i, TestInstanceBlockEntity$ErrorMarker::new)
   );
   public static final Codec<List<TestInstanceBlockEntity$ErrorMarker>> LIST_CODEC = CODEC.listOf();
}
