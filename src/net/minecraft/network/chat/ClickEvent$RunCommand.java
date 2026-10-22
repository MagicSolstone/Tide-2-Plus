package net.minecraft.network.chat;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.ExtraCodecs;

public record ClickEvent$RunCommand(String command) implements ClickEvent {
   public static final MapCodec<ClickEvent$RunCommand> CODEC = RecordCodecBuilder.mapCodec(
      i -> i.group(ExtraCodecs.CHAT_STRING.fieldOf("command").forGetter(ClickEvent$RunCommand::command)).apply(i, ClickEvent$RunCommand::new)
   );

   @Override
   public ClickEvent$Action action() {
      return ClickEvent$Action.RUN_COMMAND;
   }
}
