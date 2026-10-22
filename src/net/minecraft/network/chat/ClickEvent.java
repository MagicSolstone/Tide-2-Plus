package net.minecraft.network.chat;

import com.mojang.serialization.Codec;

public interface ClickEvent {
   Codec<ClickEvent> CODEC = ClickEvent$Action.CODEC.dispatch("action", ClickEvent::action, action -> action.codec);

   ClickEvent$Action action();
}
