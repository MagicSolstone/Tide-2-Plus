package net.minecraft.network.chat;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ExtraCodecs;

public record ClickEvent$Custom(Identifier id, Optional<Tag> payload) implements ClickEvent {
   public static final MapCodec<ClickEvent$Custom> CODEC = RecordCodecBuilder.mapCodec(
      i -> i.group(
            Identifier.CODEC.fieldOf("id").forGetter(ClickEvent$Custom::id), ExtraCodecs.NBT.optionalFieldOf("payload").forGetter(ClickEvent$Custom::payload)
         )
         .apply(i, ClickEvent$Custom::new)
   );

   @Override
   public ClickEvent$Action action() {
      return ClickEvent$Action.CUSTOM;
   }
}
