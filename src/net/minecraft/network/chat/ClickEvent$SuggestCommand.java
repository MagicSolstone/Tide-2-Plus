package net.minecraft.network.chat;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.ExtraCodecs;

public record ClickEvent$SuggestCommand(String command) implements ClickEvent {
   public static final MapCodec<ClickEvent$SuggestCommand> CODEC = RecordCodecBuilder.mapCodec(
      i -> i.group(ExtraCodecs.CHAT_STRING.fieldOf("command").forGetter(ClickEvent$SuggestCommand::command)).apply(i, ClickEvent$SuggestCommand::new)
   );

   @Override
   public ClickEvent$Action action() {
      return ClickEvent$Action.SUGGEST_COMMAND;
   }
}
