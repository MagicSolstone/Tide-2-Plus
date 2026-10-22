package net.minecraft.network.chat;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.ExtraCodecs;

public record ClickEvent$ChangePage(int page) implements ClickEvent {
   public static final MapCodec<ClickEvent$ChangePage> CODEC = RecordCodecBuilder.mapCodec(
      i -> i.group(ExtraCodecs.POSITIVE_INT.fieldOf("page").forGetter(ClickEvent$ChangePage::page)).apply(i, ClickEvent$ChangePage::new)
   );

   @Override
   public ClickEvent$Action action() {
      return ClickEvent$Action.CHANGE_PAGE;
   }
}
